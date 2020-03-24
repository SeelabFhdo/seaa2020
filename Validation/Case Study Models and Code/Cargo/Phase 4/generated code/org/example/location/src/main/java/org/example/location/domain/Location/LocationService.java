package org.example.location.domain.Location;

public class LocationService {

    public LocationService() {
    }

    private ListLocation location;

    public ListLocation getLocation() {
        return location;
    }

    public void setLocation(ListLocation location) {
        this.location = location;
    }

    public LocationService(ListLocation location) {
        this.location = location;
    }

    public void resolvebyportcode(String unspecified) {
    }
}
