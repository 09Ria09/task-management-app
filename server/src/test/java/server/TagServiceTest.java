package server;

import commons.Board;
import commons.Tag;
import commons.Task;
import commons.TaskList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import server.api.TestBoardRepository;
import server.services.BoardService;
import server.services.ListService;
import server.services.TagService;
import server.services.TaskService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TagServiceTest {

    private BoardService boardService;
    private ListService listService;
    private TaskService taskService;
    private TagService tagService;

    @BeforeEach
    public void setup(){
        boardService = new BoardService();
        listService = new ListService();
        taskService = new TaskService();
        tagService = new TagService();
        TestBoardRepository repo = new TestBoardRepository();
        ReflectionTestUtils.setField(tagService, "boardRepository", repo);
        ReflectionTestUtils.setField(taskService, "boardRepository", repo);
        ReflectionTestUtils.setField(boardService, "boardRepository", repo);
        ReflectionTestUtils.setField(listService, "boardRepository", repo);
    }

    @Test
    public void testGetAllBoardTags(){
        Board b = new Board();
        Tag tag = new Tag("Name", "Color");
        boardService.addBoard(b);
        assertEquals(0, tagService.getBoardTags(b.id).size());
        tagService.addBoardTag(b.id, tag);
        assertTrue(tagService.getBoardTags(b.id).contains(tag));
    }

    @Test
    public void testGetAllTaskTags(){
        Board b = new Board();
        Tag tag = new Tag("Name", "Color");
        boardService.addBoard(b);
        assertEquals(0, tagService.getBoardTags(b.id).size());
        tagService.addBoardTag(b.id, tag);
        assertTrue(tagService.getBoardTags(b.id).contains(tag));

        TaskList taskList = new TaskList("List");
        listService.addList(b.id, taskList);
        Task task = new Task("Task", "Desc");
        taskService.addTask(b.id, taskList.id, task);
        assertEquals(0, tagService.getTaskTags(b.id, taskList.id, task.id).size());
        tagService.addTaskTag(b.id, taskList.id, task.id, tag.id);
        assertTrue(tagService.getTaskTags(b.id, taskList.id, task.id).contains(tag));
    }

    @Test
    public void testGetBoardTag(){
        Board b = new Board();
        Tag tag = new Tag("Name", "Color");
        boardService.addBoard(b);
        tagService.addBoardTag(b.id, tag);
        assertEquals(tagService.getBoardTagByID(b.id, tag.id), tag);
    }

    @Test
    public void testGetTaskTag(){
        Board b = new Board();
        Tag tag = new Tag("Name", "Color");
        boardService.addBoard(b);
        tagService.addBoardTag(b.id, tag);

        TaskList taskList = new TaskList("List");
        listService.addList(b.id, taskList);
        Task task = new Task("Task", "Desc");
        taskService.addTask(b.id, taskList.id, task);
        tagService.addTaskTag(b.id, taskList.id, task.id, tag.id);
        assertEquals(tagService.getTaskTagByID(b.id, taskList.id, task.id, tag.id), tag);
    }

    @Test
    public void testRenameTag(){
        Board b = new Board();
        Tag tag = new Tag("Name", "Color");
        boardService.addBoard(b);
        tagService.addBoardTag(b.id, tag);
        assertEquals(tagService.getBoardTagByID(b.id, tag.id), tag);
        tagService.renameTag(b.id, tag.id, "NewName");
        assertEquals(tagService.getBoardTagByID(b.id, tag.id).getName(), "NewName");
    }

    @Test
    public void testRecolorTag(){
        Board b = new Board();
        Tag tag = new Tag("Name", "Color");
        boardService.addBoard(b);
        tagService.addBoardTag(b.id, tag);
        assertEquals(tagService.getBoardTagByID(b.id, tag.id), tag);
        tagService.recolorTag(b.id, tag.id, "NewColor");
        assertEquals(tagService.getBoardTagByID(b.id, tag.id).getColor(), "NewColor");
    }

    @Test
    public void testRemoveBoardTag(){
        Board b = new Board();
        Tag tag = new Tag("Name", "Color");
        boardService.addBoard(b);
        tagService.addBoardTag(b.id, tag);
        assertEquals(tagService.getBoardTagByID(b.id, tag.id), tag);
        tagService.removeBoardTag(b.id, tag.id);
        assertEquals(0, tagService.getBoardTags(b.id).size());
    }

    @Test
    public void testRemoveTaskTag(){
        Board b = new Board();
        Tag tag = new Tag("Name", "Color");
        boardService.addBoard(b);
        tagService.addBoardTag(b.id, tag);

        TaskList taskList = new TaskList("List");
        listService.addList(b.id, taskList);
        Task task = new Task("Task", "Desc");
        taskService.addTask(b.id, taskList.id, task);
        tagService.addTaskTag(b.id, taskList.id, task.id, tag.id);
        assertEquals(tagService.getTaskTagByID(b.id, taskList.id, task.id, tag.id), tag);
        tagService.removeTaskTag(b.id, taskList.id, task.id, tag.id);
        assertEquals(0, tagService.getTaskTags(b.id, taskList.id, task.id).size());
    }

    @Test
    public void testRemoveBoardAndTaskTag(){
        Board b = new Board();
        Tag tag = new Tag("Name", "Color");
        boardService.addBoard(b);
        tagService.addBoardTag(b.id, tag);

        TaskList taskList = new TaskList("List");
        listService.addList(b.id, taskList);
        Task task = new Task("Task", "Desc");
        taskService.addTask(b.id, taskList.id, task);
        tagService.addTaskTag(b.id, taskList.id, task.id, tag.id);
        tagService.removeBoardTag(b.id, tag.id);
        assertEquals(0, tagService.getBoardTags(b.id).size());
        assertEquals(0, tagService.getTaskTags(b.id, taskList.id, task.id).size());
    }

    @Test
    public void testRemoveTaskNotBoardTag(){
        Board b = new Board();
        Tag tag = new Tag("Name", "Color");
        boardService.addBoard(b);
        tagService.addBoardTag(b.id, tag);

        TaskList taskList = new TaskList("List");
        listService.addList(b.id, taskList);
        Task task = new Task("Task", "Desc");
        taskService.addTask(b.id, taskList.id, task);
        tagService.addTaskTag(b.id, taskList.id, task.id, tag.id);
        tagService.removeTaskTag(b.id, taskList.id, task.id, tag.id);
        assertEquals(1, tagService.getBoardTags(b.id).size());
        assertEquals(0, tagService.getTaskTags(b.id, taskList.id, task.id).size());
    }
}
