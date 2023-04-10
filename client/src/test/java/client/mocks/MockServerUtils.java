package client.mocks;


import client.utils.RestUtils;
import client.utils.ServerUtils;

import java.util.Optional;

public class MockServerUtils extends ServerUtils {

    private RestUtils mockRestUtils;
    private String serverAddress;
    private Optional<String> isTalioServerResult;

    public void setMockRestUtils(final RestUtils mockRestUtils) {
        this.mockRestUtils = mockRestUtils;
    }

    public void setServerAddress(final String serverAddress) {
        this.serverAddress = serverAddress;
    }

    @Override
    public RestUtils getRestUtils() {
        return mockRestUtils;
    }

    @Override
    public String getServerAddress() {
        return serverAddress;
    }

    public void setIsTalioServerResult(final Optional<String> isTalioServerResult) {
        this.isTalioServerResult = isTalioServerResult;
    }
    @Override
    public Optional<String> isTalioServer() {
        return isTalioServerResult;
    }
}