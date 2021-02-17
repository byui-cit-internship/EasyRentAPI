package com.getsimplex.steptimer.model;

public class ReservationItem {
    private String description;
    private Integer itemId;
    private Integer uniqueItemId;
    private Boolean returned;

    public void setReturned(Boolean returned) { this.returned = returned; }

    public Boolean getReturned() { 
        return returned; 
    }

    public String getDescription    () {
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

    public Integer getUniqueItemId() {
        return uniqueItemId;
    }

    public void setUniqueItemId(Integer uniqueItemId) {
        this.uniqueItemId = uniqueItemId;
    }
}
