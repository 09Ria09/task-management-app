package client.utils;

import client.customExceptions.BoardException;
import client.customExceptions.ExceptionBuilder;
import jakarta.ws.rs.client.*;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import javafx.util.Pair;
import org.glassfish.jersey.client.ClientConfig;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class RestUtils {

    public Response sendRequest(final String address, final String path,
                                final Methods method, final Object entity,
                                final Pair<String, Object> parameter) {

        Client clientBuilder = ClientBuilder.newClient(new ClientConfig());
        if (address == null || address.isEmpty()) {
            throw new IllegalArgumentException("Server address cannot be empty");
        }

        Invocation.Builder builder = clientBuilder.target(address).path(path)
                .queryParam(parameter.getKey(), parameter.getValue())
                .request().accept(APPLICATION_JSON);
        return send(builder, method, entity);
    }

    public Response sendRequest(final String address, final String path,
                                final Methods method, final Object entity) {

        if (address == null || address.isEmpty()) {
            throw new IllegalArgumentException("Server address cannot be empty");
        }
        Invocation.Builder builder = ClientBuilder.newClient(new ClientConfig())
                .target(address)
                .path(path)
                .request()
                .accept(APPLICATION_JSON);
        return send(builder, method, entity);
    }

    private Response send(final Invocation.Builder builder, final Methods method,
                          final Object entity){
        switch (method) {
            case GET:
                return builder.get();
            case POST:
                return builder.post(Entity.entity(entity, APPLICATION_JSON));
            case PUT:
                return builder.put(Entity.entity(entity, APPLICATION_JSON));
            case DELETE:
                return builder.delete();
            default:
                throw new IllegalArgumentException("Invalid method");
        }
    }

    public <T> T handleResponse(final Response response, final Class<T> type,
                                final String method) throws Exception {
        ExceptionBuilder exception = new ExceptionBuilder().type(type);
        switch (response.getStatus()) {
            case 200:
                return response.readEntity(type);
            case 400:
                throw exception.message("Wrong value inputted ! (" + method + ")").build();
            case 404:
                throw exception.message("Not Found ! (" + method + ")").build();
            default:
                throw exception.message("An error occured in the response handling ! ("
                        + method + ")").build();
        }
    }

    public <T> T handleResponse(final Response response, final GenericType<T> type,
                                final String method) throws BoardException {
        switch (response.getStatus()) {
            case 200:
                return response.readEntity(type);
            case 400:
                throw new BoardException(type.getRawType().getTypeName() +
                        " : Wrong value inputted ! (" + method + ")");
            case 404:
                throw new BoardException(type.getRawType().getTypeName() +
                        " : Not Found ! (" + method + ")");
            default:
                throw new BoardException(type.getRawType().getTypeName() +
                        " : An error occured in the response handling ! (" + method + ")");
        }
    }

    public static enum Methods{
        GET,
        POST,
        PUT,
        DELETE;
    }
}
