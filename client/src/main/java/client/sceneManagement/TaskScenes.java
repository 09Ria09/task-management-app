package client.sceneManagement;

import client.scenes.CreateTaskCtrl;
import client.scenes.EditTaskCtrl;
import javafx.scene.Parent;
import javafx.util.Pair;

public class TaskScenes {

    private final Pair<CreateTaskCtrl, Parent> createTask;
    private final Pair<EditTaskCtrl, Parent> editTask;

    public TaskScenes(final Pair<CreateTaskCtrl, Parent> createTask,
                      final Pair<EditTaskCtrl, Parent> editTask) {
        this.createTask = createTask;
        this.editTask = editTask;
    }

    public Pair<CreateTaskCtrl, Parent> getCreateTask() {
        return createTask;
    }

    public Pair<EditTaskCtrl, Parent> getEditTask() {
        return editTask;
    }
}
