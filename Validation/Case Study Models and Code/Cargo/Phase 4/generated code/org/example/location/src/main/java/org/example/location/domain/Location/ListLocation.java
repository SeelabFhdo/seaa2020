package org.example.location.domain.Location;

import java.util.ArrayList;

public class ListLocation extends ArrayList<ListLocation.ListLocationItem> {

    public static class ListLocationItem {

        public ListLocationItem() {
        }

        private Location location;

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        public ListLocationItem(Location location) {
            this.location = location;
        }
    }
}
