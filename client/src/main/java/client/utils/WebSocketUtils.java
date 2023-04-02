package client.utils;

import commons.Board;
import commons.Task;
import commons.TaskList;
import org.springframework.http.codec.cbor.Jackson2CborDecoder;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import javax.inject.Inject;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public class WebSocketUtils {

    private final ServerUtils serverUtils;
    private StompSession session;

    private Map<String, Consumer<Board>> boardConsumers = new HashMap<>();
    private Map<String, Consumer<TaskList>> listConsumers = new HashMap<>();
    private Map<String, Consumer<Task>> taskConsumers = new HashMap<>();

    @Inject
    public WebSocketUtils(ServerUtils utils){
        this.serverUtils = utils;
        tryToConnect();
    }

    public void tryToConnect(){
        if(!serverUtils.getWebsocketURL().isEmpty())
            this.session = connectTo(this.serverUtils.getWebsocketURL());
    }

    public boolean isConnected(){
        return session != null && session.isConnected();
    }

    private StompSession connectTo(String url){
        StandardWebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        try{
            return stompClient.connect(url, new StompSessionHandlerAdapter() {}).get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        throw new IllegalStateException();
    }

    public <T> void registerForBoardMessages(String dest, Consumer<Board> consumer){
        if(boardConsumers.get(dest) == null) {
            session.subscribe(dest, new StompFrameHandler() {
                @Override
                public Type getPayloadType(StompHeaders headers) {
                    return Board.class;
                }

                @Override
                public void handleFrame(StompHeaders headers, Object payload) {
                    try {
                        boardConsumers.getOrDefault(dest, (o) -> {
                        }).accept((Board) payload);
                    } catch (ClassCastException e) {
                        System.out.println("Error during websocket handling : " + e.getMessage());
                    }
                }
            });
        }
        boardConsumers.put(dest, consumer);
        System.out.println("Registered correctly : " + dest);
    }
}
