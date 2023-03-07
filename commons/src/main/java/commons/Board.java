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

    public Board(String name, List<TaskList> listTaskList, List<Tag> tags) {
        this.name = name;
        this.taskLists = listTaskList;
        this.tags = tags;
    }

    public Board() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TaskList> getListTaskList() {
        return taskLists;
    }

    public void setListTaskList(List<TaskList> listTaskList) {
        this.taskLists = listTaskList;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public void addTaskList(TaskList taskList) {
        taskLists.add(taskList);
    }

    public void removeTaskList(TaskList taskList) {
        taskLists.remove(taskList);
    }

    public void addTask(Tag tag) {
        tags.add(tag);
    }

    public void removeTask(Tag tag) {
        tags.remove(tag);
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
