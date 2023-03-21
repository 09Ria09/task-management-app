package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Task;
import commons.TaskList;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ListCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final Map<Long, Integer> listsMap;
    private long currentBoardId;

    @Inject
    public ListCtrl(final ServerUtils server, final MainCtrl mainCtrl, final long currentBoardId) {
        this.currentBoardId = currentBoardId;
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.listsMap = new HashMap<>();
    }

    public void addTask() {
        mainCtrl.showCreateTask();
    }
}
