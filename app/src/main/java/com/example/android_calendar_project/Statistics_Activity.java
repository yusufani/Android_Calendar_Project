package com.example.android_calendar_project;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class Statistics_Activity extends AppCompatActivity {
    TextView number_of_events , number_of_reminders , interval_selected_rate , completed_number_rate, most_common_mp3, most_common_color;
    DBOpenHelper dbOpenHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_);
        initialize_layout();
        int n_of_events = get_number_of_events();
        int n_of_reminders = get_number_of_reminders();
        int n_of_not_interval_selected = get_interval_rate();
        int n_of_completed_events = get_number_of_completed_events();
        String most_c_color = get_most_common_color();
        String most_c_ringtone= get_most_common_ring_tone();
        number_of_events.setText(String.valueOf(n_of_events));
        number_of_reminders.setText(String.valueOf(n_of_reminders));
        Float  mean  = 1 - (new Float(n_of_not_interval_selected)/new Float(n_of_reminders));
        Float  mean2  = (new Float(n_of_completed_events)/new Float(n_of_events));
        interval_selected_rate.setText("%" + String.valueOf(100*mean));
        completed_number_rate.setText("%"+String.valueOf(100 * mean2));

        most_common_mp3.setText(most_c_ringtone);
        String[] info = most_c_color.split("---");
        most_common_color.setBackgroundColor(Color.parseColor(info[1]));
        most_common_color.setText(info[0]+"\n"+info[1]);

    }

    private void initialize_layout() {
        number_of_events = findViewById(R.id.STATISTICS_number_of_events);
        number_of_reminders = findViewById(R.id.STATISTICS_number_of_reminders);
        interval_selected_rate = findViewById(R.id.STATISTICS_interval_rate);
        completed_number_rate = findViewById(R.id.STATISTICS_completed_events);
        most_common_mp3 = findViewById(R.id.STATISTICS_most_common_mp3);
        most_common_color = findViewById(R.id.STATISTICS_most_common_color);
    }
    public int get_number_of_events() {

        dbOpenHelper = new DBOpenHelper(Statistics_Activity.this);
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        int count = dbOpenHelper.get_number_of_events(db);
        dbOpenHelper.close();
        return count;
    }
    public int get_number_of_reminders() {
        dbOpenHelper = new DBOpenHelper(Statistics_Activity.this);
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        int count = dbOpenHelper.get_number_of_reminders(db);
        dbOpenHelper.close();
        return count;
    }
    public int get_number_of_completed_events() {
        dbOpenHelper = new DBOpenHelper(Statistics_Activity.this);
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        int count = dbOpenHelper.get_number_of_completed_events(db);
        dbOpenHelper.close();
        return count;
    }
    public int get_interval_rate() {
        dbOpenHelper = new DBOpenHelper(Statistics_Activity.this);
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        int count = dbOpenHelper.get_interval_rate(db);
        dbOpenHelper.close();
        return count;
    }
    public String get_most_common_ring_tone() {
        dbOpenHelper = new DBOpenHelper(Statistics_Activity.this);
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.get_most_common_mp3_of_reminders(db);
        int count = 0 ;
        String mp3 = "" ;
        while (cursor.moveToNext()) {
            count = cursor.getInt(cursor.getColumnIndex(cursor.getColumnNames()[1]));
             mp3 = cursor.getString(cursor.getColumnIndex(DBStructure.REMIND_RING_TONE));
            break;
        }
        cursor.close();
        dbOpenHelper.close();
        return count+" times\n"+mp3;
    }
    public String get_most_common_color() {
        dbOpenHelper = new DBOpenHelper(Statistics_Activity.this);
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.get_most_common_color_of_events(db);
        int count = 0 ;
        String color = "" ;
        while (cursor.moveToNext()) {
             count =cursor.getInt(cursor.getColumnIndex(cursor.getColumnNames()[1]));
             color = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT_COLOR));
            break;
        }
        cursor.close();
        dbOpenHelper.close();
        return count+"---"+color;
    }


}
