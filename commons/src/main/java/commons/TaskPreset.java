package commons;

import javax.persistence.*;
import java.awt.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class TaskPreset implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    private String name;
    private String backgroundColor;
    private String fontColor;
    private boolean isDefault;

    public TaskPreset(final String name) {
        this.isDefault = false;
        this.name = name;
    }

    public TaskPreset(final String name, final String backgroundColor,
                      final String fontColor) {
        this.isDefault = false;
        this.name = name;
        this.backgroundColor = backgroundColor;
        this.fontColor = fontColor;
    }

    public TaskPreset() {

    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(final String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(final String fontColor) {
        this.fontColor = fontColor;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(final boolean isDefault) {
        this.isDefault = isDefault;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskPreset that = (TaskPreset) o;

        if (id != that.id) return false;
        if (isDefault != that.isDefault) return false;
        if (!Objects.equals(name, that.name)) return false;
        if (!Objects.equals(backgroundColor, that.backgroundColor))
            return false;
        return Objects.equals(fontColor, that.fontColor);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (backgroundColor != null ? backgroundColor.hashCode() : 0);
        result = 31 * result + (fontColor != null ? fontColor.hashCode() : 0);
        result = 31 * result + (isDefault ? 1 : 0);
        return result;
    }

    public long getId() {
        return id;
    }
}
