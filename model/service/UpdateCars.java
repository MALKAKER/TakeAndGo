/**/package com.javaproject.malki.takeandgo.model.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.javaproject.malki.takeandgo.model.backend.DbManagerFactory;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class UpdateCars extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.javaproject.malki.takeandgo.model.service.action.FOO";
    private static final String ACTION_BAZ = "com.javaproject.malki.takeandgo.model.service.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.javaproject.malki.takeandgo.model.service.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.javaproject.malki.takeandgo.model.service.extra.PARAM2";

    //status
    public static final int AVAILABILITY_STATUS= 1;
    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;
    public static final String POSITIVE = "positive";
    //to the debugger
    private static final String TAG = "UpdateService";


    public UpdateCars() {
        super("UpdateCars");
    }



    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "Update Service Started!");

        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        Boolean isNewAvailableCars = false;
        /* Update UI: Service is Running */
        receiver.send(STATUS_RUNNING, Bundle.EMPTY);
        try {
            Thread.sleep(10000);
            isNewAvailableCars = DbManagerFactory.getManager().isClosedOrder();
            Bundle bundle = new Bundle();
            bundle.putBoolean(POSITIVE, isNewAvailableCars);
            receiver.send(AVAILABILITY_STATUS,bundle);
        } catch (InterruptedException e) {
                /* Sending error message back to activity */
            Bundle bundle = new Bundle();
            bundle.putString(Intent.EXTRA_TEXT, e.toString());
            receiver.send(STATUS_ERROR, bundle);
            e.printStackTrace();
        }


    }


}
