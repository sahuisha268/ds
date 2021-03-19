package in.games.dsgames.Model;

/**
 * Developed by Binplus Technologies pvt. ltd.  on 12,December,2020
 */
public class NewDigitSectionModel {
    String id,digit;
    boolean deleted;
    String section;

    public NewDigitSectionModel() {
    }

    public NewDigitSectionModel(String id, String digit, boolean deleted, String section) {
        this.id = id;
        this.digit = digit;
        this.deleted = deleted;
        this.section = section;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDigit() {
        return digit;
    }

    public void setDigit(String digit) {
        this.digit = digit;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }
}
