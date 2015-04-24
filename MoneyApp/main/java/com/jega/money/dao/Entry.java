package com.jega.money.dao;

import java.util.Calendar;

/**
 * Created by jegasmlm on 4/2/2015.
 */
public class Entry {

    private float amount;
    private Category category;
    private String description;
    private Calendar date;

    public Entry(float amount) {
        this.amount = amount;
        date = Calendar.getInstance();
    }

    public Entry(float amount, Category category, String description) {
        this.amount = amount;
        this.category = category;
        this.description = description;
        date = Calendar.getInstance();
    }

    public float getAmount() {
        return amount;
    }

    public Category getCategory() {
        return category;
    }

    public Calendar getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }
}
