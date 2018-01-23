package com.javaproject.malki.takeandgo;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.javaproject.malki.takeandgo.controller.ConstValues;
import com.javaproject.malki.takeandgo.controller.PresentBranches;
import com.javaproject.malki.takeandgo.model.backend.ConstCars;

/**
 * Created by malki on 22-Jan-18.
 */

public class CarOnBoard extends IntentService {

    boolean isRun = false;
    //user id and car number
    String user;
    String car;
    final String TAG = "Boarded_Service";
    private long order;

    public CarOnBoard() {
        super("board");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onCreate() {
        super.onCreate();
        isRun = true;
        Notification.Builder nBuilder = new Notification.Builder(getBaseContext());
        nBuilder.setSmallIcon(R.drawable.large_logo);
        nBuilder.setContentTitle("On Board");
        nBuilder.setContentText("Car Number " + car + " is rented.");
        Notification notification = nBuilder.build();
        startForeground(1234, notification);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Bundle b = intent.getExtras();
        user = b.getString(ConstCars.OrderConst.CLIENT_NUMBER);
        car = b.getString(ConstCars.OrderConst.CAR_NUMBER);
        order = b.getLong(ConstCars.OrderConst.ORDER_NUMBER);
        while (true)
        {
            try {
                Thread.sleep(1000);
            }catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            Log.d(TAG, "Foreground Service print ...");
        }
    }
    //constructor, initiate the service details
//    CarOnBoard(String us, String car)
//    {
//        this.user = us;
//        this.car = car;
//        this.isRun = true;
//    }
//    String serviceInfo()
//    {
//        return "On Boarded Car # " + car;
//    }
//    @Override
//    public IBinder onBind(Intent intent)
//    {
//        return null;
//    }
//    @Override
//    public void onCreate()
//    {
//        super.onCreate();
//        Log.d(TAG, serviceInfo() + " onCreate ...");
//        isRun = true;
//    }
//    @Override
//    public void onDestroy() {
//        Log.d(TAG, serviceInfo() + " onDestroy ...");
//        isRun = false;
//        super.onDestroy();
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId)
//    {
//        Bundle b = intent.getExtras();
//        user = b.getString(ConstCars.OrderConst.CLIENT_NUMBER);
//        car = b.getString(ConstCars.OrderConst.CAR_NUMBER);
//        isRun = true;
//        Notification.Builder nBuilder = new Notification.Builder(getBaseContext());
//        nBuilder.setSmallIcon(R.drawable.large_logo);
//        nBuilder.setContentTitle("On Board");
//        nBuilder.setContentText("Car Number " + car + " is rented.");
//        Notification notification = nBuilder.build();
//        startForeground(startId, notification);
//        Log.d(TAG, serviceInfo() + " onStartCommand start ...");
////        Thread thread = new Thread()
////        {
////            @Override
////            public void run() {
////                while (isRun)
////                {
////
////                    Log.d(TAG, serviceInfo() + " on board ...");
////                }
////            }
////        };
////        thread.start();
//        Log.d(TAG, serviceInfo() + " onStartCommand end ...");
//        return Service.START_REDELIVER_INTENT;
//    }
}
