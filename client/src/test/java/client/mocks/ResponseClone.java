package client.mocks;

import jakarta.ws.rs.core.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.*;

public class ResponseClone extends Response {
    private final int status;
    private final Object entity;

    public ResponseClone(final int status, final Object entity) {
        this.status = status;
        this.entity = entity;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public StatusType getStatusInfo() {
        return null;
    }

    @Override
    public Object getEntity() {
        return entity;
    }

    @Override
    public <T> T readEntity(final Class<T> entityType) {
        if (entityType.isInstance(entity)) {
            return entityType.cast(entity);
        }
        return null;
    }

    @Override
    public <T> T readEntity(final GenericType<T> entityType) {
        Type type = entityType.getType();
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type rawType = parameterizedType.getRawType();
            if (rawType == List.class && entity instanceof List) {
                Type[] typeArguments = parameterizedType.getActualTypeArguments();
                if (typeArguments.length == 1 && typeArguments[0] instanceof Class) {
                    Class<?> elementType = (Class<?>) typeArguments[0];
                    List<?> list = (List<?>) entity;
                    if (list.isEmpty() || elementType.isInstance(list.get(0))) {
                        return (T) entityType.getRawType().cast(entity);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public <T> T readEntity(final Class<T> entityType, final Annotation[] annotations) {
        return null;
    }

    @Override
    public <T> T readEntity(final GenericType<T> entityType, final Annotation[] annotations) {
        return null;
    }

    @Override
    public boolean hasEntity() {
        return false;
    }

    @Override
    public boolean bufferEntity() {
        return false;
    }

    @Override
    public void close() {

    }

    @Override
    public MediaType getMediaType() {
        return null;
    }

    @Override
    public Locale getLanguage() {
        return null;
    }

    @Override
    public int getLength() {
        return 0;
    }

    @Override
    public Set<String> getAllowedMethods() {
        return null;
    }

    @Override
    public Map<String, NewCookie> getCookies() {
        return null;
    }

    @Override
    public EntityTag getEntityTag() {
        return null;
    }

    @Override
    public Date getDate() {
        return null;
    }

    @Override
    public Date getLastModified() {
        return null;
    }

    @Override
    public URI getLocation() {
        return null;
    }

    @Override
    public Set<Link> getLinks() {
        return null;
    }

    @Override
    public boolean hasLink(final String relation) {
        return false;
    }

    @Override
    public Link getLink(final String relation) {
        return null;
    }

    @Override
    public Link.Builder getLinkBuilder(final String relation) {
        return null;
    }

    @Override
    public MultivaluedMap<String, Object> getMetadata() {
        return null;
    }

    @Override
    public MultivaluedMap<String, String> getStringHeaders() {
        return null;
    }

    @Override
    public String getHeaderString(final String name) {
        return null;
    }
}
