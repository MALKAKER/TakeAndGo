package com.javaproject.malki.takeandgo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.javaproject.malki.takeandgo.controller.ConstValues;

/**
 * Created by malki on 23-Jan-18.
 */

public class ReceiveOnBoard extends BroadcastReceiver {
    public ReceiveOnBoard() {
        super();
    }

    @Override
    public IBinder peekService(Context myContext, Intent service) {
        return super.peekService(myContext, service);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().matches(ConstValues.ON_BOARD_SERVICE)) {
            //TODO, UPDATE THE DISPLAY OF MY CAR
        }//TODO, ADD SERVICE FOR AVAILABLE CARS
    }
}
