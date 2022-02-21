package com.mc2022.template;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button serviceButton;
    TextView textView;
    BroadcastReceiver broadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        serviceButton = findViewById(R.id.serviceStartStopButton);

        //serviceButton.setBackgroundColor(Color.GREEN);
        serviceButton.setOnClickListener(this);
    }




    @Override
    public void onClick(View view) {

        if(view==serviceButton){
            if(serviceButton.getText().toString().equals(getString(R.string.StartServiceButton))) {

                String stringUrl = "https://petwear.in/mc2022/news/news_0.json";
                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if(!isMyServiceRunning(DownloadService.class)) {
                    if (networkInfo != null && networkInfo.isConnected()) {

                        startService(new Intent(this, DownloadService.class));
                        serviceButton.setText(R.string.StopServiceButton);
                        serviceButton.setBackgroundColor(Color.RED);
                    }
                }else {
                    stopService(new Intent(this, DownloadService.class));
                    serviceButton.setText(R.string.StartServiceButton);
                    serviceButton.setBackgroundColor(Color.parseColor("#00AA00"));
                }
            }
            else if(serviceButton.getText().toString().equals(getString(R.string.StopServiceButton))) {
                if(isMyServiceRunning(DownloadService.class)) {
                    stopService(new Intent(this, DownloadService.class));
                    serviceButton.setText(R.string.StartServiceButton);
                    serviceButton.setBackgroundColor(Color.parseColor("#00AA00"));
                }
                else{
                    startService(new Intent(this, DownloadService.class));
                    serviceButton.setText(R.string.StopServiceButton);
                    serviceButton.setBackgroundColor(Color.RED);
                }
            }
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        }
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    private class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO: This method is called when the BroadcastReceiver is receiving
            // an Intent broadcast.

            if(Intent.ACTION_POWER_DISCONNECTED.equals(intent.getAction()) &&
                    Intent.ACTION_BATTERY_OKAY.equals(intent.getAction())){
                Toast.makeText(context, "Power Disconnected and Battery Okay. Starting Service!", Toast.LENGTH_SHORT).show();
                //Intent serIntent = new Intent(context, DownloadService.class);
                context.startService(intent);
            }

            //throw new UnsupportedOperationException("Not yet implemented");
        }
    }

}