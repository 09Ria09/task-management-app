package client.utils;
import client.mocks.MockMainCtrl;
import client.scenes.connectScenes.ServerTimeoutCtrl;
import client.scenes.connectScenes.UnexpectedErrorCtrl;
import client.scenes.connectScenes.WrongServerCtrl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ServerCtrlTest {

    @Test
    public void testUnexpectedError() {
        MockMainCtrl mainCtrl = new MockMainCtrl();
        UnexpectedErrorCtrl unexpectedErrorCtrl = new UnexpectedErrorCtrl(mainCtrl);
        unexpectedErrorCtrl.showSelectServer();
        assertTrue(mainCtrl.isMethodCalled("showSelectServer"));
    }

    @Test
    public void testServerTimeout() {
        MockMainCtrl mainCtrl = new MockMainCtrl();
        ServerTimeoutCtrl serverTimeoutCtrl = new ServerTimeoutCtrl(mainCtrl);
        serverTimeoutCtrl.showSelectServer();
        assertTrue(mainCtrl.isMethodCalled("showSelectServer"));
    }

    @Test
    public void testWrongServer() {
        MockMainCtrl mainCtrl = new MockMainCtrl();
        WrongServerCtrl wrongServerCtrl = new WrongServerCtrl(mainCtrl);
        wrongServerCtrl.showSelectServer();
        assertTrue(mainCtrl.isMethodCalled("showSelectServer"));
    }
}
