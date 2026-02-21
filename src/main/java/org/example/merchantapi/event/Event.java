package org.example.merchantapi.event;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="events")
public class Event {

    @Id
    private String eventId;
    private String merchantId;
    private LocalDateTime timeStamp;

    @Enumerated(EnumType.STRING)
    private ProductType product;

    @Enumerated(EnumType.STRING)
    private StatusType status;
    private String eventType;
    private BigDecimal amount;
    private ChannelType channel;
    private String region;

    @Enumerated(EnumType.STRING)
    private MerchantTierType merchantTier;


    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public ProductType getProduct() {
        return product;
    }

    public void setProduct(ProductType product) {
        this.product = product;
    }

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public ChannelType getChannel() {
        return channel;
    }

    public void setChannel(ChannelType channel) {
        this.channel = channel;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public MerchantTierType getMerchantTier() {
        return merchantTier;
    }

    public void setMerchantTier(MerchantTierType merchantTier) {
        this.merchantTier = merchantTier;
    }

}
