package com.monicaivan.notificationsdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "channel_id";
    private static final String KEY_TEXT_REPLY = "key_text_reply";
    private static final int CONVERSATION_REQUEST_CODE = 123;
    private NotificationManagerCompat notificationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        createNotificationChannel();
        notificationManager = NotificationManagerCompat.from(this);

        findViewById(R.id.basic_notification_button).setOnClickListener(view -> createBasicNotification());
        findViewById(R.id.long_text_notification_button).setOnClickListener(view -> createBasicNotificationLongText());
        findViewById(R.id.intent_notification_button).setOnClickListener(view -> createIntentNotification());
        findViewById(R.id.progress_notification_button).setOnClickListener(view -> createProgressBarNotification());
    }

    ///Basic notifications
    private void createBasicNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle("Basic Notification")
                .setContentText("One line text.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notificationManager.notify(1, builder.build());
    }

    private void createBasicNotificationLongText() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle("Notification Title")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("This is a notification with a longer paragraph. This is a notification with a longer paragraph."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notificationManager.notify(2, builder.build());
    }

    private void createIntentNotification() {
        Intent intent = new Intent(this, Cat.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle("Cat")
                .setContentText("Meow.")
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
        notificationManager.notify(3, builder.build());
    }

    //Progress bar notification
    private void createProgressBarNotification() {

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setContentTitle("Progress bar notification")
                .setContentText("Loading")
                .setSmallIcon(R.drawable.ic_notifications)
                .setPriority(NotificationCompat.PRIORITY_LOW);

        final int PROGRESS_MAX = 100;
        final int[] currentProgress = {0};

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (currentProgress[0] == PROGRESS_MAX) {
                    builder.setContentText("Done")
                            .setProgress(0, 0, false);
                    notificationManager.notify(3, builder.build());
                    timer.cancel();
                } else {
                    currentProgress[0] += 10;
                    builder.setProgress(PROGRESS_MAX, currentProgress[0], false);
                    notificationManager.notify(3, builder.build());
                }
            }
        }, 0, 1000);
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
