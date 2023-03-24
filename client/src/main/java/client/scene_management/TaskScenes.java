package client.scene_management;

import client.scenes.CreateTaskCtrl;
import javafx.scene.Parent;
import javafx.util.Pair;

public class TaskScenes {

    private final Pair<CreateTaskCtrl, Parent> createTask;

    public TaskScenes(final Pair<CreateTaskCtrl, Parent> createTask) {
        this.createTask = createTask;
    }

    public Pair<CreateTaskCtrl, Parent> getCreateTask() {
        return createTask;
    }


}
