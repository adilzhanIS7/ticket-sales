package com.example.demo.models;

import java.util.Date;

import java.math.BigDecimal;

public class Ticket {
    private Integer id;
    private String goingFrom;
    private String goingTo;
    private Date dateWhen;
    private Date dateBack;
    private BigDecimal price;

    public Ticket(Integer id, String goingFrom, String goingTo, Date dateWhen, Date dateBack, BigDecimal price) {
        this.id = id;
        this.goingFrom = goingFrom;
        this.goingTo = goingTo;
        this.dateWhen = dateWhen;
        this.dateBack = dateBack;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGoingFrom() {
        return goingFrom;
    }

    public void setGoingFrom(String goingFrom) {
        this.goingFrom = goingFrom;
    }

    public String getGoingTo() {
        return goingTo;
    }

    public void setGoingTo(String goingTo) {
        this.goingTo = goingTo;
    }

    public Date getDateWhen() {
        return dateWhen;
    }

    public void setDateWhen(Date dateWhen) {
        this.dateWhen = dateWhen;
    }

    public Date getDateBack() {
        return dateBack;
    }

    public void setDateBack(Date dateBack) {
        this.dateBack = dateBack;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}

