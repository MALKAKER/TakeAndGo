package com.javaproject.malki.takeandgo.controller;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.javaproject.malki.takeandgo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AvailableCars extends Fragment {


    public AvailableCars() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_available_cars, container, false);
    }

}
