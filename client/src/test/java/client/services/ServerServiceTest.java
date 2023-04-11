package client.services;


import client.mocks.MockMainCtrl;
import client.mocks.MockServerUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ServerServiceTest {
    private ServerService serverService;
    private MockMainCtrl mockMainCtrl;
    private MockServerUtils mockServerUtils;

    @BeforeEach
    public void setUp() {
        mockMainCtrl = new MockMainCtrl();
        mockServerUtils = new MockServerUtils();
        serverService = new ServerService(mockServerUtils, mockMainCtrl);
    }

    @Test
    void testConnect_success() {
        mockServerUtils.setIsTalioServerResult(Optional.empty());
        String result = serverService.connect("localhost");
        assertEquals("connected", result);
    }

    @Test
    void testConnect_wrongServer() {
        mockServerUtils.setIsTalioServerResult(Optional.of("Not a Talio server"));
        String result = serverService.connect("localhost");
        assertEquals("wrongServer", result);
    }

    @Test
    void testConnect_timeout() {
        mockServerUtils.setIsTalioServerResult(Optional.of("IOException"));
        String result = serverService.connect("localhost");
        assertEquals("timeout", result);
    }

    @Test
    void testConnect_unexpectedError() {
        mockServerUtils.setIsTalioServerResult(Optional.of("InterruptedException"));
        String result = serverService.connect("localhost");
        assertEquals("unexpectedError", result);
    }

    @Test
    void testShowBoardCatalogue() {
        serverService.showBoardCatalogue();
        assertTrue(mockMainCtrl.isMethodCalled("showBoardCatalogue"), "The showBoardCatalogue method should be called");
    }

    @Test
    void testShowWrongServer() {
        serverService.showWrongServer();
        assertTrue(mockMainCtrl.isMethodCalled("showWrongServer"), "The showWrongServer method should be called");
    }

    @Test
    void testShowTimeout() {
        serverService.showTimeout();
        assertTrue(mockMainCtrl.isMethodCalled("showTimeout"), "The showTimeout method should be called");
    }

    @Test
    void testShowUnexpectedError() {
        serverService.showUnexpectedError();
        assertTrue(mockMainCtrl.isMethodCalled("showUnexpectedError"), "The showUnexpectedError method should be called");
    }
}