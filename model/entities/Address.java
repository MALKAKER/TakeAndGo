package com.javaproject.malki.takeandgo.model.entities;

/**
 * describe some address in the world
 * Created by malki on 04-Nov-17.
 */

public class Address {
    private String city;
    private ENUMS.COUNTRY country;
    private String street;
    private int house;


    //constructor
    public Address(String city, ENUMS.COUNTRY country, String street, int house) throws Exception{
        this.setCity(city);
        this.setCountry(country);
        this.setStreet(street);
        this.setHouse(house);

    }
    //copy constructor
    public Address(Address branchAddress) throws Exception
    {
        this.setCity(branchAddress.city);
        this.setCountry(branchAddress.country);
        this.setStreet(branchAddress.getStreet());
        this.setHouse(branchAddress.house);

    }

    public Address() {

    }

    //get and set
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public ENUMS.COUNTRY getCountry() {
        return country;
    }

    public void setCountry(ENUMS.COUNTRY country) {
        this.country = country;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getHouse() {
        return house;
    }

    public void setHouse(int house) throws Exception {
        if (house < 1)
        {
            throw new Exception("invalid value!");
        }
        this.house = house;
    }





    @Override
    public String toString() {
        return String.format("%s,%s,%s,%d",this.country, this.city, this.street, this.house);
    }

}
