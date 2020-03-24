package org.example.location.domain.Location;

public class Location {

    public Location() {
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private long customerID;

    public long getCustomerID() {
        return customerID;
    }

    public void setCustomerID(long customerID) {
        this.customerID = customerID;
    }

    private String portcode;

    public String getPortcode() {
        return portcode;
    }

    public void setPortcode(String portcode) {
        this.portcode = portcode;
    }

    public Location(String name, long customerID, String portcode) {
        this.name = name;
        this.customerID = customerID;
        this.portcode = portcode;
    }
}
