package server.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import commons.SubTask;
import commons.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import server.services.BoardService;
import server.services.ListService;
import server.services.SubTaskService;
import server.services.TaskService;

import java.util.List;
import java.util.NoSuchElementException;

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

    @MockBean
    private SimpMessagingTemplate messages;


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

    @Test
    public void testAddSubTaskEndpointBadRequest() throws Exception {
        SubTask invalidSubTask = new SubTask("", false);
        String requestBody = new ObjectMapper().writeValueAsString(invalidSubTask);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/subtasks/1/2/3/subtask")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isBadRequest());

        Mockito.verify(subTaskService, Mockito.times(0))
            .addSubTask(1, 2, 3,invalidSubTask);
    }

    @Test
    public void testAddSubTaskEndpointNotFound() throws Exception {
        SubTask newSubTask = new SubTask("New SubTask", false);
        String requestBody = new ObjectMapper().writeValueAsString(newSubTask);
        Mockito.when(subTaskService.addSubTask(1, 2, 3, newSubTask))
            .thenThrow(NoSuchElementException.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/subtasks/1/2/3/subtask")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isNotFound());

        Mockito.verify(subTaskService, Mockito.times(1))
            .addSubTask(1, 2, 3, newSubTask);
    }

    @Test
    public void testGetSubTaskEndpoint() throws Exception {
        SubTask subTask = new SubTask("New SubTask", false);
        Mockito.when(subTaskService.getSubTask(1, 2, 7, 10)).thenReturn(subTask);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/subtasks/1/tasklist/2/tasks/7/subtasks/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is("New SubTask")))
                .andExpect(jsonPath("completed", is(false)));
    }

    @Test
    public void testGetSubTaskEndpointNotFound() throws Exception {
        Mockito.when(subTaskService.getSubTask(1, 2, 7, 10))
                .thenThrow(NoSuchElementException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/subtasks/1/tasklist/2/tasks/7/subtasks/10"))
                .andExpect(status().isNotFound());

        Mockito.verify(subTaskService, Mockito.times(1))
                .getSubTask(1, 2, 7, 10);
    }

    @Test
    public void testGetAllSubTasksEndpoint() throws Exception{
        List<SubTask> subTasks = List.of(new SubTask("SubTask1", false),
                new SubTask("SubTask2", true));
        Mockito.when(subTaskService.getSubTasks(1, 4, 8)).thenReturn(subTasks);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/subtasks/1/tasklist/4/tasks/8/subtasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("SubTask1")))
                .andExpect(jsonPath("$[0].completed", is(false)))
                .andExpect(jsonPath("$[1].name", is("SubTask2")))
                .andExpect(jsonPath("$[1].completed", is(true)));
    }

    @Test
    public void testGetAllSubTasksEndpointNotFound() throws Exception{
        Mockito.when(subTaskService.getSubTasks(1, 4, 8))
                        .thenThrow(NoSuchElementException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/subtasks/1/tasklist/4/tasks/8/subtasks"))
                .andExpect(status().isNotFound());

        Mockito.verify(subTaskService, Mockito.times(1))
                .getSubTasks(1, 4, 8);
    }

    @Test
    public void testRenameSubTaskEndpoint() throws Exception {
        Mockito.when(subTaskService.renameSubTask(1, 2, 4, 5,  "Test 2")).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/subtasks/1/2/4/5")
                        .param("name", "Test 2"))
                .andExpect(status().isOk());
        Mockito.verify(subTaskService, Mockito.times(1)).renameSubTask(1, 2, 4, 5, "Test 2");
    }

    @Test
    public void testRenameSubTaskEndpointBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/subtasks/1/2/4/5")
                        .param("name", ""))
                .andExpect(status().isBadRequest());
        Mockito.verify(subTaskService, Mockito.times(0)).renameSubTask(1, 2, 4, 5, "");
    }

    @Test
    public void testRenameSubTaskEndpointNotFound() throws Exception {
        Mockito.when(subTaskService.renameSubTask(1, 2, 4, 5,  "Test 2"))
                .thenThrow(NoSuchElementException.class);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/subtasks/1/2/4/5")
                        .param("name", "Test 2"))
                .andExpect(status().isNotFound());
        Mockito.verify(subTaskService, Mockito.times(1)).renameSubTask(1, 2, 4, 5, "Test 2");
    }

    @Test
    public void testCompleteSubTaskEndpoint() throws Exception {
        Mockito.when(subTaskService.completeSubTask(1, 2, 4, 5,  true)).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/subtasks/1/2/4/5/complete")
                        .param("complete", "true"))
                .andExpect(status().isOk());
        Mockito.verify(subTaskService, Mockito.times(1)).completeSubTask(1, 2, 4, 5, true);
    }

    @Test
    public void testCompleteSubTaskEndpointNotFound() throws Exception {
        Mockito.when(subTaskService.completeSubTask(1, 2, 4, 5,  true))
                .thenThrow(NoSuchElementException.class);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/subtasks/1/2/4/5/complete")
                        .param("complete", "true"))
                .andExpect(status().isNotFound());
        Mockito.verify(subTaskService, Mockito.times(1)).completeSubTask(1, 2, 4, 5, true);
    }

    @Test
    public void testDeleteSubTaskEndpoint() throws Exception {
        Mockito.when(subTaskService.removeSubTaskById(1, 2, 5, 9)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/subtasks/1/2/5/9"))
                .andExpect(status().isOk());
        Mockito.verify(subTaskService, Mockito.times(1)).removeSubTaskById(1, 2, 5, 9);
    }

    @Test
    public void testDeleteSubTaskEndpointNotFound() throws Exception {
        Mockito.when(subTaskService.removeSubTaskById(1, 2, 5, 9))
                .thenThrow(NoSuchElementException.class);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/subtasks/1/2/5/9"))
                .andExpect(status().isNotFound());
        Mockito.verify(subTaskService, Mockito.times(1)).removeSubTaskById(1, 2, 5, 9);
    }

    @Test
    public void testDeleteSubTaskEndpointInternalServerError() throws Exception {
        Mockito.when(subTaskService.removeSubTaskById(1, 2, 5, 9))
                .thenThrow(RuntimeException.class);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/subtasks/1/2/5/9"))
                .andExpect(status().isInternalServerError());
        Mockito.verify(subTaskService, Mockito.times(1)).removeSubTaskById(1, 2, 5, 9);
    }

    @Test
    public void testReorderSubTaskEndpoint() throws Exception {
        Mockito.when(subTaskService.reorderSubTask(1, 2, 4, 5,  10)).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/subtasks/1/2/4/reorder/5")
                        .param("newIndex", "10"))
                .andExpect(status().isOk());
        Mockito.verify(subTaskService, Mockito.times(1)).reorderSubTask(1, 2, 4, 5, 10);
    }

    @Test
    public void testReorderSubTaskEndpointNotFound() throws Exception {
        Mockito.when(subTaskService.reorderSubTask(1, 2, 4, 5,  10))
                        .thenThrow(NoSuchElementException.class);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/subtasks/1/2/4/reorder/5")
                        .param("newIndex", "10"))
                .andExpect(status().isNotFound());
        Mockito.verify(subTaskService, Mockito.times(1)).reorderSubTask(1, 2, 4, 5, 10);
    }

}