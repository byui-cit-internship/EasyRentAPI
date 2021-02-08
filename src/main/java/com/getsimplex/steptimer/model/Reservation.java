package com.getsimplex.steptimer.model;

import java.util.ArrayList;
import java.util.Date;

public class Reservation {
    private String reservationId;
    private String customerId;
    private ArrayList<ReservationItem> reservationItems = new ArrayList<>();
    private Long dueDate;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    private String customerName;

    public Long getDueDate() { return dueDate; }

    public void setDueDate(Long dueDate) { this.dueDate = dueDate; }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public ArrayList<ReservationItem> getReservationItems() {
        return reservationItems;
    }

    public void setReservationItems(ArrayList<ReservationItem> reservationItem) {
        this.reservationItems = reservationItem;
    }
}