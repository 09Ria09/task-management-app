package client.services;

import client.CustomAlert;
import client.customExceptions.TaskException;
import client.utils.NetworkUtils;
import client.utils.SubTaskUtils;
import client.utils.TaskUtils;
import commons.SubTask;
import commons.Task;
import commons.TaskList;
import commons.TaskPreset;
import javafx.scene.control.Alert;

public class DetailedTaskViewService {

    private TaskUtils taskUtils;
    private Task task;
    private TaskList taskList;
    private long boardID;
    private CustomAlert customAlert;
    private SubTaskUtils subTaskUtils;

    public DetailedTaskViewService(final NetworkUtils networkUtils, final Task task,
                                   final TaskList taskList, final long boardID,
                                   final CustomAlert customAlert) {
        this.taskUtils = networkUtils.getTaskUtils();
        this.subTaskUtils = networkUtils.getSubTaskUtils();
        this.task = task;
        this.taskList = taskList;
        this.boardID = boardID;
        this.customAlert = customAlert;
    }

    public void showAlert(final TaskException e) {
        Alert alert = customAlert.showAlert(e.getMessage());
        alert.showAndWait();
    }

    public boolean deleteTask() {
        try {
            taskUtils.deleteTask(this.boardID, this.taskList.id, this.task.id);
            return true;
        } catch (TaskException e) {
            showAlert(e);
            return false;
        }
    }

    public SubTask makeSubTask(final String newName) {
        return new SubTask(newName, false);
    }

    public SubTask addSubTask(final SubTask subTask) {
        try {
            return subTaskUtils.addSubTask(this.boardID,
                    this.taskList.id, task.id, subTask);
        } catch (TaskException e) {
            showAlert(e);
            return null;
        }
    }

    public void changePreset(final TaskPreset preset) {
        if (preset == null)
            return;
        task.setTaskPreset(preset);
        taskUtils.setPreset(this.boardID, this.taskList.id, task.getId(), preset);
    }

    public void editDescription(final String newDesc) {
        try {
            this.taskUtils.editDescription(this.boardID, this.taskList.id,
                    task.getId(), newDesc);
        } catch (TaskException e) {
            showAlert(e);
        }
    }

    public void editName(final String newName) {
        try {
            this.taskUtils.renameTask(this.boardID, this.taskList.id, task.getId(), newName);
        } catch (TaskException e) {
            showAlert(e);
        }
    }
}
