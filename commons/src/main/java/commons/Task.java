package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Task implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    @ManyToOne
    private TaskList list;
//this refers to the task list that a task is assigned to
    private String name;
    private String description;

    @OneToMany(cascade=CascadeType.ALL)
    private List<SubTask> subtasks;

    public Task(){

    }

    public Task(final String name, final String description) {
        this.name = name;
        this.description = description;
        this.subtasks = new ArrayList<>();
    }

    public Task(final String name, final String description, final List<SubTask> subtasks) {
        this.name = name;
        this.description = description;
        this.subtasks = subtasks;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
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
        return "Task (" + id + ") : " + name + "\nDescription : " + description;
    }

    public List<SubTask> getSubtasks() {
        return subtasks;
    }

    public void addSubtask(final SubTask subtask){
        this.subtasks.add(subtask);
    }

    public boolean removeSubtask(final SubTask subTask){
        return this.subtasks.remove(subTask);
    }

    public TaskList getList() {
        return list;
    }
}
