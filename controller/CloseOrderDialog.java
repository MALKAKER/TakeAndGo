package com.javaproject.malki.takeandgo.controller;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.javaproject.malki.takeandgo.R;
import com.javaproject.malki.takeandgo.model.backend.ConstCars;
import com.javaproject.malki.takeandgo.model.backend.DbManagerFactory;
import com.javaproject.malki.takeandgo.model.entities.Branch;
import com.javaproject.malki.takeandgo.model.entities.ENUMS;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CloseOrderDialog extends DialogFragment implements SeekBar.OnSeekBarChangeListener {
    private Spinner pickLocation;
    private SeekBar fuelAmount;
    private SeekBar kilometers;
    private CheckBox checkBox;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2018-01-23 23:39:08 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        pickLocation = (Spinner)getActivity().findViewById( R.id.pickLocation );
        fuelAmount = (SeekBar)getActivity().findViewById( R.id.fuelAmount );
        //enable to fill fuel amount
        fuelAmount.setEnabled(false);
        kilometers = (SeekBar)getActivity().findViewById( R.id.kilometers );
        checkBox = (CheckBox)getActivity().findViewById( R.id.checkBox );
    }

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

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(R.layout.fragment_close_order_dialog);
        findViews();
        //set spinner views
        setSpinnerViews();
        builder.setMessage("Close order")
                .setPositiveButton("Close order", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            closeAction();
                            Toast toast = Toast.makeText(getActivity(), "Car returned successfully:)", Toast.LENGTH_LONG );
                            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                            toast.show();
                        }catch (Exception e)
                        {
                            Toast toast = Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG );
                            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                            toast.show();
                        }
                        dismiss();
                    }
                })
                .setNegativeButton("Not now!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                    }});
        return builder.create();
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
            final int orderNumber = mArgs.getInt(ConstCars.OrderConst.ORDER_NUMBER);
            DecimalFormat df = new DecimalFormat(".##");
            final DecimalFormat bill = new DecimalFormat(df.format(mArgs.getFloat(ConstCars.OrderConst.BILL_AMOUNT)));
            final String location = pickLocation.getSelectedItem().toString();
            checkFill(location);
            final int level = kilometers.getProgress();
            final Boolean isFuel = checkBox.isChecked();
            if(isFuel)
            {
                fuelAmount.setEnabled(true);
                final int f = fuelAmount.getProgress();
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
                        Toast toast = Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG );
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.show();
                    }

                    @Override
                    protected Boolean doInBackground(Void... voids) {
                        float a = new Float(level);
                        long b = (new Long(location));
                        float c = new Float(f);
                        return DbManagerFactory.getManager().CloseOrder(a, orderNumber, bill, b,isFuel, c );

                    }
                };
            }
            else
            {
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
                };
            }


        }catch (Exception e)
         {
             Toast toast = Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG );
             toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
             toast.show();
        }
    }
}