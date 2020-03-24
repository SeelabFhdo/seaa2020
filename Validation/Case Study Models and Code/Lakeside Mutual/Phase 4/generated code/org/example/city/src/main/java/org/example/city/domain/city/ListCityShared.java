package org.example.city.domain.city;

import java.util.ArrayList;

public class ListCityShared extends ArrayList<ListCityShared.ListCitySharedItem> {

    public static class ListCitySharedItem {

        public ListCitySharedItem() {
        }

        private CityShared cityShared;

        public CityShared getCityShared() {
            return cityShared;
        }

        public void setCityShared(CityShared cityShared) {
            this.cityShared = cityShared;
        }

        public ListCitySharedItem(CityShared cityShared) {
            this.cityShared = cityShared;
        }
    }
}
