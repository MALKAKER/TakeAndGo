package com.javaproject.malki.takeandgo.model.receiver;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * receiver to receive the updates from the
 * server which is searching for new available cars
 * Created by malki on 08-Feb-18.
 */

public class AvailableCarsReceiver extends ResultReceiver {
    private Receiver mReceiver;

    public AvailableCarsReceiver(Handler handler) {
        super(handler);
    }

    public void setReceiver(Receiver receiver) {
        mReceiver = receiver;
    }

    public interface Receiver {
        public void onReceiveResult(int resultCode, Bundle resultData);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (mReceiver != null) {
            mReceiver.onReceiveResult(resultCode, resultData);
        }
    }
}
