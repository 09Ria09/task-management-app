package client.services;

import client.CustomAlert;
import client.customExceptions.SubTaskException;
import client.utils.NetworkUtils;
import client.utils.SubTaskUtils;
import com.google.inject.Inject;
import commons.SubTask;
import commons.Task;
import javafx.scene.control.Alert;
import javafx.stage.Modality;

import java.util.List;
import java.util.Objects;

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


    public boolean setSubTaskName(final String newName) throws Exception {
        try {
            if (!newName.equals(subTask.getName()) || !newName.equals("")) {
                subTaskUtils.renameSubTask(this.boardID, this.listID,
                        this.task.id, subTask.id, newName);
                return true;
            }
            return false;
        } catch (SubTaskException e) {
            throw new RuntimeException(e);
        }
    }

    public void completeSubTask(final boolean checked) {
        try {
            subTaskUtils.completeSubTask(this.boardID, this.listID,
                    this.task.id, subTask.id, checked);
        } catch (SubTaskException e) {
            Alert alert = customAlert.showAlert(e.getMessage());
            alert.showAndWait();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public boolean moveUp(final String text) {
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
        }catch (Exception e){
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setHeaderText("Oops, something went wrong!");
            alert.setContentText("We're sorry :( something went wrong : "+ e.getMessage());
            alert.showAndWait();
            return false;
        }
    }

    public void moveDown() {

    }

}
