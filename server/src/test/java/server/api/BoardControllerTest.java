package server.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.test.util.ReflectionTestUtils;
import server.BoardService;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BoardControllerTest {
    private BoardService service;
    private TestBoardRepository repo;
    private BoardController controller;

    @BeforeEach
    public void setup(final TestInfo info) {
        if(info.getTags()
                .stream()
                .anyMatch(t -> t.equals("skipSetup"))){
            return;
        }
        service = new BoardService();
        repo = new TestBoardRepository();
        ReflectionTestUtils.setField(service, "boardRepository", repo);
        controller = new BoardController(service);
    }

    @Test
    @Tag("skipSetup")
    public void testDefaultBoardBeforeSetup() {
        service = new BoardService();
        repo = new TestBoardRepository();
        ReflectionTestUtils.setField(service, "boardRepository", repo);
        controller = null;
        assertEquals(0, service.getBoards().size());
    }

    @Test
    public void testDefaultBoardAfterSetup() {
        assertEquals(1, service.getBoards().size());
    }
}