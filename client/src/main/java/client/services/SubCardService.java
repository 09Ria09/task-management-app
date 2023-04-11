package client.services;

import client.CustomAlert;
import client.customExceptions.SubTaskException;
import client.utils.NetworkUtils;
import client.utils.SubTaskUtils;
import com.google.inject.Inject;
import commons.SubTask;
import commons.Task;
import javafx.scene.control.Alert;

import java.util.List;

public class SubCardService {

    private SubTaskUtils subTaskUtils;
    private SubTask subTask;
    private CustomAlert customAlert;
    private long boardID;
    private long listID;
    private Task task;

    @Inject
    public SubCardService(final NetworkUtils networkUtils, final SubTask subTask,
                          final CustomAlert customAlert, final long boardID,
                          final long listID, final Task task) {
        this.subTaskUtils = networkUtils.getSubTaskUtils();
        this.subTask = subTask;
        this.customAlert = customAlert;
        this.boardID = boardID;
        this.listID = listID;
        this.task = task;
    }

    public void showAlert(final SubTaskException e) {
        Alert alert = customAlert.showAlert(e.getMessage());
        alert.showAndWait();
    }


    public boolean setSubTaskName(final String newName) {
        try {
            if (!newName.equals(subTask.getName()) || !newName.equals("")) {
                subTaskUtils.renameSubTask(this.boardID, this.listID,
                        this.task.id, subTask.id, newName);
                return true;
            }
            return false;
        } catch (SubTaskException e) {
            showAlert(e);
            return false;
        }
    }

    public void completeSubTask(final boolean checked) {
        try {
            subTaskUtils.completeSubTask(this.boardID, this.listID,
                    this.task.id, subTask.id, checked);
        } catch (SubTaskException e) {
            showAlert(e);
        }
    }

    public boolean moveUp() {
        try {
            List<SubTask> subTasks = task.getSubtasks();
            int index = subTasks.indexOf(subTask);
            if (index > 0) {
                subTaskUtils.reorderSubTask(this.boardID,
                        this.listID, task.id, subTask.id, index - 1);
                task.reorderSubTasks(subTask.id, index - 1);
                return true;
            } else {
                return false;
            }
        }catch (SubTaskException e){
            showAlert(e);
            return false;
        }
    }

    public boolean moveDown() {
        try {
            List<SubTask> subTasks = task.getSubtasks();
            int index = subTasks.indexOf(subTask);
            System.out.println(task.getSubtasks().size());
            if (index < task.getSubtasks().size() - 1) {
                subTaskUtils.reorderSubTask(this.boardID, this.listID,
                        task.id, subTask.id, index + 1);
                task.reorderSubTasks(subTask.id, index + 1);
                return true;
            } else {
                return false;
            }
        }catch (SubTaskException e){
            showAlert(e);
            return false;
        }
    }

    public boolean deleteSubTask() {
        try {
            subTaskUtils.deleteSubTask(this.boardID, this.listID,
                    this.task.id, subTask.id);
            return true;
        } catch (SubTaskException e) {
            showAlert(e);
            return false;
        }
    }

}
