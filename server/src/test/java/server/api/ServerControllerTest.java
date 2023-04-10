package server.api;

import commons.Board;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ServerController.class)
public class ServerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCheckIsTalioServer() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/talio"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(true)));
    }

    @Test
    public void testCheckIsNotTalioServer() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/talioqqqqqqqqq"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testGetAdminKey() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/talio/admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", lessThanOrEqualTo(999999)));
    }
}
