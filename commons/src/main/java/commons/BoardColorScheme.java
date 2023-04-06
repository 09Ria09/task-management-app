package commons;

import javax.persistence.*;

@Entity
public class BoardColorScheme {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
    private String boardBackgroundColor;

    private String boardTextColor;

    private String listBackgroundColor;

    private String listTextColor;
    private String white = "0xffffffff";

    public BoardColorScheme() {
        this.boardBackgroundColor = white;
        this.boardTextColor = white;
        this.listBackgroundColor = white;
        this.listTextColor = white;
    }




    public String getBoardBackgroundColor() {
        return boardBackgroundColor;
    }

    public void setBoardBackgroundColor(final String boardBackgroundColor) {
        this.boardBackgroundColor = boardBackgroundColor;
    }

    public String getBoardTextColor() {
        return boardTextColor;
    }

    public void setBoardTextColor(final String boardTextColor) {
        this.boardTextColor = boardTextColor;
    }

    public String getListBackgroundColor() {
        return listBackgroundColor;
    }

    public void setListBackgroundColor(final String listBackgroundColor) {
        this.listBackgroundColor = listBackgroundColor;
    }

    public String getListTextColor() {
        return listTextColor;
    }

    public void setListTextColor(final String listTextColor) {
        this.listTextColor = listTextColor;
    }

    @Override
    public String toString() {
        return "BoardColorScheme{" +
                "id=" + id +
                ", boardBackgroundColor='" + boardBackgroundColor + '\'' +
                ", boardTextColor='" + boardTextColor + '\'' +
                ", listBackgroundColor='" + listBackgroundColor + '\'' +
                ", listTextColor='" + listTextColor + '\'' +
                ", taskPresets=" +
                '}';
    }

    public long getId() {
        return this.id;
    }



}
