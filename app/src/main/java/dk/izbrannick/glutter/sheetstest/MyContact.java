package dk.izbrannick.glutter.sheetstest;

/**
 * Created by luther on 03/11/2016.
 */

public class MyContact {
    private String name;
    private int numberPrimary, numberSecondary, numberOther;
    private boolean isLeader;


    public MyContact(String name, int numberPrimary, int numberSecondary, int numberOther, boolean isLeader) {

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

    public int getNumberPrimary() {
        return numberPrimary;
    }

    public void setNumberPrimary(int numberPrimary) {
        this.numberPrimary = numberPrimary;
    }

    public int getNumberSecondary() {
        return numberSecondary;
    }

    public void setNumberSecondary(int numberSecondary) {
        this.numberSecondary = numberSecondary;
    }

    public int getNumberOther() {
        return numberOther;
    }

    public void setNumberOther(int numberOther) {
        this.numberOther = numberOther;
    }

    public boolean isLeader() {
        return isLeader;
    }

    public void setLeaderState(boolean leader) {
        isLeader = leader;
    }
}
