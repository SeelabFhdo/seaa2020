package org.example.city.domain.city;

public class City {

    public City() {
    }

    private String name;

    public String getName() {
        return name;
    }

    private String postalCode;

    public String getPostalCode() {
        return postalCode;
    }

    public City(String name, String postalCode) {
        this.name = name;
        this.postalCode = postalCode;
    }
}
