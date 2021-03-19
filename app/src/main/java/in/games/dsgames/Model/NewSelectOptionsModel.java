package in.games.dsgames.Model;

import java.util.ArrayList;

/**
 * Developed by Binplus Technologies pvt. ltd.  on 12,December,2020
 */
public class NewSelectOptionsModel {
    String name ;
    ArrayList<NewDigitModel> digit_list;
    String id ;

    public NewSelectOptionsModel() {
    }

    public NewSelectOptionsModel(String id, String name, ArrayList<NewDigitModel> digit_list) {
        this.name = name;
        this.digit_list = digit_list;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<NewDigitModel> getDigit_list() {
        return digit_list;
    }

    public void setDigit_list(ArrayList<NewDigitModel> digit_list) {
        this.digit_list = digit_list;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
