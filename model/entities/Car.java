package com.javaproject.malki.takeandgo.model.entities;

/**
 * this class describes the car generally
 * Created by malki on 04-Nov-17.
 */

public class Car
{
    private int locationNumber;//need to change with google maps api
    private String modelType;
    private float mileage;
    private String licencePlate;
    private ENUMS.FUEL_MODE fuelMode;



    public void setLocationNumber(int locationNumber) {
        this.locationNumber = locationNumber;
    }

    public void setModelType(String newModelType) throws Exception {
        modelType = newModelType;
    }

    public void setMileage(float mileage) throws Exception {
        //there is no way the car mileage will be under 100 km
        if (mileage < 1)
        {
            throw new Exception("ERROR: Too less mileage!");
        }
        this.mileage = mileage;
    }

    public void setLicencePlate(String licencePlate) throws Exception {
        if(licencePlate.length() < 7 || licencePlate.length() > 8)
        {
            throw new Exception("ERROR: Israeli licence plates have only 7 or 8 figures!\n");
        }
        this.licencePlate = licencePlate;
    }

    public int getLocationNumber() {

        return locationNumber;
    }

    public String getModelType() {
        return modelType;
    }

    public float getMileage() {
        return mileage;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public ENUMS.FUEL_MODE getFuelMode() {
        return fuelMode;
    }

    public void setFuelMode(ENUMS.FUEL_MODE fuelMode) {
        this.fuelMode = fuelMode;
    }
    //constructor

    public Car(int locationNumber, String modelType, float mileage, String licencePlate, ENUMS.FUEL_MODE fuel) throws Exception {
        this.locationNumber = locationNumber;
        this.modelType = modelType;
        this.mileage = mileage;
        this.licencePlate = licencePlate;
        this.fuelMode = fuel;
    }


    //copy constructor
    public Car(Car car) throws Exception {
        this.locationNumber = car.locationNumber;
        this.modelType = car.modelType;
        this.mileage = car.mileage;
        this.licencePlate = car.licencePlate;
        this.fuelMode = car.fuelMode;
    }
    //empty constructor
    public Car() {

    }

    @Override
    public String toString() {
        String s = String.format("Model: %s\nLocation: %s\nMileage: %f\nLicense Plate: %s\n Fuel Mode: %s",
                 getModelType(),getLocationNumber(), getMileage(), getLicencePlate(), getFuelMode().toString());
        return s;
    }
}
