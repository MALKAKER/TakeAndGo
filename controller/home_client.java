package com.javaproject.malki.takeandgo.controller;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.javaproject.malki.takeandgo.R;
import com.javaproject.malki.takeandgo.model.backend.DbManagerFactory;
import com.javaproject.malki.takeandgo.model.entities.Branch;
import com.javaproject.malki.takeandgo.model.entities.Car;
import com.javaproject.malki.takeandgo.model.entities.Client;
import com.javaproject.malki.takeandgo.model.receiver.AvailableCarsReceiver;
import com.javaproject.malki.takeandgo.model.service.RoutineUpdateService;
import com.javaproject.malki.takeandgo.model.service.UpdateCars;

import java.util.List;

public class home_client extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //final Dialog dialog = new Dialog(this); // Context, this, etc.
    private EditText enterUser;
    private EditText enterPassword;
    private Button register;
    private Button signIn;
    private Button clear;
    private CheckBox isSave;

    //intent filter
    private IntentFilter mIntentFilter;
    private BroadcastReceiver mReceiver ;
    //initiate the fragment activators
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_client);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String message = getString(R.string.No_update);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = getString(R.string.refreshing);
                Snackbar.make(fab, message, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                UpdateCarList();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //generate intent filter for the 2 types of update and on-board service
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(ConstValues.ON_ROUTINE_UPDATE_SERVICE);
        mIntentFilter.addAction(ConstValues.ON_UPDATE_SERVICE);
        //on board service
        mIntentFilter.addAction(ConstValues.ON_BOARD_SERVICE);

        Intent serviceIntent = new Intent(this, RoutineUpdateService.class);
        startService(serviceIntent);

        //initiate the view with my car fragment, I want that happen only once
        //when the client open the app in the first time a day, when he returns to the app- I want him to return to the last
        //option he was in.
        MyCar fragment = new MyCar();
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);//add(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        //generate receiver
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String doIt = intent.getAction();
                switch (doIt)
                {
                    case ConstValues.ON_ROUTINE_UPDATE_SERVICE:
                        if(intent.getBooleanExtra(ConstValues.IS_AVAILABLE_CARS,false))
                        {
                            String message = getString(R.string.New_cars_are_available);
                            Snackbar.make(fab, message, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                            UpdateCarList();
                        }
                        break;
                    case ConstValues.ON_UPDATE_SERVICE:
                        // do nothing -> not necessary
                        break;
                    case ConstValues.ON_BOARD_SERVICE:
                        // not necessary, it other optional way to initiate mycar...
                        break;
                }
            }
        };
    }
    /*
    * if their are a new available cars while the user in the activity of show cars
    * the app update the list
    * */
    private void UpdateCarList() {
        Fragment f = getFragmentManager().findFragmentById(R.id.fragment_container);
        if(f != null && f instanceof PresentBranches) {
            PresentBranches presentBranches = (PresentBranches) f;
            presentBranches.showCars();
        }
        else if(f != null && f instanceof AvailableCars)
        {
            AvailableCars availableCars = (AvailableCars) f;
            //todo: update the list of available cars
        }

    }

    //kind of exstras for the fragment
    Bundle bundle = new Bundle();
    @Override
    protected void onStart() {
        super.onStart();
        //I want the dialog to be open daily while the user
        // return to the app so the dialog is opened here
        openDialog();
        bundle.putString(ConstValues.User, enterUser.getText().toString());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_client, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //kind of exstras for the fragment
        Bundle bundle = new Bundle();
        bundle.putString(ConstValues.User, enterUser.getText().toString());

        if (id == R.id.nav_contact) {
           CompanyDetails fragment = new CompanyDetails();
            fragment.setArguments(bundle);
           FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
           fragmentTransaction.replace(R.id.fragment_container, fragment);//add(R.id.fragment_container, fragment);
           fragmentTransaction.addToBackStack(null);
           fragmentTransaction.commit();
       } else if (id == R.id.nav_branches) {
           PresentBranches fragment = new PresentBranches();
            fragment.setArguments(bundle);
           FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
           fragmentTransaction.replace(R.id.fragment_container, fragment);//add(R.id.fragment_container, fragment);
           fragmentTransaction.addToBackStack(null);
           fragmentTransaction.commit();
        } else if (id == R.id.nav_availableCars) {
           AvailableCars fragment = new AvailableCars();
            fragment.setArguments(bundle);
           FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
           fragmentTransaction.replace(R.id.fragment_container, fragment);//add(R.id.fragment_container, fragment);
           fragmentTransaction.addToBackStack(null);
           fragmentTransaction.commit();
       }else if (id == R.id.nav_myCar) {
            MyCar fragment = new MyCar();
            fragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);//add(R.id.fragment_container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_logOut) {
           //log out from the app
           logOutFromTheApp();

       } else if (id == R.id.nav_share) {
           //sharing the app with apps that can share text
           Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
           sharingIntent.setType("text/plain");
           String shareBody = "Hey there I am using Car2Go!";
           sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Join Car2Go");
           sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
           startActivity(Intent.createChooser(sharingIntent, "Share via"));
        } else if (id == R.id.nav_send) {
           //sharing the app with apps that can share text
           Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
           sharingIntent.setType("text/plain");
           String shareBody = "Hey there I am using Car2Go!";
           sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Join Car2Go");
           sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
           startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logOutFromTheApp() {
        //dialog asking if the user wants to leave the app
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage(R.string.Do_you_want_to_exit);
        //ok - exit
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                finish();
                Intent i=new Intent();
                i.putExtra("finish", true);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
                //startActivity(i);
                finish();

            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alert=builder.create();
        alert.show();
    }
    /*
    * register receiver
    * */
    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
    }
    /*
    * release receiver
    * */
    @Override
    protected void onPause() {
        unregisterReceiver(mReceiver);
        super.onPause();
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        log in activity
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    /*clear shared preference*/
    private void clearSharedPreferences()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        enterUser.setText("");
        enterPassword.setText("");
        //Toast.makeText(this, "clear Preferences", Toast.LENGTH_SHORT).show();
    }

    /*load shared preference*/
    private void loadSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        if (sharedPreferences.contains(ConstValues.User))
        {
            this.enterUser.setText( sharedPreferences.getString(ConstValues.User, null));
        }
        if (sharedPreferences.contains(ConstValues.Password))
        {
            enterPassword.setText(sharedPreferences.getString(ConstValues.Password, null));
        }
    }

    /* save shared preference*/
    private void saveSharedPreferences()
    {
        try {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String name = enterUser.getText().toString();
            String pass = enterPassword.getText().toString();
            editor.putString(ConstValues.User, name);
            editor.putString(ConstValues.Password, pass);
            editor.commit();
        } catch (Exception ex)
        {
//            Toast.makeText(this, "failed to save Preferences", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * CheckPassword, confirms user password
     * @param name
     * @param password1
     * @param dialog
     * @throws Exception
     */
    private void CheckPassword(final String name, final String password1, final Dialog dialog) throws Exception {
        new AsyncTask<Void, Void, Boolean>()
        {
            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if(aBoolean)
                {
                    try {
                        Toast.makeText(getApplicationContext(),getString(R.string.welcome) ,Toast.LENGTH_LONG).show();
                        //if the user wants to save the password save shared preference
                        if(isSave.isChecked())
                        {
                            saveSharedPreferences();
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    //if the user name and the password are correct, the dialog is closed
                    dialog.dismiss();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Not a user", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            protected Boolean doInBackground(Void... params) {
                //wait to the debugger
                if(android.os.Debug.isDebuggerConnected())
                    android.os.Debug.waitForDebugger();
                Client c = null;
                try {
                    //get client name
                    c = DbManagerFactory.getManager().GetClient(name);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //If the input password is not correct
                if(c == null || !c.getPassword().equals(password1))
                    return false;
                return true;
            } }.execute();

    }

    /**
     * RegisterActivity, go to the register activity
     */
    private void RegisterActivity()
    {
        Intent intent = new Intent(this.getBaseContext(),Register.class);
        startActivity(intent);
    }

    /**
     * SignInActivity, main function to control the sign/log in activity
     * @param d
     */
    private void SignInActivity(Dialog d)
    {
        try
        {
            //Toast.makeText(this, "blah", Toast.LENGTH_SHORT).show();
            String user = this.enterUser.getText().toString();
            String pass = this.enterPassword.getText().toString();
            //checks if the editText is full
            if (!CheckNoInput(user, pass))
            {

            }
            //check if the password is correct
            else
            {
                CheckPassword(user, pass, d);
            }

        }
        catch (Exception e)
        {
            Toast toast = Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG );
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
        }

    }

    private Boolean CheckNoInput(String user, String pass) {
        if(user.trim().equals(""))
        {
            Toast toast = Toast.makeText(getApplicationContext(),R.string.Enter_user_name, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
            return false;
        }
        else if (pass.equals(""))
        {
            Toast toast = Toast.makeText(getApplicationContext(),R.string.Enter_password_error, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
            return  false;
        }
        return true;
    }

    /*
    * opens login dialog
    * */
    public void openDialog() {
        final Dialog dialog = new Dialog(this);

        //define dialog to ignore backButton
        dialog.setCancelable(false);

        //initiate dialog layout
        dialog.setContentView(R.layout.login_register_dialog);

        //the dialog's title
        dialog.setTitle(R.string.Welcome_to_Take_And_Go);


        register = (Button)dialog.findViewById( R.id.register );
        signIn = (Button)dialog.findViewById( R.id.signIn );
        clear = (Button)dialog.findViewById(R.id.clear);
        isSave = (CheckBox)dialog.findViewById(R.id.isSave);
        enterUser = (EditText)dialog.findViewById( R.id.enterUser );
        enterPassword = (EditText)dialog.findViewById( R.id.enterPassword );
        //shared preference
        loadSharedPreferences();
        //register button
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity();
            }
        });
        //sign in button
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInActivity(dialog);
            }
        });
        
        //clear preferance
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterUser.setText("");
                enterPassword.setText("");
            }
        });

        dialog.show();
    }

}
