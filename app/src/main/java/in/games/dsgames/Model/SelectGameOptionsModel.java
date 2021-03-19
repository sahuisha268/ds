package in.games.dsgames.Model;

import java.util.List;

public class SelectGameOptionsModel {
    String name ;
    List<String> digit_list;
    String id ;

    public SelectGameOptionsModel(String id,String name, List<String> digit_list) {
        this.name = name;
        this.digit_list = digit_list;
        this.id = id;
    }

    public SelectGameOptionsModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getDigit_list() {
        return digit_list;
    }

    public void setDigit_list(List<String> digit_list) {
        this.digit_list = digit_list;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
