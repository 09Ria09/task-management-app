package client.scenes;

import client.CustomAlert;
import client.customExceptions.TaskException;
import client.utils.ServerUtils;
import client.utils.TaskUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class EditTaskCtrl {

    private CardCtrl cardCtrl;
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField newTaskName;

    @FXML
    private TextArea newTaskDesc;
    private CustomAlert customAlert;

    @Inject
    public EditTaskCtrl(final ServerUtils server, final MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void cancel() {
        newTaskDesc.clear();
        newTaskName.clear();
        mainCtrl.showBoardCatalogue();
    }

    public void confirmTask() {
        try {
            String newName = newTaskName.getText();
            TaskUtils taskUtils = cardCtrl.getTaskUtils();

            if(newName != null) {
                taskUtils.renameTask(cardCtrl.getListController().getBoardID(),
                        cardCtrl.getListController().getTaskList().id,
                        cardCtrl.getTask().id, newName);
            }
        } catch (TaskException e) {
            Alert alert = customAlert.showAlert(e.getMessage());
            alert.showAndWait();
            return;
        }

        newTaskName.clear();
    }

    public void confirmDesc() {
        try {
            String newDesc = newTaskDesc.getText();
            TaskUtils taskUtils = cardCtrl.getTaskUtils();

            if(newDesc != null) {
                taskUtils.editDescription(cardCtrl.getListController().getBoardID(),
                        cardCtrl.getListController().getTaskList().id,
                        cardCtrl.getTask().id, newDesc);
            }
        } catch (TaskException e) {
            Alert alert = customAlert.showAlert(e.getMessage());
            alert.showAndWait();
            return;
        }

        newTaskDesc.clear();
    }


    public void setCardCtrl(final CardCtrl cardCtrl) {
        this.cardCtrl = cardCtrl;
    }

    public void setCustomAlert(final CustomAlert customAlert) {
        this.customAlert = customAlert;
    }

}
