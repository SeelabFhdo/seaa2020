package org.example.policy.domain.policy;

import java.util.Date;

public class PolicyEvent {

    public PolicyEvent() {
    }

    private long originator;

    public long getOriginator() {
        return originator;
    }

    private Date date;

    public Date getDate() {
        return date;
    }

    private CustomerShared CustomerShared;

    public CustomerShared getCustomerShared() {
        return CustomerShared;
    }

    public void setCustomerShared(CustomerShared CustomerShared) {
        this.CustomerShared = CustomerShared;
    }

    public PolicyEvent(long originator, Date date, CustomerShared CustomerShared) {
        this.originator = originator;
        this.date = date;
        this.CustomerShared = CustomerShared;
    }
}
