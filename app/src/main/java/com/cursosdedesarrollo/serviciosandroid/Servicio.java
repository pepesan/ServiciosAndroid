package com.cursosdedesarrollo.serviciosandroid;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by pepesan on 13/3/18.
 */

public class Servicio extends Service {

    public static final String ACTION_PROGRESO =
            "com.cursosdedesarrollo.intent.action.PROGRESO";
    public static final String ACTION_FIN =
            "com.cursosdedesarrollo.intent.action.FIN";

    @Nullable
    /** indicates how to behave if the service is killed */
    int mStartMode;

    /** interface for clients that bind */
    IBinder mBinder;

    /** indicates whether onRebind should be used */
    boolean mAllowRebind;

    Servicio(){
        /*

            Service.START_STICKY

            Service is restarted if it gets terminated. Intent data passed to the onStartCommand method is null. Used for services which manages their own state and do not depend on the Intent data.

            Service.START_NOT_STICKY

            Service is not restarted. Used for services which are periodically triggered anyway. The service is only restarted if the runtime has pending startService() calls since the service termination.

            Service.START_REDELIVER_INTENT

            Similar to Service.START_STICKY but the original Intent is re-delivered to the onStartCommand method.
         */
        this.mStartMode=Service.START_NOT_STICKY;
    }
    /** Called when the service is being created. */
    @Override
    public void onCreate() {
        Log.d("App","onCreate");
    }

    /** The service is starting, due to a call to startService() */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        Log.d("App","onStartCommand");
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        int iter =10;
        for(int i=1; i<=iter; i++) {
            tareaLarga();
            Log.d("app","Valor: "+i);
        }
        return mStartMode;
    }

    /** A client is binding to the service with bindService() */
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("App","onBind");
        return mBinder;
    }

    private void tareaLarga()
    {
        try {
            Thread.sleep(1000);
        } catch(InterruptedException e) {}
    }

    /** Called when all clients have unbound with unbindService() */
    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("App","onUnbind");
        return mAllowRebind;
    }

    /** Called when a client is binding to the service with bindService()*/
    @Override
    public void onRebind(Intent intent) {
        Log.d("App","onRebind");
    }

    /** Called when The service is no longer used and is being destroyed */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("App","onDestroy");
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }
}
