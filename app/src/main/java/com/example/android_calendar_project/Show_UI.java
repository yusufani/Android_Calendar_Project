package com.example.android_calendar_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Show_UI extends AppCompatActivity {
    TextView info;
    Button ok, go, close;
    Events event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show__ui);
        initialize_layout();
        int id = getIntent().getIntExtra("event_id", -1);
        event = CustomCalendarView.get_event(id, getApplicationContext());
        info.setText(event.getEVENT_NAME() +"\n\n"+event.share_content());
    }

    private void initialize_layout() {
        ok = findViewById(R.id.SHOW_UI_ok_BUTTON);
        go = findViewById(R.id.SHOW_UI_go_BUTTON);
        close = findViewById(R.id.SHOW_UI_close_button);
        info = findViewById(R.id.SHOW_UI_info);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                event.setDONE(true);
                CustomCalendarView.update_event(event, getApplicationContext());
                finish();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                event.setDONE(false);
                CustomCalendarView.update_event(event, getApplicationContext());
                finish();
            }
        });
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Not implemented yet ", Toast.LENGTH_SHORT).show();
                Intent new_intent = new Intent(getApplicationContext(), Event_Main_Activity.class);
                new_intent.putExtra("event_id", event.getEVENT_ID());
                new_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(new_intent);
                finish();
            }
        });

    }
}
