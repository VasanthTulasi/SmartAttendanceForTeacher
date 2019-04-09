package com.smart.teacher.attendance.vasanth.smartattendanceforteacher;

public class CardClass {
    private int indexNumber;
    private String nameOfPerson;
    private String rollNumberForMember;

    public CardClass(int indexNumber, String nameOfPerson) {
        this.indexNumber = indexNumber;
        this.nameOfPerson = nameOfPerson;
    }

    public CardClass(int indexNumber, String nameOfPerson, String rollNumberForMember) {
        this.indexNumber = indexNumber;
        this.nameOfPerson = nameOfPerson;
        this.rollNumberForMember = rollNumberForMember;
    }

    public String getRollNumberForMember() {
        return rollNumberForMember;
    }

    public void setRollNumberForMember(String rollNumberForMember) {
        this.rollNumberForMember = rollNumberForMember;
    }

    public int getIndexNumber() {
        return indexNumber;
    }

    public void setIndexNumber(int indexNumber) {
        this.indexNumber = indexNumber;
    }

    public String getNameOfPerson() {
        return nameOfPerson;
    }

    public void setNameOfPerson(String nameOfPerson) {
        this.nameOfPerson = nameOfPerson;
    }
}
