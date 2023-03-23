package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.Optional;

public class SelectServerCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField addressField;

    @Inject
    public SelectServerCtrl(final ServerUtils server, final MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * takes input from user and checks if it is a valid server after setting the serveraddress
     * if it is a valid server it will show the boardoverview
     * if it is not a valid server it will show the wrongserver scene
     * if there is a timeout it will show the timeout scene
     * if there is an error it will show the serverdown scene
     */

    public void showServerBoards(){
        String serverAddress = addressField.getText();
        server.setServerAddress(serverAddress);
        System.out.println(server.getServerAddress());
        try {
            Optional<String> result = server.isTalioServer();
            if (result.isEmpty()) {
                addressField.clear();
                mainCtrl.showBoardOverview();
            } else if(result.get().equals("Not a Talio server")||
                    result.get().equals("Unexpected response status")) {
                mainCtrl.showWrongServer();
            } else if(result.get().equals("IOException")){
                mainCtrl.showTimeout();
            } else if(result.get().equals("InterruptedException")||
                    result.get().equals("Exception")){
                mainCtrl.showUnexpectedError();
            }
        } catch (Exception e) {
            mainCtrl.showUnexpectedError();
        }
    }

}
