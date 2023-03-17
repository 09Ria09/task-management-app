package server.api;

import java.util.List;
import java.util.Random;

import commons.TaskList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import server.database.ListRepository;

@RestController
@RequestMapping("/api/tasks")
public class ListController {

    private final Random random;
    private final ListRepository repo;

    public ListController(final Random random, final ListRepository repo) {
        this.random = random;
        this.repo = repo;
    }

    @GetMapping(path = { "", "/" })
    public List<TaskList> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskList> getById(@PathVariable("id") final long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    @PostMapping(path = { "", "/" })
    public ResponseEntity<TaskList> add(@RequestBody final TaskList list) {

        if (isNullOrEmpty(list.getName()) || list == null) {
            return ResponseEntity.badRequest().build();
        }

        TaskList saved = repo.save(list);
        return ResponseEntity.ok(saved);
    }

    private static boolean isNullOrEmpty(final String s) {
        return s == null || s.isEmpty();
    }

    @GetMapping("rnd")
    public ResponseEntity<TaskList> getRandom() {
        var lists = repo.findAll();
        var idx = random.nextInt((int) repo.count());
        return ResponseEntity.ok(lists.get(idx));
    }
}