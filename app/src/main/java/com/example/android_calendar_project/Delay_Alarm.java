package com.example.android_calendar_project;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class Delay_Alarm extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent current = getIntent();
        int rem_id = current.getIntExtra("reminder_id", -1);

        Intent intent = new Intent(Delay_Alarm.this, Alarm_Receiver.class);
        intent.putExtra("reminder_id", rem_id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(Delay_Alarm.this, rem_id, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmManager = (AlarmManager) Delay_Alarm.this.getSystemService(Context.ALARM_SERVICE);
        Calendar f = Calendar.getInstance();
        Context context;
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        int de = sharedPreferences.getInt("delay",15);
        Toast.makeText(getApplicationContext(),"Alarm snoozed to "+de+ " minutes later",Toast.LENGTH_SHORT).show();
        f.add(Calendar.MINUTE,sharedPreferences.getInt("delay",de));
        alarmManager.set(AlarmManager.RTC_WAKEUP,f.getTimeInMillis() , pendingIntent);
        finish();
    }
}
