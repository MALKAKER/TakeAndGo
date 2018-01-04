package com.javaproject.malki.takeandgo.model.DataSource;

import android.content.ContentValues;

import com.javaproject.malki.projectstepone.model.backend.ConstCars;
import com.javaproject.malki.projectstepone.model.backend.DB_Manager;
import com.javaproject.malki.projectstepone.model.entities.Branch;
import com.javaproject.malki.projectstepone.model.entities.Car;
import com.javaproject.malki.projectstepone.model.entities.CarModel;
import com.javaproject.malki.projectstepone.model.entities.Client;
import com.javaproject.malki.projectstepone.model.entities.ENUMS;
import com.javaproject.malki.projectstepone.model.entities.Order;

import java.util.List;

import static com.javaproject.malki.projectstepone.model.DataSource.List_DB.branches;
import static com.javaproject.malki.projectstepone.model.DataSource.List_DB.carModels;
import static com.javaproject.malki.projectstepone.model.DataSource.List_DB.cars;
import static com.javaproject.malki.projectstepone.model.DataSource.List_DB.clients;
import static com.javaproject.malki.projectstepone.model.DataSource.List_DB.orders;
import static com.javaproject.malki.projectstepone.model.backend.ConstCars.ContentValuesToBranch;
import static com.javaproject.malki.projectstepone.model.backend.ConstCars.ContentValuesToCar;
import static com.javaproject.malki.projectstepone.model.backend.ConstCars.ContentValuesToCarModel;
import static com.javaproject.malki.projectstepone.model.backend.ConstCars.ContentValuesToClient;
import static com.javaproject.malki.projectstepone.model.backend.ConstCars.ContentValuesToOrder;

/**
 * the class manage data's storage and retrievement
 * Created by malki on 20-Nov-17.
 */

public class ListDbManager implements DB_Manager{

    /*
    * ClientExist: checks if client exist
    * */
    @Override
    public boolean ClientExist(String ID) {
        for (Client c:List_DB.clients) {
            if(c.getClientID().equals(ID))
            {
                return true;
            }
        }
        return false;
    }
    /*
    * ModelExist: checks if model exist
    * */
    @Override
    public boolean ModelExist(String details) {
        for (CarModel c:List_DB.carModels) {
            if(c.getModel().equals(details))
            {
                return true;
            }
        }
        return false;
    }

    /*
    * CarExist: checks if car exist
    * */
    @Override
    public boolean CarExist(String newLicencePlate) {
        for (Car c:List_DB.cars) {
            if(c.getLicencePlate().equals(newLicencePlate))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean BranchExist(int ID) {
        for (Branch b:List_DB.branches) {
            if(b.getBranchNumber()==(ID))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public Client GetClient(String ID) throws Exception {
        for (Client c:List_DB.clients) {
            if(c.getClientID().equals(ID))
            {
                return c;
            }
        }
        throw new Exception("ERROR: The client doesn't exist!\n");
    }

    @Override
    public CarModel GetModel(String details) throws Exception {
        for (CarModel c:List_DB.carModels) {
            if(c.getModel().equals(details))
            {
                return c;
            }
        }
        throw new Exception("ERROR: The model doesn't exist!\n");
    }

    @Override
    public Car GetCar(String newLicencePlate) throws Exception {
        for (Car c:List_DB.cars) {
            if(c.getLicencePlate().equals(newLicencePlate))
            {
                return c;
            }
        }
        throw new Exception("ERROR: The car doesn't exist!\n");
    }

    @Override
    public Branch GetBranch(int ID) throws Exception {
        for (Branch b:List_DB.branches) {
            if(b.getBranchNumber() == (ID))
            {
                return b;
            }
        }
        throw new Exception("ERROR: The branch doesn't exist!\n");
    }

    @Override
    public Order GetOrder(int ID) throws Exception {
        for (Order o:List_DB.orders) {
            if(o.getOrderNumber() == (ID))
            {
                return o;
            }
        }
        throw new Exception("ERROR: The order doesn't exist!\n");
    }

    /*
            * AddClient: adds client to list
            * */
    @Override
    public String AddClient(ContentValues newClient) throws Exception {
        String user = null;
        if(!ClientExist(newClient.getAsString(ConstCars.ClientConst.CLIENT_ID)))
        {
            Client client = ContentValuesToClient(newClient);
            user = (String)newClient.get(ConstCars.ClientConst.CLIENT_ID);
            client.setUserName(user);
            (List_DB.clients).add(client);
        }
        else
        {
            throw new Exception("ERROR: This client is already exist in the DB.\n");
        }
        return user;
    }
    /*
    * AddModel: adds model to list
    * */
    @Override
    public String AddModel(ContentValues carModel) throws Exception {
        if(!ModelExist(carModel.getAsString(ConstCars.CarModelConst.MODEL)))
        {
            CarModel model = ContentValuesToCarModel(carModel);
            (List_DB.carModels).add(model);
        }
        else
        {
            throw new Exception("ERROR: This car-model is already exist in the DB.\n");
        }
        return (String) carModel.get(ConstCars.CarModelConst.MODEL_NAME);
    }

    @Override
    public String AddCar(ContentValues car) throws Exception {
        if (ModelExist(car.getAsString(ConstCars.CarConst.MODEL_TYPE)) && !CarExist(car.getAsString(ConstCars.CarConst.LICENCE_NUMBER)))
        {
            Car c = ContentValuesToCar(car);
            (List_DB.cars).add(c);
        }
        else
        {
            throw new Exception("ERROR: That car is already exist in the DB.\n");
        }
        return (String) car.get(ConstCars.CarConst.LICENCE_NUMBER);
    }

    @Override
    public String AddOrder(ContentValues order) throws Exception {
        if (ClientExist((String)order.get(ConstCars.OrderConst.CLIENT_NUMBER)))
        {
            Order o = ContentValuesToOrder(order);
            List_DB.orders.add(o);
        }
        else
        {
            throw new Exception("ERROR: You are not registered in our system\nPlease register first.\n");
        }
        return (String) order.get(ConstCars.OrderConst.ORDER_NUMBER);
    }

    @Override
    public String AddBranch(ContentValues branch) throws Exception {
        if(!BranchExist(branch.getAsInteger(ConstCars.BranchConst.BRANCH_NUMBER)))
        {
            Branch b = ContentValuesToBranch(branch);
            List_DB.branches.add(b);
        }
        else
        {
            throw new Exception("ERROR: Branch is already exist!\n");
        }
        return branch.getAsLong( ConstCars.BranchConst.BRANCH_NUMBER).toString();
    }

    @Override
    //maybe to move that function to controller??
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

    /*
    * GetModels() retrieve all models are existed in the system
    * */
    @Override
    public List<CarModel> GetModels() {
        return carModels;
    }
    /*
    * GetClients() retrieve all clients are existed in the system
    * */
    @Override
    public List<Client> GetClients() {
        return clients;
    }
    /*
    * GetBranches() retrieve all branches are existed in the system
    * */
    @Override
    public List<Branch> GetBranches() {
        return branches;
    }
    /*
    * GetCar() retrieve all cars are existed in the system
    * */
    @Override
    public List<Car> GetCars() {
        return cars;
    }
    /*
    * I added - GetOrders() retrieve all orders are existed in the system
    * */
    @Override
    public List<Order> GetOrders() {
        return orders;
    }
}
