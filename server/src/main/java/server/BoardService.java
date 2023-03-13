package server;

import commons.Board;
import commons.Task;
import commons.TaskList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.BoardRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class BoardService {

    /**
     * The JPA repository that contains all the data about the boards, lists and tasks.
     * It is constructed automatically.
     */
    @Autowired
    private BoardRepository boardRepository;

    /**
     * @return a list of all the boards stored in the repository.
     */
    public List<Board> getBoards(){
        return new ArrayList<Board>(boardRepository.findAll());
    }

    /**
     *
     * @param id the id of the board that will be acessed.
     * @return the board with the corresponding id, or null if there is none.
     */
    public Board getBoard(final long id){
        return boardRepository.findById(id).orElse(null);
    }

    /**
     * Adds a new board to the repository to make it persistent.
     * @param board the board that will be added.
     */
    public void addBoard(final Board board){
        boardRepository.save(board);
    }

    /**
     * Removes a board from the repository.
     * @param board the board to remove.
     */
    public void removeBoard(final Board board){
        boardRepository.delete(board);
    }

    /**
     * Change the name of a board to a new name, if the provided id is assigned to a board.
     * @param boardID the id of the board to rename.
     * @param newName the new name of the board.
     */
    public void renameBoard(final long boardID, final String newName){
        boardRepository.findById(boardID)
                .ifPresent(x -> x.setName(newName));
    }

    /**
     * Returns all the task lists of the board assigned to the provided id.
     * It returns an empty list if the id is not assigned to a board.
     * @param boardId the id of the board from which we want to access the board.
     * @return a list of all the task lists of the board.
     */
    public List<TaskList> getLists(final long boardId){
        System.out.println("getting lists");
        return boardRepository.findById(boardId)
                .map(Board::getListTaskList)
                .orElse(new ArrayList<>());
    }

    /**
     * Returns a specific list of a specific board, by ids.
     * Both the id of the board and the id of the list are needed to access it.
     * @param boardId the id of the board.
     * @param listId the id of the task list.
     * @return the task list corresponding to the specified ids.
     */
    public TaskList getList(final long boardId, final long listId){
        return boardRepository.findById(boardId)
                .flatMap(b -> b.getTaskListById(listId))
                .orElse(null);
    }

    /**
     * Adds a new task list to a specific board in the JPARepository.
     * @param boardID the id of the board where the list will be added.
     * @param list the task list that will be added to the board and made persistent.
     */
    public void addList(final long boardID, final TaskList list){
        System.out.println("add list : " + list.getName());
        boardRepository.findById(boardID)
                .ifPresent(x -> {
                    x.addTaskList(list);
                    boardRepository.save(x);
                });
    }

    /**
     * Removes the specified list from the specified board.
     * @param boardID the id of the board where the list will be removed.
     * @param list the task list that will be removed from the board.
     */
    public void removeList(final long boardID, final TaskList list){
        boardRepository.findById(boardID)
                .ifPresent(x -> {
                    x.removeTaskList(list);
                    boardRepository.save(x);
                });
    }

    /**
     * Changes the name of a task list in a board to a new name.
     * @param boardID the id of the board where the list to rename is.
     * @param listID the id of the list to rename.
     * @param newName the new name of the list.
     */
    public void renameList(final long boardID, final long listID, final String newName){
        boardRepository.findById(boardID)
                .ifPresent( x-> {
                    x.getTaskListById(listID).ifPresent(y -> y.setName(newName));
                    boardRepository.save(x);
                });
    }

    /**
     * Gets a list of the tasks that are in a specified task list inside a specific board.
     * @param boardId the id of the board.
     * @param listId the id of the list.
     * @return the list of the tasks that are in the task list corresponding to the ids given.
     */
    public List<Task> getTasks(final long boardId, final long listId){
        return boardRepository.findById(boardId)
                .flatMap(b -> b.getTaskListById(listId))
                .map(TaskList::getTasks)
                .orElse(new ArrayList<>());
    }

    /**
     * Gets a specific task from a specific task list in a specific board, by ids.
     * The id of the board, the list and the task are needed to access it.
     * @param boardId the id of the board.
     * @param listId the id of the list.
     * @param taskId the id of the task.
     * @return the task corresponding to the given ids.
     */
    public Task getTask(final long boardId, final long listId, final long taskId){
        return boardRepository.findById(boardId)
                .flatMap(b -> b.getTaskListById(listId))
                .flatMap(l -> l.getTaskById(taskId))
                .orElse(null);
    }

    /**
     * Adds a new task in a specific list in a specific board to the JPA Repository.
     * @param boardID the id of the board where the task will be added.
     * @param listID the id of the list where the task will be added.
     * @param task the task that will be added to the task list and made persistent.
     */
    public void addTask(final long boardID, final long listID, final Task task){
        boardRepository.findById(boardID)
                .ifPresent(b -> {
                    b.getTaskListById(listID).ifPresent(l -> l.addTask(task));
                    boardRepository.save(b);
                });
    }

    /**
     * Removes a task from a list in a specific board.
     * @param boardID the id of the board where the task will be removed.
     * @param listID the id of the list from which the task will be removed.
     * @param task the task that will be removed.
     */
    public void removeTask(final long boardID, final long listID, final Task task){
        boardRepository.findById(boardID)
                .ifPresent(b -> {
                    b.getTaskListById(listID).ifPresent(l -> l.removeTask(task));
                    boardRepository.save(b);
                });

    }

    /**
     * Changes the name of a task to a new name.
     * @param boardID the id of the board where the task will be renamed.
     * @param listID the id of the list in which the task will be renamed.
     * @param taskId the id of the task to rename.
     * @param newName the new name of the task.
     */
    public void renameTask(final long boardID, final long listID,
                           final long taskId, final String newName){
        boardRepository.findById(boardID)
                .ifPresent(b -> {
                    b.getTaskListById(listID).flatMap(l -> l.getTaskById(taskId))
                                            .ifPresent(t -> t.setName(newName));
                    boardRepository.save(b);
                });

    }
}
