package com.javaproject.malki.takeandgo.controller;


import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.javaproject.malki.takeandgo.R;
import com.javaproject.malki.takeandgo.ReceiveOnBoard;
import com.javaproject.malki.takeandgo.model.backend.ConstCars;
import com.javaproject.malki.takeandgo.model.backend.DbManagerFactory;
import com.javaproject.malki.takeandgo.model.entities.Car;
import com.javaproject.malki.takeandgo.model.entities.Order;

import java.text.DecimalFormat;
import android.app.DialogFragment;
import android.app.Fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyCar extends Fragment {
    Order currentOrder;
    Car car;
    View view;
    //flag if the order is new
    Boolean flag;
    final float pricePerDay = new Float("120");
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
                    //change the flag value so the event on change checked will not appear
                    flag = true;
                    isRent.setChecked(true);
                    licence.setText(String.valueOf(car.getLicencePlate()));
                    model.setText(car.getModelType());
                    mileage.setText(String.valueOf(car.getMileage()));
                    fuel.setText(String.valueOf(car.getFuelMode()));
                    lcation.setText(String.valueOf(car.getLocationNumber()));
                    myCar.setVisibility(View.VISIBLE);

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
                if (orders != null) {
                    for (Order o : orders) {
                        if (user.equals(o.getClientNumber())) {
                            currentOrder = o;
                            try {
                                car = DbManagerFactory.getManager().GetCar(o.getCarNumber());
                                car = car;
                            } catch (Exception e) {
                                //in case there is a error in the car don't show order
                                car = null;
                                currentOrder = null;
                            }
                            return null;
                        }
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
        //to make sure the other intent of getting car will not accured if the car is on
        flag = false;
        view =  inflater.inflate(R.layout.fragment_my_car, container, false);
        // Inflate the layout for this fragment
        myCar = (RelativeLayout)view.findViewById( R.id.myCar );
        licence = (TextView)view.findViewById( R.id.licence );
        model = (TextView)view.findViewById( R.id.model );
        lcation = (TextView)view.findViewById( R.id.lcation );
        mileage = (TextView)view.findViewById( R.id.mileage );
        fuel = (TextView)view.findViewById( R.id.fuel );
        isRent = (Switch)view.findViewById( R.id.isRent );
        checkOrderOpen();
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
                    isRent.setChecked(false);
                    myCar.setVisibility(View.GONE);
                }
            }
        });

        return view;
    }

    private void closeOrder() {
        Bundle args = new Bundle();
        args.putString(ConstCars.OrderConst.ORDER_NUMBER, String.valueOf(currentOrder.getOrderNumber()));
        DecimalFormat d = calculateCost();
        args.putString(ConstCars.OrderConst.BILL_AMOUNT, String.valueOf(d));
        CloseOrderDialog newFragment = new CloseOrderDialog().newInstance();
        newFragment.setArguments(args);
        newFragment.show(getActivity().getFragmentManager(),"dialog");
    }

    private DecimalFormat calculateCost() {
        DecimalFormat d = null;
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date d1, d2;
        try {
            d1 = currentOrder.getStartRent();
            d2 = date;
            long diff = d2.getDate() - d1.getDate();
            //calculate basic cost
            float cost =diff * pricePerDay;
            String str = String.valueOf(cost);
            d = new DecimalFormat(str);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return d;
    }

}
