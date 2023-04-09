package client.scenes.connectScenes;

import client.services.ServerService;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;


public class SelectServerCtrl {

    private final ServerService serverService;

    @FXML
    private TextField addressField;

    @FXML
    private Button connectButton;


    @Inject
    public SelectServerCtrl(final ServerService serverService) {
        this.serverService = serverService;
    }

    /**
     * takes input from user and checks if it is a valid server after setting the serveraddress
     * if it is a valid server it will show the boardoverview
     * if it is not a valid server it will show the wrongserver scene
     * if there is a timeout it will show the timeout scene
     * if there is an error it will show the serverdown scene
     */

    public void showServerBoards() {
        connectButton.setText("Connecting...");
        String serverAddress = addressField.getText().toLowerCase();

        String result = serverService.connect(serverAddress);

        connectButton.setText("Connect!");
        addressField.clear();

        switch (result) {
            case "connected":
                serverService.showBoardCatalogue();
                break;
            case "wrongServer":
                serverService.showWrongServer();
                break;
            case "timeout":
                serverService.showTimeout();
                break;
            case "unexpectedError":
            default:
                serverService.showUnexpectedError();
                break;
        }
    }
}


