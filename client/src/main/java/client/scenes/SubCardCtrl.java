package client.scenes;

import client.CustomAlert;
import client.utils.TaskListUtils;
import client.utils.TaskUtils;
import com.google.inject.Inject;
import commons.SubTask;
import commons.Task;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class SubCardCtrl {
    private SubTask subTask;
    @FXML
    private Text text;
    private ListCtrl listController;
    private TaskListUtils taskListUtils;
    private CustomAlert customAlert;
    private TaskUtils taskUtils;
    private MainCtrl mainCtrl;

    @Inject
    public void initialize(final SubTask subTask, final ListCtrl listCtrl,
                           final TaskListUtils listUtils, final CustomAlert customAlert,
                           final TaskUtils taskUtils, final MainCtrl mainCtrl) {
        this.subTask= subTask;
        this.text.setText(subTask.getName());
        this.listController = listCtrl;
        this.taskListUtils = listUtils;
        this.customAlert = customAlert;
        this.taskUtils = taskUtils;
        this.mainCtrl = mainCtrl;
    }

}
