package client.scenes;

import javax.inject.Inject;

public class WrongServerCtrl {

    private final MainCtrl mainCtrl;

    @Inject
    public WrongServerCtrl(final MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    //used for the go back button
    public void showSelectServer() {
        mainCtrl.showSelectServer();
    }
}
