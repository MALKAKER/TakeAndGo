package com.javaproject.malki.takeandgo.model.backend;

import android.content.ContentValues;

import com.javaproject.malki.takeandgo.model.entities.Branch;
import com.javaproject.malki.takeandgo.model.entities.Car;
import com.javaproject.malki.takeandgo.model.entities.CarWithStatus;
import com.javaproject.malki.takeandgo.model.entities.Client;
import com.javaproject.malki.takeandgo.model.entities.ENUMS;
import com.javaproject.malki.takeandgo.model.entities.Order;

import java.util.Dictionary;
import java.util.List;

/**
 * An interface that defines the behaviour of the DB
 * Created by Malki on 12-Nov-17.
 */

public interface DB_Manager {
    //Check if the user is already exist in the data base
    public boolean ClientExist(String ID);
    //Check if the branch is already exist in the data base
    public boolean BranchExist(int ID);
    //Check if the user is already exist in the data base and return the object
    public Client GetClient(String ID) throws Exception;
    //Check if the Order is already exist in the data base and return the object
    public Order GetOrder(int ID) throws Exception;
    //Check if the branch is already exist in the data base and return the object
    public Branch GetBranch(int ID) throws Exception;
    //Check if the car is already exist in the data base and return the object
    //TODO TRANSFER TO CARWITHSTATUS
    public Car GetCar(String ID) throws Exception;
    //Add client to data base
    public String AddClient(ContentValues values) throws Exception;
    //Add Order into data base
    public String AddOrder(ContentValues values) throws Exception;
    //Add Branch into data base
    public String AddBranch(ContentValues values) throws Exception;
    //Update fuel quantity
    public ENUMS.FUEL_MODE UpdateQuantity(float start, float end, float newFuel, String carModel) throws Exception;
    //Returns list of all users
    public List<Client> GetClients();
    //Returns list of branches
    public List<Branch> GetBranches();
    //Returns list of cars
    //TODO CAR WITH STATUS
    public List<Car> GetCars();
    //to some statistics - return all the orders
    public List<Order> GetOrders();
    //update car mileage
    public boolean UpdateMileage(float kilometers, String licencePlate);
    //return list of available cars
    public List<Car> AvailableCars();
    // return list of available cars in a specific location
    public List<Car> AvailableCars(String location);
    // return list of available cars in defined radius near the current location
    public List<Car> AvailableCars(int radius, String address);
    //Bonus-> return available cars per branch according to model
    public List<Branch> IsModelInBranches(String model);
    //return branches where the model exist there
    public Dictionary<String, List<Branch>>  ModelInBranches();
    // return active orders
    public List<Order> GetOpenOrders();
    //close order, user enter the number of kilometers
    public boolean CloseOrder(float kilometers, int orderNumber, Boolean isFuel, Float fuelVol);
    public boolean CloseOrder(float kilometers, int orderNumber);
    //check if order closed in the previous 10 sec.
    public boolean isClosedOrder();

}
