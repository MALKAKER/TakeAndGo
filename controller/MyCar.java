package com.javaproject.malki.takeandgo.controller;


import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.javaproject.malki.takeandgo.R;
import com.javaproject.malki.takeandgo.ReceiveOnBoard;
import com.javaproject.malki.takeandgo.model.backend.DbManagerFactory;
import com.javaproject.malki.takeandgo.model.entities.Car;
import com.javaproject.malki.takeandgo.model.entities.Order;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyCar extends Fragment {
    Order currentOrder;
    Car car;
    View view;
    //flag if the order is new
    Boolean flag = false;
    private RelativeLayout myCar;
    private TextView licence;
    private TextView model;
    private TextView lcation;
    private TextView mileage;
    private TextView fuel;
    private Switch isRent;



    public MyCar() {
        // Required empty public constructor
    }

    public void checkOrderOpen()
    {
        new AsyncTask<Void,Void,Void>()
        {
            @Override
            protected void onPostExecute(Void aVoid) {
                if(currentOrder!=null && car !=null)
                {
                    Toast.makeText(getActivity(),"teat1",Toast.LENGTH_SHORT).show();
                    //change the flag value so the event on change checked will not appear
                    flag = true;
                    isRent.setChecked(true);
                    licence.setText(String.valueOf(car.getLocationNumber()));
                    model.setText(car.getModelType());
                    mileage.setText(String.valueOf(car.getMileage()));
                    fuel.setText(String.valueOf(car.getFuelMode()));
                    lcation.setText(String.valueOf(car.getLocationNumber()));
                    myCar.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(),"teat1",Toast.LENGTH_SHORT).show();

                }else
                {
                    isRent.setChecked(false);
                    if(car==null)
                        Toast.makeText(getActivity(), getString(R.string.There_is_a_problem_with_the_car), Toast.LENGTH_SHORT).show();
                    if(currentOrder == null)
                        Toast.makeText(getActivity(), getString(R.string.No_open_order), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected Void doInBackground(Void... voids) {
                List<Order> orders = DbManagerFactory.getManager().GetOpenOrders();
                Bundle bundle = getArguments();
                final String user = bundle.getString(ConstValues.User);
                for(Order o: orders)
                {
                    if(user.equals(o.getClientNumber()))
                    {
                        currentOrder = o;
                        try {
                            car = DbManagerFactory.getManager().GetCar(o.getCarNumber());
                            car = car;
                        } catch (Exception e) {
                            //in case there is a error in the car don't show order
                            car = null; currentOrder = null;
                        }
                        return null;
                    }
                }
                //if there is no open order on the user name the order is not exist
                car = null;
                currentOrder = null;
                return null;
            }
        }.execute();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_my_car, container, false);
        // Inflate the layout for this fragment
        myCar = (RelativeLayout)view.findViewById( R.id.myCar );
        licence = (TextView)view.findViewById( R.id.licence );
        model = (TextView)view.findViewById( R.id.model );
        lcation = (TextView)view.findViewById( R.id.lcation );
        mileage = (TextView)view.findViewById( R.id.mileage );
        fuel = (TextView)view.findViewById( R.id.fuel );
        isRent = (Switch)view.findViewById( R.id.isRent );
        isRent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked && !flag)
                {
                    //change to the fragment where users open the orders
                    PresentBranches fragment = new PresentBranches();
                    fragment.setArguments(getArguments());
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, fragment);//add(R.id.fragment_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                else if(!isChecked)
                {
                    flag = false;
                    closeOrder();
                }
            }
        });
        checkOrderOpen();
        return view;
    }

    private void closeOrder() {

    }

}
