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

public class AlarmService extends Service {

    String title = "";
    int challengeType = -1;
    int alarmId = 0;

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
        Log.d("AlarmService", "onStartCommand triggered");
        title = intent.getStringExtra("TITLE");
        // Extract the alarm title and challenge type from the intent
        if (intent != null && intent.hasExtra("CHALLENGE")) {
            challengeType = intent.getIntExtra("CHALLENGE", 0);
        }
        Log.d("c1", "c1: " + challengeType);
        Log.d("t1", "t1: " + title);

        // replace these 3 lines with sendNotification(title); to revert changes
        NotificationThread2 notificationThread2 = new NotificationThread2(title);
        Thread thread = new Thread(notificationThread2);
        thread.start();

        return START_STICKY;
    }

    private void sendNotification(String title) {
        // Create an intent to open AlarmRingActivity when the notification is tapped
        Intent intent = new Intent(AlarmService.this, AlarmRingActivity.class);
        intent.putExtra("ALARM_ID", alarmId);
        intent.putExtra("CHALLENGE", challengeType);

        PendingIntent pendingIntent = PendingIntent.getActivity(AlarmService.this,
                alarmId + 1000, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Build the notification with title, text, and an icon
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Alarm")
                .setContentText(title)
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());// Show the notification
    }

    private void createNotificationChannel() {
        // For API 26 and above, create a notification channel

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