package client.utils;

import client.mocks.HttpResponseClone;
import client.mocks.MockHttpClient;
import client.mocks.MockRestUtils;
import client.mocks.ResponseClone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ServerUtilsTest {
    private ServerUtils serverUtils;

    @BeforeEach
    public void setup() {
        serverUtils = new ServerUtils();
    }

    @Test
    public void testIsTalioServer_good() {
        String serverAddress = "localhost";
        serverUtils.setServerAddress(serverAddress);
        MockHttpClient mockHttpClient = new MockHttpClient();

        mockHttpClient.setResponse(new HttpResponseClone(200, "true"));
        serverUtils.setHttpClient(mockHttpClient);

        Optional<String> result = serverUtils.isTalioServer();
        assertTrue(result.isEmpty());
    }

    @Test
    public void testIsTalioServer_notTalio() {
        String serverAddress = "localhost";
        serverUtils.setServerAddress(serverAddress);
        MockHttpClient mockHttpClient = new MockHttpClient();

        mockHttpClient.setResponse(new HttpResponseClone(404, ""));
        serverUtils.setHttpClient(mockHttpClient);

        Optional<String> result = serverUtils.isTalioServer();
        assertEquals("Not a Talio server", result.get());
    }

    @Test
    public void testIsTalioServer_notTalio2() {
        String serverAddress = "localhost";
        serverUtils.setServerAddress(serverAddress);
        MockHttpClient mockHttpClient = new MockHttpClient();

        mockHttpClient.setResponse(new HttpResponseClone(200, "false"));
        serverUtils.setHttpClient(mockHttpClient);

        Optional<String> result = serverUtils.isTalioServer();
        assertEquals("Not a Talio server", result.get());
    }

    @Test
    public void testIsTalioServer_notTalio3() {
        String serverAddress = "localhost";
        serverUtils.setServerAddress(serverAddress);
        MockHttpClient mockHttpClient = new MockHttpClient();

        mockHttpClient.setResponse(new HttpResponseClone(500, ""));
        serverUtils.setHttpClient(mockHttpClient);

        Optional<String> result = serverUtils.isTalioServer();
        assertEquals("Unexpected response status", result.get());
    }
    @Test
    public void testIsTalioServer_IOException() {
        String serverAddress = "localhost";
        serverUtils.setServerAddress(serverAddress);
        MockHttpClient mockHttpClient = new MockHttpClient();

        mockHttpClient.setIoException(true);
        mockHttpClient.setResponse(null);
        serverUtils.setHttpClient(mockHttpClient);

        Optional<String> result = serverUtils.isTalioServer();
        assertEquals("IOException", result.get());
    }

    @Test
    public void testIsTalioServer_InterruptedException() throws Exception {
        String serverAddress = "localhost";
        serverUtils.setServerAddress(serverAddress);
        MockHttpClient mockHttpClient = new MockHttpClient();

        mockHttpClient.setInterruptedException(true);
        mockHttpClient.setResponse(null);
        serverUtils.setHttpClient(mockHttpClient);

        Optional<String> result = serverUtils.isTalioServer();
        assertEquals("InterruptedException", result.get());
    }


    @Test
    public void testGetWebsocketURL() {
        String serverAddress = "localhost";
        serverUtils.setServerAddress(serverAddress);

        String websocketURL = serverUtils.getWebsocketURL();
        assertEquals("ws://localhost:8080/websocket", websocketURL);
    }

    @Test
    public void testGetWebsocketURLEmpty() {
        serverUtils.disconnect();

        String websocketURL = serverUtils.getWebsocketURL();
        assertEquals("", websocketURL);
    }

    @Test
    public void testGetServerAddress() {
        String serverAddress = "localhost";
        serverUtils.setServerAddress(serverAddress);

        String result = serverUtils.getServerAddress();
        assertEquals("http://localhost:8080", result);
    }

    @Test
    public void testDisconnect() {
        String serverAddress = "localhost";
        serverUtils.setServerAddress(serverAddress);
        serverUtils.disconnect();

        String result = serverUtils.getServerAddress();
        assertNull(result);
    }

    @Test
    public void testGetAdminKey() throws Exception {
        String serverAddress = "localhost";
        serverUtils.setServerAddress(serverAddress);

        MockRestUtils mockRestUtils = new MockRestUtils();
        mockRestUtils.setMockResponse(new ResponseClone(200, "admin_key"));
        serverUtils.setRestUtils(mockRestUtils);

        String adminKey = serverUtils.getAdminKey();
        assertEquals("admin_key", adminKey);
    }

    @Test
    public void testGetRestUtils() {
        assertNotNull(serverUtils.getRestUtils());
    }
}