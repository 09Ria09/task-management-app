package server.config;

import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.StompWebSocketEndpointRegistration;
import org.springframework.web.socket.config.annotation.WebMvcStompEndpointRegistry;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;
import org.springframework.web.util.UrlPathHelper;

import java.util.ArrayList;
import java.util.List;

public class TestStompEndpointRegistry implements StompEndpointRegistry {

    private List<String> endpoints = new ArrayList<>();
    @Override
    public StompWebSocketEndpointRegistration addEndpoint(String... paths) {
        endpoints.addAll(List.of(paths));
        return null;
    }

    @Override
    public void setOrder(int order) {

    }

    @Override
    public void setUrlPathHelper(UrlPathHelper urlPathHelper) {

    }

    @Override
    public WebMvcStompEndpointRegistry setErrorHandler(StompSubProtocolErrorHandler errorHandler) {
        return null;
    }

    public List<String> getEndpoints() {
        return endpoints;
    }
}
