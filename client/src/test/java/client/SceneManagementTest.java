package client;

import client.customExceptions.BoardException;
import client.mocks.MockMainCtrl;
import client.sceneManagement.BoardScenes;
import client.scenes.*;
import client.utils.*;
import javafx.scene.Parent;
import javafx.util.Pair;
import org.apache.catalina.Server;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class SceneManagementTest {
    @Test
    public void testGetEditBoard() {
        EditBoardCtrl editBoardCtrl = new EditBoardCtrl(new ServerUtils(), new MainCtrl(),
                new BoardUtils(new ServerUtils()), new CustomAlert(), new WebSocketUtils(new ServerUtils()));
        Pair<EditBoardCtrl, Parent> editBoardCtrlParentPair = new Pair<EditBoardCtrl, Parent>(editBoardCtrl,
                new Parent() {});
        BoardScenes boardScenes = new BoardScenes(editBoardCtrlParentPair, null, null);
        assertSame(editBoardCtrlParentPair, boardScenes.getEditBoard());
    }

    @Test
    public void testGetColorManagementView() throws BoardException {
        ColorManagementViewCtrl colorManagementViewCtrl = new ColorManagementViewCtrl(new ServerUtils(), new MainCtrl(),
                new BoardUtils(new ServerUtils()), new WebSocketUtils(new ServerUtils()));
        Pair<ColorManagementViewCtrl, Parent> colorManagementViewCtrlParentPair = new Pair<ColorManagementViewCtrl,
                Parent>(colorManagementViewCtrl, new Parent() {});
        BoardScenes boardScenes = new BoardScenes(null, colorManagementViewCtrlParentPair, null);
        assertSame(colorManagementViewCtrlParentPair, boardScenes.getColorManagementView());
    }

    @Test
    public void testGetTagOverview() {
        TagOverviewCtrl tagOverviewCtrl = new TagOverviewCtrl(new TagUtils(new ServerUtils()), new CustomAlert(),
                new BoardUtils(new ServerUtils()), new MainCtrl(), new WebSocketUtils(new ServerUtils()),
                new LayoutUtils());
        Pair<TagOverviewCtrl, Parent> tagOverviewCtrlParentPair = new Pair<TagOverviewCtrl,
                Parent>(tagOverviewCtrl, new Parent() {});
        BoardScenes boardScenes = new BoardScenes(null, null, tagOverviewCtrlParentPair);
        assertSame(tagOverviewCtrlParentPair, boardScenes.getTagOverview());
    }
}
