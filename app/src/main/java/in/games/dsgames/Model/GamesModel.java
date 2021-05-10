package in.games.dsgames.Model;

import java.util.ArrayList;

/**
 * Developed by Binplus Technologies pvt. ltd.  on 06,January,2021
 */
public class GamesModel {
    String title;
    ArrayList<TableModel> list;

    public GamesModel(String title, ArrayList<TableModel> list) {
        this.title = title;
        this.list = list;
    }

    public GamesModel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<TableModel> getList() {
        return list;
    }

    public void setList(ArrayList<TableModel> list) {
        this.list = list;
    }
}
