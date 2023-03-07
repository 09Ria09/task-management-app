package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.util.*;


@Entity
public class TaskList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
    private String name;
    @OneToMany
    private List<Task> tasks;

    public TaskList(final String name) {
        this.name = name;
        this.tasks = new ArrayList<Task>();
    }

    public TaskList(final String name, final List<Task> tasks) {
        this.name = name;
        this.tasks = tasks;
    }

    public TaskList(){

    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public List<Task> getTasks() {
        return tasks;
    }


    public void addTask(final Task task) {
        tasks.add(task);
    }

    public void removeTask(final Task task) {
        tasks.remove(task);
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder("TaskList (" + id + ") :\n");
        for (Task task : tasks)
            string.append(task.toString()).append("\n");
        return string.toString();
    }

    @Override
    public boolean equals(final Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }


}