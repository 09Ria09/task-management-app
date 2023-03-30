package client.scenes;

import client.CustomAlert;
import client.customExceptions.SubTaskException;
import client.customExceptions.TaskException;
import client.utils.SubTaskUtils;
import client.utils.TaskListUtils;
import client.utils.TaskUtils;
import com.google.inject.Inject;
import commons.SubTask;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.text.Text;

import java.util.Optional;

public class SubCardCtrl {
    private SubTask subTask;
    @FXML
    private Text text;
    private ListCtrl listController;
    private TaskListUtils taskListUtils;
    private CustomAlert customAlert;
    private TaskUtils taskUtils;
    private MainCtrl mainCtrl;
    private SubTaskUtils subTaskUtils;
    private DetailedTaskViewCtrl detailedTaskViewCtrl;


    @Inject
    void initialize(final SubTask subTask, final ListCtrl listCtrl,
                    final TaskListUtils listUtils, final CustomAlert customAlert,
                    final TaskUtils taskUtils, final MainCtrl mainCtrl,
                    final SubTaskUtils subTaskUtils, DetailedTaskViewCtrl detailedTaskViewCtrl
    ) {
        this.subTask= subTask;
        this.text.setText(subTask.getName());
        this.listController = listCtrl;
        this.taskListUtils = listUtils;
        this.customAlert = customAlert;
        this.taskUtils = taskUtils;
        this.mainCtrl = mainCtrl;
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
//                listController.getTaskList().id, detailedTaskViewCtrl.getTask().id, subTask.id, name);
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

    public boolean setSubTaskName(String name) throws SubTaskException, Exception {
        try {
            if(!name.equals(subTask.getName()) || !name.equals("")) {
                subTaskUtils.renameSubTask(listController.getBoardID(),
                        listController.getTaskList().id, detailedTaskViewCtrl.getTask().id, subTask.id, name);
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

}
