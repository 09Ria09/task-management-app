package client.scenes.connectScenes;

import client.mocks.MockServerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SelectServerCtrlTest {

    private SelectServerCtrl selectServerCtrl;
    private MockServerService mockServerService;

    @BeforeEach
    public void setUp() {
        mockServerService = new MockServerService(null, null);
        selectServerCtrl = new SelectServerCtrl(mockServerService);
    }

    @Test
    public void testConnectAndShowBoards_connected() {
        mockServerService.setConnectResult("connected");

        String result = selectServerCtrl.connectShowBoards("testServer");

        assertEquals("connected", result);
        assertTrue(mockServerService.isMethodCalled("connect"));
        assertTrue(mockServerService.isMethodCalled("showBoardCatalogue"));
    }

    @Test
    public void testConnectAndShowBoards_wrongServer() {
        mockServerService.setConnectResult("wrongServer");

        String result = selectServerCtrl.connectShowBoards("testServer");

        assertEquals("wrongServer", result);
        assertTrue(mockServerService.isMethodCalled("connect"));
        assertTrue(mockServerService.isMethodCalled("showWrongServer"));
    }

    @Test
    public void testConnectAndShowBoards_timeout() {
        mockServerService.setConnectResult("timeout");

        String result = selectServerCtrl.connectShowBoards("testServer");

        assertEquals("timeout", result);
        assertTrue(mockServerService.isMethodCalled("connect"));
        assertTrue(mockServerService.isMethodCalled("showTimeout"));
    }

    @Test
    public void testConnectAndShowBoards_unexpectedError() {
        mockServerService.setConnectResult("unexpectedError");

        String result = selectServerCtrl.connectShowBoards("testServer");

        assertEquals("unexpectedError", result);
        assertTrue(mockServerService.isMethodCalled("connect"));
        assertTrue(mockServerService.isMethodCalled("showUnexpectedError"));
    }
}
