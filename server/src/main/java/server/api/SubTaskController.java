package server.api;

import commons.SubTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import server.services.BoardService;
import server.services.SubTaskService;
import server.services.TaskService;

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
            messages.convertAndSend("/topic/" + boardid + "/" + listid + "/modifytask",
                    taskService.getTask(boardid, listid, taskid));
            return ResponseEntity.ok(createdSubTask);
        }
        catch (NoSuchElementException e) { return ResponseEntity.notFound().build(); }
    }
}
