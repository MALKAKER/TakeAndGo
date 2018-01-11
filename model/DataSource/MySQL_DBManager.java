package com.javaproject.malki.takeandgo.model.DataSource;

import android.content.ContentValues;
import android.text.method.DateTimeKeyListener;
import android.util.Log;

import com.javaproject.malki.takeandgo.model.backend.ConstCars;
import com.javaproject.malki.takeandgo.model.backend.DB_Manager;
import com.javaproject.malki.takeandgo.model.entities.Branch;
import com.javaproject.malki.takeandgo.model.entities.Car;
import com.javaproject.malki.takeandgo.model.entities.CarWithStatus;
import com.javaproject.malki.takeandgo.model.entities.Client;
import com.javaproject.malki.takeandgo.model.entities.ENUMS;
import com.javaproject.malki.takeandgo.model.entities.Order;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.List;

import static com.javaproject.malki.takeandgo.model.backend.ConstCars.ContentValuesToBranch;
import static com.javaproject.malki.takeandgo.model.backend.ConstCars.ContentValuesToCar;
import static com.javaproject.malki.takeandgo.model.backend.ConstCars.ContentValuesToClient;
import static com.javaproject.malki.takeandgo.model.backend.ConstCars.ContentValuesToOrder;

/**
 * Created by malki on 30-Dec-17.
 */

public class MySQL_DBManager implements DB_Manager{


    private final String UserName="maker";
    private final String WEB_URL = "http://"+UserName+".vlab.jct.ac.il/Car2Go/secondApp";


    private boolean updateFlag = false;


    public void printLog(String message)
    {
        Log.d(this.getClass().getName(),"\n"+message);
    }
    public void printLog(Exception message)
    {
        Log.d(this.getClass().getName(),"ERROR: "+message);
    }
    private void SetUpdate()
    {
        updateFlag = true;
    }

    @Override
    public boolean ClientExist(String ID) {
        boolean exist = false;
        try {
            ContentValues values = new ContentValues();
            values.put(ConstCars.ClientConst.CLIENT_ID, ID);
            String result = PHPtools.POST(WEB_URL + "/Client_Exist.php", values);
            exist = Boolean.parseBoolean(result);

        }catch (Exception e)
        {}
        if (exist)
            return true;
        else
            return false;
    }

    @Override
    public boolean BranchExist(int ID) {
        boolean exist = false;
        try {
            ContentValues values = new ContentValues();
            values.put(ConstCars.BranchConst.BRANCH_NUMBER, ID);
            String result = PHPtools.POST(WEB_URL + "/Branch_Exist.php", values);
            exist = Boolean.parseBoolean(result);

        }catch (Exception e)
        {}
        if (exist)
            return true;
        else
            return false;
    }

    @Override
    public Client GetClient(String ID) throws Exception {
        ContentValues values = new ContentValues();
        //prepare query
        values.put(ConstCars.ClientConst.CLIENT_ID, ID);
        //get the requested client from the server
        String result = PHPtools.POST(WEB_URL + "/Get_User.php", values);
        if(result != null)
        {
            //convert the result into json array
            JSONArray array = new JSONObject(result).getJSONArray(ID);
            //get the client into json object
            JSONObject jsonObject = array.getJSONObject(0);
            //convert json format to contentValues
            ContentValues contentValues = PHPtools.JsonToContentValues(jsonObject);
            Client client = ConstCars.ContentValuesToClient(contentValues);
            return client;
        }

        throw new Exception("ERROR: The client doesn't exist!\n");
    }



    @Override
    public Order GetOrder(int ID) throws Exception {
        ContentValues values = new ContentValues();
        //prepare query
        values.put(ConstCars.OrderConst.ORDER_NUMBER, ID);
        //get the requested client from the server
        String result = PHPtools.POST(WEB_URL + "/Get_Order.php", values);
        if(result != null)
        {
            //convert the result into json array
            JSONArray array = new JSONObject(result).getJSONArray(String.valueOf(ID));
            //get the client into json object
            JSONObject jsonObject = array.getJSONObject(0);
            //convert json format to contentValues
            ContentValues contentValues = PHPtools.JsonToContentValues(jsonObject);
            Order order = ConstCars.ContentValuesToOrder(contentValues);
            return order;
        }
        throw new Exception("ERROR: The car doesn't exist!\n");

    }

    @Override
    public Branch GetBranch(int ID) throws Exception {
        ContentValues values = new ContentValues();
        //prepare query
        values.put(ConstCars.BranchConst.BRANCH_NUMBER, ID);
        //get the requested client from the server
        String result = PHPtools.POST(WEB_URL + "/Get_Branch.php", values);
        if(result != null)
        {
            //convert the result into json array
            JSONArray array = new JSONObject(result).getJSONArray(String.valueOf(ID));
            //get the client into json object
            JSONObject jsonObject = array.getJSONObject(0);
            //convert json format to contentValues
            ContentValues contentValues = PHPtools.JsonToContentValues(jsonObject);
            Branch branch = ConstCars.ContentValuesToBranch(contentValues);
            return branch;
        }
        throw new Exception("ERROR: The branch doesn't exist!\n");

    }

    @Override
    public CarWithStatus GetCar(String ID) throws Exception {
        ContentValues values = new ContentValues();
        //prepare query
        values.put(ConstCars.CarConst.LICENCE_NUMBER, ID);
        //get the requested client from the server
        String result = PHPtools.POST(WEB_URL + "/Get_Car.php", values);
        if(result != null)
        {
            //convert the result into json array
            JSONArray array = new JSONObject(result).getJSONArray(String.valueOf(ID));
            //get the client into json object
            JSONObject jsonObject = array.getJSONObject(0);
            //convert json format to contentValues
            ContentValues contentValues = PHPtools.JsonToContentValues(jsonObject);
            CarWithStatus car = ConstCars.ContentValuesToCarWithStatus(contentValues);
            return car;
        }
        throw new Exception("ERROR: The order doesn't exist!\n");

    }

    @Override
    public String AddClient(ContentValues values) throws Exception {
        String result = null;
        try {
            if(!ClientExist(values.getAsString(ConstCars.ClientConst.CLIENT_ID)))
            {
                values.put(ConstCars.ClientConst.USER_NAME, values.getAsString(ConstCars.ClientConst.CLIENT_ID));
                result = PHPtools.POST(WEB_URL + "/AddUser.php", values);
                if (result.equals(""))
                {
                    SetUpdate();
                    printLog("Client number " +result + "successfully added.");
                }

            }
            else
            {
                throw new Exception("ERROR: This client is already exist in the DB.\n");
            }

        } catch (IOException e) {
                printLog("ERROR: " +e);
        }
        return  result;

    }

    @Override
    public String AddOrder(ContentValues values) throws Exception {
        String result = null;
        try {
            if (ClientExist((String)values.get(ConstCars.OrderConst.CLIENT_NUMBER)))
            {
                result = PHPtools.POST(WEB_URL + "/AddOrder.php", values);
                if (!result.equals("")){
                    SetUpdate();
                    printLog("Order number " + result + "successfully added.");
                }
                return result;
            }
            else
            {
                throw new Exception("ERROR: You are not registered in our system\nPlease register first.\n");
            }

        } catch (IOException e) {
            printLog("ERROR: " +e);
        }
        return result;
    }

    @Override
    public String AddBranch(ContentValues values) throws Exception {
        String result = null;
        try {
            if(!BranchExist(values.getAsInteger(ConstCars.BranchConst.BRANCH_NUMBER)))
            {
                result = PHPtools.POST(WEB_URL + "/AddBranch.php", values);
                if (!result.equals("")){
                    SetUpdate();
                    printLog("Branch number " + result + "successfully added.");
                }
                return result;
            }
            else
            {
                throw new Exception("ERROR: Branch is already exist!\n");
            }

        } catch (IOException e) {
            printLog("ERROR: " +e);
        }
        return  result;
    }

    @Override
    public ENUMS.FUEL_MODE UpdateQuantity(float start, float end, float newFuel, String licencePlate) throws Exception {
        float quantity = (start-end) * 11 - newFuel;
        ENUMS.FUEL_MODE[] fuelLevel = ENUMS.FUEL_MODE.values();
        Car car = GetCar(licencePlate);
        ENUMS.FUEL_MODE tmp = car.getFuelMode();
        int index ;
        for (index = 0; index < fuelLevel.length; index++)
        {
            if(fuelLevel[index].equals(car.getFuelMode())){break;}
        }
        //over 22 liter the fuel level decreases in one degree - to ask the lecturer what to do
        //if the client waste a lot fuel and fill the cell by himself- maybe add a field:(
        if(quantity > 22)
        {
            //if the function returns null its an error
            return index > 0 ?  fuelLevel[index - 1] : null;
        }
        return fuelLevel[index];
    }


    @Override
    public List<Client> GetClients() {
        List<Client> result = new ArrayList<Client>();

        try {

            String str = PHPtools.GET(WEB_URL + "/Get_Users.php");
            JSONArray array = new JSONObject(str).getJSONArray("users");


            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);

                ContentValues contentValues = PHPtools.JsonToContentValues(jsonObject);
                Client client = ConstCars.ContentValuesToClient(contentValues);
                result.add(client);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Branch> GetBranches() {
        List<Branch> result = new ArrayList<Branch>();

        try {

            String str = PHPtools.GET(WEB_URL + "/Get_Branches.php");
            JSONArray array = new JSONObject(str).getJSONArray("branches");


            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);

                ContentValues contentValues = PHPtools.JsonToContentValues(jsonObject);
                Branch branch = ConstCars.ContentValuesToBranch(contentValues);
                result.add(branch);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Car> GetCars() {
        List<Car> result = new ArrayList<Car>();

        try {

            String str = PHPtools.GET(WEB_URL + "/Get_Cars.php");
            JSONArray array = new JSONObject(str).getJSONArray("cars");


            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);

                ContentValues contentValues = PHPtools.JsonToContentValues(jsonObject);
                Car car = ConstCars.ContentValuesToCar(contentValues);
                result.add(car);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Order> GetOrders() {
        List<Order> result = new ArrayList<Order>();

        try {

            String str = PHPtools.GET(WEB_URL + "/Get_Orders.php");
            JSONArray array = new JSONObject(str).getJSONArray("orders");


            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);

                ContentValues contentValues = PHPtools.JsonToContentValues(jsonObject);
                Order order = ConstCars.ContentValuesToOrder(contentValues);
                result.add(order);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean UpdateMileage(float kilometers, String licencePlate) {
        boolean success = true;
        try {
            //create query that update the car's mileage
            ContentValues query = new ContentValues();
            query.put("mileage", kilometers);
            query.put(ConstCars.CarConst.LICENCE_NUMBER, licencePlate);
            //post query
            String str = PHPtools.POST(WEB_URL + "/UpdateMileage.php", query );
            success = Boolean.valueOf(str);
            return success;
        } catch (Exception e) {
            success= false;
        }
        return  success;
    }

    @Override
    public List<Car> AvailableCars() {
        List<Car> result = new ArrayList<Car>();

        try {

            String str = PHPtools.GET(WEB_URL + "/AvailableCars.php");
            JSONArray array = new JSONObject(str).getJSONArray("availableCars");


            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);

                ContentValues contentValues = PHPtools.JsonToContentValues(jsonObject);
                Car car = ConstCars.ContentValuesToCar(contentValues);
                result.add(car);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Car> AvailableCars(String location) {
        List<Car> result = new ArrayList<Car>();

        try {
            ContentValues query = new ContentValues();
            query.put(ConstCars.CarConst.LOCATION_NUMBER, location);
            String str = PHPtools.POST(WEB_URL + "/AvailableCars.php", query);
            JSONArray array = new JSONObject(str).getJSONArray("availableCars");


            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);

                ContentValues contentValues = PHPtools.JsonToContentValues(jsonObject);
                Car car = ConstCars.ContentValuesToCar(contentValues);
                result.add(car);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Car> AvailableCars(int radius) {
        List<Car> result = new ArrayList<Car>();

        try {
            ContentValues query = new ContentValues();
            query.put("radius", radius);
            String str = PHPtools.POST(WEB_URL + "/getDistance.php", query);
            JSONArray array = new JSONObject(str).getJSONArray("cars");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);

                ContentValues contentValues = PHPtools.JsonToContentValues(jsonObject);
                Car car = ConstCars.ContentValuesToCar(contentValues);
                result.add(car);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Branch> IsModelInBranches(String model) {
        //todo bonus
        return null;
    }

    @Override
    public Dictionary<String, List<Branch>> ModelInBranches() {
        //todo bonus
        return null;
    }

    @Override
    public List<Order> GetOpenOrders() {
        List<Order> result = new ArrayList<Order>();

        try {

            String str = PHPtools.GET(WEB_URL + "/GetOpenOrders.php");
            JSONArray array = new JSONObject(str).getJSONArray("orders");


            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);

                ContentValues contentValues = PHPtools.JsonToContentValues(jsonObject);
                Order order = ConstCars.ContentValuesToOrder(contentValues);
                result.add(order);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean CloseOrder(float kilometers, int orderNumber) {
        boolean success = true;
        try {
            Date date = new Date();
            //create query that update the order
            ContentValues query = new ContentValues();
            query.put("kilometers", kilometers);
            query.put(ConstCars.OrderConst.ORDER_NUMBER, orderNumber);
            query.put(ConstCars.OrderConst.END_RENT, String.valueOf(date));
            //post query
            String str = PHPtools.POST(WEB_URL + "/UpdateMileage.php", query );
            success = Boolean.valueOf(str);
            return success;
        } catch (Exception e) {
            success= false;
        }
        return  success;
    }

    @Override
    public boolean CloseOrder(float kilometers, int orderNumber, Boolean isFuel, Float fuelVol) {
        boolean success = true;
        try {
            Date date = new Date();
            //create query that update the order
            ContentValues query = new ContentValues();
            query.put("kilometers", kilometers);
            query.put(ConstCars.OrderConst.ORDER_NUMBER, orderNumber);
            query.put(ConstCars.OrderConst.END_RENT, String.valueOf(date));
            if(isFuel)
            {
                query.put(ConstCars.OrderConst.IS_FUEL, isFuel.toString());
                query.put(ConstCars.OrderConst.FUEL_VOL, fuelVol.toString());
            }
            else
            {
                return CloseOrder(kilometers, orderNumber);
            }
            //post query
            String str = PHPtools.POST(WEB_URL + "/UpdateMileage.php", query );
            success = Boolean.valueOf(str);
            return success;
        } catch (Exception e) {
            success= false;
        }
        return  success;
    }

    @Override
    public boolean isClosedOrder() {
        //todo, dont know what to do here
        return false;
    }
}
