package server;

import commons.Board;
import commons.TaskList;
import org.springframework.beans.factory.annotation.Autowired;
import server.database.BoardRepository;

import java.util.ArrayList;
import java.util.List;

public class ListService {

    @Autowired
    private BoardRepository boardRepository;


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
     * Removes the list specified by a board if and a list id
     *
     * @param boardID the id of the board where the list will be removed
     * @param taskListID the id of the task list that will be removed
     */
    public void removeListByID(final long boardID, final long taskListID) {
        boardRepository.findById(boardID)
                .ifPresent(x -> {
                    x.getTaskListById(taskListID).ifPresent(x::removeTaskList);
                    boardRepository.save(x);
                });
    }


}
