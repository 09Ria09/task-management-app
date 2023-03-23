package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Entity
public class TaskList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
    private String name;

    @OneToMany(cascade=CascadeType.ALL, orphanRemoval = true)
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

    public Optional<Task> getTaskById(final long id){
        return tasks.stream().filter(x -> x.id == id).findFirst();
    }

    public void addTask(final Task task) {
        tasks.add(task);
    }

    public void removeTask(final Task task) {
        tasks.remove(task);
    }

    public void setTasks(final List<Task> tasks) {
        this.tasks = tasks;
    }

    public long getId() {
        return id;
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


    public void reorder(final long taskid, final int newIndex) {
        var task = getTaskById(taskid);
        if(task.isEmpty()) {
            return;
        }

        int index = tasks.indexOf(task.get());

        if(index == newIndex) {
            return;
        }

        Task temp = tasks.remove(index);
        tasks.add(newIndex, temp);

    }
}