package dk.izbrannick.glutter.sheetstest;

import java.util.ArrayList;

/**
 * Created by luther on 03/11/2016.
 */

public class MyContact {
    private String name, numberPrimary, email, credit;
    private ArrayList<Object> groups;

    public MyContact(Object name, Object numberPrimary, Object email, Object credit, ArrayList<Object> groups) {

        this.setName(String.valueOf(name));
        this.setNumberPrimary(String.valueOf(numberPrimary));
        this.setNumberOther(String.valueOf(email));

        this.setCredit(String.valueOf(credit));

        this.groups = new ArrayList<>();
        this.groups = groups;


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

    public ArrayList<Object> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<Object> groups) {
        //TODO: find out if this is needed
        this.groups = new ArrayList<>();
        this.groups = groups;
    }

    @Override
    public String toString() {
        return name + " " + groups;
    }
}
