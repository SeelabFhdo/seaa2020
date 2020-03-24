package org.example.customer.domain.customer;

public class AddressShared {

    public AddressShared() {
    }

    private String streetAddress;

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    private String postalCode;

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    private String city;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public AddressShared(String streetAddress, String postalCode, String city) {
        this.streetAddress = streetAddress;
        this.postalCode = postalCode;
        this.city = city;
    }
}
