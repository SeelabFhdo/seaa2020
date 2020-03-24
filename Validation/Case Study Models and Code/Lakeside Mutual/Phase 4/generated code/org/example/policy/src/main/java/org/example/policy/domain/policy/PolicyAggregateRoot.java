package org.example.policy.domain.policy;

import java.util.Date;

public class PolicyAggregateRoot {

    public PolicyAggregateRoot() {
    }

    private Date creationDate;

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    private PolicyId id;

    public PolicyId getId() {
        return id;
    }

    public void setId(PolicyId id) {
        this.id = id;
    }

    private PolicyPeriod policyPeriod;

    public PolicyPeriod getPolicyPeriod() {
        return policyPeriod;
    }

    public void setPolicyPeriod(PolicyPeriod policyPeriod) {
        this.policyPeriod = policyPeriod;
    }

    private PolicyType policyType;

    public PolicyType getPolicyType() {
        return policyType;
    }

    public void setPolicyType(PolicyType policyType) {
        this.policyType = policyType;
    }

    private MoneyAmount policyLimit;

    public MoneyAmount getPolicyLimit() {
        return policyLimit;
    }

    public void setPolicyLimit(MoneyAmount policyLimit) {
        this.policyLimit = policyLimit;
    }

    private MoneyAmount insurancePremium;

    public MoneyAmount getInsurancePremium() {
        return insurancePremium;
    }

    public void setInsurancePremium(MoneyAmount insurancePremium) {
        this.insurancePremium = insurancePremium;
    }

    private InsuringAgreementEntity insuringAgreement;

    public InsuringAgreementEntity getInsuringAgreement() {
        return insuringAgreement;
    }

    public void setInsuringAgreement(InsuringAgreementEntity insuringAgreement) {
        this.insuringAgreement = insuringAgreement;
    }

    public PolicyAggregateRoot(Date creationDate, PolicyId id, PolicyPeriod policyPeriod, PolicyType policyType, MoneyAmount policyLimit, MoneyAmount insurancePremium, InsuringAgreementEntity insuringAgreement) {
        this.creationDate = creationDate;
        this.id = id;
        this.policyPeriod = policyPeriod;
        this.policyType = policyType;
        this.policyLimit = policyLimit;
        this.insurancePremium = insurancePremium;
        this.insuringAgreement = insuringAgreement;
    }
}
