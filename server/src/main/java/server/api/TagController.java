package server.api;

import commons.Tag;
import commons.Task;
import commons.TaskList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import server.services.BoardService;
import server.services.TagService;

import java.util.*;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagService tagService;
    private final SimpMessagingTemplate messages;

    @Autowired
    public TagController(final TagService tagService, final BoardService boardService,
                         final SimpMessagingTemplate messages) {
        this.tagService = tagService;
        this.messages = messages;
        if(boardService.getBoards().isEmpty()) {
            boardService.createDefaultBoard();
        }
    }

    /**
     * Get util method to get all the tags from a board
     *
     * @param boardid ID of the board
     * @return a list of tags from the corresponding board
     */
    @GetMapping("/{boardid}/tags")
    public ResponseEntity<List<Tag>> getBoardTags(
            @PathVariable("boardid") final long boardid
    ) {
        try {
            List<Tag> tags = tagService.getBoardTags(boardid);
            return ResponseEntity.ok(tags);
        } catch(NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get util method to get all the tags from a task
     *
     * @param boardid ID of the board
     * @param listid ID of the list
     * @param taskid ID of the task from which all the tags are
     * @return a list of tags from the corresponding task
     */
    @GetMapping("/{boardid}/{listid}/{taskid}/tags")
    public ResponseEntity<List<Tag>> getTaskTags(
            @PathVariable("boardid") final long boardid,
            @PathVariable("listid") final long listid,
            @PathVariable("taskid") final long taskid
    ) {
        try {
            List<Tag> tags = tagService.getTaskTags(boardid, listid, taskid);
            return ResponseEntity.ok(tags);
        } catch(NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get util method to get a certain tag from a board
     *
     * @param boardid ID of the board
     * @param tagid ID of the tag you want to retrieve
     * @return the tag corresponding to the ID's given
     */
    @GetMapping("/{boardid}/{tagid}")
    public ResponseEntity<Tag> getBoardTag(
            @PathVariable("boardid") final long boardid,
            @PathVariable("tagid") final long tagid
    ) {
        try {
            Tag tag = tagService.getBoardTagByID(boardid, tagid);
            return ResponseEntity.ok(tag);
        } catch(NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get util method to get a certain tag from a task
     *
     * @param boardid ID of the board
     * @param listid ID of the list
     * @param taskid ID of the task you want to get the tag from
     * @param tagid ID of the tag you want to get
     * @return the tag corresponding to the ID's
     */
    @GetMapping("/{boardid}/{listid}/{taskid}/{tagid}")
    public ResponseEntity<Tag> getTaskTag(
            @PathVariable("boardid") final long boardid,
            @PathVariable("listid") final long listid,
            @PathVariable("taskid") final long taskid,
            @PathVariable("tagid") final long tagid
    ) {
        try {
            Tag tag = tagService.getTaskTagByID(boardid, listid, taskid, tagid);
            return ResponseEntity.ok(tag);
        } catch(NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Post util method to add a tag to a board
     *
     * @param boardid ID of the board
     * @param tag the tag you want to add
     * @return the tag that is added
     */
    @PostMapping("/{boardid}/tag")
    public ResponseEntity<Tag> addBoardTag(
            @PathVariable("boardid") final long boardid,
            @RequestBody final Tag tag
    ) {
        try {
            if(tag == null || isNullOrEmpty(tag.getName())
                || isNullOrEmpty(tag.getColorBackground()) || isNullOrEmpty(tag.getColorFont())){
                return ResponseEntity.badRequest().build();
            }
            Tag addedTag = tagService.addBoardTag(boardid, tag);
            Tag finalAddedTag = addedTag;
            addedTag = tagService.getBoardTags(boardid).stream()
                    .filter(x -> x.equals(finalAddedTag))
                    .findAny().orElse(null);


            messages.convertAndSend("/topic/" + boardid + "/addtag", addedTag);
            return ResponseEntity.ok(addedTag);
        } catch(NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Post util method to add a tag to a task
     *
     * @param boardid ID of the board
     * @param listid ID of the list
     * @param taskid ID of the task where you want to add the tag to
     * @param tag ID of the tag that will be added
     * @return the tag that has been added
     */
    @PostMapping("/{boardid}/{listid}/{taskid}/add")
    public ResponseEntity<Tag> addTaskTag(
            @PathVariable("boardid") final long boardid,
            @PathVariable("listid") final long listid,
            @PathVariable("taskid") final long taskid,
            @RequestBody final Tag tag
    ) {
        try {
            if(isNullOrEmpty(tag.getName())) {
                return ResponseEntity.badRequest().build();
            }
            Tag addedTag = tagService.addTaskTag(boardid, listid, taskid, tag.id);
            Task task = tagService.getTask(boardid, listid, taskid);
            messages.convertAndSend("/topic/" + boardid + "/modifytask", task);
            return ResponseEntity.ok(addedTag);
        } catch(NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Put request method to rename a tag
     *
     * @param boardid ID of the board
     * @param tagid ID of the tag that you will rename
     * @param name the new name of the tag
     * @return the tag after it is renamed
     */
    @PutMapping("/{boardid}/{tagid}/rename")
    public ResponseEntity<Tag> renameTag(
            @PathVariable("boardid") final long boardid,
            @PathVariable("tagid") final long tagid,
            @RequestParam final String name
    ) {
        try {
            if(isNullOrEmpty(name)) {
                return ResponseEntity.badRequest().build();
            }
            Tag tag = tagService.renameTag(boardid, tagid, name);
            messages.convertAndSend("/topic/" + boardid + "/changetag", tag);
            return ResponseEntity.ok(tag);
        } catch(NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Put request method to recolor a tag
     *
     * @param boardid ID of the board
     * @param tagid ID of the tag you want to recolor
     * @param backgroundColor the new color of the tag as an int
     * @param fontColor the new color of the tag as an int
     * @return the tag after it's color is changed
     */
    @PutMapping("/{boardid}/{tagid}/recolor")
    public ResponseEntity<Tag> recolorTag(
            @PathVariable("boardid") final long boardid,
            @PathVariable("tagid") final long tagid,
            @RequestParam final String backgroundColor,
            @RequestParam final String fontColor
    ) {
        try {
            if(isNullOrEmpty(backgroundColor) || isNullOrEmpty(fontColor)) {
                return ResponseEntity.badRequest().build();
            }
            Tag tag = tagService.recolorTag(boardid, tagid, backgroundColor, fontColor);
            messages.convertAndSend("/topic/" + boardid + "/changetag", tag);
            messages.convertAndSend("/topic/" + boardid + "/recolortag", tag);
            return ResponseEntity.ok(tag);
        } catch(NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete request to delete a tag from a board
     * It will also this tag from all tasks it is assigned to
     *
     * @param boardid ID of the board
     * @param tagid ID of the tag you want to delete
     * @return the tag you have deleted
     */
    @DeleteMapping("/{boardid}/delete/{tagid}")
    public ResponseEntity<Tag> deleteBoardTag(
            @PathVariable("boardid") final long boardid,
            @PathVariable("tagid") final long tagid
    ) {
        try {
            Tag tag = tagService.getBoardTagByID(boardid, tagid);
            for(TaskList list : tagService.getBoard(boardid).getListTaskList()) {
                for (Task task : list.getTasks()) {
                    if (task.getTagById(tagid).isPresent()) {
                        task.removeTag(tag);
                        messages.convertAndSend("/topic/" + boardid + "/modifytask", task);
                    }
                }
            }
            tag = tagService.removeBoardTag(boardid, tagid);
            messages.convertAndSend("/topic/" + boardid + "/deletetag", tag);
            return ResponseEntity.ok(tag);
        } catch(NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete request to delete a tag from a task
     *
     * @param boardid ID of the board
     * @param listid ID of the task list
     * @param taskid ID of the task you want to delete a tag from
     * @param tagid ID of the tag you want to remove
     * @return the tag you have deleted
     */
    @DeleteMapping("/{boardid}/{listid}/{taskid}/delete/{tagid}")
    public ResponseEntity<Tag> deleteTaskTag(
            @PathVariable("boardid") final long boardid,
            @PathVariable("listid") final long listid,
            @PathVariable("taskid") final long taskid,
            @PathVariable("tagid") final long tagid
    ) {
        try {
            Tag tag = tagService.removeTaskTag(boardid, listid, taskid, tagid);
            Task task = tagService.getTask(boardid, listid, taskid);
            messages.convertAndSend("/topic/" + boardid + "/modifytask", task);
            return ResponseEntity.ok(tag);
        } catch(NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Method to check if a string is null or empty
     *
     * @param name the string that will be checked
     * @return a boolean value, true if it is null or empty
     */
    private boolean isNullOrEmpty(final String name) {
        return name == null || name.isEmpty();
    }
}
