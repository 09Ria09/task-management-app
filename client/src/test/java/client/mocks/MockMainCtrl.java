package client.mocks;

import client.scenes.MainCtrl;

import java.util.ArrayList;
import java.util.List;

public class MockMainCtrl extends MainCtrl {
    private List<String> called = new ArrayList<>();

    @Override
    public void showBoardCatalogue() {
        called.add("showBoardCatalogue");
    }

    @Override
    public void showWrongServer() {
        called.add("showWrongServer");
    }

    @Override
    public void showTimeout() {
        called.add("showTimeout");
    }

    @Override
    public void showUnexpectedError() {
        called.add("showUnexpectedError");
    }

    public List<String> getCalledMethods() {
        return called;
    }

    public boolean isMethodCalled(final String methodName) {
        return called.contains(methodName);
    }
}
