package org.example.customer.domain.customer;

public class Address {

    public Address() {
    }

    private final long id = 0L;

    private String streetAddress;

    public String getStreetAddress() {
        return streetAddress;
    }

    private CityShared CityShared;

    public CityShared getCityShared() {
        return CityShared;
    }

    public void setCityShared(CityShared CityShared) {
        this.CityShared = CityShared;
    }

    public Address(String streetAddress, CityShared CityShared) {
        this.streetAddress = streetAddress;
        this.CityShared = CityShared;
    }
}
