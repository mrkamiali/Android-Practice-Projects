package com.kamranali.firebasemodel;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by kamranali on 21/02/2017.
 */

public class AppController extends Application{
    private AppController instance;

    public AppController getInstance() {
        if (instance==null){
            instance = new AppController();
        }
        return instance;
    }
    public static boolean checkConectivity(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

}
