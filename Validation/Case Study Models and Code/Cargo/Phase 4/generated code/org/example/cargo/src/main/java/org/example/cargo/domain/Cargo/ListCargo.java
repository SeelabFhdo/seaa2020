package org.example.cargo.domain.Cargo;

import java.util.ArrayList;

public class ListCargo extends ArrayList<ListCargo.ListCargoItem> {

    public static class ListCargoItem {

        public ListCargoItem() {
        }

        private Cargo cargo;

        public Cargo getCargo() {
            return cargo;
        }

        public void setCargo(Cargo cargo) {
            this.cargo = cargo;
        }

        public ListCargoItem(Cargo cargo) {
            this.cargo = cargo;
        }
    }
}
