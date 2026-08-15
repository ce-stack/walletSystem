package com.system.wallet.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "outbox_events")
public class Outbox_event {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "aggregateType")
    private String aggregateType;

    @Column(name = "aggerate_id")
    private Long aggerate_id;

    public Outbox_event(String event_type) {
        this.event_type = event_type;
    }

    @Column(name = "event_type")
    private String event_type;

    @Column(name = "retry_count")
    private int retry_count;

    @Column(name = "payload" , columnDefinition = "TEXT")
    private String payload;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "published_at")
    private Date published_at;

    public Outbox_event() {

    }

    public Outbox_event(String aggregateType, Long aggerate_id, String event_type, int retry_count, String payload, String status, Date created_at) {
        this.aggregateType = aggregateType;
        this.aggerate_id = aggerate_id;
        this.event_type = event_type;
        this.retry_count = retry_count;
        this.payload = payload;
        this.status = status;
        this.createdAt = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAggregateType() {
        return aggregateType;
    }

    public void setAggregateType(String aggregateType) {
        this.aggregateType = aggregateType;
    }

    public Long getAggerate_id() {
        return aggerate_id;
    }

    public void setAggerate_id(Long aggerate_id) {
        this.aggerate_id = aggerate_id;
    }

    public String getEvent_type() {
        return event_type;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }

    public int getRetry_count() {
        return retry_count;
    }

    public void setRetry_count(int retry_count) {
        this.retry_count = retry_count;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreated_at() {
        return createdAt;
    }

    public void setCreated_at(Date created_at) {
        this.createdAt = created_at;
    }

    public Date getPublished_at() {
        return published_at;
    }

    public void setPublished_at(Date published_at) {
        this.published_at = published_at;
    }

    @Override
    public String toString() {
        return "Outbox_event{" +
                "id=" + id +
                ", aggregateType='" + aggregateType + '\'' +
                ", aggerate_id=" + aggerate_id +
                ", event_type='" + event_type + '\'' +
                ", retry_count=" + retry_count +
                ", payload='" + payload + '\'' +
                ", status='" + status + '\'' +
                ", created_at=" + createdAt +
                ", published_at=" + published_at +
                '}';
    }
}
