package server.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Task;
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
import server.services.TaskService;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @MockBean
    private BoardService boardService;

    @MockBean
    private ListService listService;

    @Test
    public void testGetAllTaskEndpoint() throws Exception {
        List<Task> tasks = List.of(new Task("Test Task", "nothing"),
                new Task("Task 1", "descr"));
        Mockito.when(taskService.getTasks(1, 4)).thenReturn(tasks);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tasks/1/tasklist/4/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Test Task")))
                .andExpect(jsonPath("$[0].description", is("nothing")))
                .andExpect(jsonPath("$[1].name", is("Task 1")))
                .andExpect(jsonPath("$[1].description", is("descr")));
    }

    @Test
    public void testGetTaskEndpoint() throws Exception {
        Task task = new Task("Test Task !", "Random Description123");
        Mockito.when(taskService.getTask(1, 2, 7)).thenReturn(task);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tasks/1/tasklist/2/task/7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is("Test Task !")))
                .andExpect(jsonPath("description", is("Random Description123")));
    }

    @Test
    public void testDeleteTaskEndpoint() throws Exception {
        Mockito.when(taskService.removeTaskById(1, 2, 5)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/tasks/1/2/5"))
                .andExpect(status().isOk());
        Mockito.verify(taskService, Mockito.times(1)).removeTaskById(1, 2, 5);
    }

    @Test
    public void testRenameTaskEndpoint() throws Exception {
        Mockito.when(taskService.renameTask(1, 2, 4, "Test 2")).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/tasks/1/2/4")
                        .param("name", "Test 2"))
                .andExpect(status().isOk());
        Mockito.verify(taskService, Mockito.times(1)).renameTask(1, 2, 4, "Test 2");
    }

    @Test
    public void testAddTaskEndpoint() throws Exception {
        Task newTask = new Task("New Task", "");
        String requestBody = new ObjectMapper().writeValueAsString(newTask);
        Mockito.when(taskService.addTask(1, 2, newTask)).thenReturn(newTask);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/tasks/1/2/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is("New Task")));
        Mockito.verify(taskService, Mockito.times(1)).addTask(1, 2, newTask);
    }
}