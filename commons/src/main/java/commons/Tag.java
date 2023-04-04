package commons;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.awt.*;

@Entity
public class Tag implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    private String name;
    private String colorBackground;
    private String colorFont;

    public Tag(final String name, final String colorBackground, final String colorFont) {
        this.name = name;
        setColors(colorBackground, colorFont);
    }

    public Tag(final String name, final String colorBackground) {
        this.name = name;
        setColors(colorBackground);
    }
    public Tag() {
        this.name = "";
        this.colorBackground = "#000000";
        this.colorFont = "#FFFFFF";
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getColorBackground() {
        return colorBackground;
    }

    public String getColorFont() {
        return colorFont;
    }

    public void setColors(final String colorBackground) {
        int colorBackgroundInt = Color.parseColor(colorBackground);
        this.colorBackground = colorBackground;
        this.colorFont = String.format("#%06X", 0xFFFFFF - colorBackgroundInt);
    }

    public void setColors(final String colorBackground, final String colorFont) {
        this.colorBackground = colorBackground;
        this.colorFont = colorFont;
    }

    @Override
    public boolean equals(final Object obj) {
        if(!(obj instanceof Tag))
            return false;
        Tag other = (Tag) obj;
        return other.color.equals(color) && other.name.equals(name);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return "Tag (" + id + ") : " + name + " -> color=" + colorBackground + " font=" + colorFont;
    }

    public long getId() {
        return id;
    }

}
