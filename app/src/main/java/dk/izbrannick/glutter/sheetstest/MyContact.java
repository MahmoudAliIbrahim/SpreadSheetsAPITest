package dk.izbrannick.glutter.sheetstest;

/**
 * Created by luther on 03/11/2016.
 */

public class MyContact {
    private String name;
    private int numberPrimary, numberSecondary, numberOther;


    public MyContact(String name, int numberPrimary, int numberSecondary, int numberOther) {

        this.setName(name);
        this.setNumberPrimary(numberPrimary);
        this.setNumberSecondary(numberSecondary);
        this.setNumberOther(numberOther);
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
}
