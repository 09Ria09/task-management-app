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
    @ElementCollection
    private List<String> boardMembers;
    @OneToMany(cascade=CascadeType.ALL, orphanRemoval = true)
    private List<TaskList> taskLists;
    @OneToMany(cascade=CascadeType.ALL)
    private List<Tag> tags;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private BoardColorScheme boardColorScheme;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskPreset> taskPresets;


    public Board(final String name, final List<TaskList> listTaskList, final List<Tag> tags) {
        this.name = name;
        this.taskLists = listTaskList;
        this.tags = tags;
        this.boardMembers= new ArrayList<>();
        boardColorScheme = new BoardColorScheme();
        this.taskPresets = new ArrayList<>();
    }

    /**
     * I added this separate constructor in order to create the key in the
     * service based on the id. We can't access the id before the board
     * is actually saved so that is why this process was moved to the
     * boardservice
     *
     * @param name the name of the board
     * @param listTaskList the list of tasklists
     * @param tags the list of tags
     * @param inviteKey the key to invite people to the board
     */
    public Board(final String name, final List<TaskList> listTaskList,
                 final List<Tag> tags, final String inviteKey) {
        this.name = name;
        this.taskLists = listTaskList;
        this.tags = tags;
        this.boardMembers = new ArrayList<>();
        this.inviteKey = inviteKey;
        boardColorScheme = new BoardColorScheme();
        this.taskPresets = new ArrayList<>();
        addTaskList(new TaskList("To Do"));
        addTaskList(new TaskList("Doing"));
        addTaskList(new TaskList("Done"));
        TaskPreset taskPreset=new TaskPreset("Default", "0xccccccff", "0x000000ff");
        taskPreset.setDefault(true);
        addTaskPreset(taskPreset);
    }

    public Board() {
        this.taskLists = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.name = "";
        this.boardMembers = new ArrayList<>();
        this.inviteKey = "";
        boardColorScheme = new BoardColorScheme();
        this.taskPresets = new ArrayList<>();

    }

    public List<TaskPreset> getTaskPresets() {
        return taskPresets;
    }

    //this constructor is just for copying a board
    // to solve the issue of deleting a board causing null
    //pointer exceptions
    public Board(final Board other) {
        this.name = other.name;
        this.taskLists = new ArrayList<>(other.taskLists);
        this.tags = new ArrayList<>(other.tags);
        this.boardMembers = new ArrayList<>(other.boardMembers);
        this.id = other.id;
        boardColorScheme = new BoardColorScheme();
        this.inviteKey = other.inviteKey;
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

    public Optional<Tag> getTagById(final long id) {
        return tags.stream().filter(x -> x.id == id).findFirst();
    }

    public void addTag(final Tag tag) {
        tags.add(tag);
    }

    public void removeTag(final Tag tag) {
        tags.remove(tag);
    }


    /**
     * method used for getting the invite key of a board
     * used for copying the invite key in the overview
     * @return the invite key of a board
     */
    public String getInviteKey() {
        return inviteKey;
    }

    /**
     * method used for returning the board members of a board
     * this way we can try and identify further which boards
     * a user (which should be identified further by their connection
     * string) has joined
     * @return the list of board members
     */
    public List<String> getBoardMembers() {
        return boardMembers;
    }

    /**
     * This method adds a new member to the list of
     * existing members.
     * @param member the member that will be added.
     */
    public void addBoardMember(final String member){
        if(!this.boardMembers.contains(member))
            this.boardMembers.add(member);
    }

    /**
     * This method removes a current member to the list of
     * existing members.
     * @param member the member that will be removed.
     */
    public void removeBoardMember(final String member){
        this.boardMembers.remove(member);
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

        res = res + "Members:\n";
        for(String member : this.boardMembers) {
            res = res + member + "\n";
        }

        return res;
    }

    public BoardColorScheme getBoardColorScheme() {
        return boardColorScheme;
    }

    public void setBoardColorScheme(final BoardColorScheme boardColorScheme) {
        this.boardColorScheme = boardColorScheme;
    }



    public void addTaskPreset(final TaskPreset taskPreset) {
        taskPresets.add(taskPreset);
    }

    /**
     * This method removes a task preset from the list of
     * existing task presets.
     * @param taskPresetId the id of the task preset that will be removed.
     */
    public void removeTaskPreset(final long taskPresetId) {
        TaskPreset taskPreset = null;
        TaskPreset defaultPreset = null;
        for (TaskPreset preset : taskPresets) {
            if(preset.isDefault())
                defaultPreset = preset;
            if(preset.getId() == taskPresetId)
                taskPreset = preset;
        }
        for (TaskList taskList : taskLists) {
            for (Task task : taskList.getTasks()) {
                if (task.getTaskPreset() != null && task.getTaskPreset().equals(taskPreset)) {
                    task.setTaskPreset(defaultPreset);
                }
            }
        }
        taskPresets.remove(taskPreset);
    }

    /**
     * This method updates a task preset from the list of
     * existing task presets.
     * @param taskPreset the task preset that will be updated.
     */
    public void updateTaskPreset(final TaskPreset taskPreset) {
        if (taskPreset.isDefault()) {
            for (TaskPreset preset : taskPresets) {
                if (preset.isDefault() && !preset.equals(taskPreset)) {
                    preset.setDefault(false);
                }
            }
        }
        int index = -1;
        for (int i = 0; i < taskPresets.size(); i++) {
            if (taskPresets.get(i).getId() == taskPreset.getId()) {
                index = i;
                break;
            }
        }
        if (index >= 0) {
            taskPresets.set(index, taskPreset);
        }
    }

    /**
     * This method returns the default task preset.
     * @return the default task preset.
     */
    public TaskPreset findDefaultTaskPreset() {
        for (TaskPreset taskPreset : taskPresets) {
            if (taskPreset.isDefault()) {
                return taskPreset;
            }
        }
        return null;
    }
}
