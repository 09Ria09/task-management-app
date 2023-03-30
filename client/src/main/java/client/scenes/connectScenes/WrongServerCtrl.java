package client.scenes.connectScenes;

import client.scenes.MainCtrl;
import client.utils.LayoutUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import javax.inject.Inject;

public class WrongServerCtrl {
    private final MainCtrl mainCtrl;
    private final LayoutUtils layoutUtils;

    @FXML
    private Label text1;

    @FXML
    private Label text2;

    @FXML
    private Label text3;

    @FXML
    private Button backButton;

    @FXML
    private ImageView image;

    @FXML
    private VBox root;

    @Inject
    public WrongServerCtrl(final MainCtrl mainCtrl, final LayoutUtils layoutUtils) {
        this.mainCtrl = mainCtrl;
        this.layoutUtils = layoutUtils;
    }

    public void initialize(){
        text1.fontProperty().bind(layoutUtils.createFontBinding(root, 0.12D, 48.0d));
        text2.fontProperty().bind(layoutUtils.createFontBinding(root, 0.08D, 36.0d));
        text3.fontProperty().bind(layoutUtils.createFontBinding(root, 0.06D, 30.0d));
        backButton.fontProperty().bind(layoutUtils.createFontBinding(backButton, 0.4D, 30.0d));
        backButton.prefHeightProperty().bind(root.heightProperty().multiply(0.075D));
        backButton.prefWidthProperty().bind(root.widthProperty().multiply(0.15D));
        image.fitHeightProperty().bind(layoutUtils.createVisibilityBinding(root, 600, 400, 150));
        image.fitWidthProperty().bind(layoutUtils.createVisibilityBinding(root, 600, 400, 150));
    }

    //used for the go back button
    public void showSelectServer() {
        mainCtrl.showSelectServer();
    }
}
