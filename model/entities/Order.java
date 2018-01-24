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

    private String clientNumber;//client number is the client's ID
    private boolean orderStatus; //true = open , false = close
    private String carNumber;
    private Date startRent ;//initialized only when creating the order
    private Date endRent;
    private Float startMileage;
    private Float endMileage;
    private boolean isFuel;
    private Float fuelVol;
    private DecimalFormat billAmount;
    private int orderNumber;



    //private functions
    private void ErrorValue(Float val) throws Exception
    {
        if( val <= 0)
        {
            throw new Exception("invalid value!\n");
        }
    }

    //constructor
    public Order(String clientNumber, boolean orderStatus, String carNumber, Date endRent, Float startMileage,
                 Float endMileage, Boolean isFuel, DecimalFormat billAmount) throws Exception {
        this.setClientNumber(clientNumber);
        this.setOrderStatus(orderStatus);
        this.setCarNumber(carNumber);
        this.setEndRent(endRent);
        this.setStartMileage(startMileage);
        this.setEndMileage(endMileage);
        this.setFuel(isFuel);
        this.setBillAmount(billAmount);
        //initialized only once when generate the order

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
    public Order() {
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
//        if (endtRent.before(this.startRent))
//        {
//            throw new Exception("ERROR: The finish date isn't consistent with the start date!\n ");
//        }
        this.endRent = endtRent;
    }

    public Float getStartMileage() {
        return startMileage;
    }

    public void setStartMileage(Float startMileage) throws Exception {
        ErrorValue(startMileage);
        this.startMileage = startMileage;
    }

    public Float getEndMileage() {
        return endMileage;
    }

    public void setEndMileage(Float endMileage) throws Exception {
        //ErrorValue(endMileage);
        if(!isOrderStatus())
            this.endMileage = endMileage;

    }

    public Boolean isFuel() {
        return isFuel;
    }

    public void setFuel(Boolean fuel) {
        if(!isOrderStatus())
            isFuel = fuel;
    }

    public Float getFuelVol() {
        return fuelVol;
    }

    public void setFuelVol(Float fuelVol) throws Exception {
        if(!isOrderStatus()){
            //ErrorValue(fuelVol);
            if(isFuel()!=null)
                this.fuelVol =isFuel()? fuelVol : 0;
            else
                this.fuelVol =null;
        }
    }

    public DecimalFormat getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(DecimalFormat billAmount) {
        if(!isOrderStatus())
            this.billAmount = billAmount;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    @Override
    public String toString() {
        return String.format("User ID: %S\nLicence Plate: %s\nRent Duration: %tc - "+ (!isOrderStatus()? "%tc":"present")+"\nOrder #: %d\n",
                 clientNumber, carNumber, startRent, endRent, orderNumber);
    }
}
