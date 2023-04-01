package server.api;

import commons.SubTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.services.BoardService;
import server.services.SubTaskService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("api/subtasks")
public class SubTaskController {

    private final SubTaskService subTaskService;

    @Autowired
    public SubTaskController(final SubTaskService subTaskService, final BoardService boardService) {
        this.subTaskService = subTaskService;
        if(boardService.getBoards().isEmpty()){
            boardService.createDefaultBoard();
        }
    }

    @PostMapping("/{boardid}/{listid}/{taskid}/subtask")
    public ResponseEntity<SubTask> addTask(
            @PathVariable("boardid") final long boardid,
            @PathVariable("listid") final long listid,
            @PathVariable("taskid") final long taskid,
            @RequestBody final SubTask subTask
    ) {
        try {
            if (subTask == null || subTask.getName() == null || subTask.getName()
                    .replaceAll("\\s", "").equals("")) {
                return ResponseEntity.badRequest().build();
            }
            SubTask createdSubTask = subTaskService.addSubTask(boardid, listid, taskid, subTask);
            return ResponseEntity.ok(createdSubTask);
        }
        catch (NoSuchElementException e) { return ResponseEntity.notFound().build(); }
    }

    @GetMapping("/{boardid}/tasklist/{tasklistid}/tasks/{taskid}/subtasks")
    public ResponseEntity<List<SubTask>> getSubTasks(@PathVariable("boardid") final long boardId,
                                                     @PathVariable("tasklistid") final long listId,
                                                     @PathVariable("taskid") final long taskId) {
        try {
            List<SubTask> subTasks = subTaskService.getSubTasks(boardId, listId,taskId);
            return ResponseEntity.ok(subTasks);
        } catch (NoSuchElementException e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{boardid}/tasklist/{tasklistid}/tasks/{taskid}/subtasks/{subtaskid}")
    public ResponseEntity<SubTask> getSubTask(@PathVariable("boardid") final long boardId,
                                              @PathVariable("tasklistid") final long listId,
                                              @PathVariable("taskid") final long taskId,
                                              @PathVariable("subtaskid") final long subTaskId) {
        try {
            SubTask subTask = subTaskService.getSubTask(boardId, listId, taskId, subTaskId);
            return ResponseEntity.ok(subTask);
        } catch(Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{boardid}/{tasklistid}/{taskid}/{subtaskid}")
    public ResponseEntity<SubTask> renameSubTask(@PathVariable("boardid") final long boardId,
                                                 @PathVariable("tasklistid") final long listId,
                                                 @PathVariable("taskid") final long taskId,
                                                 @PathVariable("subtaskid") final long subTaskid,
                                                 @RequestParam final String name) {
        try {
            if(name == null || name.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            SubTask renamedSubTask = subTaskService.
                    renameSubTask(boardId, listId, taskId, subTaskid, name);
            return ResponseEntity.ok(renamedSubTask);
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{boardid}/{tasklistid}/{taskid}/{subtaskid}")
    public ResponseEntity<SubTask> deleteSubTask(@PathVariable("boardid") final long boardId,
                                                 @PathVariable("tasklistid") final long listId,
                                                 @PathVariable("taskid") final long taskId,
                                                 @PathVariable("subtaskid") final long subTaskid) {
        try {
            System.out.println(
                    "boardid"
            );
            SubTask deletedSubTask = subTaskService.
                    removeSubTaskById(boardId, listId, taskId, subTaskid);
            return ResponseEntity.ok(deletedSubTask);
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{boardid}/{tasklistid}/{taskid}/reorder/{subtaskid}")
    public ResponseEntity<SubTask> reorderSubTasks(
            @PathVariable("boardid") final long boardid,
            @PathVariable("tasklistid") final long tasklistid,
            @PathVariable ("taskid") final long taskid,
            @PathVariable("subtaskid") final long subtaskid,
            @RequestParam final int newIndex) {
        try {
            SubTask subTask = subTaskService.reorderSubTask(boardid, tasklistid,
                    taskid, subtaskid, newIndex);
            return ResponseEntity.ok(subTask);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
