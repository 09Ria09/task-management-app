package client.scenes;



import javax.inject.Inject;

public class ServerTimeoutCtrl {
    private final MainCtrl mainCtrl;

    @Inject
    public ServerTimeoutCtrl(final MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    //used for the go back button
    public void showSelectServer() {
        mainCtrl.showSelectServer();
    }


}
