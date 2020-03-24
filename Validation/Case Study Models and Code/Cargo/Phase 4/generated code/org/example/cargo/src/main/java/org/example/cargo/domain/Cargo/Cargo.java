package org.example.cargo.domain.Cargo;

public class Cargo {

    public Cargo() {
    }

    private long trackingID;

    public long getTrackingID() {
        return trackingID;
    }

    public void setTrackingID(long trackingID) {
        this.trackingID = trackingID;
    }

    private DeliverySpecification goal;

    public DeliverySpecification getGoal() {
        return goal;
    }

    public void setGoal(DeliverySpecification goal) {
        this.goal = goal;
    }

    private DeliveryHistory deliveryHistory;

    public DeliveryHistory getDeliveryHistory() {
        return deliveryHistory;
    }

    public void setDeliveryHistory(DeliveryHistory deliveryHistory) {
        this.deliveryHistory = deliveryHistory;
    }

    private CustomerShared CustomerShared;

    public CustomerShared getCustomerShared() {
        return CustomerShared;
    }

    public void setCustomerShared(CustomerShared CustomerShared) {
        this.CustomerShared = CustomerShared;
    }

    public Cargo(long trackingID, DeliverySpecification goal, DeliveryHistory deliveryHistory, CustomerShared CustomerShared) {
        this.trackingID = trackingID;
        this.goal = goal;
        this.deliveryHistory = deliveryHistory;
        this.CustomerShared = CustomerShared;
    }

    public void findbyTrackingID(String unspecified) {
    }

    public void findbyCustomerID(String unspecified) {
    }
}
