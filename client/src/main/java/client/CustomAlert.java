package client;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Objects;

public class CustomAlert {

    /**
     * This class is essentially made just to not have
     * to duplicate code for every error alert
     * it is injected in every controller where
     * error handling is necessary and it is customized by
     * just adding a message to it (which is the message
     * of the exception)
     * @param message the message to display
     * @return a new alert which looks nicer:)
     */
    public Alert showAlert(final String message) {
        var alert = new Alert(Alert.AlertType.ERROR);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setHeaderText("Oops, something went wrong!");
        alert.setContentText(message);
        //custom error image
        ImageView imageView = new ImageView(new Image("client/images/error.png"));
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
        alert.setGraphic(imageView);
        // custom icon
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("client/images/errorIcon.png"));

        alert.getDialogPane().getStylesheets().add(
                Objects.requireNonNull(getClass()
                        .getResource("/client/styles/alert.css")).toExternalForm());

        return alert;
    }
}
