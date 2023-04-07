package server.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Board;
import commons.Tag;
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
import server.services.TagService;
import server.services.TaskService;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TagController.class)
public class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @MockBean
    private BoardService boardService;

    @MockBean
    private ListService listService;

    @MockBean TagService tagService;

    @MockBean
    private SimpMessagingTemplate messagingTemplate;

    @Test
    public void testGetAllBoardTagsEndpoint() throws Exception {
        List<Tag> tags = List.of(new Tag("Name", "#FFFFFF"),
                new Tag("NotName", "#FFFFFA"));
        Mockito.when(tagService.getBoardTags(Mockito.any(Long.class))).thenReturn(tags);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tags/1/tags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Name")))
                .andExpect(jsonPath("$[0].colorBackground", is("#FFFFFF")))
                .andExpect(jsonPath("$[1].name", is("NotName")))
                .andExpect(jsonPath("$[1].colorBackground", is("#FFFFFA")));
    }

    @Test
    public void testGetAllTaskTagsEndpoint() throws Exception {
        List<Tag> tags = List.of(new Tag("Name", "#FFFFFF"),
                new Tag("NotName", "#FFFFFA"));
        Mockito.when(tagService.getTaskTags(1, 2, 3)).thenReturn(tags);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tags/1/2/3/tags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Name")))
                .andExpect(jsonPath("$[0].colorBackground", is("#FFFFFF")))
                .andExpect(jsonPath("$[1].name", is("NotName")))
                .andExpect(jsonPath("$[1].colorBackground", is("#FFFFFA")));
    }

    @Test
    public void testGetBoardTagEndpoint() throws Exception {
        Tag tag1 = new Tag("Name", "#FFFFFF");
        Mockito.when(tagService.getBoardTagByID(1, 4)).thenReturn(tag1);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tags/1/4"))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("name", is("Name")))
                .andExpect(jsonPath("colorBackground", is("#FFFFFF")));
    }

    @Test
    public void testGetTaskTagEndpoint() throws Exception {
        Tag tag = new Tag("Test Tag !", "#FFFFFF");
        Mockito.when(tagService.getTaskTagByID(1, 2, 5, 8)).thenReturn(tag);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tags/1/2/5/8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is("Test Tag !")))
                .andExpect(jsonPath("colorBackground", is("#FFFFFF")));
    }

    @Test
    public void testRenameTagEndpoint() throws Exception {
        Mockito.when(tagService.renameTag(1, 3, "NewTagName")).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/tags/1/3/rename")
                        .param("name", "NewTagName"))
                .andExpect(status().isOk());
        Mockito.verify(tagService, Mockito.times(1)).renameTag(1, 3, "NewTagName");
    }

    @Test
    public void testRecolorTagEndpoint() throws Exception {
        Mockito.when(tagService.recolorTag(1, 3, "NewTagColor",
            "NewTagColor1")).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/tags/1/3/recolor")
                        .param("backgroundColor", "NewTagColor")
                .param("fontColor", "NewTagColor1"))
                .andExpect(status().isOk());
        Mockito.verify(tagService, Mockito.times(1))
            .recolorTag(1, 3, "NewTagColor", "NewTagColor1");
    }

    @Test
    public void testAddBoardTagEndpoint() throws Exception {
        Tag newTag = new Tag("Name", "#FFFFFF");
        String requestBody = new ObjectMapper().writeValueAsString(newTag);
        Mockito.when(tagService.addBoardTag(1, newTag)).thenReturn(newTag);
        Mockito.when(tagService.getBoardTags(1)).thenReturn(List.of(newTag));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/tags/1/tag")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is("Name")));
        Mockito.verify(tagService, Mockito.times(1)).addBoardTag(1, newTag);
    }

    @Test
    public void testAddTaskTagEndpoint() throws Exception {
        Tag newTag = new Tag("Name", "#424242");
        String requestBody = new ObjectMapper().writeValueAsString(newTag);
        Mockito.when(tagService.addTaskTag(1, 2, 3, newTag.id)).thenReturn(newTag);
        Mockito.when(tagService.getBoardTags(1)).thenReturn(List.of(newTag));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/tags/1/2/3/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is("Name")));
        Mockito.verify(tagService, Mockito.times(1)).addTaskTag(1, 2, 3, newTag.id);
    }

    @Test
    public void testDeleteBoardTagEndpoint() throws Exception {
        Mockito.when(tagService.getBoard(1)).thenReturn(new Board());
        Mockito.when(tagService.removeBoardTag(1, 2)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/tags/1/delete/2"))
                .andExpect(status().isOk());
        Mockito.verify(tagService, Mockito.times(1)).removeBoardTag(1, 2);
    }

    @Test
    public void testDeleteTaskTagEndpoint() throws Exception {
        Mockito.when(tagService.removeTaskTag(1, 2, 4, 7)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/tags/1/2/4/delete/7"))
                .andExpect(status().isOk());
        Mockito.verify(tagService, Mockito.times(1)).removeTaskTag(1, 2, 4, 7);
    }

    @Test
    public void testGetAllBoardTagsEndpointNotFound() throws Exception {
        Mockito.when(tagService.getBoardTags(1234)).thenThrow(NoSuchElementException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tags/1234/tags"))
                .andExpect(status().isNotFound());

        Mockito.verify(tagService, Mockito.times(1)).getBoardTags(1234);
    }

    @Test
    public void testGetAllTaskTagsEndpointNotFound() throws Exception {
        Mockito.when(tagService.getTaskTags(1234, 4321, 23)).thenThrow(NoSuchElementException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tags/1234/4321/23/tags"))
                .andExpect(status().isNotFound());

        Mockito.verify(tagService, Mockito.times(1)).getTaskTags(1234, 4321, 23);
    }

    @Test
    public void testGetTaskTagEndpointNotFound() throws Exception {
        Mockito.when(tagService.getTaskTagByID(1234, 4321, 23, 32)).thenThrow(NoSuchElementException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tags/1234/4321/23/32"))
                .andExpect(status().isNotFound());

        Mockito.verify(tagService, Mockito.times(1)).getTaskTagByID(1234, 4321, 23, 32);
    }

    @Test
    public void testGetBoardTagEndpointNotFound() throws Exception {
        Mockito.when(tagService.getBoardTagByID(1234, 4321)).thenThrow(NoSuchElementException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tags/1234/4321"))
                .andExpect(status().isNotFound());

        Mockito.verify(tagService, Mockito.times(1)).getBoardTagByID(1234, 4321);
    }

    @Test
    public void testAddBoardTagEndpointBadRequest() throws Exception {
        Tag tag = new Tag("", "#FFFFFF");
        String requestBody = new ObjectMapper().writeValueAsString(tag);
        Mockito.when(tagService.addBoardTag(1, tag)).thenReturn(tag);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/tags/1/tag")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
        Mockito.verify(tagService, Mockito.times(0))
                .addBoardTag(1, tag);
    }

    @Test
    public void testAddTaskTagEndpointBadRequest() throws Exception {
        Tag tag = new Tag("", "#424242");
        String requestBody = new ObjectMapper().writeValueAsString(tag);
        Mockito.when(tagService.addTaskTag(1, 2, 3, tag.id)).thenReturn(tag);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/tags/1/2/3/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
        Mockito.verify(tagService, Mockito.times(0))
                .addTaskTag(1, 2, 3, tag.id);
    }

    @Test
    public void testAddBoardTagEndpointNotFound() throws Exception {
        Tag tag = new Tag("Name", "#FFFFFF");
        String requestBody = new ObjectMapper().writeValueAsString(tag);
        Mockito.when(tagService.addBoardTag(1, tag)).thenThrow(NoSuchElementException.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/tags/1/tag")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound());
        Mockito.verify(tagService, Mockito.times(1))
                .addBoardTag(1, tag);
    }

    @Test
    public void testAddTaskTagEndpointNotFound() throws Exception {
        Tag tag = new Tag("Name", "#424242");
        String requestBody = new ObjectMapper().writeValueAsString(tag);
        Mockito.when(tagService.addTaskTag(1, 2, 3, tag.id)).thenThrow(NoSuchElementException.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/tags/1/2/3/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound());
        Mockito.verify(tagService, Mockito.times(1))
                .addTaskTag(1, 2, 3, tag.id);
    }

    @Test
    public void renameTagEndpointBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/tags/1/2/rename")
                        .param("name", ""))
                .andExpect(status().isBadRequest());
        Mockito.verify(tagService, Mockito.times(0))
                .renameTag(1, 2, "");
    }

    @Test
    public void recolorTagEndpointBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/tags/1/3/recolor")
                .param("backgroundColor", "")
                .param("fontColor", "NewTagColor"))
            .andExpect(status().isBadRequest());
        Mockito.verify(tagService, Mockito.times(0))
            .recolorTag(1, 3, "", "");
    }

    @Test
    public void recolorTagEndpointBadRequest1() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/tags/1/3/recolor")
                .param("backgroundColor", "NewTagColor")
                .param("fontColor", ""))
            .andExpect(status().isBadRequest());
        Mockito.verify(tagService, Mockito.times(0))
            .recolorTag(1, 3, "NewTagColor", "");
    }

    @Test
    public void renameTagEndpointNotFound() throws Exception {
        Mockito.when(tagService.renameTag(1, 2, "Name"))
                .thenThrow(NoSuchElementException.class);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/tags/1/2/rename")
                        .param("name", "Name"))
                .andExpect(status().isNotFound());

        Mockito.verify(tagService, Mockito.times(1))
                .renameTag(1, 2, "Name");
    }

    @Test
    public void recolorTagEndpointNotFound() throws Exception {
        Mockito.when(tagService.recolorTag(1, 2, "#424242", "#434343"))
                .thenThrow(NoSuchElementException.class);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/tags/1/2/recolor")
                        .param("backgroundColor", "#424242")
                        .param("fontColor", "#434343"))
                .andExpect(status().isNotFound());

        Mockito.verify(tagService, Mockito.times(1))
                .recolorTag(1, 2, "#424242", "#434343");
    }

    @Test
    public void deleteBoardTagEndpointNotFound() throws Exception {
        Mockito.when(tagService.removeBoardTag(1, 2))
                .thenThrow(NoSuchElementException.class);
        Mockito.when(tagService.getBoard(1)).thenReturn(new Board());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/tags/1/delete/2"))
                .andExpect(status().isNotFound());

        Mockito.verify(tagService, Mockito.times(1))
                .removeBoardTag(1, 2);
    }

    @Test
    public void deleteTaskTagEndpointNotFound() throws Exception {
        Mockito.when(tagService.removeTaskTag(1, 2, 5, 9))
                .thenThrow(NoSuchElementException.class);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/tags/1/2/5/delete/9"))
                .andExpect(status().isNotFound());

        Mockito.verify(tagService, Mockito.times(1))
                .removeTaskTag(1, 2, 5, 9);
    }

}
