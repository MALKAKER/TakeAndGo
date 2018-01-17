package com.javaproject.malki.takeandgo.controller;


import android.content.ClipData;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;

import com.javaproject.malki.takeandgo.R;
import com.javaproject.malki.takeandgo.model.backend.DbManagerFactory;
import com.javaproject.malki.takeandgo.model.entities.Branch;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PresentBranches extends Fragment {
    private SearchView presentBranchesSearchBranches;
    private ListView presentBranchesBranchesList;
    private LinearLayout presentBranchesBranchView;
    private LinearLayout presentBranchesCarsView;
    //private List<Branch> branches;
    private Branch selectedBranch;
    private List<String> myStringList;
    private List<String> myBaseStringList;
    private List<Branch> branches;
    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2018-01-17 19:23:58 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        presentBranchesSearchBranches = (SearchView)getActivity().findViewById( R.id.present_branches_SearchBranches );
        presentBranchesBranchesList = (ListView)getActivity().findViewById( R.id.present_branches_branchesList );
        presentBranchesBranchView = (LinearLayout)getActivity().findViewById( R.id.present_branches_branchView );
        presentBranchesCarsView = (LinearLayout)getActivity().findViewById( R.id.present_branches_carsView );
    }

    private List<String> getLocations(List<Branch> branches)
    {
        List<String> lst = new ArrayList<String>();
        for (Branch b : branches)
        {
            //add branches city
            if (!myBaseStringList.contains(b.getBranchAddress().getCity().toString()) )
                lst.add(String.format("%d", b.getBranchAddress().getCity()));
        }
        return lst;
    }

    public PresentBranches() {
        // Required empty public constructor
    }

    private void searchView()
    {
        myStringList = new ArrayList<>();
        myStringList.addAll(myBaseStringList);
        ListView listView = presentBranchesBranchesList;

        final ArrayAdapter listAdapter =
                new ArrayAdapter(getActivity(), R.layout.showbranch, branches) {
                    @Override
                    public Filter getFilter() {
                        return new Filter() {
                            @Override
                            protected FilterResults performFiltering(CharSequence constraint) {

                                FilterResults filterResults = new FilterResults();

                                myStringList.clear();
                                for (String s : myBaseStringList) {
                                    if (s.startsWith(constraint.toString()))
                                        myStringList.add(s);
                                }


                                return filterResults;
                            }

                            @Override
                            protected void publishResults(CharSequence constraint, FilterResults results) {
                                //  clear();
                                //  addAll((List<String>)results.values);
                                notifyDataSetChanged();
                            }
                        };
                    }
                };

        listView.setAdapter(listAdapter);

        SearchView searchView = presentBranchesSearchBranches;

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listAdapter.getFilter().filter(newText);
                listAdapter.notifyDataSetChanged();

                return true;
            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //initiate controls
        findViews();
        //init list
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                branches = DbManagerFactory.getManager().GetBranches();
                return null;
            }
        }.execute();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_present_branches, container, false);
    }

}
