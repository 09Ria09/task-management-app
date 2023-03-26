package server.api;

import commons.Board;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.services.BoardService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;

    /**
     * Initialize the Board Controller. If the JPA Repository is empty, a default board is created
     * @param boardService the service used to interact with the JPA Repository
     */

    public BoardController(final BoardService boardService) {
        this.boardService = boardService;
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
        if (board == null || isNullOrEmpty(board.getName())) {
            return ResponseEntity.badRequest().build();
        }
        Board createdBoard = boardService.addBoard(board);
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
            Board boardToDelete = boardService.getBoard(boardid);
            boardService.removeBoardByID(boardid);
            return ResponseEntity.ok(boardToDelete);
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
        if (name == null || name.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        try{
            boardService.renameBoard(boardid, name);
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
}
