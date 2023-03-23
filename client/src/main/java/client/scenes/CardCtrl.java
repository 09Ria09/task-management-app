package client.scenes;

import client.utils.TaskListUtils;
import commons.Task;
import commons.TaskList;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;

public class CardCtrl {
    private Task task;
    @FXML
    public Text text;

    private ListCtrl listController;
    private TaskListUtils taskListUtils;

    /**
     * This initializes the card using a task
     * @param task the task used for initialization
     */
    @Inject
    public void initialize(final Task task, final ListCtrl listCtrl,
                           final TaskListUtils listUtils) {
        this.task= task;
        this.text.setText(task.getName());
        this.listController = listCtrl;
        this.taskListUtils = listUtils;
    }

    /**
     * @param o an object
     * @return true if the object provided is the same to this object
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardCtrl cardCtrl = (CardCtrl) o;
        return Objects.equals(task, cardCtrl.task);
    }

    /**
     * @return the hashCode of this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(task);
    }

    public boolean moveUp() {
        TaskList taskList = listController.getTaskList();

        List<Task> tasks = taskList.getTasks();
        int index = tasks.indexOf(task);
        if(index > 0) {
            taskListUtils.reorderTask(listController.getBoardID(), taskList.id, task.id, index-1);
            listController.hardRefresh(taskList, listController.getBoardID());
            return true;
        } else {
            return false;
        }
    }

    public boolean moveDown() {
        TaskList taskList = listController.getTaskList();

        List<Task> tasks = taskList.getTasks();
        int index = tasks.indexOf(task);
        if(index < tasks.size()-1) {
            taskListUtils.reorderTask(listController.getBoardID(), taskList.id, task.id, index+1);
            listController.hardRefresh(taskList, listController.getBoardID());
            return true;
        } else {
            return false;
        }
    }
}
