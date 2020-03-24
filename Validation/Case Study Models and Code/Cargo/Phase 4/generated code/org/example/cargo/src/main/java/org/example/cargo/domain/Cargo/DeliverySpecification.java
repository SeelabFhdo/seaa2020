package org.example.cargo.domain.Cargo;

import java.util.Date;

public class DeliverySpecification {

    public DeliverySpecification() {
    }

    private Date arrivaltime;

    public Date getArrivaltime() {
        return arrivaltime;
    }

    public void setArrivaltime(Date arrivaltime) {
        this.arrivaltime = arrivaltime;
    }

    public DeliverySpecification(Date arrivaltime) {
        this.arrivaltime = arrivaltime;
    }
}
