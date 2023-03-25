package client.scenes;

import client.customExceptions.TaskException;
import client.utils.ServerUtils;
import client.utils.TaskUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

public class EditTaskCtrl {

    private CardCtrl cardCtrl;
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField newTaskName;

    @FXML
    private TextArea newTaskDesc;

    @Inject
    public EditTaskCtrl(final ServerUtils server, final MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void cancel() {
        newTaskDesc.clear();
        newTaskName.clear();
        mainCtrl.showBoardOverview();
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
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
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
                taskUtils.renameDescription(cardCtrl.getListController().getBoardID(),
                        cardCtrl.getListController().getTaskList().id,
                        cardCtrl.getTask().id, newDesc);
            }
        } catch (TaskException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }

        newTaskDesc.clear();
    }


    public void setCardCtrl(final CardCtrl cardCtrl) {
        this.cardCtrl = cardCtrl;
    }

}
