package client.utils;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;

public class LayoutUtils {

    /**
     * This method creates a font binding that will binds the font to the size of the given
     * root region. The font returned will have a size corresponding to the following formula :
     * Math.min(root.getHeight(), root.getWidth()/2.0D) * multiplier.
     * If the formula gives a size bigger than the max size given, then the font will be set
     * to the max size.
     * @param root the root region
     * @param multiplier the size multiplier
     * @param max the maximum font size allowed.
     * @return a font binding with the properties above.
     */
    public ObjectBinding<Font> createFontBinding(final Region root, final double multiplier,
                                                 final double max){
        return Bindings.createObjectBinding(() -> {
            double fontSize = Math.min(root.getHeight(), root.getWidth()/2.0D) * multiplier;
            return Font.font(Math.min(fontSize, max));
        }, root.heightProperty());
    }

    /**
     * This method creates a binding that can used to hide an object when the size of the
     * root region is too small. If the pane is smaller than the minimum dimensions given,
     * then the property will return a value of 1.
     * @param root the root region
     * @param minWidth the minimum width to show the object.
     * @param minHeight the minimum height to show the object.
     * @param size the size the property should have if the pane is large enough.
     * @return a binding with the properties given above.
     */
    public DoubleBinding createVisibilityBinding(final Region root, final double minWidth,
                                                 final double minHeight, final double size){
        return Bindings.createDoubleBinding(
                () -> root.getWidth() > minWidth && root.getHeight() > minHeight ? size : 1,
                root.heightProperty(),
                root.widthProperty());
    }
}
