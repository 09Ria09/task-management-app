package client.services;

import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;

import java.util.Optional;

public class ServerService {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @Inject
    public ServerService(final ServerUtils server, final MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public String connect(final String serverAddress) {
        server.setServerAddress(serverAddress);
        try {
            Optional<String> result = server.isTalioServer();
            if (result.isEmpty()) {
                return "connected";
            } else if (result.get().equals("Not a Talio server")
                    || result.get().equals("Unexpected response status")) {
                return "wrongServer";
            } else if (result.get().equals("IOException")) {
                return "timeout";
            } else if (result.get().equals("InterruptedException")
                    || result.get().equals("Exception")) {
                return "unexpectedError";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "unexpectedError";
        }
        return "unexpectedError";
    }

    public void showBoardCatalogue() {
        mainCtrl.populateBoardCatalogue();
        mainCtrl.showBoardCatalogue();
    }

    public void showWrongServer() {
        mainCtrl.showWrongServer();
    }

    public void showTimeout() {
        mainCtrl.showTimeout();
    }

    public void showUnexpectedError() {
        mainCtrl.showUnexpectedError();
    }
}
