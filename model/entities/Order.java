package com.javaproject.malki.takeandgo.model.entities;

import java.text.DecimalFormat;
import java.util.Date;

/**
 * this class describes the car's order details
 * Created by malki on 04-Nov-17.
 */

public class Order
{
    //static field to edit the request number
    private static int number = 0;
    private String clientNumber;//client number is the client's ID
    private boolean orderStatus; //true = open , false = close
    private String carNumber;
    private Date startRent = new Date();//initialized only when creating the order
    private Date endRent;
    private float startMileage;
    private float endMileage;
    private boolean isFuel;
    private float fuelVol = 0;
    private DecimalFormat billAmount;
    final int orderNumber;



    //private functions
    private void ErrorValue(float val) throws Exception
    {
        if( val <= 0)
        {
            throw new Exception("invalid value!\n");
        }
    }

    //constructor
    public Order(String clientNumber, boolean orderStatus, String carNumber, Date endRent, float startMileage,
                 float endMileage, boolean isFuel, DecimalFormat billAmount) throws Exception {
        this.setClientNumber(clientNumber);
        this.setOrderStatus(orderStatus);
        this.setCarNumber(carNumber);
        this.setEndRent(endRent);
        this.setStartMileage(startMileage);
        this.setEndMileage(endMileage);
        this.setFuel(isFuel);
        this.setBillAmount(billAmount);
        //initialized only once when generate the order
        this.orderNumber = ++number;
    }

    // copy constructor
    public Order(Order newOrder) throws Exception {

        this.setClientNumber(newOrder.clientNumber);
        this.setOrderStatus(newOrder.orderStatus);
        this.setCarNumber(newOrder.carNumber);
        this.setEndRent(newOrder.endRent);
        this.setStartMileage(newOrder.startMileage);
        this.setEndMileage(newOrder.endMileage);
        this.setFuel(newOrder.isFuel);
        this.setBillAmount(newOrder.billAmount);
        this.orderNumber = this.getOrderNumber();//?
    }
    //empty constructor
    public Order(int num) {
        this.orderNumber = ++number;
    }
    //get and set
    public String getClientNumber() {
        return clientNumber;
    }

    public void setClientNumber(String clientNumber) {
        this.clientNumber = clientNumber;
    }

    public boolean isOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(boolean orderStatus) throws Exception {
        this.orderStatus = orderStatus;
        //when the order get close the date of end rent will be updated
        if(!orderStatus){this.setEndRent(new Date());}
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public Date getStartRent() {
        return startRent;
    }

    public void setStartRent(Date startRent) {
        this.startRent = startRent;
    }

    public Date getEndRent() {
        return endRent;
    }

    public void setEndRent(Date endtRent) throws Exception {
        if (endtRent.before(this.startRent))
        {
            throw new Exception("ERROR: The finish date isn't consistent with the start date!\n ");
        }
        this.endRent = endtRent;
    }

    public float getStartMileage() {
        return startMileage;
    }

    public void setStartMileage(float startMileage) throws Exception {
        ErrorValue(startMileage);
        this.startMileage = startMileage;
    }

    public float getEndMileage() {
        return endMileage;
    }

    public void setEndMileage(float endMileage) throws Exception {
        ErrorValue(endMileage);
        this.endMileage = endMileage;
    }

    public boolean isFuel() {
        return isFuel;
    }

    public void setFuel(boolean fuel) {
        isFuel = fuel;
    }

    public float getFuelVol() {
        return fuelVol;
    }

    public void setFuelVol(float fuelVol) throws Exception {
        ErrorValue(fuelVol);
        this.fuelVol =isFuel()? fuelVol : 0;
    }

    public DecimalFormat getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(DecimalFormat billAmount) {
        this.billAmount = billAmount;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    @Override
    public String toString() {
        return String.format("User ID: %S\nLicence Plate: %s\nRent Duration: %tc - %tc\nOrder #: %d\n",
                 clientNumber, carNumber, startRent, endRent, orderNumber);
    }
}
