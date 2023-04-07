package client.sceneManagement;

import client.scenes.CreateTaskCtrl;
import client.scenes.DetailedTaskViewCtrl;
import javafx.scene.Parent;
import javafx.util.Pair;

public class TaskScenes {

    private final Pair<CreateTaskCtrl, Parent> createTask;
    private final Pair<DetailedTaskViewCtrl, Parent> detailedTaskView;

    public TaskScenes(final Pair<CreateTaskCtrl, Parent> createTask,
                      final Pair<DetailedTaskViewCtrl, Parent> detailedTaskView) {
        this.createTask = createTask;
        this.detailedTaskView = detailedTaskView;
    }

    public Pair<CreateTaskCtrl, Parent> getCreateTask() {
        return createTask;
    }

    public Pair<DetailedTaskViewCtrl, Parent> getDetailedTaskView() { return detailedTaskView; }
}
