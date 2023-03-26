package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {
    private Board board;
    private TaskList taskList1;
    private TaskList taskList2;

    @BeforeEach
    public void setup() {
         board = new Board("Test board", new ArrayList<>(), new ArrayList<>());
         taskList1 = new TaskList("TaskList");
         taskList2 = new TaskList("SomeOtherTaskList");
    }

    @Test
    public void constructorTest() {
        assertNotNull(board);
    }

    @Test
    void getName() {
        assertEquals("Test board",board.getName());
    }

    @Test
    void setName() {
        board.setName("New name");
        assertEquals("New name", board.getName());
    }

    @Test
    void getListTaskList() {
        assertTrue(board.getListTaskList().isEmpty());
    }

    @Test
    void setListTaskList() {
        List<TaskList> taskLists = new ArrayList<>();
        taskLists.add(taskList1);
        taskLists.add(taskList2);
        board.setListTaskList(taskLists);
        assertEquals(taskLists, board.getListTaskList());
    }

    @Test
    void addTaskList() {
        board.addTaskList(taskList1);
        assertEquals(taskList1, board.getListTaskList().get(0));
    }

    @Test
    void removeTaskList() {
        board.addTaskList(taskList1);
        board.addTaskList(taskList2);
        board.removeTaskList(taskList1);
        assertEquals(taskList2, board.getListTaskList().get(0));
    }

    @Test
    void getTaskListById() {
        board.addTaskList(taskList1);
        board.addTaskList(taskList2);
        assertEquals(Optional.of(taskList1), board.getTaskListById(taskList1.getId()));
    }


    @Test
    void testEquals() {
        Board board2 = new Board("Other board", new ArrayList<>(), new ArrayList<>());
        assertNotEquals(board, board2);
    }

    @Test
    void testHashCode() {
        Board board2 = new Board("Other board", new ArrayList<>(), new ArrayList<>());
        assertNotEquals(board.hashCode(), board2.hashCode());
    }

    @Test
    void testToString() {
        board.addTaskList(taskList1);
        String boardString = "Board (" + board.getId() + ") : Test board\nLists:\n"
                + taskList1.toString() + "\nTags:\n" + "Members:\n";
        assertEquals(boardString, board.toString());
    }
}
