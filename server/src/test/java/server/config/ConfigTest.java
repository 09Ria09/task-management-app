package server.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.WebSocketConfig;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConfigTest {


    private WebSocketConfig config;

    private TestStompEndpointRegistry endpointRegistry;
    private TestMessageBrokerRegistry brokerRegistry;

    @BeforeEach
    public void setup(){
        config = new WebSocketConfig();
        endpointRegistry = new TestStompEndpointRegistry();
        brokerRegistry = new TestMessageBrokerRegistry();

    }

    @Test
    public void testAddEndpoint(){
        config.registerStompEndpoints(endpointRegistry);
        assertFalse(endpointRegistry.getEndpoints().contains("test"));
        assertTrue(endpointRegistry.getEndpoints().contains("websocket"));
    }

    @Test
    public void testConfigureMessageBroker(){
        config.configureMessageBroker(brokerRegistry);
        assertTrue(brokerRegistry.getDestinationPrefixes().contains("/topic"));
        assertTrue(brokerRegistry.getApplicationDestinationPrefixes().contains("/app"));
    }
}
