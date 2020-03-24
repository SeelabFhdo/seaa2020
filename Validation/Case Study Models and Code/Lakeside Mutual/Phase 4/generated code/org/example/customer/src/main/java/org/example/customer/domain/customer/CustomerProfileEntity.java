package org.example.customer.domain.customer;

import java.util.Date;

public class CustomerProfileEntity {

    public CustomerProfileEntity() {
    }

    private String firstname;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    private String lastname;

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    private Date birthday;

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String phoneNumber;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    private Address currentAddress;

    public Address getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(Address currentAddress) {
        this.currentAddress = currentAddress;
    }

    private ListAddress moveHistory;

    public ListAddress getMoveHistory() {
        return moveHistory;
    }

    public void setMoveHistory(ListAddress moveHistory) {
        this.moveHistory = moveHistory;
    }

    public CustomerProfileEntity(String firstname, String lastname, Date birthday, String email, String phoneNumber, Address currentAddress, ListAddress moveHistory) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthday = birthday;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.currentAddress = currentAddress;
        this.moveHistory = moveHistory;
    }

    public void moveToAddress(Address unspecified) {
    }
}
