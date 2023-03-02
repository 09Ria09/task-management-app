package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
    private String name;
    private String description;

    @OneToMany
    private List<SubTask> subtasks;

    private Task(){

    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.subtasks = new ArrayList<>();
    }

    public Task(String name, String description, List<SubTask> subtasks) {
        this.name = name;
        this.description = description;
        this.subtasks = subtasks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        return "Task (" + id + ") : " + name + "\nDescription : " + description;
    }

    public List<SubTask> getSubtasks() {
        return subtasks;
    }

    public void addSubtask(SubTask subtask){
        this.subtasks.add(subtask);
    }

    public boolean removeSubtask(SubTask subTask){
        return this.subtasks.remove(subTask);
    }
}
