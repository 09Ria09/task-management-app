package client.mocks;

import client.scenes.MainCtrl;
import client.services.ServerService;
import client.utils.ServerUtils;

import java.util.ArrayList;
import java.util.List;

public class MockServerService extends ServerService {
    private List<String> calledMethods = new ArrayList<>();
    private String connectResult;

    public MockServerService(final ServerUtils server, final MainCtrl mainCtrl) {
        super(server, mainCtrl);
    }

    @Override
    public String connect(final String serverAddress) {
        calledMethods.add("connect");
        return connectResult;
    }

    @Override
    public void showBoardCatalogue() {
        calledMethods.add("showBoardCatalogue");
    }

    @Override
    public void showWrongServer() {
        calledMethods.add("showWrongServer");
    }

    @Override
    public void showTimeout() {
        calledMethods.add("showTimeout");
    }

    @Override
    public void showUnexpectedError() {
        calledMethods.add("showUnexpectedError");
    }

    public void setConnectResult(final String connectResult) {
        this.connectResult = connectResult;
    }

    public List<String> getCalledMethods() {
        return calledMethods;
    }

    public boolean isMethodCalled(final String methodName) {
        return calledMethods.contains(methodName);
    }
}
