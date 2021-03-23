package in.games.dsgames.Model;

public class StarlineModel {
    String id, s_game_time, s_game_end_time, s_game_number, updated_at, created_at;

    public StarlineModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getS_game_time() {
        return s_game_time;
    }

    public void setS_game_time(String s_game_time) {
        this.s_game_time = s_game_time;
    }

    public String getS_game_end_time() {
        return s_game_end_time;
    }

    public void setS_game_end_time(String s_game_end_time) {
        this.s_game_end_time = s_game_end_time;
    }

    public String getS_game_number() {
        return s_game_number;
    }

    public void setS_game_number(String s_game_number) {
        this.s_game_number = s_game_number;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
