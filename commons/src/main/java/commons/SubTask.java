package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class SubTask implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
    private String name;
    private boolean completed;

    public SubTask(final String name, final boolean completed) {
        this.name = name;
        this.completed = completed;
    }

    public SubTask() {
        this.name = "";
        this.completed = false;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(final boolean completed) {
        this.completed = completed;
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
        return "Subtask (" + id + ") : " + name + " -> " + (completed ? "" : "not") + " completed";
    }
}
