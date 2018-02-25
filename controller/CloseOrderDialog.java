package com.javaproject.malki.takeandgo.controller;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.app.DialogFragment;
import android.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.javaproject.malki.takeandgo.R;
import com.javaproject.malki.takeandgo.model.backend.ConstCars;
import com.javaproject.malki.takeandgo.model.backend.DbManagerFactory;
import com.javaproject.malki.takeandgo.model.entities.Branch;
import com.javaproject.malki.takeandgo.model.entities.ENUMS;
import com.javaproject.malki.takeandgo.model.service.CarOnBoard;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.javaproject.malki.takeandgo.R.id.myCar;

/**
 * A simple {@link Fragment} subclass.
 */
public class CloseOrderDialog extends DialogFragment implements SeekBar.OnSeekBarChangeListener {
    private Spinner pickLocation;
    private SeekBar fuelAmount;
    private SeekBar kilometers;
    private CheckBox checkBox;
    private Button closeOrderButton;
    private Button cancalButton;
    //operation
    private static final String TAG = "closeOrderCheck";
    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2018-01-23 23:39:08 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */

    //get strings from current DB to spinner
    private List<String> getLocations(List<Branch> branchList)
    {

        List<String> lst = new ArrayList<String>();
        for (Branch b : branchList)
        {
            lst.add(String.format("%d", b.getBranchNumber()));
        }
        return lst;
    }
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//seekBar implementation
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
        Toast.makeText(getActivity(),progress +"% fuel", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        //do nothing
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        //do nothing
    }
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


    //Check if the field was filled
    private void checkFill(String str) throws Exception {
        if(str.equals(""))
        {
            throw new Exception(getString(R.string.Missing_field_Error));
        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //init spiner
    private void initiSpinners(List<String> b) {
        ArrayAdapter<String> branchAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,b);
        branchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pickLocation.setAdapter(branchAdapter);

    }
    public CloseOrderDialog() {
        // Required empty public constructor
    }
    static CloseOrderDialog newInstance() {
        return new CloseOrderDialog();
    }
    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                    Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        View view = inflater.inflate(R.layout.fragment_close_order_dialog, container, false);
        //AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        pickLocation = (Spinner)view.findViewById( R.id.pickLocation );
        fuelAmount = (SeekBar)view.findViewById( R.id.fuelAmount );
        fuelAmount.setEnabled(false);
        fuelAmount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Toast.makeText(getActivity(),i +"% fuel", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //enable to fill fuel amount
        kilometers = (SeekBar)view.findViewById( R.id.kilometers );
        kilometers.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Toast.makeText(getActivity(),i +" Km", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        checkBox = (CheckBox)view.findViewById( R.id.checkBox );
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    fuelAmount.setEnabled(true);
                }
                else
                {
                    fuelAmount.setEnabled(false);
                }
            }
        });
        closeOrderButton = (Button)view.findViewById( R.id.closeOrderButton );
        closeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeAction();
                //dismiss();
            }
        });
        cancalButton = (Button)view.findViewById( R.id.cancalButton );
        cancalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        //set spinner views
        setSpinnerViews();

        return view;
    }

    private void setSpinnerViews() {
        new AsyncTask<Void,Void,List<String>>(){
            @Override
            protected void onPostExecute(List<String> locs) {
                super.onPostExecute(locs);
                //create the view
                initiSpinners(locs);
            }

            @Override
            protected List<String> doInBackground(Void... voids) {
                List<Branch> tmp = DbManagerFactory.getManager().GetBranches();
                List<String> str = getLocations(tmp);
                if(str!=null)
                    return str;
                return null;
            }
        }.execute();
    }

    private void closeAction() {
        try {
            final ContentValues contentValues = new ContentValues();
            Bundle mArgs = getArguments();
            final int level = kilometers.getProgress();
            final Boolean isFuel = checkBox.isChecked();
            final int f = isFuel? fuelAmount.getProgress():0;
            final int orderNumber = Integer.parseInt(mArgs.getString(ConstCars.OrderConst.ORDER_NUMBER));
            final float bill = mArgs.getFloat(ConstCars.OrderConst.BILL_AMOUNT)+ level * 10 - 5 * f;
            final String location = pickLocation.getSelectedItem().toString();
            checkFill(location);


            if(isFuel)
            {

                new AsyncTask<Void,Void,Boolean>()
                {
                    @Override
                    protected void onPostExecute(Boolean B) {
                        String msg;
                        if(B)
                        {
                            //stop service
                            getActivity().stopService(new Intent(getActivity(), CarOnBoard.class));
                            msg = "Car returned successfully :)";
                        }
                        else
                        {
                            msg = "failed to close order";
                        }
                        Toast toast = Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG );
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.show();
                        dismiss();
                    }

                    @Override
                    protected Boolean doInBackground(Void... voids) {
                        float a = new Float(level);
                        long b = (new Long(location));
                        float c = new Float(f);
                        return DbManagerFactory.getManager().CloseOrder(a, orderNumber, bill, b,isFuel, c );

                    }
                }.execute();
            }
            else
            {
                Log.i(TAG, "fuel 262");
                new AsyncTask<Void,Void,Boolean>()
                {
                    @Override
                    protected void onPostExecute(Boolean B) {
                        String msg;
                        if(B)
                        {
                            msg = "Order closed";
                        }
                        else
                        {
                            msg = "failed to close order";
                        }
                        dismiss();
                        Toast toast = Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG );
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.show();
                    }

                    @Override
                    protected Boolean doInBackground(Void... voids) {
                        float a = new Float(level);
                        long b = (new Long(location));
                        return DbManagerFactory.getManager().CloseOrder(a, orderNumber, bill, b);

                    }
                }.execute();
            }


        }catch (Exception e)
         {
             Toast toast = Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG );
             toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
             toast.show();
        }
    }
}