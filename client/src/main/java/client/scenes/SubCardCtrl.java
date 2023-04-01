package client.scenes;

import client.CustomAlert;
import client.customExceptions.SubTaskException;
import client.utils.SubTaskUtils;
import client.utils.TaskListUtils;
import com.google.inject.Inject;
import commons.SubTask;
import commons.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.text.Text;
import javafx.stage.Modality;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class SubCardCtrl {
    private SubTask subTask;
    @FXML
    private Text text;
    private ListCtrl listController;
    private TaskListUtils taskListUtils;
    private CustomAlert customAlert;
    private SubTaskUtils subTaskUtils;
    private DetailedTaskViewCtrl detailedTaskViewCtrl;


    @Inject
    void initialize(final SubTask subTask, final ListCtrl listCtrl,
                    final TaskListUtils listUtils, final CustomAlert customAlert,
                     final SubTaskUtils subTaskUtils,
                    final DetailedTaskViewCtrl detailedTaskViewCtrl
    ) {
        this.subTask= subTask;
        this.text.setText(subTask.getName());
        this.listController = listCtrl;
        this.taskListUtils = listUtils;
        this.customAlert = customAlert;
        this.subTaskUtils = subTaskUtils;
        this.detailedTaskViewCtrl = detailedTaskViewCtrl;
    }

    public void deleteSubTask() throws SubTaskException {
        subTaskUtils.deleteSubTask(listController.getBoardID(),
                listController.getTaskList().id, detailedTaskViewCtrl.getTask().id, subTask.id);
    }

//    public void renameSubTask() throws SubTaskException {
//        String name = "renamed";
//        subTaskUtils.renameSubTask(listController.getBoardID(),
//        listController.getTaskList().id, detailedTaskViewCtrl.getTask().id, subTask.id, name);
//    }

    public void renameSubTask() throws Exception {
        TextInputDialog dialog = new TextInputDialog(subTask.getName());
        dialog.setTitle("Talio: Rename Sub Task");
        dialog.setHeaderText("Enter new name");
        dialog.setContentText("Name:");
        Optional<String> newName = dialog.showAndWait();
        if(newName.isPresent()) {
            setSubTaskName(newName.get());
        }
        try {
            subTask = subTaskUtils.getSubTask(listController.getBoardID(),
                    listController.getTaskList().id, detailedTaskViewCtrl.getTask().id,
                    subTask.id);
            text.setText(subTask.getName());
        } catch (SubTaskException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean setSubTaskName(final String name) throws SubTaskException, Exception {
        try {
            if(!name.equals(subTask.getName()) || !name.equals("")) {
                subTaskUtils.renameSubTask(listController.getBoardID(),
                        listController.getTaskList().id,
                        detailedTaskViewCtrl.getTask().id, subTask.id, name);
                return true;
            }
        } catch (SubTaskException e) {
            Alert alert = customAlert.showAlert(e.getMessage());
            alert.showAndWait();
            return false;
        } catch (Exception e){
            return false;
        }
        return false;
    }

    public boolean moveSubTaskUp() {
        try {
            long boardId = listController.getBoardID();
            long listId = listController.getTaskList().id;
            Task task = detailedTaskViewCtrl.getTask();
            List<SubTask> subTasks = task.getSubtasks();
            int index = subTasks.indexOf(subTask);
            if (index > 0) {
                subTaskUtils.reorderSubTask(boardId,
                        listId, task.id, subTask.id, index - 1);
                task.reorderSubTasks(subTask.id, index - 1);
                if(!Objects.equals(task.getName(), text.getText())){
                    text.setText(text.getText());
                    return true;
                }
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
        return false;
    }

    public boolean moveSubTaskDown() {
        try {
            long boardId = listController.getBoardID();
            long listId = listController.getTaskList().id;
            Task task = detailedTaskViewCtrl.getTask();
            List<SubTask> subTasks = task.getSubtasks();
            int index = subTasks.indexOf(subTask);
            System.out.println(task.getSubtasks().size());
            if (index < task.getSubtasks().size() - 1) {
                subTaskUtils.reorderSubTask(boardId,
                        listId, task.id, subTask.id, index + 1);
                task.reorderSubTasks(subTask.id, index + 1);
                if(!Objects.equals(task.getName(), text.getText())){
                    text.setText(text.getText());
                    return true;
                }
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
        return false;
    }

}
