package com.javaproject.malki.takeandgo.controller;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.javaproject.malki.takeandgo.R;
import com.javaproject.malki.takeandgo.model.backend.DbManagerFactory;
import com.javaproject.malki.takeandgo.model.entities.Car;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * return available cars in a nominal radius
 */
public class AvailableCars extends Fragment implements View.OnClickListener, NumberPicker.OnValueChangeListener {
    private int Radius;
    private String address;
    private List<Car> availableCars;
    private ListView presentCarsListView;
    private EditText searchAddress;
    private Button searchBranchesNearby;
    static Dialog d ;




    public AvailableCars() {
        // Required empty public constructor
    }

    /*
    * show() to pick a radius number
    * */
    public void show()
    {

        final Dialog d = new Dialog(getActivity());
        d.setTitle("Pick a radius");
        d.setContentView(R.layout.numberpicker_dialog);
        final Button addRadiusButton = (Button) d.findViewById(R.id.button2);
        Button cancelButton = (Button) d.findViewById(R.id.button1);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            Radius = newVal;
        }
        });
        Toast.makeText(getActivity(),String.valueOf(Radius),Toast.LENGTH_SHORT).show();
        np.setMaxValue(100000);
        np.setMinValue(0);
        np.setWrapSelectorWheel(true);
        addRadiusButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                //if (v == addRadiusButton)
                new AsyncTask<Void, Void , ListAdapter>()
                {
                    @Override
                    protected void onPostExecute(ListAdapter adapter) {
                        try {
                            if (adapter != null)
                                presentCarsListView.setAdapter(adapter);
                            else
                                throw new Exception("No results");
                        }catch (Exception e)
                        {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT);
                        }
                    }

                    @Override
                    protected ListAdapter doInBackground(Void... voids) {
                        //List<Car> results = new ArrayList<Car>();
                        if(availableCars!=null)
                            availableCars.clear();
                        availableCars = DbManagerFactory.getManager().AvailableCars(Radius, address);
                        if(availableCars!=null)
                            return new ArrayAdapter<Car>(getActivity(), R.layout.result_presentation, availableCars);
                        else
                            return null;
                    }

                }.execute();
                d.dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();

    }
    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
        Toast.makeText(getActivity(), i ,Toast.LENGTH_LONG).show();

    }

    @Override
    public void onClick(View view) {
       
        if ( view == searchBranchesNearby ) {

            address = searchAddress.getText().toString();
            if (!address.equals(""))
            {
                //pick radius and search available cars
                show();
            }
            else
            {
                //do nothing
            }

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_available_cars, container, false);
        presentCarsListView = (ListView)view.findViewById( R.id.present_cars_listView );
        searchAddress = (EditText)view.findViewById( R.id.searchAddress );
        searchBranchesNearby = (Button)view.findViewById( R.id.searchBranchesNearby );

        searchBranchesNearby.setOnClickListener( this );
        return view;
    }

}
