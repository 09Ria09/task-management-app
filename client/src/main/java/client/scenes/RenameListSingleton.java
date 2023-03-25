package client.scenes;

public class RenameListSingleton {
    private static final RenameListSingleton renameListSingleton = new RenameListSingleton();
    private long boardId;
    private long listId;

    private RenameListSingleton(){}

    public static RenameListSingleton getInstance() {
        return renameListSingleton;
    }

    /**
     * Ids set when rename is pressed on a list in the board overview.
     * @param boardId id of board that the list to be renamed belongs to.
     * @param listId id of list to be renamed
     */
    public void setIds(long boardId, long listId) {
        this.boardId = boardId;
        this.listId = listId;
    }

    public long getBoardId() {
        return this.boardId;
    }

    public long getListId() {
        return this.listId;
    }
}
