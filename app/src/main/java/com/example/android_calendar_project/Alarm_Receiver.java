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
import android.os.VibrationEffect;
import android.util.Log;
import android.os.Vibrator;

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
        Intent go_event = new Intent(context, Event_Main_Activity.class);
        go_event.putExtra("event_id",event.getEVENT_ID());
        PendingIntent pendingIntent2 = PendingIntent.getActivity(context, 0, go_event, PendingIntent.FLAG_ONE_SHOT);

        Intent delay_alarm = new Intent(context, Delay_Alarm.class);
        delay_alarm.putExtra("reminder_id",reminder.getREMIND_ID());
        PendingIntent pendingIntent3 = PendingIntent.getActivity(context, 0, delay_alarm, PendingIntent.FLAG_ONE_SHOT);
        if(reminder.getVIBRATION()){
            Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(700, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                //deprecated in API 26
                v.vibrate(700);
            }

// Vibrate for 400 milliseconds
            v.vibrate(400);
        }
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
                    .setSmallIcon(R.mipmap.app_icon)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .addAction(R.drawable.button_go_event,"Go Event",pendingIntent2)
                    .addAction(R.drawable.button_go_event,"Delay",pendingIntent3)
                    .setContentText(event.share_content())
                    .setSubText(event.getEVENT_NAME())
                    .setDeleteIntent(pendingIntent)
                    .setGroup("Group_calendar_View")
                    .build();

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            notificationManagerCompat.notify(reminder.getREMIND_ID(), notification);
        }
        if (reminder.getSHOW_ALERT_DIAOLOG()) {
            Intent intent_new = new Intent(context, Show_UI.class);
            intent_new.putExtra("event_id", event.getEVENT_ID());
            intent_new.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent_new);
        }
    }


}
