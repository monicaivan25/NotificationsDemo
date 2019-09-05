# NotificationsDemo
Android push notifications demo

## Before you start
Initialise constants for all the notification channels you want to implement.
```java
private static final String CHANNEL_ID = "channel_id";
private static final String PRIORITY_CHANNEL_ID = "pr_channel_id";
```
Create the notification channels and assign them to a NotificationManager object.
```java
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
```
After calling ```createNotificationChannels()``` get a NotificationManagerCompat object. 
```java 
notificationManagerCompat = NotificationManagerCompat.from(this);
```
Show the notification with 
```java
notificationManagerCompat.notify(id, builder.build());
//or
notificationManagerCompat.notify(id, notification);
``` 

## Basic notification
```java
NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_notifications)
        .setContentTitle("Basic Notification")
        .setContentText("Text");
```
## Intent notification
Create a PendingIntent
```java
Intent intent = new Intent(this, Cat.class);
intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
```
Add the intent to the NotificationCompat.Builder object with ```.setContentIntent(pendingIntent)```. Add ```.setAutoCancel(true)``` to make the notification close after interaction. 
```java
NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_notifications)
        .setContentTitle("Intent notification")
        .setContentText("Click me")
        .setContentIntent(pendingIntent)
        .setAutoCancel(true);
```
  
## Progress bar notification
Create a basic notification
```java
NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
      .setContentTitle("Progress bar notification")
      .setContentText("Loading")
      .setSmallIcon(R.drawable.ic_notifications);
```
Update it with the help of threads, by incrementing the progress with ```.setProgress(int MAX_PROGRESS, int CURRENT_PROGRESS, boolean indeterminate)```
```java
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
```  
  
## Expandable notifications

#### Expandable text notification

```java
NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_notifications)
        .setContentTitle("Expandable text notification")
        
        .setStyle(new NotificationCompat.BigTextStyle()
                .bigText(getString(R.string.big_text)));
```
#### Expandable image notification
```java
NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_notifications)
        .setContentTitle("Expandable image notification")
        .setContentText("Expand to see the full image")
        
        .setLargeIcon(bitmap)
        .setStyle(new NotificationCompat.BigPictureStyle()
                .bigPicture(bitmap)
                .bigLargeIcon(null));
```
  
#### Expandable media notification
```java
Notification notification = new Notification.Builder(this, CHANNEL_ID)
        .setVisibility(Notification.VISIBILITY_PUBLIC)
        .setSmallIcon(R.drawable.ic_notifications)
        .setContentTitle("Expandable media notification")
        .setContentText("Artist")
        .setLargeIcon(bitmap)
        
        .addAction(R.drawable.ic_prev, "Previous", null) // #0
        .addAction(R.drawable.ic_pause, "Pause", pendingIntent)  // #1
        .addAction(R.drawable.ic_next, "Next", null)     // #2
        
        .setStyle(new Notification.MediaStyle()
                .setShowActionsInCompactView(1 /* #1: pause button */)
                .setMediaSession(null))
        
        .build();
```        
  
## Inbox-style notifications
Notifications that display multiple lines.
```java
NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_notifications)
        .setContentTitle("Inbox notification")
        .setLargeIcon(bitmap)
        
        .setStyle(new NotificationCompat.InboxStyle()
                .addLine("First Line")
                .addLine("Second Line")
                .addLine("Third Line"));
```

## Heads-up notifications
Use ```.setPtiority(NotificationCompat.PRIORITY_HIGH)```, and use the **PRIORITY_CHANEL_ID**, which is the id for the channel with the importance equal to **NotificationManager.IMPORTANCE_HIGH**
```java
NotificationCompat.Builder builder = new NotificationCompat.Builder(this, PRIORITY_CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_notifications)
        .setContentTitle("Foreground Notification")
        .setContentText("This is important!!!")
        .setPriority(NotificationCompat.PRIORITY_HIGH);
```
  
## Full screen notification
```java
NotificationCompat.Builder notificationBuilder =
        new NotificationCompat.Builder(this, PRIORITY_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle("Incoming call")
                .setContentText("(919) 555-1234")
                
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .setFullScreenIntent(fullScreenPendingIntent, true);
```
  
  
  
  
  
  
