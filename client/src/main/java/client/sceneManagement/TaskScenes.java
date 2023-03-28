package client.sceneManagement;

import client.scenes.CreateTaskCtrl;
import client.scenes.DetailedTaskViewCtrl;
import client.scenes.EditTaskCtrl;
import javafx.scene.Parent;
import javafx.util.Pair;

public class TaskScenes {

    private final Pair<CreateTaskCtrl, Parent> createTask;
    private final Pair<EditTaskCtrl, Parent> editTask;
    private final Pair<DetailedTaskViewCtrl, Parent> detailedTaskView;

    public TaskScenes(final Pair<CreateTaskCtrl, Parent> createTask,
                      final Pair<EditTaskCtrl, Parent> editTask,
                      final Pair<DetailedTaskViewCtrl, Parent> detailedTaskView) {
        this.createTask = createTask;
        this.editTask = editTask;
        this.detailedTaskView = detailedTaskView;
    }

    public Pair<CreateTaskCtrl, Parent> getCreateTask() {
        return createTask;
    }

    public Pair<EditTaskCtrl, Parent> getEditTask() {
        return editTask;
    }

    public Pair<DetailedTaskViewCtrl, Parent> getDetailedTaskView() { return detailedTaskView; }
}
