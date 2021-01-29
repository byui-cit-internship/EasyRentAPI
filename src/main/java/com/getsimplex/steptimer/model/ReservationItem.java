package com.getsimplex.steptimer.model;

public class ReservationItem {
    private String description;
    private Integer itemId;
    private Boolean returned;

    public Boolean getReturned() { return returned; }

    public void setReturned(Boolean returned) { this.returned = returned; }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }
}
