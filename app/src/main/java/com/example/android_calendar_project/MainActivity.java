package com.example.android_calendar_project;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ScrollView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    CustomCalendarView customCalendarView;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomCalendarView.set_theme(MainActivity.this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        customCalendarView= findViewById(R.id.custom_calendar_view);
        //ScrollView scrollView = findViewById(R.id.MAIN_SCROOL);
        customCalendarView.setOnTouchListener(new Gesture_control(MainActivity.this) {
            public void onSwipeTop() {
                Toast.makeText(MainActivity.this, "Next Year", Toast.LENGTH_SHORT).show();
                CustomCalendarView.calendar.add(Calendar.YEAR, 1);
                CustomCalendarView.set_up_calendar();
            }
            public void onSwipeRight() {
                Toast.makeText(MainActivity.this, "Next Month", Toast.LENGTH_SHORT).show();
                CustomCalendarView.calendar.add(Calendar.MONTH, 1);
                CustomCalendarView.set_up_calendar();
            }
            public void onSwipeLeft() {
                Toast.makeText(MainActivity.this, "Previous Month", Toast.LENGTH_SHORT).show();
                CustomCalendarView.calendar.add(Calendar.MONTH, -1);
                CustomCalendarView.set_up_calendar();
            }
            public void onSwipeBottom() {
                Toast.makeText(MainActivity.this, "Previous Year", Toast.LENGTH_SHORT).show();
                CustomCalendarView.calendar.add(Calendar.YEAR, -1);
                CustomCalendarView.set_up_calendar();
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(getApplicationContext(), "Settings Selected", Toast.LENGTH_SHORT).show();
            Intent newIntent = new Intent(getApplicationContext(), Settings_Acticity.class);
            startActivity(newIntent);
            return true;
        }else if ( id== R.id.action_show_all_events){
            AlertDialog alertDialog;
            Toast.makeText(getApplicationContext(), "Showing all events ", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setCancelable(true);
            View showView = LayoutInflater.from(MainActivity.this).inflate(R.layout.show_events, null);
            RecyclerView recyclerView = showView.findViewById(R.id.events_RV);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(showView.getContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
            EventsRecyclerAdapter eventsRecyclerAdapter = new EventsRecyclerAdapter(showView.getContext(), CustomCalendarView.collectsAllevents());
            recyclerView.setAdapter(eventsRecyclerAdapter);
            eventsRecyclerAdapter.notifyDataSetChanged();

            builder.setView(showView);
            alertDialog = builder.create();
            alertDialog.show();

            alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    CustomCalendarView.set_up_calendar();
                }
            });
            return true;
        }else if (id== R.id.show_statistics){
            Toast.makeText(getApplicationContext(), "Statistics Selected", Toast.LENGTH_SHORT).show();
            Intent newIntent = new Intent(getApplicationContext(), Statistics_Activity.class);
            startActivity(newIntent);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCELED) {
            return;
        }

    }
}
