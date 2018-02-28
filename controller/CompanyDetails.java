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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.javaproject.malki.takeandgo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompanyDetails extends Fragment implements View.OnClickListener {

    private TextView companyDetailsPhone;
    private TextView companyDetailsEmail;
    private TextView companyDetailsSite;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2018-01-17 17:26:07 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        companyDetailsPhone = (TextView)getActivity().findViewById( R.id.companyDetailsPhone );
        companyDetailsEmail = (TextView)getActivity().findViewById( R.id.companyDetailsEmail );
        companyDetailsSite = (TextView)getActivity().findViewById( R.id.companyDetailsSite );
    }

    public CompanyDetails() {
        // Required empty public constructor
        
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.companyDetailsPhone:
                dial_company();
                break;
            case R.id.companyDetailsEmail:
                sendEmail();
                break;
            case R.id.companyDetailsSite:
                goToWebSite();
                break;
        }
    }

    private void goToWebSite() {
        //did in the xml!!!!!!
    }

    private void dial_company() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+companyDetailsPhone.getText().toString()));
        startActivity(intent);
    }

    protected void sendEmail() {

        String[] TO = {"malkiaker@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.setType("plain/text");
        try {
            Toast.makeText(getActivity(),
                    "Send Email...", Toast.LENGTH_SHORT).show();
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(),
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
    public void composeEmail(String[] addresses, String subject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_company_details, container, false);
    }

}
