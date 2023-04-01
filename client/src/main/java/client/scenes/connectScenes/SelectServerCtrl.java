package client.scenes.connectScenes;

import client.scenes.BoardCatalogueCtrl;
import client.scenes.MainCtrl;
import client.utils.LayoutUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;

import java.util.Optional;

public class SelectServerCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private final BoardCatalogueCtrl boardCatalogueCtrl;
    private final LayoutUtils layoutUtils;

    @FXML
    private TextField addressField;

    @FXML
    private VBox root;

    @FXML
    private Button connectButton;

    @FXML
    private Label title;

    @FXML
    private Label addressLabel;

    @Inject
    public SelectServerCtrl(final ServerUtils server, final MainCtrl mainCtrl,
                            final BoardCatalogueCtrl boardCatalogueCtrl,
                            final LayoutUtils layoutUtils) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.boardCatalogueCtrl=boardCatalogueCtrl;
        this.layoutUtils = layoutUtils;
    }

    public void initialize(){
        title.fontProperty().bind(layoutUtils.createFontBinding(root, 0.12D, 48.0d, "verdana",
                FontWeight.BOLD));
        connectButton.fontProperty().bind(layoutUtils.createFontBinding(root, 0.075D, 36.0D));
        connectButton.maxWidthProperty().bind(root.widthProperty().multiply(0.35D));
        connectButton.maxHeightProperty().bind(root.heightProperty().multiply(0.2D));

        addressLabel.fontProperty().bind(layoutUtils.createFontBinding(root, 0.05D, 36.0D,
                "verdana", FontWeight.BOLD));
        addressField.fontProperty().bind(layoutUtils.createFontBinding(root, 0.05D, 36.0D));
    }

    /**
     * takes input from user and checks if it is a valid server after setting the serveraddress
     * if it is a valid server it will show the boardoverview
     * if it is not a valid server it will show the wrongserver scene
     * if there is a timeout it will show the timeout scene
     * if there is an error it will show the serverdown scene
     */

    public void showServerBoards(){
        connectButton.setText("Connecting...");
        String serverAddress = addressField.getText().toLowerCase();
        server.setServerAddress(serverAddress);
        System.out.println(server.getServerAddress());
        try {
            Optional<String> result = server.isTalioServer();
            if (result.isEmpty()) {
                addressField.clear();
                connectButton.setText("Connect!");
                mainCtrl.populateBoardCatalogue();
                mainCtrl.showBoardCatalogue();
            } else if(result.get().equals("Not a Talio server")||
                    result.get().equals("Unexpected response status")) {
                connectButton.setText("Connect!");
                mainCtrl.showWrongServer();
            } else if(result.get().equals("IOException")){
                connectButton.setText("Connect!");
                mainCtrl.showTimeout();
            } else if(result.get().equals("InterruptedException")||
                    result.get().equals("Exception")){
                connectButton.setText("Connect!");
                mainCtrl.showUnexpectedError();
            }
        } catch (Exception e) {
            mainCtrl.showUnexpectedError();
            e.printStackTrace();
        }
    }

}
