package server.api;

import java.util.List;

import commons.Task;
import org.springframework.web.bind.annotation.*;

import server.TaskService;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private TaskService taskService;

    /**
     * Post util method to add a task to a specific board and list
     *
     * @param boardid the id from the board you want to add a task to
     * @param boardid the id from the board you want to add a task to
     * @param task the tasklist that you want to add to board with the given id
     */
    @PostMapping("/{boardid}/{listid}/task")
    public void addTask(
            @PathVariable("boardid") final long boardid,
            @PathVariable("listid") final long listid,
            @RequestBody final Task task
    ) {
        taskService.addTask(boardid, listid, task);
    }

    /**
     * Get util method to get all the tasks from a specific list
     * @param boardid the id of the board that will be accessed
     * @param listid the id of the list that will be accessed
     * @return a list of tasks from the corresponding list
     */
    @GetMapping("/{boardid}/{listid}/task")
    public List<Task> getTasks(@PathVariable("boardid") final long boardid
                               , @PathVariable("listid") final long listid) {
        return taskService.getTasks(boardid, listid);
    }

    /**
     * Get util method for a specific task of a specific list
     *
     * @param boardid the id of the board from which we want to access the task
     * @param listid the id of the list from which we want to access the task
     * @param taskid the id of the task that will be returned
     * @return the corresponding task from the corresponding list
     */
    @GetMapping("/{boardid}/{listid}/{taskid}")
    public Task getTask(
            @PathVariable("boardid") final long boardid,
            @PathVariable("listid") final long listid,
            @PathVariable("taskid") final long taskid
    ) {
        return taskService.getTask(boardid, listid, taskid);
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
    public void renameTask(
            @PathVariable("boardid") final long boardid,
            @PathVariable("listid") final long listid,
            @PathVariable("taskid") final long taskid,
            @RequestParam final String name
    ) {
        taskService.renameTask(boardid, listid, taskid, name);
    }


    /**
     * Delete request to delete a task form a list
     *
     * @param boardid the id from the board you want to remove a task from
     * @param boardid the id from the board you want to remove a task from
     * @param task the tasklist that you want to remove from board with the given id
     */
    @PostMapping("/{boardid}/{listid}/task")
    public void removeTask(
            @PathVariable("boardid") final long boardid,
            @PathVariable("listid") final long listid,
            @RequestBody final Task task
    ) {
        taskService.removeTask(boardid, listid, task);
    }

    private static boolean isNullOrEmpty(final String s) {
        return s == null || s.isEmpty();
    }

}