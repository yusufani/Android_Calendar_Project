package com.example.android_calendar_project;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class Alarm_Receiver extends BroadcastReceiver {
    String TAG = "ALARM RECEIVER";
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        int rem_id = intent.getIntExtra("reminder_id", -1);
        Log.e(TAG, "Rem ," + rem_id);
        Reminder reminder = CustomCalendarView.get_remind(rem_id, context);
        Intent activityIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, activityIntent, PendingIntent.FLAG_ONE_SHOT);

        Events event = CustomCalendarView.get_event(reminder.getEVENT_ID(), context);


        if (reminder.getPLAY_RINGTONE()) {
            Resources res = context.getResources();
            Log.v(TAG, reminder.getRING_TONE());
            int soundId = res.getIdentifier(reminder.getRING_TONE(), "raw", context.getPackageName());
            MediaPlayer mPlayer = MediaPlayer.create(context, soundId);
            mPlayer.start();
        }

        if (reminder.getSHOW_NOTIFICATION()) {
            String channelId = "channel_id";
            CharSequence name = "channel_name";
            String description = "descripton";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId, name, NotificationManager.IMPORTANCE_HIGH);
                NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }

            Notification notification = new NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentText(event.toString())
                    .setSubText(reminder.get_time())
                    .setDeleteIntent(pendingIntent)
                    .setGroup("Group_calendar_View")
                    .build();

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            notificationManagerCompat.notify(reminder.getREMIND_ID(), notification);
        }
        if (reminder.getSHOW_ALERT_DIAOLOG()) {
            Intent intent_new = new Intent(context, Show_UI.class);
            intent_new.putExtra("event_id", event.getEVENT_ID());
            context.startActivity(intent_new);
        }
    }


}
