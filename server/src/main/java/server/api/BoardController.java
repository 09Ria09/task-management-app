package server.api;

import commons.Board;
import commons.BoardColorScheme;
import commons.TaskPreset;
import commons.BoardEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import server.services.BoardService;

import java.util.*;
import java.util.function.Consumer;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;
    private final SimpMessagingTemplate messages;

    /**
     * Initialize the Board Controller. If the JPA Repository is empty, a default board is created
     * @param boardService the service used to interact with the JPA Repository
     */
    @Autowired
    public BoardController(final BoardService boardService,
                           final SimpMessagingTemplate messages) {
        this.boardService = boardService;
        this.messages = messages;
        if(this.boardService.getBoards().isEmpty()){
            this.boardService.createDefaultBoard();
        }
    }

    /**
     * Get util method to get all boards, retrieved as a list
     *
     * @return a list of all the boards stored in the repository
     */
    @GetMapping(path = { "", "/" })
    public ResponseEntity<List<Board>> getBoards() {
        List<Board> boards = boardService.getBoards();
        return ResponseEntity.ok(boards);
    }

    private Map<Object, Consumer<BoardEvent>> listeners = new HashMap<>();

    @GetMapping("/updates")
    public DeferredResult<ResponseEntity<BoardEvent>> getUpdates() {

        var noContent = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        var res = new DeferredResult<ResponseEntity<BoardEvent>>(5000L, noContent);

        var key = new Object();
        listeners.put(key, event -> {
            res.setResult(ResponseEntity.ok(event));
        });
        res.onCompletion(() -> {
            listeners.remove(key);
        });
        return res;
    }

    /**
     * Get util method to get a specific board
     *
     * @param boardid the id of the board that will be accessed
     * @return the board with the corresponding id as response entity
     */
    @GetMapping("/{boardid}")
    public ResponseEntity<Board> getBoard(@PathVariable("boardid") final long boardid) {
        try {
            Board board = boardService.getBoard(boardid);
            return ResponseEntity.ok(board);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Post util method to add a board
     *
     * @param board the board you want to add
     */
    @PostMapping(path = { "", "/" })
    public ResponseEntity<Board> addBoard(@RequestBody final Board board) {
        if (board == null || isNullOrEmpty(board.getName().replaceAll("\\s", ""))) {
            return ResponseEntity.badRequest().build();
        }
        Board createdBoard = boardService.addBoardAndInit(board);
        BoardEvent event = new BoardEvent("ADD", createdBoard);
        listeners.forEach((k, l) -> l.accept(event));
        return ResponseEntity.ok(createdBoard);
    }

    /**
     * Delete request to delete a board with a specific id
     *
     * @param boardid the corresponding id of the board that will be deleted
     * @return the deleted board if it was deleted successfully,
     * otherwise a not found/error response
     */
    @DeleteMapping("/{boardid}")
    public ResponseEntity<Board> deleteBoard(@PathVariable("boardid") final long boardid) {
        try {
            //the remove method checks if the board exists anyway,
            //so we can be certain that the getBoard will also return something non-null
            Board boardToDeleteCopy = null;
            Board boardToDelete = boardService.getBoard(boardid);
            if(boardToDelete!=null) boardToDeleteCopy = new Board(boardToDelete);
            boardService.removeBoardByID(boardid);
            messages.convertAndSend("/topic/deleteboard", boardToDelete);
            BoardEvent event = new BoardEvent("DELETE", boardToDeleteCopy);
            listeners.forEach((k, l) -> l.accept(event));
            return ResponseEntity.ok(boardToDeleteCopy);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            // Log the error or perform error-handling
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Put request to rename a board
     * @param boardid the id of the board that will be renamed
     * @param name the new name of the board
     * @return the renamed board if it was renamed successfully,
     * otherwise a not found/error response
     */
    @PutMapping("/{boardid}")
    public ResponseEntity<Board> renameBoard(
            @PathVariable("boardid") final long boardid,
            @RequestParam final String name
    ) {
        if (name == null || name.replaceAll("\\s", "").isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        try{
            boardService.renameBoard(boardid, name);
            messages.convertAndSend("/topic/renameboard",
                    boardService.getBoard(boardid));
            return ResponseEntity.ok(boardService.getBoard(boardid));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }catch (Exception e) {
            // Log the error and perform error-handling
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Put request to join a board
     * I was not sure how else we could implement joining a board
     * without keeping a reference to some sort of joined members
     * I believe each member will basically be replaced/mapped eventually
     * by a connection string, but for now I will follow this simpler
     * approach
     * @param boardid the id of the board that will be joined
     * @param memberName the name of the member that will join the board
     * @return the board with the new member if it was joined successfully,
     * otherwise a not found/error response
     */
    @PutMapping("/{boardid}/join")
    public ResponseEntity<Board> joinBoard(
            @PathVariable("boardid") final long boardid,
            @RequestParam final String memberName
    ) {
        if(isNullOrEmpty(memberName)){
            return ResponseEntity.badRequest().build();
        }
        try{
            boardService.joinBoard(boardid, memberName);
            return ResponseEntity.ok(boardService.getBoard(boardid));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    private static boolean isNullOrEmpty(final String s) {
        return s == null || s.isEmpty();
    }

    @GetMapping("/{boardid}/boardcolorscheme")
    public ResponseEntity<BoardColorScheme> getBoardColorScheme(@PathVariable("boardid")
                                                                    final long boardid) {
        try {
            BoardColorScheme boardColorScheme = boardService.
                    getBoard(boardid).getBoardColorScheme();
            return ResponseEntity.ok(boardColorScheme);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{boardid}/setboardcolorscheme")
    public ResponseEntity<BoardColorScheme> setBoardColorScheme(
            @PathVariable("boardid") final long boardid,
            final @RequestBody BoardColorScheme boardColorScheme) {
        try {
            BoardColorScheme colorScheme = boardService
                    .setBoardColorScheme(boardid, boardColorScheme);
            messages.convertAndSend("/topic/" + boardid + "/changecolor",
                    boardService.getBoard(boardid));
            return ResponseEntity.ok(colorScheme);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(path = { "/{boardid}" + "/addtaskpreset" })
    public ResponseEntity<TaskPreset> addTaskPreset(@PathVariable("boardid") final long boardId,
                                                    @RequestBody final TaskPreset taskPreset) {
        if (getBoard(boardId) == null) {
            return ResponseEntity.badRequest().build();
        }
        TaskPreset createdTaskPreset = boardService.setTaskPreset(boardId, taskPreset);
        return ResponseEntity.ok(createdTaskPreset);
    }

    @DeleteMapping(path = { "/{boardid}" + "/removetaskpreset" + "/{taskpresetid}" })
    public ResponseEntity removeTaskPreset(@PathVariable("boardid") final long boardId,
                                           @PathVariable("taskpresetid") final long taskPresetId) {
        var board=getBoard(boardId);
        if (board == null || board.getBody().getTaskPresets().stream()
            .noneMatch(taskPreset -> taskPreset.getId() == taskPresetId)) {
            return ResponseEntity.badRequest().build();
        }
        boardService.removeTaskPreset(boardId, taskPresetId);
        return ResponseEntity.ok().build();
    }

    @PutMapping(path = { "/{boardid}" + "/updatetaskpreset" })
    public ResponseEntity<TaskPreset> updateTaskPreset(@PathVariable("boardid") final long boardId,
                                                       @RequestBody final TaskPreset taskPreset) {
        if (getBoard(boardId) == null) {
            return ResponseEntity.badRequest().build();
        }
        TaskPreset updatedTaskPreset = boardService.updateTaskPreset(boardId, taskPreset);
        return ResponseEntity.ok(updatedTaskPreset);
    }
}
