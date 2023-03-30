package server.api;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import server.services.BoardService;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BoardController.class)
public class BoardControllerTest {
    private BoardService service;
    private TestBoardRepository repo;
    private BoardController controller;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BoardService boardService;

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

    //Dorian I will need your help on betteer understanding this one
    @Test
    public void testDefaultBoardAfterSetup() {
        assertEquals(2, service.getBoards().size());
    }

    @Test
    public void testGetAllBoardEndpoint() throws Exception {
        List<Board> boards = List.of(new Board("Test Board", new ArrayList<>(), new ArrayList<>()),
                new Board("Board 1", new ArrayList<>(), new ArrayList<>()));
        Mockito.when(boardService.getBoards()).thenReturn(boards);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/boards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Test Board")))
                .andExpect(jsonPath("$[1].name", is("Board 1")));
    }

    @Test
    public void testGetBoardEndpoint() throws Exception {
        Board board = new Board("Test Board !", List.of(), List.of());
        Mockito.when(boardService.getBoard(1)).thenReturn(board);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/boards/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is("Test Board !")));
    }

    @Test
    public void testDeleteBoardEndpoint() throws Exception {
        Mockito.doNothing().when(boardService).removeBoardByID(1);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/boards/1"))
                .andExpect(status().isOk());
        Mockito.verify(boardService, Mockito.times(1)).removeBoardByID(1);
    }

    @Test
    public void testRenameBoardEndpoint() throws Exception {
        Mockito.doNothing().when(boardService).renameBoard(1, "Test 2");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/boards/1")
                        .param("name", "Test 2"))
                .andExpect(status().isOk());
        Mockito.verify(boardService, Mockito.times(1)).renameBoard(1, "Test 2");
    }

    @Test
    public void testAddBoardEndpoint() throws Exception {
        Board newBoard = new Board("New Board", List.of(), List.of());
        String requestBody = new ObjectMapper().writeValueAsString(newBoard);
        Mockito.when(boardService.addBoard(newBoard)).thenReturn(newBoard);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/boards/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is("New Board")));
        Mockito.verify(boardService, Mockito.times(1)).addBoard(newBoard);
    }
}