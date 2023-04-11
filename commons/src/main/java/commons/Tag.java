package commons;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

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
        int colorBackgroundInt = Integer.parseInt(colorBackground.substring(1), 16);
        this.colorBackground = colorBackground;
        this.colorFont = String.format("#%06X", 0xFFFFFF - colorBackgroundInt);
    }

    public void setColors(final String colorBackground, final String colorFont) {
        this.colorBackground = colorBackground;
        this.colorFont = colorFont;
    }

    /**
     * @param o an object
     * @return true if the object provided is the same to this object
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(name, tag.name) &&
            Objects.equals(colorBackground, tag.colorBackground) &&
            Objects.equals(colorFont, tag.colorFont);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }


    @Override
    public String toString() {
        return name;
    }

    public long getId() {
        return id;
    }

}
