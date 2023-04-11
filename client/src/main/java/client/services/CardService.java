package client.services;

import client.CustomAlert;
import client.customExceptions.TaskException;
import client.customExceptions.TaskListException;
import client.utils.NetworkUtils;
import client.utils.TaskListUtils;
import client.utils.TaskUtils;
import commons.Task;
import commons.TaskList;
import commons.TaskPreset;
import javafx.scene.control.Alert;

import java.util.List;

public class CardService {

    private TaskUtils taskUtils;
    private Task task;
    private long boardID;
    private TaskList taskList;
    private TaskListUtils taskListUtils;
    private CustomAlert customAlert;

    public CardService(final NetworkUtils networkUtils, final Task task,
                       final long boardID, final TaskList taskList, final CustomAlert customAlert) {
        this.taskUtils = networkUtils.getTaskUtils();
        this.taskListUtils = networkUtils.getTaskListUtils();
        this.task = task;
        this.boardID = boardID;
        this.taskList = taskList;
        this.customAlert = customAlert;
    }

    public void showAlert(final Exception e) {
        Alert alert = customAlert.showAlert(e.getMessage());
        alert.showAndWait();
    }

    public boolean moveUp() {
        try {
            List<Task> tasks = taskList.getTasks();
            int index = tasks.indexOf(task);
            if (index > 0) {
                taskListUtils.reorderTask(this.boardID, this.taskList.id,
                        this.task.id, index - 1);
                taskList.reorder(task.id, index - 1);
                return true;
            } else {
                return false;
            }
        } catch (TaskListException e) {
            showAlert(e);
            return false;
        }
    }

    public boolean moveDown() {
        try {
            List<Task> tasks = taskList.getTasks();
            int index = tasks.indexOf(task);
            if (index < tasks.size() - 1) {
                taskListUtils.reorderTask(this.boardID, this.taskList.id,
                        task.id, index + 1);
                taskList.reorder(task.id, index + 1);
                return true;
            } else {
                return false;
            }
        } catch (TaskListException e) {
            showAlert(e);
            return false;
        }
    }

    public boolean deleteTask() {
        try {
            taskUtils.deleteTask(this.boardID, this.taskList.id, task.id);
            return true;
        } catch (TaskException e) {
            showAlert(e);
            return false;
        }
    }

    public TaskPreset setPreset(final TaskPreset preset) {
        taskUtils.setPreset(boardID, this.taskList.id, task.id, preset);
        task.setTaskPreset(preset);
        return task.getTaskPreset();
    }

}
