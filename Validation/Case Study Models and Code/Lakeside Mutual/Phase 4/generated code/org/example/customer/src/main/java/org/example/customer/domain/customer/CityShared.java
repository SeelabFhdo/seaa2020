package org.example.customer.domain.customer;

public class CityShared {

    public CityShared() {
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String postalCode;

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public CityShared(String name, String postalCode) {
        this.name = name;
        this.postalCode = postalCode;
    }
}
