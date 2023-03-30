package server.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import commons.SubTask;
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
import server.services.SubTaskService;
import server.services.TaskService;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SubTaskController.class)
public class SubTaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubTaskService subTaskService;

    @MockBean
    private TaskService taskService;

    @MockBean
    private BoardService boardService;

    @MockBean
    private ListService listService;


    @Test
    public void testAddSubTaskEndpoint() throws Exception {
        SubTask newSubTask = new SubTask("New SubTask", false);
        String requestBody = new ObjectMapper().writeValueAsString(newSubTask);
        Mockito.when(subTaskService.addSubTask(1, 2, 15, newSubTask)).thenReturn(newSubTask);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/subtasks/1/2/15/subtask")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is("New SubTask")))
                .andExpect(jsonPath("completed", is(false)));
        Mockito.verify(subTaskService, Mockito.times(1)).addSubTask(1, 2, 15, newSubTask);
    }
}