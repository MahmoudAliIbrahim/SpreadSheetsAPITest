package dk.izbrannick.glutter.sheetstest;

/**
 * Created by luther on 03/11/2016.
 */

public class MyContact {
    private String name;
    private String numberPrimary, numberSecondary, numberOther;
    private boolean isLeader;

    public MyContact(String name, String numberPrimary, String numberSecondary, String numberOther, boolean isLeader) {

        this.setName(name);
        this.setNumberPrimary(numberPrimary);
        this.setNumberSecondary(numberSecondary);
        this.setNumberOther(numberOther);
        this.setLeaderState(isLeader);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumberPrimary() {
        return numberPrimary;
    }

    public void setNumberPrimary(String numberPrimary) {
        this.numberPrimary = numberPrimary;
    }

    public String getNumberSecondary() {
        return numberSecondary;
    }

    public void setNumberSecondary(String numberSecondary) {
        this.numberSecondary = numberSecondary;
    }

    public String getNumberOther() {
        return numberOther;
    }

    public void setNumberOther(String numberOther) {
        this.numberOther = numberOther;
    }

    public boolean isLeader() {
        return isLeader;
    }

    public void setLeaderState(boolean leader) {
        isLeader = leader;
    }
}
