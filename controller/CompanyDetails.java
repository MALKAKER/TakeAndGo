package com.javaproject.malki.takeandgo.controller;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Fragment;
import android.widget.Toast;

import com.javaproject.malki.takeandgo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompanyDetails extends Fragment {


    public CompanyDetails() {
        // Required empty public constructor
        String[] s = {"malkiaker@gmail.com"};
        //composeEmail(s,"");
        sendEmail();
    }
    public void composeEmail(String[] addresses, String subject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);

    }
    protected void sendEmail() {

        String[] TO = {"malkiaker@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.setType("plain/text");
        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            Toast.makeText(getActivity(),
                    "Send Email...", Toast.LENGTH_SHORT).show();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(),
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_company_details, container, false);
    }

}
