package com.github.alexanderfefelov.bgbilling.plugin.search.common.model;

import java.util.Date;

public class SearchResult {

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getContractId() {
        return contractId;
    }

    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public Date getContractStartDate() {
        return contractStartDate;
    }

    public void setContractStartDate(Date contractStartDate) {
        this.contractStartDate = contractStartDate;
    }

    public Date getContractExpirationDate() {
        return contractExpirationDate;
    }

    public void setContractExpirationDate(Date contractExpirationDate) {
        this.contractExpirationDate = contractExpirationDate;
    }

    public String getContractComment() {
        return contractComment;
    }

    public void setContractComment(String contractComment) {
        this.contractComment = contractComment;
    }

    public Boolean getContractPostpaidMode() {
        return contractPostpaidMode;
    }

    public void setContractPostpaidMode(Boolean contractPostpaidMode) {
        this.contractPostpaidMode = contractPostpaidMode;
    }

    public Double getContractLimit() {
        return contractLimit;
    }

    public void setContractLimit(Double contractLimit) {
        this.contractLimit = contractLimit;
    }

    public String getContractPricingPlans() {
        return contractPricingPlans;
    }

    public void setContractPricingPlans(String contractPricingPlans) {
        this.contractPricingPlans = contractPricingPlans;
    }

    public Double getContractBalance() {
        return contractBalance;
    }

    public void setContractBalance(Double contractBalance) {
        this.contractBalance = contractBalance;
    }

    public String getContractBalanceChangedAt() {
        return contractBalanceChangedAt;
    }

    public void setContractBalanceChangedAt(String contractBalanceChangedAt) {
        this.contractBalanceChangedAt = contractBalanceChangedAt;
    }

    private String source;
    private Integer contractId;
    private String contractNo;
    private Date contractStartDate;
    private Date contractExpirationDate;
    private String contractComment;
    private Boolean contractPostpaidMode;
    private Double contractBalance;
    private String contractBalanceChangedAt;
    private Double contractLimit;
    private String contractPricingPlans;

}
