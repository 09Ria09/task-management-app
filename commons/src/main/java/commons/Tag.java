package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Tag implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    private String name;
    private String color;

    public Tag(final String name, final String color) {
        this.name = name;
        this.color = color;
    }

    public Tag() {
        this.name = "";
        this.color = "FFFFFF";
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(final String color) {
        this.color = color;
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
        return "Tag (" + id + ") : " + name + " -> color=#" + color;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
