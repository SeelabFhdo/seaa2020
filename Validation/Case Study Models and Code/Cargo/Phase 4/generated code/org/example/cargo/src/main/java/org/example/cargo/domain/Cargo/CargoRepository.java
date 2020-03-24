package org.example.cargo.domain.Cargo;

public class CargoRepository {

    public CargoRepository() {
    }

    private ListCargo cargo;

    public ListCargo getCargo() {
        return cargo;
    }

    public void setCargo(ListCargo cargo) {
        this.cargo = cargo;
    }

    public CargoRepository(ListCargo cargo) {
        this.cargo = cargo;
    }

    public ListCargo findbyTrackingID(String unspecified) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public ListCargo findbyCustomerID(String unspecified) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
