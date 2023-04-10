package server.api;

import commons.Task;
import commons.TaskPreset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import server.services.BoardService;
import server.services.TaskService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {


    private final TaskService taskService;
    private final BoardService boardService;
    private final SimpMessagingTemplate messages;

    @Autowired
    public TaskController(final TaskService taskService, final BoardService boardService,
                          final SimpMessagingTemplate messages) {
        this.taskService = taskService;
        this.boardService = boardService;
        this.messages = messages;
        if(boardService.getBoards().isEmpty()){
            boardService.createDefaultBoard();
        }
    }

    /**
     * Post util method to add a task to a specific board and list
     *
     * @param boardid the id from the board you want to add a task to
     * @param task    the tasklist that you want to add to board with the given id
     * @return
     */
    @PostMapping("/{boardid}/{listid}/task")
    public ResponseEntity<Task> addTask(
            @PathVariable("boardid") final long boardid,
            @PathVariable("listid") final long listid,
            @RequestBody final Task task
    ) {
        try {
            if (task == null ||isNullOrEmpty(task.getName().replaceAll("\\s", ""))) {
                return ResponseEntity.badRequest().build();
            }
            Task createdTask = taskService.addTask(boardid, listid, task);
            messages.convertAndSend("/topic/" + boardid + "/refreshboard",
                    boardService.getBoard(boardid));
            return ResponseEntity.ok(createdTask);
        }
        catch (NoSuchElementException e) { return ResponseEntity.notFound().build(); }
    }

    /**
     * Get util method to get all the tasks from a specific list
     * @param boardid the id of the board that will be accessed
     * @param listid the id of the list that will be accessed
     * @return a list of tasks from the corresponding list
     */
    @GetMapping("/{boardid}/tasklist/{listid}/tasks")
    public ResponseEntity<List<Task>> getTasks(@PathVariable("boardid") final long boardid
                               , @PathVariable("listid") final long listid) {
        try {
            List<Task> tasks = taskService.getTasks(boardid, listid);
            return ResponseEntity.ok(tasks);
        }
        catch (NoSuchElementException e) { return ResponseEntity.notFound().build(); }
    }

    /**
     * Get util method for a specific task of a specific list
     *
     * @param boardid the id of the board from which we want to access the task
     * @param listid the id of the list from which we want to access the task
     * @param taskid the id of the task that will be returned
     * @return the corresponding task from the corresponding list
     */
    @GetMapping("/{boardid}/tasklist/{listid}/task/{taskid}")
    public ResponseEntity<Task> getTask(
            @PathVariable("boardid") final long boardid,
            @PathVariable("listid") final long listid,
            @PathVariable("taskid") final long taskid
    ) {
        try {
            Task task = taskService.getTask(boardid, listid, taskid);
            return ResponseEntity.ok(task);
        }
        catch (NoSuchElementException e) { return ResponseEntity.notFound().build(); }
    }

    /**
     * Put request to change the name of a tasklist
     *
     * @param boardid the id of the board from where the task will be renamed
     * @param listid the id of the list in which the task of which the name will be changed is
     * @param taskid the id of the task to be renamed
     * @param name the new name for the task
     */
    @PutMapping("/{boardid}/{listid}/{taskid}")
    public ResponseEntity<Task> renameTask(
            @PathVariable("boardid") final long boardid,
            @PathVariable("listid") final long listid,
            @PathVariable("taskid") final long taskid,
            @RequestParam final String name
    ) {
        try {
            if (name == null || name.replaceAll("\\s", "").isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            Task task = taskService.renameTask(boardid, listid, taskid, name);
            messages.convertAndSend("/topic/" + boardid + "/modifytask",
                    taskService.getTask(boardid, listid, taskid));
            return ResponseEntity.ok(task);
        }
        catch (NoSuchElementException e) { return ResponseEntity.notFound().build(); }
        catch (Exception e) { return ResponseEntity.internalServerError().build(); }
    }


    /**
     * Delete request to delete a task form a list
     *
     * @param boardid the id from the board you want to remove a task from
     * @param boardid the id from the board you want to remove a task from
     * @param taskid the task id that you want to remove from board with the given id
     */
    @DeleteMapping("/{boardid}/{listid}/{taskid}")
    public ResponseEntity<Task> deleteTask(
            @PathVariable("boardid") final long boardid,
            @PathVariable("listid") final long listid,
            @PathVariable("taskid") final long taskid
    ) {
        try {
            Task removedTask = taskService.removeTaskById(boardid, listid, taskid);
            messages.convertAndSend("/topic/" + boardid + "/" + listid + "/deletetask",
                    removedTask);
            System.out.println("/topic/" + boardid + "/" + listid + "/deletetask");
            messages.convertAndSend("/topic/" + boardid + "/refreshboard",
                    boardService.getBoard(boardid));
            return ResponseEntity.ok(removedTask);
        }
        catch (NoSuchElementException e) { return ResponseEntity.notFound().build(); }
        catch (Exception e) { return ResponseEntity.internalServerError().build(); }
    }

    private static boolean isNullOrEmpty(final String s) {
        return s == null || s.isEmpty();
    }

    @PutMapping("/{boardid}/{listid}/desc/{taskid}")
    public ResponseEntity<Task> editDescription(
            @PathVariable("boardid") final long boardid,
            @PathVariable("listid") final long listid,
            @PathVariable("taskid") final long taskid,
            @RequestParam final String description
    ) {
        try {
            if (description == null || description.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            Task task = taskService.editDescription(boardid, listid, taskid, description);
            messages.convertAndSend("/topic/" + boardid + "/modifytask",
                    taskService.getTask(boardid, listid, taskid));
            return ResponseEntity.ok(task);
        }
        catch (NoSuchElementException e) { return ResponseEntity.notFound().build(); }
        catch (Exception e) { return ResponseEntity.internalServerError().build(); }
    }


    @PutMapping("/{boardid}/{listid}/{taskid}/preset")
    public ResponseEntity<Task> setPreset(
            @PathVariable("boardid") final long boardid,
            @PathVariable("listid") final long listid,
            @PathVariable("taskid") final long taskid,
            @RequestBody final TaskPreset preset
    ) {
        try {
            if (preset == null) {
                return ResponseEntity.badRequest().build();
            }
            Task task = taskService.setPreset(boardid, listid, taskid, preset);
            messages.convertAndSend("/topic/" + boardid + "/" + listid + "/modifytask",
                    taskService.getTask(boardid, listid, taskid));
            return ResponseEntity.ok(task);
        }
        catch (NoSuchElementException e) { return ResponseEntity.notFound().build(); }
        catch (Exception e) { return ResponseEntity.internalServerError().build(); }
    }
}