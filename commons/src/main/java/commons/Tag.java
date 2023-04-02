package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.awt.*;

@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    private String name;
    private int colorBackground;
    private int colorFont;

    public Tag(final String name, final int colorBackground, final int colorFont) {
        this.name = name;
        this.colorBackground = colorBackground;
        this.colorFont = colorFont;
    }

    public Tag(final String name, final int colorBackground) {
        this.name = name;
        this.colorBackground = colorBackground;
        this.colorFont = 0xFFFFFF - colorBackground;
    }

    public Tag() {
        this.name = "";
        this.colorBackground = 0x000000;
        this.colorFont = 0xFFFFFF;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getColorBackground() {
        return colorBackground;
    }

    public int getColorFont() {
        return colorFont;
    }

    public void setColors(final int colorBackground, final int colorFont) {
        this.colorBackground = colorBackground;
        this.colorFont = colorFont;
    }

    @Override
    public boolean equals(final Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return "Tag (" + id + ") : " + name + " -> color=" + colorBackground + " font=" + colorFont;
    }
}
