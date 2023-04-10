package client;

import client.customExceptions.BoardException;
import client.sceneManagement.BoardScenes;
import client.sceneManagement.ListScenes;
import client.sceneManagement.ServerScenes;
import client.sceneManagement.TaskScenes;
import client.scenes.*;
import client.scenes.adminScenes.AdminBoardCtrl;
import client.scenes.adminScenes.AdminLoginCtrl;
import client.scenes.connectScenes.SelectServerCtrl;
import client.scenes.connectScenes.ServerTimeoutCtrl;
import client.scenes.connectScenes.UnexpectedErrorCtrl;
import client.scenes.connectScenes.WrongServerCtrl;
import client.services.ServerService;
import client.utils.*;
import javafx.scene.Parent;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class SceneManagementTest {
    @Test
    public void testGetEditBoard() {
        EditBoardCtrl editBoardCtrl = new EditBoardCtrl(new NetworkUtils(null, null, null, null, null, null),
                null, null, null);
        Pair<EditBoardCtrl, Parent> editBoardCtrlParentPair = new Pair<EditBoardCtrl, Parent>(editBoardCtrl,
                new Parent() {});
        BoardScenes boardScenes = new BoardScenes(editBoardCtrlParentPair, null, null);
        assertSame(editBoardCtrlParentPair, boardScenes.getEditBoard());
    }

    @Test
    public void testGetColorManagementView() throws BoardException {
        ColorManagementViewCtrl colorManagementViewCtrl = new ColorManagementViewCtrl(null,
                new NetworkUtils(null, null, null, null, null, null),
                null, null);
        Pair<ColorManagementViewCtrl, Parent> colorManagementViewCtrlParentPair = new Pair<ColorManagementViewCtrl,
                Parent>(colorManagementViewCtrl, new Parent() {});
        BoardScenes boardScenes = new BoardScenes(null, colorManagementViewCtrlParentPair, null);
        assertSame(colorManagementViewCtrlParentPair, boardScenes.getColorManagementView());
    }

    @Test
    public void testGetTagOverview() {
        TagOverviewCtrl tagOverviewCtrl = new TagOverviewCtrl(null, null, null, null,
                new NetworkUtils(null, null, null, null, null, null));
        Pair<TagOverviewCtrl, Parent> tagOverviewCtrlParentPair = new Pair<TagOverviewCtrl,
                Parent>(tagOverviewCtrl, new Parent() {});
        BoardScenes boardScenes = new BoardScenes(null, null, tagOverviewCtrlParentPair);
        assertSame(tagOverviewCtrlParentPair, boardScenes.getTagOverview());
    }

    @Test
    public void testGetCreateList() {
        TaskListUtils taskListUtils = new TaskListUtils(new ServerUtils());
        EditBoardCtrl editBoardCtrl = new EditBoardCtrl(new NetworkUtils(null, null, null, null, null, null),
                null, null, null);
        BoardCatalogueCtrl boardCatalogueCtrl = new BoardCatalogueCtrl(null, null,
                new NetworkUtils(null, null, null, null, null, null), null, null, null);
        BoardOverviewCtrl boardOverviewCtrl = new BoardOverviewCtrl(null, null,
                new NetworkUtils(null, null, null, null, null, null), null, null,
                new WebSocketUtils(new ServerUtils()));
        CreateListCtrl createListCtrl = new CreateListCtrl(new NetworkUtils(null, null, null, null, null, null),
                boardOverviewCtrl, new MainCtrl(), null, null, null);
        Pair<CreateListCtrl, Parent> createListCtrlParentPair = new Pair<CreateListCtrl,
                Parent>(createListCtrl, new Parent() {});
        ListScenes listScenes = new ListScenes(createListCtrlParentPair);
        assertSame(createListCtrlParentPair, listScenes.getCreateList());
    }

    @Test
    public void testGetSelectServer() {
        SelectServerCtrl selectServerCtrl = new SelectServerCtrl(new ServerService(new ServerUtils(), new MainCtrl()));
        Pair<SelectServerCtrl, Parent> selectServerCtrlParentPair = new Pair<SelectServerCtrl,
                Parent>(selectServerCtrl, new Parent() {});
        ServerScenes serverScenes = new ServerScenes(selectServerCtrlParentPair, null, null,
                null, null, null);
        assertSame(selectServerCtrlParentPair, serverScenes.getSelectServer());
    }

    @Test
    public void testGetWrongServer() {
        WrongServerCtrl wrongServerCtrl = new WrongServerCtrl(null);
        Pair<WrongServerCtrl, Parent> wrongServerCtrlParentPair = new Pair<WrongServerCtrl,
                Parent>(wrongServerCtrl, new Parent() {});
        ServerScenes serverScenes = new ServerScenes(null, wrongServerCtrlParentPair, null,
                null, null, null);
        assertSame(wrongServerCtrlParentPair, serverScenes.getWrongServer());
    }

    @Test
    public void testGetServerTimeout() {
        ServerTimeoutCtrl controller = new ServerTimeoutCtrl(null);
        Pair<ServerTimeoutCtrl, Parent> controllerPair = new Pair<ServerTimeoutCtrl,
                Parent>(controller, new Parent() {});
        ServerScenes serverScenes = new ServerScenes(null, null, controllerPair,
                null, null, null);
        assertSame(controllerPair, serverScenes.getServerTimeout());
    }

    @Test
    public void testGetUnexpectedError() {
        UnexpectedErrorCtrl controller = new UnexpectedErrorCtrl(null);
        Pair<UnexpectedErrorCtrl, Parent> controllerPair = new Pair<UnexpectedErrorCtrl,
                Parent>(controller, new Parent() {});
        ServerScenes serverScenes = new ServerScenes(null, null, null,
                controllerPair, null, null);
        assertSame(controllerPair, serverScenes.getUnexpectedError());
    }

    @Test
    public void testGetAdminLogin() {
        AdminLoginCtrl controller = new AdminLoginCtrl(null, new NetworkUtils(null, null, null, null, null, null), null);
        Pair<AdminLoginCtrl, Parent> controllerPair = new Pair<AdminLoginCtrl,
                Parent>(controller, new Parent() {});
        ServerScenes serverScenes = new ServerScenes(null, null, null,
                null, controllerPair, null);
        assertSame(controllerPair, serverScenes.getAdminLogin());
    }

    @Test
    public void testGetAdminBoard() {
        AdminBoardCtrl controller = new AdminBoardCtrl(null, null);
        Pair<AdminBoardCtrl, Parent> controllerPair = new Pair<AdminBoardCtrl,
                Parent>(controller, new Parent() {});
        ServerScenes serverScenes = new ServerScenes(null, null, null,
                null, null, controllerPair);
        assertSame(controllerPair, serverScenes.getAdminBoard());
    }

    @Test
    public void testGetCreateTask() {
        CreateTaskCtrl controller = new CreateTaskCtrl(null, null, null, null, new NetworkUtils(null, null, null, null, null, null));
        Pair<CreateTaskCtrl, Parent> controllerPair = new Pair<CreateTaskCtrl,
                Parent>(controller, new Parent() {});
        TaskScenes taskScenes = new TaskScenes(controllerPair, null);
        assertSame(controllerPair, taskScenes.getCreateTask());
    }

    @Test
    public void testGetDetailedTaskView() {
        DetailedTaskViewCtrl controller = new DetailedTaskViewCtrl(new NetworkUtils(null, null, null, null, null, null), null, null,
                null);
        Pair<DetailedTaskViewCtrl, Parent> controllerPair = new Pair<DetailedTaskViewCtrl,
                Parent>(controller, new Parent() {});
        TaskScenes taskScenes = new TaskScenes(null, controllerPair);
        assertSame(controllerPair, taskScenes.getDetailedTaskView());
    }
}
