package commons;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.awt.*;
import java.util.Objects;

@Entity
public class TaskPreset {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    private String name;
    private Color backgroundColor;
    private Color fontColor;
    private boolean isDefault;

    public TaskPreset(final String name) {
        this.isDefault = false;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(final Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Color getFontColor() {
        return fontColor;
    }

    public void setFontColor(final Color fontColor) {
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
        return "TaskPreset{" +
                "name='" + name + '\'' +
                '}';
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
}
