package org.example.policy.domain.policy;

import java.util.Date;

public class PolicyPeriod {

    public PolicyPeriod() {
    }

    private Date startDate;

    public Date getStartDate() {
        return startDate;
    }

    private Date endDate;

    public Date getEndDate() {
        return endDate;
    }

    public PolicyPeriod(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
