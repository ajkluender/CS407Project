package com.cs407.cs407finalproject;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.cs407.cs407finalproject.data.Alarm;
import com.cs407.cs407finalproject.data.AlarmDBHelper;

public class AlarmService extends Service {

    Alarm alarm;
    String title = "";
    int alarmId = 0;
    int challengeType = -1;
    boolean isRecurring = false;

    private static final String CHANNEL_ID = "AlarmServiceChannel";

    @Override
    public void onCreate() {
        super.onCreate();

        // replace these 3 lines with createNotificationChannel(); to revert changes
        NotificationThread1 notificationThread1 = new NotificationThread1();
        Thread thread = new Thread(notificationThread1);
        thread.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.hasExtra("ALARM")) {
            alarm = (Alarm) intent.getSerializableExtra("ALARM");
        }
        if (intent != null && intent.hasExtra("TITLE")) {
            title = intent.getStringExtra("TITLE");
        }
        if (intent != null && intent.hasExtra("ALARM_ID")) {
            alarmId = intent.getIntExtra("ALARM_ID", -1);
        }
        if (intent != null && intent.hasExtra("CHALLENGE")) {
            challengeType = intent.getIntExtra("CHALLENGE", 0);
        }
        if (intent != null && intent.hasExtra("RECURRING")) {
            isRecurring = intent.getBooleanExtra("RECURRING", false);
        }

        // replace these 3 lines with sendNotification(title); to revert changes
        NotificationThread2 notificationThread2 = new NotificationThread2(title);
        Thread thread = new Thread(notificationThread2);
        thread.start();

        return START_STICKY;
    }

    private void sendNotification(String title) {
        Intent intent = new Intent(AlarmService.this, AlarmRingActivity.class);
        intent.putExtra("ALARM", alarm);
        intent.putExtra("TITLE", title);
        intent.putExtra("ALARM_ID", alarmId);
        intent.putExtra("CHALLENGE", challengeType);
        intent.putExtra("RECURRING", isRecurring);

        PendingIntent pendingIntent = PendingIntent.getActivity(AlarmService.this,
                alarmId + 10000, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Alarm")
                .setContentText(title)
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

    private void createNotificationChannel() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class NotificationThread1 implements Runnable {
        @Override
        public void run() {
            createNotificationChannel();
        }
    }

    class NotificationThread2 implements Runnable {

        private String title;

        public NotificationThread2(String title) {
            this.title = title;
        }

        @Override
        public void run() {
            sendNotification(title);
        }
    }
}