package com.codePenguin.codePenguin;


import javax.persistence.Embeddable;

@Embeddable
public class SingleShot {

    private String location;
    private boolean status;

    public SingleShot(){}

    public SingleShot(String location) {
        this.location = location;
        this.status = false;
    }

    // getter and setter
    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }
}
