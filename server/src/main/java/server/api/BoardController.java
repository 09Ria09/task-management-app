package server.api;

import commons.TaskList;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import server.BoardService;

import java.util.List;

@Controller
@RequestMapping("/api/board")
public class BoardController {
    private final BoardService service;

    public BoardController(final BoardService service) {
        this.service = service;
    }

    /**
     * @return all the lists of the board.
     */
    @GetMapping(path = {"", "/"})
    public ResponseEntity<List<TaskList>> getAllLists() {
        /*System.out.println("woww");
        var l=new ArrayList<TaskList>();
        var l2=new ArrayList<Task>();
        l2.add(new Task("task 1","walaka"));
        l2.add(new Task("task 2","malaka"));
        l.add(new TaskList("a",l2));
        l.add(new TaskList("b"));
        l.add(new TaskList("c"));
        service.addBoard(new Board("board",l,null));
        */
        return ResponseEntity.ok(service.getBoard(1).getListTaskList());
    }
}