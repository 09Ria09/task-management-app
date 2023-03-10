package client.scenes;

import com.google.inject.Inject;

import client.utils.ServerUtils;
import commons.Task;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

public class CreateTaskCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField taskName;

    @FXML
    private TextField taskDesc;

    @Inject
    public CreateTaskCtrl(final ServerUtils server, final MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;

    }

    public void cancel() {
        clearFields();
        mainCtrl.showOverview();
    }

    public void confirm() {
        try {
            //PLACEHOLDER CODE: WILL SOON BE SOMETHING LIKE THE LINE BELOW
            //server.addTask(getTask());
            System.out.println("hello world");
        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }

        clearFields();
        mainCtrl.showOverview();
    }

    private Task getTask() {
        var name = taskName.getText();
        var desc = taskDesc.getText();
        return new Task(name, desc);
    }

    private void clearFields() {
        taskDesc.clear();
        taskName.clear();
    }
}