package com.javaproject.malki.takeandgo.model.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import com.javaproject.malki.takeandgo.controller.ConstValues;
import com.javaproject.malki.takeandgo.model.backend.DbManagerFactory;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class RoutineUpdateService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.javaproject.malki.takeandgo.model.service.action.FOO";
    private static final String ACTION_BAZ = "com.javaproject.malki.takeandgo.model.service.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.javaproject.malki.takeandgo.model.service.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.javaproject.malki.takeandgo.model.service.extra.PARAM2";

    public RoutineUpdateService() {
        super("RoutineUpdateService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        boolean isRun = true;

        while (isRun)
        {
            /* Update UI: Service is Running */
            try {
                Thread.sleep(10000);
                Intent broadcastIntent = new Intent();
                Boolean isNewCars = DbManagerFactory.getManager().isClosedOrder();;
                broadcastIntent.setAction(ConstValues.ON_ROUTINE_UPDATE_SERVICE);
                broadcastIntent.putExtra(ConstValues.IS_AVAILABLE_CARS, isNewCars);
                sendBroadcast(broadcastIntent);
            } catch (InterruptedException e) {
                /* Sending error message back to activity */
                isRun = false;
                e.printStackTrace();
            }
        }
    }


}
