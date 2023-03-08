package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.util.*;

@Entity
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    private String name;

    @OneToMany
    private List<TaskList> taskLists;
    @OneToMany
    private List<Tag> tags;

    public Board(final String name, final List<TaskList> listTaskList, final List<Tag> tags) {
        this.name = name;
        this.taskLists = listTaskList;
        this.tags = tags;
    }

    public Board() {
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public List<TaskList> getListTaskList() {
        return taskLists;
    }

    public void setListTaskList(final List<TaskList> listTaskList) {
        this.taskLists = listTaskList;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(final List<Tag> tags) {
        this.tags = tags;
    }

    public void addTaskList(final TaskList taskList) {
        taskLists.add(taskList);
    }

    public void removeTaskList(final TaskList taskList) {
        taskLists.remove(taskList);
    }

    public Optional<TaskList> getTaskListById(final long id){
        return taskLists.stream().filter(x -> x.id == id).findFirst();
    }

    public void addTag(final Tag tag) {
        tags.add(tag);
    }

    public void removeTag(final Tag tag) {
        tags.remove(tag);
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
        String res = "Lists:\n";
        for (TaskList taskList : this.taskLists) {
            res = res + taskList.toString() + "\n";
        }

        res = res + "Tags:\n";
        for(Tag tag : this.tags) {
            res = res + tag.toString() + "\n";
        }

        return res;
    }


}
