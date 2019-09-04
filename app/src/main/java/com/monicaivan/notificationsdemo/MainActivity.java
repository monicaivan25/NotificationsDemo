package com.monicaivan.notificationsdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "channel_id";
    private static final String PRIORITY_CHANNEL_ID = "pr_channel_id";
    private static final String KEY_TEXT_REPLY = "key_text_reply";
    private static final int CONVERSATION_REQUEST_CODE = 123;
    private Bitmap bitmap;
    private NotificationManagerCompat notificationManagerCompat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cat);
        createNotificationChannels();
        notificationManagerCompat = NotificationManagerCompat.from(this);

        findViewById(R.id.basic_notification_button).setOnClickListener(view -> createBasicNotification());
        findViewById(R.id.intent_notification_button).setOnClickListener(view -> createIntentNotification());
        findViewById(R.id.progress_notification_button).setOnClickListener(view -> createProgressBarNotification());
        findViewById(R.id.expandable_image_notification_button).setOnClickListener(view -> createExpandableImageNotification());
        findViewById(R.id.expandable_text_notification_button).setOnClickListener(view -> createExpandableTextNotification());
        findViewById(R.id.expandable_media_notification_button).setOnClickListener(view -> createExpandableMediaNotification());
        findViewById(R.id.inbox_notification_button).setOnClickListener(view -> createInboxNotification());
        findViewById(R.id.foreground_notification_button).setOnClickListener(view -> createForegroundNotification());
    }

    ///Basic notifications
    private void createBasicNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle("Basic Notification")
                .setContentText("Text");

        notificationManagerCompat.notify(1, builder.build());
    }

    private void createIntentNotification() {
        Intent intent = new Intent(this, Cat.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle("Intent notification")
                .setContentText("Click me")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        notificationManagerCompat.notify(3, builder.build());
    }

    //Progress bar notification
    private void createProgressBarNotification() {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setContentTitle("Progress bar notification")
                .setContentText("Loading")
                .setSmallIcon(R.drawable.ic_notifications);

        final int PROGRESS_MAX = 100;
        final int[] currentProgress = {0};

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (currentProgress[0] == PROGRESS_MAX) {
                    builder.setContentText("Done")
                            .setProgress(0, 0, false);
                    notificationManagerCompat.notify(4, builder.build());
                    timer.cancel();
                } else {
                    currentProgress[0] += 10;
                    builder.setProgress(PROGRESS_MAX, currentProgress[0], false);
                    notificationManagerCompat.notify(4, builder.build());
                }
            }
        }, 0, 1000);
    }

    //Expandable notification
    private void createExpandableTextNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle("Expandable text notification")
                .setLargeIcon(bitmap)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(getString(R.string.big_text)));

        notificationManagerCompat.notify(5, builder.build());
    }

    private void createExpandableImageNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle("Expandable image notification")
                .setContentText("Expand to see the full image")
                .setLargeIcon(bitmap)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(bitmap)
                        .bigLargeIcon(null));
        notificationManagerCompat.notify(6, builder.build());
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createExpandableMediaNotification(){
        Intent intent = new Intent(this, Cat.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Notification notification = new Notification.Builder(this, CHANNEL_ID)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.ic_notifications)
                .addAction(R.drawable.ic_prev, "Previous", null) // #0
                .addAction(R.drawable.ic_pause, "Pause", pendingIntent)  // #1
                .addAction(R.drawable.ic_next, "Next", null)     // #2
                .setStyle(new Notification.MediaStyle()
                        .setShowActionsInCompactView(1 /* #1: pause button */)
                        .setMediaSession(null))
                .setContentTitle("Expandable media notification")
                .setContentText("Artist")
                .setLargeIcon(bitmap)
                .build();

        notificationManagerCompat.notify(7, notification);
    }

    //Inbox notification
    private void createInboxNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle("Inbox notification")
                .setLargeIcon(bitmap)
                .setStyle(new NotificationCompat.InboxStyle()
                    .addLine("First Line")
                    .addLine("Second Line")
                    .addLine("Third Line"));
        notificationManagerCompat.notify(8, builder.build());
    }

    //Foreground notification
    private void createForegroundNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, PRIORITY_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle("Foreground Notification")
                .setContentText("This is important!!!")
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        notificationManagerCompat.notify(9, builder.build());

    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel basicNotificationChanel = new NotificationChannel(CHANNEL_ID,
                    "basic notification chanel",
                    NotificationManager.IMPORTANCE_DEFAULT);
            NotificationChannel priorityNotificationChannel = new NotificationChannel(PRIORITY_CHANNEL_ID,
                    "priority notification channel",
                    NotificationManager.IMPORTANCE_HIGH);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(basicNotificationChanel);
            notificationManager.createNotificationChannel(priorityNotificationChannel);
        }
    }

}
