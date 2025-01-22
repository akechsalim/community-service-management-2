package com.akechsalim.community_service_management_2.dto;

public class SponsorshipDTO {
    private Double amount;
    private Long sponsorId;

    public SponsorshipDTO() {
    }

    public SponsorshipDTO( Double amount, Long sponsorId) {
        this.amount = amount;
        this.sponsorId = sponsorId;
    }

    public Double getAmount() {
        return amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    public Long getSponsorId() {
        return sponsorId;
    }
    public void setSponsorId(Long sponsorId) {
        this.sponsorId = sponsorId;
    }

}
