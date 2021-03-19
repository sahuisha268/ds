package in.games.dsgames.Model;

/**
 * Developed by Binplus Technologies pvt. ltd.  on 11,December,2020
 */
public class SelectedDigitModel {
    int delete_pos;
    String digit;

    public SelectedDigitModel() {
    }

    public SelectedDigitModel(int delete_pos, String digit) {
        this.delete_pos = delete_pos;
        this.digit = digit;
    }

    public int getDelete_pos() {
        return delete_pos;
    }

    public void setDelete_pos(int delete_pos) {
        this.delete_pos = delete_pos;
    }

    public String getDigit() {
        return digit;
    }

    public void setDigit(String digit) {
        this.digit = digit;
    }
}
