package server.api;

import java.util.List;

import commons.TaskList;
import org.springframework.web.bind.annotation.*;

import server.ListService;

@RestController
@RequestMapping("/api/tasks")
public class ListController {

    private ListService listService;

    /**
     * Post util method to add a tasklist to a specific board
     *
     * @param boardid the id from the board you want to add a tasklist to
     * @param taskList the tasklist that you want to add to board with the given id
     */
    @PostMapping("/{boardid}/tasklist")
    public void addTaskList(
            @PathVariable("boardid") final long boardid,
            @RequestBody final TaskList taskList
    ) {
        listService.addList(boardid, taskList);
    }

    /**
     * Get util method to get all the tasklists from a specific board
     * @param boardid the id of the board that will be accesses
     * @return a list of tasklists from the corresponding board
     */
    @GetMapping("/{boardid}/tasklists")
    public List<TaskList> getTaskLists(@PathVariable("boardid") final long boardid) {
        return listService.getLists(boardid);
    }

    /**
     * Get util method for a specific list of a specific board
     *
     * @param boardid the id of the board from which we want to access the list
     * @param taskListid the id of the list that will be accessed
     * @return the corresponding list from the corresponding board
     */
    @GetMapping("/{boardid}/tasklist/{taskListid}")
    public TaskList getTaskList(
            @PathVariable("boardid") final long boardid,
            @PathVariable("taskListid") final long taskListid
    ) {
        return listService.getList(boardid, taskListid);
    }

    /**
     * Put request to change the name of a tasklist
     *
     * @param boardid the id of the board from where the list will be renamed
     * @param tasklistid the id of the list of which the name will be changed
     * @param name the new name for the list
     */
    @PutMapping("/{boardid}/{tasklistid}")
    public void renameTaskList(
            @PathVariable("boardid") final long boardid,
            @PathVariable("tasklistid") final long tasklistid,
            @RequestParam final String name
    ) {
        listService.renameList(boardid, tasklistid, name);
    }

    /**
     * Delete request to delete a tasklist with a list id and board id
     *
     * @param boardid the id of the board from where the list will be deleted
     * @param tasklistid the id of the list that will be deleted
     */
    @DeleteMapping("/{boardid}/{tasklistid}")
    public void deleteTaskList(
            @PathVariable("boardid") final long boardid,
            @PathVariable("tasklistid") final long tasklistid
    ) {
        listService.removeListByID(boardid, tasklistid);
    }

    private static boolean isNullOrEmpty(final String s) {
        return s == null || s.isEmpty();
    }

}