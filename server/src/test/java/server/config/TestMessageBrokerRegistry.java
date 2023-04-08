package server.config;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.config.SimpleBrokerRegistration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TestMessageBrokerRegistry extends MessageBrokerRegistry {

    public List<String> destinationPrefixes = new ArrayList<>();
    public List<String> appPrefixes = new ArrayList<>();

    public TestMessageBrokerRegistry() {
        super(new SubscribableChannel() {
            @Override
            public boolean subscribe(MessageHandler handler) {
                return false;
            }

            @Override
            public boolean unsubscribe(MessageHandler handler) {
                return false;
            }

            @Override
            public boolean send(Message<?> message, long timeout) {
                return false;
            }
        }, (message, timeout) -> false);
    }

    @Override
    public SimpleBrokerRegistration enableSimpleBroker(String... destinationPrefixes) {
        this.destinationPrefixes.addAll(List.of(destinationPrefixes));
        return null;
    }

    @Override
    public MessageBrokerRegistry setApplicationDestinationPrefixes(String... prefixes) {
        appPrefixes.addAll(List.of(prefixes));
        return null;
    }

    @Override
    protected Collection<String> getApplicationDestinationPrefixes() {
        return appPrefixes;
    }

    public List<String> getDestinationPrefixes() {
        return destinationPrefixes;
    }
}
