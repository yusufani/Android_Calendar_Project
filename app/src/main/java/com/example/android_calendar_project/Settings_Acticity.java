package com.example.android_calendar_project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

public class Settings_Acticity extends AppCompatActivity {
    Spinner freq;
    Spinner time;
    Spinner mp3;
    ToggleButton darkmode;
    Button save;
    EditText max_interval_text;
    EditText delay_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings__acticity);
        initialize_layout();
    }

    private void initialize_layout() {
        freq = findViewById(R.id.re_freq);
        time = findViewById(R.id.re_time);
        mp3 = findViewById(R.id.re_ringtone);
        darkmode = findViewById(R.id.dark_mode);
        save = findViewById(R.id.save_settings);
        max_interval_text = findViewById(R.id.max_interval);
        delay_text = findViewById(R.id.delay);
        // Create an ArrayAdapter using the string array and a default spinner_event_type layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.default_Re_freq_Array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter_event_type to the spinner_event_type
        freq.setAdapter(adapter);


        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.default_Re_time_Array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter_event_type to the spinner_event_type
        time.setAdapter(adapter2);


        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.reminder_mp3s, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter_event_type to the spinner_event_type
        mp3.setAdapter(adapter3);


        set_values();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int freq_text = freq.getSelectedItemPosition();
                int time_text = time.getSelectedItemPosition();
                int mp3_Text = mp3.getSelectedItemPosition();
                boolean dark_mode_text = darkmode.isChecked();
                int max_interval = Integer.parseInt(max_interval_text.getText().toString());
                int delay = Integer.parseInt(delay_text.getText().toString());
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("number_of_interval",max_interval);
                editor.putInt("freq", freq_text);
                editor.putInt("time", time_text);
                editor.putInt("mp3", mp3_Text);
                editor.putBoolean("dark_mode", dark_mode_text);
                editor.putInt("delay",delay);
                editor.apply();
                Intent newIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(newIntent);

            }
        });
    }

    private void set_values() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        Log.i("Shar", String.valueOf(sharedPreferences));
        int freq_text = sharedPreferences.getInt("freq", 0);
        freq.setSelection(freq_text);

        int time_text = sharedPreferences.getInt("time", 1);
        time.setSelection(time_text);


        int mp3_Text = sharedPreferences.getInt("mp3", 0);
        mp3.setSelection(mp3_Text);


        boolean dark_mode_text = sharedPreferences.getBoolean("dark_mode", true);
        darkmode.setChecked(dark_mode_text);

        int n_interval = sharedPreferences.getInt("number_of_interval", 200);
        max_interval_text.setText(String.valueOf(n_interval));

        int de = sharedPreferences.getInt("delay",15);
        delay_text.setText(String.valueOf(de));


    }
}
