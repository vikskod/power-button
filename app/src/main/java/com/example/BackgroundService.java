package com.example;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.example.PowerButtonReceiver;
import com.example.Storage;
import com.womensaftyalert.R;

public class BackgroundService extends Service {

    private static long time = 0;
    private BroadcastReceiver mReceiver;
    public static int mId = 123;

    @Override
    public void onCreate() {
        super.onCreate();
        // register receiver that handles screen on and screen off logic
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new PowerButtonReceiver();
        registerReceiver(mReceiver, filter);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setVisibility(Notification.VISIBILITY_SECRET)
                        .setPriority(NotificationCompat.PRIORITY_MIN)
                        .setContentTitle("PowerButtonCounter läuft")
                        .setContentText("Count: " + Storage.getInstance(this).getCount());

        startForeground(mId, mBuilder.build());
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean screenOn = intent.getBooleanExtra("screen_state", false);
        if (!screenOn) {
            time = System.currentTimeMillis();
        } else {
            if(System.currentTimeMillis() - time < 1500){
                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(new long[] {0, 100}, -1);
                Storage.getInstance(this).increment();
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setVisibility(Notification.VISIBILITY_SECRET)
                        .setPriority(NotificationCompat.PRIORITY_MIN)
                        .setContentTitle("PowerButtonCounter")
                        .setContentText("Count: " + Storage.getInstance(this).getCount());
                mNotificationManager.notify(mId, mNotifyBuilder.build());
            }
            time = 0;
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        unregisterReceiver(mReceiver);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(),
                1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartServicePendingIntent);

        super.onTaskRemoved(rootIntent);
    }
}