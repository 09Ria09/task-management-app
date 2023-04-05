package server.api;

import commons.TaskList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import server.services.BoardService;
import server.services.ListService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/lists")
public class ListController {

    private final ListService listService;
    private final BoardService boardService;
    private final SimpMessagingTemplate messages;

    @Autowired
    public ListController(final ListService listService, final BoardService boardService,
                          final SimpMessagingTemplate messages) {
        this.listService = listService;
        this.boardService = boardService;
        this.messages = messages;
        if(boardService.getBoards().isEmpty()){
            boardService.createDefaultBoard();
        }
    }



    /**
     * Post util method to add a tasklist to a specific board
     *
     * @param boardid the id from the board you want to add a tasklist to
     * @param taskList the tasklist that you want to add to board with the given id
     */
    @PostMapping("/{boardid}/tasklist")
    public ResponseEntity<TaskList> addTaskList(
            @PathVariable("boardid") final long boardid,
            @RequestBody final TaskList taskList
    ) {
        try{
            if (taskList == null || taskList.getName() == null ||
                    isNullOrEmpty(taskList.getName().replaceAll("\\s", ""))) {
                return ResponseEntity.badRequest().build();
            }
            TaskList createdTaskList = listService.addList(boardid, taskList);
            messages.convertAndSend("/topic/" + boardid + "/refreshboard",
                    boardService.getBoard(boardid));
            return ResponseEntity.ok(createdTaskList);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }


    /**
     * Get util method to get all the tasklists from a specific board
     * @param boardid the id of the board that will be accesses
     * @return a list of tasklists from the corresponding board
     */
    @GetMapping("/{boardid}/tasklists")
    public ResponseEntity<List<TaskList>> getTaskLists(
            @PathVariable("boardid") final long boardid) {
        try {
            List<TaskList> lists = listService.getLists(boardid);
            return ResponseEntity.ok(lists);
        } catch (NoSuchElementException e){
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get util method for a specific list of a specific board
     *
     * @param boardid the id of the board from which we want to access the list
     * @param taskListid the id of the list that will be accessed
     * @return the corresponding list from the corresponding board
     */
    @GetMapping("/{boardid}/tasklist/{taskListid}")
    public ResponseEntity<TaskList> getTaskList(
            @PathVariable("boardid") final long boardid,
            @PathVariable("taskListid") final long taskListid
    ) {
        try {
            TaskList list = listService.getList(boardid, taskListid);
            return ResponseEntity.ok(list);
        } catch (NoSuchElementException e){
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Put request to change the name of a tasklist
     *
     * @param boardid the id of the board from where the list will be renamed
     * @param tasklistid the id of the list of which the name will be changed
     * @param name the new name for the list
     */
    @PutMapping("/{boardid}/{tasklistid}")
    public ResponseEntity<TaskList> renameTaskList(
            @PathVariable("boardid") final long boardid,
            @PathVariable("tasklistid") final long tasklistid,
            @RequestParam final String name
    ) {
        try {
            if(name == null|| name.replaceAll("\\s", "").isEmpty()){
                return ResponseEntity.badRequest().build();
            }
            TaskList list = listService.renameList(boardid, tasklistid, name);
            messages.convertAndSend("/topic/" + boardid + "/refreshboard",
                    boardService.getBoard(boardid));
            return ResponseEntity.ok(list);
        } catch(NoSuchElementException e){
            return ResponseEntity.notFound().build();
        }

//        listService.renameList(boardid, tasklistid, name);
    }

    /**
     * Delete request to delete a tasklist with a list id and board id
     *
     * @param boardid the id of the board from where the list will be deleted
     * @param tasklistid the id of the list that will be deleted
     */
    @DeleteMapping("/{boardid}/{tasklistid}")
    public ResponseEntity<TaskList> deleteTaskList(
            @PathVariable("boardid") final long boardid,
            @PathVariable("tasklistid") final long tasklistid
    ) {
        try {
            TaskList list = listService.removeListByID(boardid, tasklistid);
            messages.convertAndSend("/topic/" + boardid + "/refreshboard",
                    boardService.getBoard(boardid));
            messages.convertAndSend("/topic/" + boardid + "/deletelist",
                    list);
            System.out.println("/topic/" + boardid + "/deletelist");
            return ResponseEntity.ok(list);
        } catch(NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch(Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private static boolean isNullOrEmpty(final String s) {
        return s == null || s.isEmpty();
    }

    /**
     * A put method to change the index of a task to the new given index
     * @param boardid the board where the task is in
     * @param tasklistid the tasklist where the task is in
     * @param taskid the id of the task that needs to be repositioned
     * @param newIndex the new index of the task
     * @return the corresponding list after it's changed
     */
    @PutMapping("/{boardid}/{tasklistid}/reorder/{taskid}")
    public ResponseEntity<TaskList> reorderTasks(
            @PathVariable("boardid") final long boardid,
            @PathVariable("tasklistid") final long tasklistid,
            @PathVariable ("taskid") final long taskid,
            @RequestParam final int newIndex
    ) {
        try {
            TaskList list = listService.reorderTask(boardid, tasklistid, taskid, newIndex);
            messages.convertAndSend("/topic/" + boardid + "/refreshboard",
                    boardService.getBoard(boardid));
            return ResponseEntity.ok(list);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

}