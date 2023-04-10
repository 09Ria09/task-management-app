package client.utils;

import client.customExceptions.TagException;
import client.mocks.MockRestUtils;
import client.mocks.MockServerUtils;
import client.mocks.ResponseClone;
import commons.Tag;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TagUtilsTest {
    private TagUtils tagUtils;
    private MockRestUtils mockRestUtils;
    private MockServerUtils mockServerUtils;

    @BeforeEach
    public void setUp() {
        mockRestUtils = new MockRestUtils();
        mockServerUtils = new MockServerUtils();
        mockServerUtils.setMockRestUtils(mockRestUtils);
        mockServerUtils.setServerAddress("http://example.com");
        tagUtils = new TagUtils(mockServerUtils);
    }

    @Test
    public void testGetBoardTags_success() throws Exception {
        long boardId = 1L;
        Tag tag1 = new Tag();
        tag1.id = 1L;
        tag1.setName("I am so glad we both like Romanian music");
        Tag tag2 = new Tag();
        tag2.id = 2L;
        tag2.setName("Me too. Its actually really good");
        List<Tag> expectedResult = new ArrayList<>();
        expectedResult.add(tag1);
        expectedResult.add(tag2);

        ResponseClone mockResponse = new ResponseClone(Response.Status.OK.getStatusCode(), expectedResult);
        mockRestUtils.setMockResponse(mockResponse);

        List<Tag> result = tagUtils.getBoardTags(boardId);
        assertEquals(expectedResult.size(), result.size());
        for (int i = 0; i < expectedResult.size(); i++) {
            assertEquals(expectedResult.get(i).getId(), result.get(i).getId());
            assertEquals(expectedResult.get(i).getName(), result.get(i).getName());
        }
    }

    @Test
    public void testGetBoardTags_throwsException() {
        long boardId = 1L;
        ResponseClone mockResponse = new ResponseClone(Response.Status.NOT_FOUND.getStatusCode(), null);
        mockRestUtils.setMockResponse(mockResponse);

        assertThrows(TagException.class, () -> {
            tagUtils.getBoardTags(boardId);
        });
    }

    @Test
    public void testGetTaskTags_success() throws Exception {
        long boardId = 1L;
        long listId = 2L;
        long taskId = 1L;
        Tag tag1 = new Tag();
        tag1.id = 1L;
        tag1.setName("What about belgian music?");
        Tag tag2 = new Tag();
        tag2.id = 2L;
        tag2.setName("I haven't heard much of it. But they have good waffles");
        List<Tag> expectedResult = new ArrayList<>();
        expectedResult.add(tag1);
        expectedResult.add(tag2);

        ResponseClone mockResponse = new ResponseClone(Response.Status.OK.getStatusCode(), expectedResult);
        mockRestUtils.setMockResponse(mockResponse);

        List<Tag> result = tagUtils.getTaskTags(boardId, listId, taskId);
        assertEquals(expectedResult.size(), result.size());
        for (int i = 0; i < expectedResult.size(); i++) {
            assertEquals(expectedResult.get(i).getId(), result.get(i).getId());
            assertEquals(expectedResult.get(i).getName(), result.get(i).getName());
        }
    }

    @Test
    public void testGetTaskTags_throwsException() {
        long boardId = 1L;
        long listId = 2L;
        long taskId = 1L;
        ResponseClone mockResponse = new ResponseClone(Response.Status.NOT_FOUND.getStatusCode(), null);
        mockRestUtils.setMockResponse(mockResponse);

        assertThrows(TagException.class, () -> {
            tagUtils.getTaskTags(boardId, listId, taskId);
        });
    }

    @Test
    public void testGetBoardTag_success() throws Exception {
        long boardId = 1L;
        long tagId = 1L;
        Tag expectedResult = new Tag();
        expectedResult.id = tagId;
        expectedResult.setName("True. I got scammed once in Brussels, 8 euros for one");

        ResponseClone mockResponse = new ResponseClone(Response.Status.OK.getStatusCode(), expectedResult);
        mockRestUtils.setMockResponse(mockResponse);

        Tag result = tagUtils.getBoardTag(boardId, tagId);
        assertEquals(expectedResult.getId(), result.getId());
        assertEquals(expectedResult.getName(), result.getName());
    }

    @Test
    public void testGetBoardTag_throwsException() {
        long boardId = 1L;
        long tagId = 1L;
        ResponseClone mockResponse = new ResponseClone(Response.Status.NOT_FOUND.getStatusCode(), null);
        mockRestUtils.setMockResponse(mockResponse);

        assertThrows(TagException.class, () -> {
            tagUtils.getBoardTag(boardId, tagId);
        });
    }

    @Test
    public void testGetTaskTag_success() throws Exception {
        long boardId = 1L;
        long listId = 2L;
        long taskId = 1L;
        long tagId = 1L;
        Tag expectedResult = new Tag();
        expectedResult.id = tagId;
        expectedResult.setName("8 EUROS? NO WAY.");

        ResponseClone mockResponse = new ResponseClone(Response.Status.OK.getStatusCode(), expectedResult);
        mockRestUtils.setMockResponse(mockResponse);

        Tag result = tagUtils.getTaskTag(boardId, listId, taskId, tagId);
        assertEquals(expectedResult.getId(), result.getId());
        assertEquals(expectedResult.getName(), result.getName());
    }

    @Test
    public void testGetTaskTag_throwsException() {
        long boardId = 1L;
        long listId = 1L;
        long taskId = 1L;
        long tagId = 1L;
        ResponseClone mockResponse = new ResponseClone(Response.Status.NOT_FOUND.getStatusCode(), null);
        mockRestUtils.setMockResponse(mockResponse);

        assertThrows(TagException.class, () -> {
            tagUtils.getTaskTag(boardId, listId, taskId, tagId);
        });
    }
    @Test
    public void testAddBoardTag_success() throws Exception {
        long boardId = 1L;
        Tag tagToAdd = new Tag();
        tagToAdd.setName("Yep, you should seen the look on my face");
        Tag expectedResult = new Tag();
        expectedResult.id = 3L;
        expectedResult.setName("Btw, did you know that they have 2 sides of Belgium?");

        ResponseClone mockResponse = new ResponseClone(Response.Status.OK.getStatusCode(), expectedResult);
        mockRestUtils.setMockResponse(mockResponse);

        Tag result = tagUtils.addBoardTag(boardId, tagToAdd);
        assertEquals(expectedResult.getId(), result.getId());
        assertEquals(expectedResult.getName(), result.getName());
    }

    @Test
    public void testAddBoardTag_throwsException() {
        long boardId = 1L;
        Tag tagToAdd = new Tag();
        tagToAdd.setName("Oh, yeah, Flanders and Wallonia");

        ResponseClone mockResponse = new ResponseClone(Response.Status.NOT_FOUND.getStatusCode(), null);
        mockRestUtils.setMockResponse(mockResponse);

        assertThrows(TagException.class, () -> {
            tagUtils.addBoardTag(boardId, tagToAdd);
        });
    }

    @Test
    public void testAddTaskTag_success() throws Exception {
        long boardId = 1L;
        long listId = 1L;
        long taskId = 1L;
        Tag tagToAdd = new Tag();
        tagToAdd.setName("Yep. Anyway, want some Stroopwaffels?");
        Tag expectedResult = new Tag();
        expectedResult.id = 3L;
        expectedResult.setName("What are those??");

        ResponseClone mockResponse = new ResponseClone(Response.Status.OK.getStatusCode(), expectedResult);
        mockRestUtils.setMockResponse(mockResponse);

        Tag result = tagUtils.addTaskTag(boardId, listId, taskId, tagToAdd);
        assertEquals(expectedResult.getId(), result.getId());
        assertEquals(expectedResult.getName(), result.getName());
    }

    @Test
    public void testAddTaskTag_throwsException() {
        long boardId = 1L;
        long listId = 1L;
        long taskId = 1L;
        Tag tagToAdd = new Tag();
        tagToAdd.setName("New Tag");
        ResponseClone mockResponse = new ResponseClone(Response.Status.NOT_FOUND.getStatusCode(), null);
        mockRestUtils.setMockResponse(mockResponse);

        assertThrows(TagException.class, () -> {
            tagUtils.addTaskTag(boardId, listId, taskId, tagToAdd);
        });
    }
    @Test
    public void testRenameTag_success() throws Exception {
        long boardId = 1L;
        long tagId = 1L;
        String newName = "You mean to tell me you've been here for 6 months and you've never had a stroopwaffel?";
        Tag expectedResult = new Tag();
        expectedResult.id = tagId;
        expectedResult.setName(newName);

        ResponseClone mockResponse = new ResponseClone(Response.Status.OK.getStatusCode(), expectedResult);
        mockRestUtils.setMockResponse(mockResponse);

        Tag result = tagUtils.renameTag(boardId, tagId, newName);
        assertEquals(expectedResult.getId(), result.getId());
        assertEquals(expectedResult.getName(), result.getName());
    }

    @Test
    public void testRenameTag_throwsException() {
        long boardId = 1L;
        long tagId = 1L;
        String newName = "Yeah, so?";
        ResponseClone mockResponse = new ResponseClone(Response.Status.NOT_FOUND.getStatusCode(), null);
        mockRestUtils.setMockResponse(mockResponse);

        assertThrows(TagException.class, () -> {
            tagUtils.renameTag(boardId, tagId, newName);
        });
    }

    @Test
    public void testRecolorTag_throwsException() {
        long boardId = 1L;
        long tagId = 1L;
        String newBackgroundColor = "#ABBA01";
        String newFontColor = "#BAAB10";
        ResponseClone mockResponse = new ResponseClone(Response.Status.NOT_FOUND.getStatusCode(), null);
        mockRestUtils.setMockResponse(mockResponse);

        assertThrows(TagException.class, () -> {
            tagUtils.recolorTag(boardId, tagId, newBackgroundColor, newFontColor);
        });
    }

    @Test
    public void testDeleteBoardTag() throws TagException {
        long boardId = 1L;
        long tagId = 1L;
        Tag deletedTag = new Tag();
        deletedTag.id = tagId;
        deletedTag.setName("Such a shame, you should get to know the culture");

        ResponseClone mockResponse = new ResponseClone(Response.Status.OK.getStatusCode(), deletedTag);
        mockRestUtils.setMockResponse(mockResponse);

        Tag result = tagUtils.deleteBoardTag(boardId, tagId);
        assertEquals(deletedTag, result);
    }

    @Test
    public void testDeleteBoardTag_throwsException() {
        long boardId = 1L;
        long tagId = 1L;
        ResponseClone mockResponse = new ResponseClone(Response.Status.NOT_FOUND.getStatusCode(), null);
        mockRestUtils.setMockResponse(mockResponse);

        assertThrows(TagException.class, () -> {
            tagUtils.deleteBoardTag(boardId, tagId);
        });
    }

    @Test
    public void testDeleteTaskTag() throws TagException {
        long boardId = 1L;
        long listId = 1L;
        long taskId = 1L;
        long tagId = 1L;
        Tag deletedTag = new Tag();
        deletedTag.id = tagId;
        deletedTag.setName("Ok, I'll leave for now to try one out");

        ResponseClone mockResponse = new ResponseClone(Response.Status.OK.getStatusCode(), deletedTag);
        mockRestUtils.setMockResponse(mockResponse);

        Tag result = tagUtils.deleteTaskTag(boardId, listId, taskId, tagId);
        assertEquals(deletedTag, result);
    }

    @Test
    public void testDeleteTaskTag_throwsException() {
        long boardId = 1L;
        long listId = 1L;
        long taskId = 1L;
        long tagId = 1L;
        ResponseClone mockResponse = new ResponseClone(Response.Status.NOT_FOUND.getStatusCode(), null);
        mockRestUtils.setMockResponse(mockResponse);

        assertThrows(TagException.class, () -> {
            tagUtils.deleteTaskTag(boardId, listId, taskId, tagId);
        });
    }
}