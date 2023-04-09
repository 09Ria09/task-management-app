package client.mocks;


import client.customExceptions.BoardException;
import client.utils.RestUtils;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import javafx.util.Pair;

public class MockRestUtils extends RestUtils {

    private ResponseClone mockResponse;
    private boolean throwException;
    public void setMockResponse(final ResponseClone response) {
        this.mockResponse = response;
    }

    public void setThrowErrorOnHandleResponse(final boolean throwException) {
        this.throwException = throwException;
    }
    @Override
    public Response sendRequest(final String address, final String path,
                                final Methods method, final Object entity,
                                final Pair<String, Object> parameter) {
        if (throwException) {
            throw new RuntimeException("Test exception");
        }
        return mockResponse;
    }

    @Override
    public Response sendRequest(final String address, final String path,
                                final Methods method, final Object entity) {
        if (throwException) {
            throw new RuntimeException("Test exception");
        }
        return mockResponse;
    }

    @Override
    public <T> T handleResponse(final Response response, final Class<T> type,
                                final String method) throws Exception {
        if (throwException) {
            throw new RuntimeException("Test exception");
        }
        return super.handleResponse(response, type, method);
    }

    @Override
    public <T> T handleResponse(final Response response, final GenericType<T> type,
                                final String method) throws BoardException {
        if (throwException) {
            throw new RuntimeException("Test exception");
        }
        return super.handleResponse(response, type, method);
    }

}

