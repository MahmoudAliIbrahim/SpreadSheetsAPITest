package dk.izbrannick.glutter.sheetstest;

/**
 * Created by luther on 03/11/2016.
 */

public class MyContact {
    private String name, numberPrimary, numberOther, credit;
    private boolean isLeader, isOnRecipientList;

    public MyContact(String name, String numberPrimary, boolean isOnRecipientList, String numberOther, boolean isLeader, String credit) {

        this.setName(name);
        this.setNumberPrimary(numberPrimary);
        this.setNumberOther(numberOther);
        this.setOnRecipientList(isOnRecipientList);
        this.setLeaderState(isLeader);
        this.credit = credit;
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

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }
}
