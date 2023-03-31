package server.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import commons.TaskList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import server.services.BoardService;
import server.services.ListService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ListController.class)
public class ListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ListService listService;

    @MockBean
    private BoardService boardService;

    @Test
    public void testGetAllTaskListEndpoint() throws Exception {
        List<TaskList> taskLists = List.of(new TaskList("Test TaskList"),
                new TaskList("TaskList 1"));
        Mockito.when(listService.getLists(1)).thenReturn(taskLists);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/lists/1/tasklists"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Test TaskList")))
                .andExpect(jsonPath("$[1].name", is("TaskList 1")));
    }

    @Test
    public void testGetTaskListEndpoint() throws Exception {
        TaskList taskList = new TaskList("Test TaskList !");
        Mockito.when(listService.getList(1, 2)).thenReturn(taskList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/lists/1/tasklist/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is("Test TaskList !")));
    }

    @Test
    public void testDeleteTaskListEndpoint() throws Exception {
        Mockito.when(listService.removeListByID(1, 2)).thenReturn(null);


        mockMvc.perform(MockMvcRequestBuilders.delete("/api/lists/1/2"))
                .andExpect(status().isOk());
        Mockito.verify(listService, Mockito.times(1)).removeListByID(1, 2);
    }

    @Test
    public void testRenameTaskListEndpoint() throws Exception {
        Mockito.when(listService.renameList(1, 2, "Test 2")).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/lists/1/2")
                        .param("name", "Test 2"))
                .andExpect(status().isOk());
        Mockito.verify(listService, Mockito.times(1)).renameList(1, 2, "Test 2");
    }

    @Test
    public void testAddTaskListEndpoint() throws Exception {
        TaskList newList = new TaskList("New List");
        String requestBody = new ObjectMapper().writeValueAsString(newList);
        Mockito.when(listService.addList(1, newList)).thenReturn(newList);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/lists/1/tasklist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is("New List")));
        Mockito.verify(listService, Mockito.times(1)).addList(1, newList);
    }
}