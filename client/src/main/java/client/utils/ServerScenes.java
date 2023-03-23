package client.utils;

import client.scenes.SelectServerCtrl;
import client.scenes.UnexpectedErrorCtrl;
import client.scenes.ServerTimeoutCtrl;
import client.scenes.WrongServerCtrl;
import javafx.scene.Parent;
import javafx.util.Pair;

/**
 * A class that holds all the scenes that are used to connect to the server.
 * this class is used to reduce the number of parameters in the
 * initialize method and to group related scenes together
 */
public class ServerScenes {


    private final Pair<SelectServerCtrl, Parent> selectServer;
    private final Pair<WrongServerCtrl, Parent> wrongServer;
    private final Pair<ServerTimeoutCtrl, Parent> serverTimeout;
    private final Pair<UnexpectedErrorCtrl, Parent> unexpectedError;

    public ServerScenes(final Pair<SelectServerCtrl, Parent> selectServer,
                        final Pair<WrongServerCtrl, Parent> wrongServer,
                        final Pair<ServerTimeoutCtrl, Parent> serverTimeout,
                        final Pair<UnexpectedErrorCtrl, Parent> unexpectedError) {
        this.selectServer = selectServer;
        this.wrongServer = wrongServer;
        this.serverTimeout = serverTimeout;
        this.unexpectedError = unexpectedError;
    }

    public Pair<SelectServerCtrl, Parent> getSelectServer() {
        return selectServer;
    }

    public Pair<WrongServerCtrl, Parent> getWrongServer() {
        return wrongServer;
    }

    public Pair<ServerTimeoutCtrl, Parent> getServerTimeout() {
        return serverTimeout;
    }

    public Pair<UnexpectedErrorCtrl, Parent> getUnexpectedError() {
        return unexpectedError;
    }
}
