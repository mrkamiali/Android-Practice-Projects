package com.kisok.request4feedback;

import android.app.Application;

import com.kisok.request4feedback.receiver.ConnectivityReceiver;

/**
 * Created by kamranali on 17/06/2017.
 */

public class MyApplication extends Application {
    private static MyApplication mInstance;


    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }
}
