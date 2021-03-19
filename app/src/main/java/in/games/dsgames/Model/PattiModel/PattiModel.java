package in.games.dsgames.Model.PattiModel;

public class PattiModel {
    String digit;
    int delete_pos;

    public PattiModel(String digit) {
        this.digit = digit;
    }

    public String getDigit() {
        return digit;
    }

    public void setDigit(String digit) {
        this.digit = digit;
    }

    public int getDelete_pos() {
        return delete_pos;
    }

    public void setDelete_pos(int delete_pos) {
        this.delete_pos = delete_pos;
    }
}
