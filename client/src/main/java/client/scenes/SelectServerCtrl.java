package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

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

   //takes input from user and checks if it is a valid server after setting the serveraddress
    //if it is a valid server it will show the boardoverview
    //if it is not a valid server it will show the wrongserver scene
    public void showServerBoards(){
        String serverAddress = addressField.getText();
        server.setServerAddress(serverAddress);
        System.out.println(serverAddress);
        try{
            //TODO
            //I have to implement a timeout of sorts - connecting to
            //google.com will take time and it will cause the app to be not responding
            //until going to wrong server scene
            if(server.isTalioServer()){
                addressField.clear();
                mainCtrl.showBoardOverview();
            }

        }
        catch (Exception e){
            mainCtrl.showWrongServer();
        }
    }

}
