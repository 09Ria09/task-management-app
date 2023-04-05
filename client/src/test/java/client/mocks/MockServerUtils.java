package client.mocks;


import client.utils.RestUtils;
import client.utils.ServerUtils;

public class MockServerUtils extends ServerUtils {

    private RestUtils mockRestUtils;
    private String serverAddress;

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
}