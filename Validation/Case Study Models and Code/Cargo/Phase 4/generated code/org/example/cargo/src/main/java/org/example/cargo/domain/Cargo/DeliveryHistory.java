package org.example.cargo.domain.Cargo;

public class DeliveryHistory {

    public DeliveryHistory() {
    }

    private Cargo cargo;

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    private ListHandlingEvent handlingEvent;

    public ListHandlingEvent getHandlingEvent() {
        return handlingEvent;
    }

    public void setHandlingEvent(ListHandlingEvent handlingEvent) {
        this.handlingEvent = handlingEvent;
    }

    private CarrierMovement carriermovement;

    public CarrierMovement getCarriermovement() {
        return carriermovement;
    }

    public void setCarriermovement(CarrierMovement carriermovement) {
        this.carriermovement = carriermovement;
    }

    public DeliveryHistory(Cargo cargo, ListHandlingEvent handlingEvent, CarrierMovement carriermovement) {
        this.cargo = cargo;
        this.handlingEvent = handlingEvent;
        this.carriermovement = carriermovement;
    }
}
