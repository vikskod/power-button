package com.example;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.womensaftyalert.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService(new Intent(this, BackgroundService.class));
        update();
    }

    public void update(){
        ((TextView) findViewById(R.id.count)).setText(Integer.toString(Storage.getInstance(this).getCount()));
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setVisibility(Notification.VISIBILITY_SECRET)
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setContentTitle("PowerButtonCounter")
                .setContentText("Count: " + Storage.getInstance(this).getCount());
        mNotificationManager.notify(BackgroundService.mId, mNotifyBuilder.build());
    }

    public void reset(View view){
        Storage.getInstance(this).reset();
        update();
    }

    public void increment(View view){
        Storage.getInstance(this).increment();
        update();
    }

    public void decrement(View view){
        Storage.getInstance(this).decrement();
        update();
    }

    public void stop(View view){
        stopService(new Intent(this, BackgroundService.class));
        finish();
    }

    @Override
    public void onResume(){
        super.onResume();
        update();
    }

    @Override
    public void onRestart(){
        super.onRestart();
        update();
    }

}
