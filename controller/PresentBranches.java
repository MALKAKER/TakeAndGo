package com.javaproject.malki.takeandgo.controller;


import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.javaproject.malki.takeandgo.model.service.CarOnBoard;
import com.javaproject.malki.takeandgo.R;
import com.javaproject.malki.takeandgo.model.backend.ConstCars;
import com.javaproject.malki.takeandgo.model.backend.DbManagerFactory;
import com.javaproject.malki.takeandgo.model.entities.Branch;
import com.javaproject.malki.takeandgo.model.entities.Car;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PresentBranches extends Fragment{
    private SearchView presentBranchesSearchBranches;
    private ListView presentBranchesBranchesList;
    private LinearLayout presentBranchesBranchView;
    private ListView presentBranchesCarsView;
    //the activity view
    View viewFragment;
    //private List<Branch> branches;
    private Branch selectedBranch;
    private Car selectedCar;
    private List<Branch> myStringList;
    List<Branch> Branches;
    private List<Car> cars;
    //SELECTED BRANCH NUMBER
    TextView showBranch;
    private TextView showNumber;
    private TextView showMap;
    private TextView places;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2018-01-20 23:58:43 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        showNumber = (TextView)viewFragment.findViewById( R.id.showNumber );
        showMap = (TextView)viewFragment.findViewById( R.id.showMap );
        places = (TextView)viewFragment.findViewById( R.id.places );
    }


    /*create branch view*/
    private void CreateBranchView() {
        showNumber.setText(String.valueOf((long)selectedBranch.getBranchNumber()));
        showMap.setAutoLinkMask(Linkify.MAP_ADDRESSES);
        showMap.setLinksClickable(true);
        SpannableString spanStr = new SpannableString(selectedBranch.getBranchAddress().toString().toUpperCase().replace(","," "));
        spanStr.setSpan(new UnderlineSpan(), 0, spanStr.length(), 0);
        showMap.setText(spanStr);
        //showMap.setText(selectedBranch.getBranchAddress().toString().toUpperCase().replace(","," "));
        showMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent geoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q="
                        +showMap.getText().toString()));
                startActivity(geoIntent);
            }
        });
        places.setText(String.valueOf(selectedBranch.getParkingSpace()));
        presentBranchesBranchView.setVisibility(View.VISIBLE);
        Toast.makeText(getActivity(), "Branch number " + String.valueOf(selectedBranch.getBranchNumber()) + " was selected.", Toast.LENGTH_LONG).show();
        showCars();
    }

    /*create order for current car and user*/
    private void CreateOrder() throws Exception {
        Bundle bundle = getArguments();
        final String user = bundle.getString(ConstValues.User);
        Date d = new Date();
        SimpleDateFormat tmp = new SimpleDateFormat(ConstValues.TIME_FORMAT);
        //Object date = new java.sql.Timestamp(d.getTime());
        final ContentValues cv = new ContentValues();
        //there is no chance that the user who is loged is not the current user
        cv.put(ConstCars.OrderConst.CLIENT_NUMBER, user);
        cv.put(ConstCars.OrderConst.CAR_NUMBER, selectedCar.getLicencePlate().toString());
        //in the server... cv.put(ConstCars.OrderConst.START_MILEAGE, selectedCar.getMileage());
        cv.put(ConstCars.OrderConst.START_RENT, tmp.format(d));
        new AsyncTask<Void, Void, String>(){
            @Override
            protected void onPostExecute(String num) {
                //add a service that remind that the app is on board
                Bundle extras = new Bundle();
                extras.putString(ConstCars.OrderConst.CLIENT_NUMBER, user);
                extras.putString(ConstCars.OrderConst.CAR_NUMBER, selectedCar.getLicencePlate().toString());
                extras.putString(ConstCars.OrderConst.ORDER_NUMBER, String.valueOf(num));
                getActivity().startService(new Intent(getActivity(), CarOnBoard.class).putExtras(extras).setAction(ConstValues.ON_BOARD_SERVICE));
                showCars();
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    String num = DbManagerFactory.getManager().AddOrder(cv).replace(" ","");
                    return num;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();


    }

    /*show available cars in the selected branch*/
    public void showCars()
    {

        presentBranchesCarsView.setVisibility(View.VISIBLE);
        new AsyncTask<Void, Void , ListAdapter>()
        {
            @Override
            protected void onPostExecute(ListAdapter adapter) {
                try {
                    if (adapter != null)
                        presentBranchesCarsView.setAdapter(adapter);
                    else
                        presentBranchesCarsView.setVisibility(View.GONE);
                        throw new Exception("No results");
                }catch (Exception e)
                {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT);
                }
            }

            @Override
            protected ListAdapter doInBackground(Void... voids) {
                //List<Car> results = new ArrayList<Car>();
                if(cars!=null)
                    cars.clear();
                //check if a branch was selected
                if(selectedBranch != null)
                    cars = DbManagerFactory.getManager().AvailableCars(String.valueOf(selectedBranch.getBranchNumber()));
                if(cars!=null)
                    return new ArrayAdapter<Car>(getActivity(), R.layout.result_presentation, cars);
                else
                    return null;
            }

        }.execute();
    }

    public PresentBranches() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewFragment = inflater.inflate(R.layout.fragment_present_branches, container, false);
        presentBranchesBranchesList = (ListView)viewFragment.findViewById(R.id.present_branches_branchesList);
        presentBranchesBranchesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedBranch = Branches.get(i);
                CreateBranchView();
            }
        });
        presentBranchesSearchBranches = (SearchView) viewFragment.findViewById(R.id.present_branches_SearchBranches);
        presentBranchesCarsView = (ListView) viewFragment.findViewById(R.id.present_branches_carsView);
        presentBranchesCarsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCar = cars.get(i);
                try {
                    CreateOrder();
                } catch (Exception e) {
                    Toast toast = Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT );
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                }
            }
        });
        presentBranchesBranchView = (LinearLayout) viewFragment.findViewById(R.id.present_branches_branchView);
        findViews();
        ShowFilteredBranches();
        return viewFragment;
    }

    /**
     * ShowFilteredBranches, filter branch according to city
     */
    private void ShowFilteredBranches() {
        new AsyncTask<Void, Void , List<Branch>>()
        {
            @Override
            protected void onPostExecute(final List<Branch> Bs) {
                try {
                    myStringList = new ArrayList<Branch>();
                    myStringList.addAll(Branches);
                    //TODO, I had tried a lot of times to adapt a custume layout but with no success....
                    final ArrayAdapter adapter =new ArrayAdapter<Branch>(getActivity().getBaseContext(), R.layout.result_presentation, myStringList)
                    {

                        @Override
                        public Filter getFilter() {
                            return new Filter() {
                                @Override
                                protected FilterResults performFiltering(CharSequence constraint) {

                                    FilterResults filterResults = new FilterResults();

                                    myStringList.clear();
                                    //filtering the results according to city

                                    for (Branch s : Bs) {
                                        if ((s.getBranchAddress().getCity().toLowerCase()).startsWith(constraint.toString().toLowerCase()))
                                            myStringList.add(s);
                                    }

//
                                    return filterResults;
                                }

                                @Override
                                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                                    //myStringList = (ArrayList<Branch>) filterResults.values;
                                    notifyDataSetChanged();
                                }
                            };

                        }
                    };

                    if(adapter != null) {
                        presentBranchesBranchesList.setAdapter(adapter);
                        presentBranchesSearchBranches.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                            @Override
                            public boolean onQueryTextSubmit(String query) {

                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String newText) {
                                adapter.getFilter().filter(newText);
                                adapter.notifyDataSetChanged();
                                return true;
                            }
                        });

                    }
                    else
                        throw new Exception("No results");
                }catch (Exception e)
                {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT);
                }
            }

            @Override
            protected List<Branch> doInBackground(Void... voids) {
                /*get the branches list*/

                Branches = DbManagerFactory.getManager().GetBranches();

                if(Branches != null) {

                    return Branches;
                }
                else
                {
                    return  null;
                }
            }
        }.execute();
    }

}
