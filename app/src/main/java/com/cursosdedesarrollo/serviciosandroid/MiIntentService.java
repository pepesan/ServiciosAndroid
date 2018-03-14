package com.cursosdedesarrollo.serviciosandroid;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by pepesan on 14/3/18.
 */

public class MiIntentService extends IntentService {
    public static final String ACTION_PROGRESO =
            "com.cursosdedesarrollo.intent.action.PROGRESO";
    public static final String ACTION_FIN =
            "com.cursosdedesarrollo.intent.action.FIN";

    public MiIntentService() {
        super("MiIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        int iter = intent.getIntExtra("iteraciones", 0);

        for(int i=1; i<=iter; i++) {
            tareaLarga();

            //Comunicamos el progreso
            Intent bcIntent = new Intent();
            bcIntent.setAction(ACTION_PROGRESO);
            int valor=i*10;
            Log.d("app","Valor: "+valor);
            bcIntent.putExtra("progreso", valor);
            sendBroadcast(bcIntent);
        }

        Intent bcIntent = new Intent();
        bcIntent.setAction(ACTION_FIN);
        sendBroadcast(bcIntent);
    }

    private void tareaLarga()
    {
        try {
            Thread.sleep(1000);
        } catch(InterruptedException e) {}
    }

}
