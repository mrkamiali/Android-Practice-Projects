package com.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by kamranali on 02/02/2018.
 */

public class myService extends Service {
    class myThread implements Runnable{

        private  myService context;
        int service_id ;
        myThread(myService myservice, int serviceid){
            this.service_id = serviceid;
            this.context = myservice;
        }
        @Override
        public void run() {
            int i = 0;

                    while(i<100){
                        i++;
                        Toast.makeText(myService.this, String.valueOf(i), Toast.LENGTH_SHORT).show();

                    }
            stopSelf(service_id);

        }
              //

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this,"Service started",Toast.LENGTH_LONG).show();
    new myThread(this,startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this,"Service Stopped",Toast.LENGTH_LONG).show();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
