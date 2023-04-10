package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {
    private Board board;
    private Board emptyBoard;
    private TaskList taskList1;
    private TaskList taskList2;

    @BeforeEach
    public void setup() {
         board = new Board("Test board", new ArrayList<>(),
             new ArrayList<>(List.of(new Tag("tag1", "#FFFFFF"))));
        taskList1 = new TaskList("TaskList");
         taskList2 = new TaskList("SomeOtherTaskList");
         emptyBoard = new Board("Empty", new ArrayList<>(), new ArrayList<>(), "11111");
    }

    @Test
    public void constructorTest() {
        assertNotNull(board);
        assertNotNull(emptyBoard);
    }

    @Test
    public void emptyConstructorTest() {
        Board testBoard = new Board();
        assertNotNull(testBoard);
        assertEquals("", testBoard.getName());
    }

    @Test
    void getName() {
        assertEquals("Test board",board.getName());
        assertEquals("Empty",emptyBoard.getName());
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
    void getPresets() {
        assertEquals(board.getTaskPresets().size(), 0);
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
        board.addBoardMember("User1");
        long id = board.getTags().get(0).id;
        String boardString = "Board (" + board.getId() + ") : Test board\nLists:\n"
            + taskList1.toString() + "\nTags:\n" +
            "Tag (" + id + ") : tag1 -> background=#FFFFFF font=#000000\n" + "Members:\nUser1\n";
        assertEquals(boardString, board.toString());
        board.removeBoardMember("User1");
        String boardString2 = "Board (" + board.getId() + ") : Test board\nLists:\n"
                + taskList1.toString() + "\nTags:\n" +
                "Tag (" + id + ") : tag1 -> background=#FFFFFF font=#000000\n" + "Members:\n";
        assertEquals(boardString2, board.toString());
    }

    @Test
    public void testGetTags(){
        assertEquals(1,  board.getTags().size());
        assertEquals("tag1", board.getTags().get(0).getName());
    }

    @Test
    public void testAddTag(){
        assertThrows(IndexOutOfBoundsException.class, () -> board.getTags().get(1));
        board.addTag(new Tag("tag123", "FFFFFF"));
        assertEquals("tag123", board.getTags().get(1).getName());
    }

    @Test
    public void testDeleteTag(){
        board.addTag(new Tag("tag123", "FFFFFF"));
        Tag t = new Tag("tag2", "FFFFFF");
        board.addTag(t);
        assertEquals(3, board.getTags().size());
        board.removeTag(t);
        assertEquals(2, board.getTags().size());
    }

    @Test
    public void testGetMembers(){
        board.addBoardMember("Haha");
        board.addBoardMember("AHAHAHAHA");
        ArrayList<String> list = new ArrayList<>(List.of("Haha", "AHAHAHAHA"));
        assertEquals(list, board.getBoardMembers());
    }

    @Test
    public void testSetTags(){
        List<Tag> tags = new ArrayList<>(List.of(new Tag("audio", "FFFFFF"),
                new Tag("video", "FFFFFF"),
                new Tag("auvio", "FFFFFF")));
        board.setTags(tags);
        assertEquals(tags, board.getTags());
    }

    @Test
    public void testGetInviteKey(){
        assertEquals("11111", emptyBoard.getInviteKey());
    }

    @Test
    public void testCloneBoard() {
        Board b = new Board();
        Board b2 = new Board(b);
        assertNotNull(b2);
    }

    @Test
    public void testGetTagById(){
        List<Tag> tags = new ArrayList<>(List.of(new Tag("audio", "FFFFFF"),
                new Tag("video", "FFFFFF"),
                new Tag("auvio", "FFFFFF")));
        tags.get(0).id = 5;
        board.setTags(tags);
        assertEquals(tags, board.getTags());
        assertTrue(board.getTagById(5).isPresent());
        assertEquals(board.getTagById(5).get(), tags.get(0));
    }

    @Test
    public void initTest(){
        emptyBoard.initBoard();
        assertEquals(3, emptyBoard.getListTaskList().size());
        assertEquals(1, emptyBoard.getTaskPresets().size());
    }

    @Test
    public void testColorScheme(){
        BoardColorScheme s = new BoardColorScheme();
        assertNotEquals(s, board.getBoardColorScheme());
        board.setBoardColorScheme(s);
        assertEquals(s, board.getBoardColorScheme());
    }

    @Test
    public void testAddRemovePresets(){
        TaskPreset preset = new TaskPreset("ONE");
        preset.setDefault(true);
        TaskPreset preset2 = new TaskPreset("TWO");
        board.addTaskPreset(preset);
        board.addTaskPreset(preset2);
        board.addTaskList(taskList1);
        Task task = new Task("W,", "Q,");
        task.setTaskPreset(preset);
        taskList1.addTask(task);
        for(Task t : board.getListTaskList().stream().flatMap(l -> l.getTasks().stream()).toList())
            t.setTaskPreset(preset2);
        assertEquals(2, board.getTaskPresets().size());
        board.removeTaskPreset(preset2.id);
        board.removeTaskPreset(-314159);
        assertEquals(1, board.getTaskPresets().size());
    }

    @Test
    public void testFindPresets(){
        TaskPreset preset = new TaskPreset("ONE");
        preset.setDefault(true);
        TaskPreset preset2 = new TaskPreset("TWO");
        board.addTaskPreset(preset);
        board.addTaskPreset(preset2);
        assertEquals(preset, board.findDefaultTaskPreset());
        preset.setDefault(false);
        assertNull(board.findDefaultTaskPreset());
    }

    @Test
    public void testUpdatePresets(){
        TaskPreset preset = new TaskPreset("ONE");
        preset.setDefault(true);
        TaskPreset preset2 = new TaskPreset("TWO");
        board.addTaskPreset(preset);
        board.addTaskPreset(preset2);
        preset2.id = 4;
        for(Task t : board.getListTaskList().stream().flatMap(l -> l.getTasks().stream()).toList())
            t.setTaskPreset(preset2);
        preset2.setDefault(true);
        board.updateTaskPreset(preset2);
        assertEquals(preset2, board.findDefaultTaskPreset());
    }
}
