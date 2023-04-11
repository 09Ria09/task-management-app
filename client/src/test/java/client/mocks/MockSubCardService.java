package client.mocks;

import client.CustomAlert;
import client.customExceptions.SubTaskException;
import client.services.SubCardService;
import client.utils.NetworkUtils;
import commons.SubTask;
import commons.Task;

import java.util.ArrayList;
import java.util.List;

public class MockSubCardService extends SubCardService {

    private List<String> calledMethods = new ArrayList<>();


    public MockSubCardService(NetworkUtils networkUtils, SubTask subTask,
                              CustomAlert customAlert, long boardID, long listID, Task task) {
        super(networkUtils, subTask, customAlert, boardID, listID, task);
    }

    @Override
    public void showAlert(SubTaskException e) {
        calledMethods.add("showAlert");
    }

    @Override
    public boolean setSubTaskName(String newName) {
        return true;
    }

    @Override
    public void completeSubTask(boolean checked) {
        super.completeSubTask(checked);
    }

    @Override
    public boolean moveUp() {
        return super.moveUp();
    }

    @Override
    public boolean moveDown() {
        return super.moveDown();
    }

    @Override
    public boolean deleteSubTask() {
        return super.deleteSubTask();
    }
}
