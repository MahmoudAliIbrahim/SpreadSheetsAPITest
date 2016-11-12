package dk.izbrannick.glutter.sheetstest;

/**
 * Created by luther on 03/11/2016.
 */

public class MyContact {
    private String name;
    private String numberPrimary, numberOther;
    private boolean isLeader, isOnRecipientList;

    public MyContact(String name, String numberPrimary, boolean isOnRecipientList, String numberOther, boolean isLeader) {

        this.setName(name);
        this.setNumberPrimary(numberPrimary);
        this.setNumberOther(numberOther);
        this.setOnRecipientList(isOnRecipientList);
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

    public boolean getOnRecipientList() {
        return isOnRecipientList;
    }

    public void setOnRecipientList(boolean onRecipientList) {
        this.isOnRecipientList = onRecipientList;
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
