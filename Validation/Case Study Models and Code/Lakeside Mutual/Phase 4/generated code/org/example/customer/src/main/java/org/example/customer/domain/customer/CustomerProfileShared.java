package org.example.customer.domain.customer;

import java.util.Date;

public class CustomerProfileShared {

    public CustomerProfileShared() {
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

    private AddressShared address;

    public AddressShared getAddress() {
        return address;
    }

    public void setAddress(AddressShared address) {
        this.address = address;
    }

    private ListAddressShared moveHistory;

    public ListAddressShared getMoveHistory() {
        return moveHistory;
    }

    public void setMoveHistory(ListAddressShared moveHistory) {
        this.moveHistory = moveHistory;
    }

    public CustomerProfileShared(String firstname, String lastname, Date birthday, String email, String phoneNumber, AddressShared address, ListAddressShared moveHistory) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthday = birthday;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.moveHistory = moveHistory;
    }
}
