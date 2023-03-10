package client.scenes;

import com.google.inject.Inject;

import client.utils.ServerUtils;
import commons.Person;
import commons.Task;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;

public class AddCreateTaskCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField taskName;

    @FXML
    private TextField taskDesc;

    @Inject
    public AddCreateTaskCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;

    }

    public void cancel() {
        clearFields();
        mainCtrl.showOverview();
    }

    public void confirm() {
        try {
            server.addTask(getTask());
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

    EventHandler<ActionEvent> buttonEventHandler(){
        return event -> {
            Button clickedButton = (Button) event.getTarget();
            if (clickedButton.getText().equals("Cancel")) {
                cancel();
            }
            else if (clickedButton.getText().equals("Confirm")) {
                confirm();
            }
        };
    }
}