package in.games.dsgames.Model;

/**
 * Developed by Binplus Technologies pvt. ltd.  on 11,December,2020
 */
public class SelectedSectionDigitModel {
    int delete_pos;
    String digit;
    String section;

    public SelectedSectionDigitModel() {
    }

    public SelectedSectionDigitModel(int delete_pos, String digit, String section) {
        this.delete_pos = delete_pos;
        this.digit = digit;
        this.section = section;
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

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }
}
