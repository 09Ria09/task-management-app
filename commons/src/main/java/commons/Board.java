package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    private String name;
    private String inviteKey;
    @OneToMany(cascade=CascadeType.ALL, orphanRemoval = true)
    private List<TaskList> taskLists;
    @OneToMany(cascade=CascadeType.ALL)
    private List<Tag> tags;

    public Board(final String name, final List<TaskList> listTaskList, final List<Tag> tags) {
        this.name = name;
        this.taskLists = listTaskList;
        this.tags = tags;
        this.inviteKey = generateInviteKey();
    }

    public Board() {
        this.taskLists = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.name = "";
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

    public long getId() {
        return id;
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

    /**
     *  This basically generates a random string of two uppercase letters
     *  for the invite key so that it is nice and easy to remember
     * @return a new string of two uppercase letters chosen at random
     */
    private String createKeyPart() {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        char[] keyCharacters = new char[2];
        for(int i = 0; i < 2; i++) {
            int randomIndex = (int) (Math.random() * alphabet.length());
            keyCharacters[i] = alphabet.charAt(randomIndex);
        }
        return new String(keyCharacters);
    }

    /**
     * this generates the actual invite key for a board
     * @return a string of the form "123XD" where the first three
     * digits are just the board id
     */
    private String generateInviteKey() {
        return String.format("%03d", id) + createKeyPart();
    }

    /**
     * method used for getting the invite key of a board
     * used for copying the invite key in the overview
     * @return the invite key of a board
     */
    public String getInviteKey() {
        return inviteKey;
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
        String res = "Board (" + id + ") : " + name + "\nLists:\n";
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
