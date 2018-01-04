package com.javaproject.malki.takeandgo.model.entities;

/**
 * CarWithStatus
 * Created by malki on 04-Jan-18.
 */

public class CarWithStatus extends Car {
    private Boolean carStatus = false;

    public Boolean getCarStatus() {
        return carStatus;
    }

    public void setCarStatus(Boolean carStatus) {
        this.carStatus = carStatus;
    }
}
