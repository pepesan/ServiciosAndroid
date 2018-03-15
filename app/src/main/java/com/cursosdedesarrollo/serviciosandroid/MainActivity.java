package com.cursosdedesarrollo.serviciosandroid;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Intent i;
    Intent intent;
    public static final String mBroadcastStringAction = "com.cursosdedesarrollo.broadcast.string";
    public static final String mBroadcastIntegerAction = "com.cursosdedesarrollo.broadcast.integer";
    public static final String mBroadcastArrayListAction = "com.cursosdedesarrollo.broadcast.arraylist";
    private IntentFilter mIntentFilter;
    ProgressBar  pbarProgreso;
    private BroadcastReceiver progressReceiver=new  BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(MiIntentService.ACTION_PROGRESO)) {
                int prog = intent.getIntExtra("progreso", 0);
                pbarProgreso.setProgress(prog);
            }
            else if(intent.getAction().equals(MiIntentService.ACTION_FIN)) {
                Toast.makeText(MainActivity.this, "Tarea finalizada!", Toast.LENGTH_SHORT).show();
            }
        }
    };
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(MainActivity.this, "Broadcast From Service:", Toast.LENGTH_SHORT).show();
            Log.d("app", "Broadcast From Service: \n");
            if (intent.getAction().equals(mBroadcastStringAction)) {
                Toast.makeText(MainActivity.this, intent.getStringExtra("Data"), Toast.LENGTH_SHORT).show();
                Log.d("app", "Broadcast From Service: \n" +intent.getStringExtra("Data") + "\n\n");
            } else if (intent.getAction().equals(mBroadcastIntegerAction)) {
                Toast.makeText(MainActivity.this, ""+intent.getIntExtra("Data", 0), Toast.LENGTH_SHORT).show();
                        Log.d("app", "Broadcast From Service: \n" +intent.getIntExtra("Data", 0) + "\n\n");

            } else if (intent.getAction().equals(mBroadcastArrayListAction)) {
                Toast.makeText(MainActivity.this, ""+intent.getStringArrayListExtra("Data").toString(), Toast.LENGTH_SHORT).show();
                Log.d("app", "Broadcast From Service: \n" +intent.getStringArrayListExtra("Data").toString() + "\n\n");
                Intent stopIntent = new Intent(MainActivity.this,
                        BroadcastService.class);
                stopService(stopIntent);
            }
        }
    };
    private class MiTareaAsincrona extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {

            for(int i=1; i<=10; i++) {
                tareaLarga();

                publishProgress(i*10);

                if(isCancelled())
                    break;
            }

            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int progreso = values[0].intValue();

            pbarProgreso.setProgress(progreso);
        }

        @Override
        protected void onPreExecute() {
            pbarProgreso.setMax(100);
            pbarProgreso.setProgress(0);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result)
                Toast.makeText(MainActivity.this, "Tarea finalizada!",
                        Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(MainActivity.this, "Tarea cancelada!",
                    Toast.LENGTH_SHORT).show();
        }
        private void tareaLarga()
        {
            try {
                Thread.sleep(1000);
            } catch(InterruptedException e) {}
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        pbarProgreso= findViewById(R.id.progressBar);
        setSupportActionBar(toolbar);
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(mBroadcastStringAction);
        mIntentFilter.addAction(mBroadcastIntegerAction);
        mIntentFilter.addAction(mBroadcastArrayListAction);

        Intent serviceIntent = new Intent(this, BroadcastService.class);
        startService(serviceIntent);

    }
    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MiIntentService.ACTION_PROGRESO);
        filter.addAction(MiIntentService.ACTION_FIN);
        registerReceiver(progressReceiver, filter);
        registerReceiver(mReceiver, mIntentFilter);
    }
    @Override
    protected void onPause() {
        unregisterReceiver(progressReceiver);
        unregisterReceiver(mReceiver);
        super.onPause();
    }
    // Method to start the service
    public void startService() {
        startService(new Intent(getBaseContext(), Servicio.class));
    }

    // Method to stop the service
    public void stopService() {
        stopService(new Intent(getBaseContext(), Servicio.class));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_start) {
            i= new Intent(this, Servicio.class);
            // potentially add data to the intent
            i.putExtra("KEY1", "Value to be used by the service");
            this.startService(i);
            return true;
        }
        if (id == R.id.action_stop) {
            stopService(i);
            return true;
        }

        if (id == R.id.action_start_intent_service) {
            intent = new Intent(MainActivity.this, MiIntentService.class);
            intent.putExtra("iteraciones", 10);
            startService(intent);
            return true;
        }
        if (id == R.id.action_stop_intent_service) {
            stopService(intent);
            return true;
        }
        if (id == R.id.action_start_asynctask) {
            MiTareaAsincrona tarea2 = new MiTareaAsincrona();
            tarea2.execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
