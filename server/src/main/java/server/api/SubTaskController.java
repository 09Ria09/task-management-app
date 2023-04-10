package server.api;

import commons.SubTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import server.services.BoardService;
import server.services.SubTaskService;
import server.services.TaskService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("api/subtasks")
public class SubTaskController {

    private final SubTaskService subTaskService;
    private final TaskService taskService;
    private final SimpMessagingTemplate messages;

    @Autowired
    public SubTaskController(final SubTaskService subTaskService, final BoardService boardService, 
                             final TaskService taskService, final SimpMessagingTemplate messages) {
        this.subTaskService = subTaskService;
        this.taskService = taskService;
        this.messages = messages;
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
            messages.convertAndSend("/topic/" + boardid + "/modifytask",
                    taskService.getTask(boardid, listid, taskid));
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
            messages.convertAndSend("/topic/" + boardId + "/modifytask",
                    taskService.getTask(boardId, listId, taskId));
            return ResponseEntity.ok(renamedSubTask);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{boardid}/{tasklistid}/{taskid}/{subtaskid}/complete")
    public ResponseEntity<SubTask> completeSubTask(@PathVariable("boardid") final long boardId,
                                                 @PathVariable("tasklistid") final long listId,
                                                 @PathVariable("taskid") final long taskId,
                                                 @PathVariable("subtaskid") final long subTaskid,
                                                 @RequestParam final Boolean complete) {
        try {
            SubTask changedSubTask = subTaskService.
                    completeSubTask(boardId, listId, taskId, subTaskid, complete);
            messages.convertAndSend("/topic/" + boardId + "/modifytask",
                    taskService.getTask(boardId, listId, taskId));
            return ResponseEntity.ok(changedSubTask);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{boardid}/{tasklistid}/{taskid}/{subtaskid}")
    public ResponseEntity<SubTask> deleteSubTask(@PathVariable("boardid") final long boardId,
                                                 @PathVariable("tasklistid") final long listId,
                                                 @PathVariable("taskid") final long taskId,
                                                 @PathVariable("subtaskid") final long subTaskid) {
        try {
            SubTask subTaskToDeleteCopy = null;
            SubTask deletedSubTask = subTaskService.
                    getSubTask(boardId, listId, taskId, subTaskid);
            if(deletedSubTask!=null) {
                subTaskToDeleteCopy = new SubTask(deletedSubTask);
            }
            subTaskService.removeSubTaskById(boardId, listId, taskId, subTaskid);
            messages.convertAndSend("/topic/" + boardId + "/modifytask",
                    taskService.getTask(boardId, listId, taskId));
            return ResponseEntity.ok(subTaskToDeleteCopy);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
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
            messages.convertAndSend("/topic/" + boardid + "/modifytask",
                    taskService.getTask(boardid, tasklistid, taskid));
            return ResponseEntity.ok(subTask);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
