package client.utils;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.TextField;


public class LayoutUtils {

    /**
     * Creates a change listener for a TextField to set a max input length for the textfield.
     * If the input reaches the maximum size, no more characters will be added.
     * @param maxLength the maximum input length.
     * @param textField the field that the listener checks.
     * @return a listener that checks a textfield for a max size.
     */
    public ChangeListener<String> createMaxFieldLength(final int maxLength,
                                                       final TextField textField){
        return (observable, oldValue, newValue) -> {
            if(newValue.length() > maxLength)
                textField.setText(oldValue);
        };
    }
}
