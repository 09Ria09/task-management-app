package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class SubTask {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
    private String name;
    private boolean completed;

    public SubTask(String name, boolean completed) {
        this.name = name;
        this.completed = completed;
    }

    public SubTask() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public boolean equals(Object obj) {
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
