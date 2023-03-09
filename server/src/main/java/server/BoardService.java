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

    @Autowired
    private BoardRepository boardRepository;

    public List<Board> getBoards(){
        return new ArrayList<Board>(boardRepository.findAll());
    }

    public Board getBoard(final long id){
        return boardRepository.findById(id).orElse(null);
    }

    public void addBoard(final Board board){
        boardRepository.save(board);
    }

    public void removeBoard(final Board board){
        boardRepository.delete(board);
    }

    public void renameBoard(final long boardID, final String newName){
        boardRepository.findById(boardID)
                .ifPresent(x -> x.setName(newName));
    }

    public List<TaskList> getLists(final long boardId){
        System.out.println("getting lists");
        return boardRepository.findById(boardId)
                .map(Board::getListTaskList)
                .orElse(new ArrayList<>());
    }

    public TaskList getList(final long boardId, final long listId){
        return boardRepository.findById(boardId)
                .flatMap(b -> b.getTaskListById(listId))
                .orElse(null);
    }

    public void addList(final long boardID, final TaskList list){
        System.out.println("add list : " + list.getName());
        boardRepository.findById(boardID)
                .ifPresent(x -> {
                    x.addTaskList(list);
                    boardRepository.save(x);
                });
    }

    public void removeList(final long boardID, final TaskList list){
        boardRepository.findById(boardID)
                .ifPresent(x -> {
                    x.removeTaskList(list);
                    boardRepository.save(x);
                });
    }

    public void renameList(final long boardID, final long listID, final String newName){
        boardRepository.findById(boardID)
                .ifPresent( x-> {
                    x.getTaskListById(listID).ifPresent(y -> y.setName(newName));
                    boardRepository.save(x);
                });
    }

    public List<Task> getTasks(final long boardId, final long listId){
        return boardRepository.findById(boardId)
                .flatMap(b -> b.getTaskListById(listId))
                .map(TaskList::getTasks)
                .orElse(new ArrayList<>());
    }

    public Task getTask(final long boardId, final long listId, final long taskId){
        return boardRepository.findById(boardId)
                .flatMap(b -> b.getTaskListById(listId))
                .flatMap(l -> l.getTaskById(taskId))
                .orElse(null);
    }

    public void addTask(final long boardID, final long listID, final Task task){
        boardRepository.findById(boardID)
                .ifPresent(b -> {
                    b.getTaskListById(listID).ifPresent(l -> l.addTask(task));
                    boardRepository.save(b);
                });
    }

    public void removeTask(final long boardID, final long listID, final Task task){
        boardRepository.findById(boardID)
                .ifPresent(b -> {
                    b.getTaskListById(listID).ifPresent(l -> l.removeTask(task));
                    boardRepository.save(b);
                });

    }

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
