package server.api;

import java.util.*;

import commons.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.BoardService;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;

    @Autowired
    public BoardController(final BoardService boardService) {
        this.boardService = boardService;
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
     * Get util method to get all the tasklists from a specific board
     *
     * @param boardid the id of the board that will be accesses
     * @return a list of tasklists from the corresponding board
     */
    @GetMapping("/{boardid}/tasklists")
    public ResponseEntity<List<TaskList>>
        getTaskLists(@PathVariable("boardid") final long boardid) {
        try {
            List<TaskList> taskLists = boardService.getLists(boardid);
            return ResponseEntity.ok(taskLists);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get util method for a specific list of a specific board
     *
     * @param boardid the id of the board from which we want to access the list
     * @param taskListid the id of the list that will be accessed
     * @return the corresponding list from the corresponding board as response entity
     */
    @GetMapping("/{boardid}/tasklist/{taskListid}")
    public ResponseEntity<TaskList> getTaskList(
            @PathVariable("boardid") final long boardid,
            @PathVariable("taskListid") final long taskListid
    ) {
        try {
            TaskList taskList = boardService.getList(boardid, taskListid);
            return ResponseEntity.ok(taskList);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get util method to get all the tasks in a specific list of a specific board
     *
     * @param boardid the id of the board from which we want to access the tasks
     * @param taskListid the id of the list from which we want to access the tasks
     * @return a list of tasks from the corresponding list and board
     */
    @GetMapping("/{boardid}/tasklist/{taskListid}/tasks")
    public ResponseEntity<List<Task>> getTasks(
            @PathVariable("boardid") final long boardid,
            @PathVariable("taskListid") final long taskListid
    ) {
        try {
            List<Task> tasks = boardService.getTasks(boardid, taskListid);
            return ResponseEntity.ok(tasks);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get util method to get a specific task from a specific tasklist and board
     *
     * @param boardid the id of the board from which we want to access the task
     * @param taskListid the id of the tasklist from which we want to access the task
     * @param taskid the id of the task we want to access
     * @return the task corresponding to all the given ids as response entity
     */
    @GetMapping("/{boardid}/tasklist/{taskListid}/task/{taskid}")
    public ResponseEntity<Task> getTask(
            @PathVariable("boardid") final long boardid,
            @PathVariable("taskListid") final long taskListid,
            @PathVariable("taskid") final long taskid
    ) {
        try {
            Task task = boardService.getTask(boardid, taskListid, taskid);
            return ResponseEntity.ok(task);
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
        if (board == null || board.getName() == null) {
            return ResponseEntity.badRequest().build();
        }
        Board createdBoard = boardService.addBoard(board);
        return ResponseEntity.ok(createdBoard);
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
    ) {try{
            if (taskList == null || taskList.getName() == null) {
                return ResponseEntity.badRequest().build();
            }
            TaskList createdTaskList = boardService.addList(boardid, taskList);
            return ResponseEntity.ok(createdTaskList);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Post util method to add a task to a specific board and list
     *
     * @param boardid the id from the board you want to add the task to
     * @param tasklistid the id from the list you want to add the task to
     * @param task the task you want to add to the entities with the given ids
     */
    @PostMapping("/{boardid}/{tasklistid}/task")
    public ResponseEntity<Task> addTask(
            @PathVariable("boardid") final long boardid,
            @PathVariable("tasklistid") final long tasklistid,
            @RequestBody final Task task
    ) {try{
            if (task == null || task.getName() == null) {
                return ResponseEntity.badRequest().build();
            }
            Task createdTask = boardService.addTask(boardid, tasklistid, task);
            return ResponseEntity.ok(createdTask);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
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
     * Delete request to delete a tasklist with a board and list id
     *
     * @param boardid the id of the board from where the list will be deleted
     * @param tasklistid the id of the list that will be deleted
     * @return the deleted list if it was deleted successfully,
     * otherwise a not found/error response
     */
    @DeleteMapping("/{boardid}/{tasklistid}")
    public ResponseEntity<TaskList> deleteTaskList(
            @PathVariable("boardid") final long boardid,
            @PathVariable("tasklistid") final long tasklistid
    ) {
        try {
            TaskList taskListToDelete = boardService.getList(boardid, tasklistid);
            boardService.removeListByID(boardid, tasklistid);
            return ResponseEntity.ok(taskListToDelete);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Delete request to delete a task with a board, list and task id
     *
     * @param boardid the id of the board from where the task will be deleted
     * @param tasklistid the id of the list from where the task will be deleted
     * @param taskid the id of the task that will be deleted
     * @return the deleted task if it was deleted successfully,
     * otherwise a not found/error response
     */
    @DeleteMapping("/{boardid}/{tasklistid}/{taskid}")
    public ResponseEntity<Task> deleteTask(
            @PathVariable("boardid") final long boardid,
            @PathVariable("tasklistid") final long tasklistid,
            @PathVariable("taskid") final long taskid
    ) {
        try {
            Task taskToDelete = boardService.getTask(boardid, tasklistid, taskid);
            boardService.removeTaskByID(boardid, tasklistid, taskid);
            return ResponseEntity.ok(taskToDelete);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
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
     * Put request to change the name of a tasklist
     *
     * @param boardid    the id of the board from where the list will be renamed
     * @param tasklistid the id of the list of which the name will be changed
     * @param name       the new name for the list
     * @return  the tasklist with the new name if it was renamed successfully,
     * otherwise a not found/error response
     */
    @PutMapping("/{boardid}/{tasklistid}")
    public ResponseEntity<TaskList> renameTaskList(
            @PathVariable("boardid") final long boardid,
            @PathVariable("tasklistid") final long tasklistid,
            @RequestParam final String name
    ) {
        if (name == null || name.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            boardService.renameList(boardid, tasklistid, name);
            return ResponseEntity.ok(boardService.getList(boardid, tasklistid));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Put request to change the name of a task
     *
     * @param boardid    the id of the board from where the task will be renamed
     * @param tasklistid the id of the list from where the task will be renamed
     * @param taskid     the id of the task of which the name will be changed
     * @param name       the new name for the task
     * @return the task with the new name if it was renamed successfully,
     * otherwise a not found/error response
     */
    @PutMapping("/{boardid}/{tasklistid}/{taskid}")
    public ResponseEntity<Task> renameTask(
            @PathVariable("boardid") final long boardid,
            @PathVariable("tasklistid") final long tasklistid,
            @PathVariable("taskid") final long taskid,
            @RequestParam final String name
    ) {
        if (name == null || name.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            boardService.renameTask(boardid, tasklistid, taskid, name);
            return ResponseEntity.ok(boardService.getTask(boardid, tasklistid, taskid));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }



}
