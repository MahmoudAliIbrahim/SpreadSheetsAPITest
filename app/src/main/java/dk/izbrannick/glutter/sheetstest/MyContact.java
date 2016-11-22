package dk.izbrannick.glutter.sheetstest;

import java.util.ArrayList;

/**
 * Created by luther on 03/11/2016.
 */

public class MyContact {
    private String name, numberPrimary, email, credit;
    private boolean isOnRecipientList;
    private ArrayList<String> groups;

    public MyContact(String name, String numberPrimary, boolean isOnRecipientList, String email, ArrayList<String> groups, String credit) {

        this.setName(name);
        this.setNumberPrimary(numberPrimary);
        this.setNumberOther(email);
        this.setOnRecipientList(isOnRecipientList);
        this.setGroups(groups);
        this.credit = credit;
    }

    public MyContact(Object name, Object numberPrimary, Object isOnRecipientList, Object email, Object groups, Object credit) {

        this.setName(String.valueOf(name));
        this.setNumberPrimary(String.valueOf(numberPrimary));
        this.setNumberOther(String.valueOf(email));
        this.setOnRecipientList(Boolean.valueOf(String.valueOf(isOnRecipientList)));
        this.setGroups(groups);
        this.setCredit(String.valueOf(credit));
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
        return email;
    }

    public void setNumberOther(String numberOther) {
        this.email = numberOther;
    }


    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public ArrayList<String> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<String> groups) {
        //TODO: find out if this is needed
        this.groups = new ArrayList<>();
        this.groups = groups;
    }

    public void setGroups(Object groups) {
        //TODO: find out if this is needed
        this.groups = new ArrayList<>();

        String groupsString = (String) groups;
        if (groupsString.contains(","))
        {
            for (int i =  0; i <= groupsString.split(",").length; i++) {
                this.groups.add(groupsString.split(",")[i]);
            }
        }
    }
}
