package client.scenes;

import javafx.fxml.FXML;

import java.awt.*;
import java.awt.event.ActionEvent;

public class CreateTaskCtrl {

    private String taskName;
    private String taskDescription;

    @FXML
    private Label myLabel;

    @FXML
    private Button button;

    @FXML
    protected void doit(final ActionEvent event) {
        myLabel.setText("Clicked");
    }


    public void confirmCreateTask() {
        System.out.println("Confirmed create task");
    }
}
