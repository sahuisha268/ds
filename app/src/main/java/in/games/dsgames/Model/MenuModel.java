package in.games.dsgames.Model;

public class MenuModel {
    int icon;
    String name;
    int colorcode;

    public MenuModel(int icon, String name, int colorcode) {
        this.icon = icon;
        this.name = name;
        this.colorcode = colorcode;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColorcode() {
        return colorcode;
    }

    public void setColorcode(int colorcode) {
        this.colorcode = colorcode;
    }
}
