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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskPreset preset = (TaskPreset) o;
        return id == preset.id
                && isDefault == preset.isDefault
                && Objects.equals(name, preset.name)
                && Objects.equals(backgroundColor, preset.backgroundColor)
                && Objects.equals(fontColor, preset.fontColor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, backgroundColor, fontColor, isDefault);
    }
}
