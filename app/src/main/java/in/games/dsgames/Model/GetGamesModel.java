package in.games.dsgames.Model;

public class GetGamesModel {
    String game_id,game_name,name,points,starline_points,is_starline,is_close,is_deleted,is_disabled,is_starline_disable;

   String is_ds_disabled;
    public GetGamesModel() {
    }

    public String getGame_id() {
        return game_id;
    }

    public void setGame_id(String game_id) {
        this.game_id = game_id;
    }

    public String getGame_name() {
        return game_name;
    }

    public void setGame_name(String game_name) {
        this.game_name = game_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getStarline_points() {
        return starline_points;
    }

    public void setStarline_points(String starline_points) {
        this.starline_points = starline_points;
    }

    public String getIs_starline() {
        return is_starline;
    }

    public void setIs_starline(String is_starline) {
        this.is_starline = is_starline;
    }

    public String getIs_close() {
        return is_close;
    }

    public void setIs_close(String is_close) {
        this.is_close = is_close;
    }

    public String getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(String is_deleted) {
        this.is_deleted = is_deleted;
    }

    public String getIs_disabled() {
        return is_disabled;
    }

    public void setIs_disabled(String is_disabled) {
        this.is_disabled = is_disabled;
    }

    public String getIs_starline_disable() {
        return is_starline_disable;
    }

    public void setIs_starline_disable(String is_starline_disable) {
        this.is_starline_disable = is_starline_disable;
    }

    public String getIs_ds_disabled() {
        return is_ds_disabled;
    }

    public void setIs_ds_disabled(String is_ds_disabled) {
        this.is_ds_disabled = is_ds_disabled;
    }
}
