package client.scenes;


import javax.inject.Inject;


public class UnexpectedErrorCtrl {

    private final MainCtrl mainCtrl;

    @Inject
    public UnexpectedErrorCtrl(final MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }



    //used for the go back button
    public void showSelectServer() {
        mainCtrl.showSelectServer();
    }
}
