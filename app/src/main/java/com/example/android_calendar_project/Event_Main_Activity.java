package com.example.android_calendar_project;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Logger;

import petrov.kristiyan.colorpicker.ColorPicker;


public class Event_Main_Activity extends AppCompatActivity {
    Events incoming_event = null;
    int INTENT_MAP_INFO = 30;
    FusedLocationProviderClient mFusedLocationClient;
    AlertDialog alertDialog;
    String TAG = "EVENT MAIN ACTIVITY";
    ArrayList<Reminder> reminders = new ArrayList<>();  // hold reminders
    int selected_color = 0 ;
    EditText event_name;
    EditText event_descripton;
    EditText event_location;
    EditText interval_value;
    LinearLayout interval_layout;
    TextView start_event_time;
    TextView end_event_time;
    TextView end_event_date;
    TextView start_event_date;
    TextView interval_value_text;
    Button add_reminder;
    Button add_event;
    Button add_location;
    Button select_color;
    ImageButton set_start_time;
    ImageButton set_end_time;
    ImageButton set_end_date;
    ImageButton set_start_date;
    CheckBox done ;
    SharedPreferences sharedPreferences;
    String before_time;
    int ringtone_pos;
    RecyclerView event_reminders_recycler;
    Reminder_Recyler_Adapter event_reminder_adapter_item;
    Spinner spinner_interval_Type;
    ArrayAdapter<CharSequence> adapter_interval_types;
    Spinner spinner_event_type;
    ArrayAdapter<CharSequence> adapter_event_type;
    DBOpenHelper dbOpenHelper;
    Date selected_date = null;
    Intent current_intent;
    ScrollView scrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomCalendarView.set_theme(Event_Main_Activity.this);
        setContentView(R.layout.activity_event__main_);
        initialize_layout();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        Snackbar.make(scrollView,"Fill in the empty spaces with information about the new event",Snackbar.LENGTH_SHORT).show();
    }

    private void initialize_views() {
        scrollView = findViewById(R.id.MAIN_SCROlL);
        done = findViewById(R.id.ADD_EVENT_done_CHECKBOX);
        done.setVisibility(View.GONE);
        event_name = findViewById(R.id.ADD_EVENT_event_name_edit_text);
        event_descripton = findViewById(R.id.ADD_EVENT_event_desc_edit_text);
        event_location = findViewById(R.id.ADD_EVENT_location_edit_text);
        interval_value = findViewById(R.id.ADD_EVENT_interval_value_edit_text);
        interval_layout = findViewById(R.id.interval_layout);
        select_color = findViewById(R.id.ADD_EVENT_select_color_button);
        start_event_time = findViewById(R.id.ADD_EVENT_event_start_time_text_view);
        end_event_time = findViewById(R.id.ADD_EVENT_select_end_time_text_view);
        end_event_date = findViewById(R.id.ADD_EVENT_select_end_date_text_view);
        start_event_date = findViewById(R.id.ADD_EVENT_select_start_date_text_view);
        Calendar current_date = Calendar.getInstance();
        if (selected_date != null) {
            current_date.setTime(selected_date);
        }

        start_event_date.setText(CustomCalendarView.final_only_date_format.format(current_date.getTime()));
        end_event_date.setText(CustomCalendarView.final_only_date_format.format(current_date.getTime()));
        start_event_time.setText(CustomCalendarView.final_only_time_format.format(current_date.getTime()));
        end_event_time.setText(CustomCalendarView.final_only_time_format.format(current_date.getTime()));
        interval_value_text = findViewById(R.id.ADD_EVENT_interval_type_text_view);
        add_reminder = findViewById(R.id.ADD_EVENT_create_reminder_button);
        add_event = findViewById(R.id.ADD_EVENT_add_event_button);
        add_location = findViewById(R.id.ADD_EVENT_add_location_button);
        set_start_time = findViewById(R.id.ADD_EVENT_select_event_start_time_image_button);
        set_end_time = findViewById(R.id.ADD_EVENT_select_event_end_time_Image_button);
        set_end_date = findViewById(R.id.ADD_EVENT_select_event_end_date_Image_button);
        set_start_date = findViewById(R.id.ADD_EVENT_select_event_start_date_Image_button);

        //final RadioGroup radio_interval_type = add_view.findViewById(R.id.interval_type);
        sharedPreferences = Event_Main_Activity.this.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        before_time = get_reminder_before_time(sharedPreferences);
        ringtone_pos = sharedPreferences.getInt("mp3", 0);
        event_reminders_recycler = findViewById(R.id.ADD_EVENT_reminders_list_recyler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Event_Main_Activity.this);
        event_reminders_recycler.setLayoutManager(layoutManager);
        event_reminder_adapter_item = new Reminder_Recyler_Adapter(Event_Main_Activity.this, reminders);
        event_reminders_recycler.setAdapter(event_reminder_adapter_item);
        event_reminders_recycler.setItemAnimator(new DefaultItemAnimator());

        spinner_interval_Type = findViewById(R.id.ADD_EVENT_interval_type_spinner);
        adapter_interval_types = ArrayAdapter.createFromResource(Event_Main_Activity.this,
                R.array.interval_types, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter_interval_types.setDropDownViewResource(R.layout.spinner_dropdown_menu);
        // Apply the adapter_event_type to the spinner_event_type
        spinner_interval_Type.setAdapter(adapter_interval_types);
        spinner_event_type = findViewById(R.id.ADD_EVENT_event_type_spinner);
        // Create an ArrayAdapter using the string array and a default spinner_event_type layout
        adapter_event_type = ArrayAdapter.createFromResource(Event_Main_Activity.this,
                R.array.event_types_spinner, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter_event_type.setDropDownViewResource(R.layout.spinner_dropdown_menu);
        // Apply the adapter_event_type to the spinner_event_type
        spinner_event_type.setAdapter(adapter_event_type);
    }
    private ArrayList<String> get_colors(){
        ArrayList<String> colors = new ArrayList<>();
        colors.add("#82B926");
        colors.add("#a276eb");
        colors.add("#6a3ab2");
        colors.add("#666666");
        colors.add("#FFFF00");
        colors.add("#3C8D2F");
        colors.add("#FA9F00");
        colors.add("#FF0000");
        return colors;
    }
    private void initialize_listeners() {
        select_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ColorPicker colorPicker = new ColorPicker(Event_Main_Activity.this);
                ArrayList<String> colors = get_colors();
                colorPicker
                        .setDefaultColorButton(Color.parseColor("#f84c44"))
                        .setColors(colors)
                        .setColumns(5)
                        .setRoundColorButton(true)
                        .setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                            @Override
                            public void onChooseColor(int position, int color) {
                                Log.d("position", "" + position);// will be fired only when OK button was tapped
                                selected_color=position;

                                select_color.setBackgroundColor(Color.parseColor(get_colors().get(position)));
                            }

                            @Override
                            public void onCancel() {

                            }
                        }).show();

            }
        });
        add_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                Toast.makeText(getApplicationContext(),"Please Select event location",Toast.LENGTH_SHORT).show();
                intent.putExtra("info", "new");
                startActivityForResult(intent, INTENT_MAP_INFO);

            }
        });
        add_reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(add_view.getContext(),"Creating Reminder"+start_event_time.getText().toString(), Toast.LENGTH_SHORT).show();
                if (start_event_time.getText().toString().equals(Event_Main_Activity.this.getResources().getString(R.string.start_date_not_set))) {
                    Toast.makeText(Event_Main_Activity.this, "Fill in the empty areas with information about the new reminder", Toast.LENGTH_SHORT).show();
                    return;
                }
                reminder_show(reminders, Event_Main_Activity.this, event_reminder_adapter_item,start_event_time.getText().toString(), start_event_date.getText().toString());
            }
        });
        set_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                Toast.makeText(Event_Main_Activity.this, "Select events end date", Toast.LENGTH_SHORT).show();
                String[] date_info = end_event_date.getText().toString().split("-");
                int year = Integer.parseInt(date_info[0]);
                //Güncel Yılı alıyoruz
                final int month = Integer.parseInt(date_info[1]);
                //Güncel Ayı alıyoruz
                final int day = Integer.parseInt(date_info[2]);
                //Güncel Günü alıyoruz
                /*
                int year = mcurrentTime.get(Calendar.YEAR);
                //Güncel Yılı alıyoruz
                final int month = mcurrentTime.get(Calendar.MONTH);
                //Güncel Ayı alıyoruz
                final int day = mcurrentTime.get(Calendar.DAY_OF_MONTH);
                //Güncel Günü alıyoruz
                */

                DatePickerDialog datePicker;//Datepicker objemiz
                datePicker = new DatePickerDialog(Event_Main_Activity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        String months = "", days = "";
                        if (monthOfYear <= 9)
                            months += "0";
                        if (dayOfMonth <= 9)
                            days += "0";
                        // TODO Auto-generated method stub
                        end_event_date.setText(year + "-" + months + monthOfYear + "-" + days + dayOfMonth);//Ayarla butonu tıklandığında textview'a yazdırıyoruz
                    }
                }, year, month, day);//başlarken set edilcek değerlerimizi atıyoruz
                datePicker.setTitle("Select Date");
                datePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "set", datePicker);
                datePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "cancel", datePicker);
                datePicker.show();
            }
        });
        set_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Event_Main_Activity.this, "Select events start date", Toast.LENGTH_SHORT).show();

                Calendar mcurrentTime = Calendar.getInstance();
                String[] date_info = start_event_date.getText().toString().split("-");
                int year = Integer.parseInt(date_info[0]);
                //Güncel Yılı alıyoruz
                final int month = Integer.parseInt(date_info[1]);
                //Güncel Ayı alıyoruz
                final int day = Integer.parseInt(date_info[2]);
                /*
                int year = mcurrentTime.get(Calendar.YEAR);
                //Güncel Yılı alıyoruz
                final int month = mcurrentTime.get(Calendar.MONTH);
                //Güncel Ayı alıyoruz
                final int day = mcurrentTime.get(Calendar.DAY_OF_MONTH);
                //Güncel Günü alıyoruz
                */
                DatePickerDialog datePicker;//Datepicker objemiz
                datePicker = new DatePickerDialog(Event_Main_Activity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        String months = "", days = "";
                        if (monthOfYear <= 9)
                            months += "0";
                        if (dayOfMonth <= 9)
                            days += "0";
                        // TODO Auto-generated method stub
                        start_event_date.setText(year + "-" + months + monthOfYear + "-" + days + dayOfMonth);//Ayarla butonu tıklandığında textview'a yazdırıyoruz
                    }
                }, year, month, day);//başlarken set edilcek değerlerimizi atıyoruz
                datePicker.setTitle("Select Date");
                datePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "set", datePicker);
                datePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "cancel", datePicker);
                datePicker.show();
            }
        });

        spinner_interval_Type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    interval_layout.setVisibility(View.GONE);
                } else {
                    interval_layout.setVisibility(View.VISIBLE);
                    interval_value_text.setText(spinner_interval_Type.getItemAtPosition(position).toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        set_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Event_Main_Activity.this, "Select events start time", Toast.LENGTH_SHORT).show();
                String[] info = start_event_time.getText().toString().split(":");
                int hours = Integer.parseInt(info[0]);
                int minutes = Integer.parseInt(info[1]);
                String[] date_info = start_event_date.getText().toString().split("-");
                final int year = Integer.parseInt(date_info[0]);
                //Güncel Yılı alıyoruz
                final int month = Integer.parseInt(date_info[1]);
                //Güncel Ayı alıyoruz
                final int day = Integer.parseInt(date_info[2]);
                TimePickerDialog timePickerDialog = new TimePickerDialog(Event_Main_Activity.this, R.style.Theme_AppCompat_DayNight_Dialog
                        , new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        c.set(Calendar.MINUTE, minute);
                        c.set(year, month-1, day);
                        c.setTimeZone(TimeZone.getDefault());
                        String event_time = CustomCalendarView.final_only_time_format.format(c.getTime());
                        start_event_time.setText(event_time);
                        Calendar end_date = Calendar.getInstance();
                        String end_day = end_event_date.getText().toString();
                        String end_time = end_event_time.getText().toString();
                        try {
                            end_date.setTime(CustomCalendarView.final_all_time_and_date_format.parse(end_day + " " + end_time));
                            if (c.getTimeInMillis() > end_date.getTimeInMillis()) {
                                end_event_date.setText(CustomCalendarView.final_only_date_format.format(c.getTime()));
                                end_event_time.setText(CustomCalendarView.final_only_time_format.format(
                                        c.getTime()));
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        final int freq_pos = sharedPreferences.getInt("freq", 0);
                        String[] freqs = Event_Main_Activity.this.getResources().getStringArray(R.array.default_Re_freq_Array);
                        String[] mp3s = Event_Main_Activity.this.getResources().getStringArray(R.array.reminder_mp3s);

                        int l = Integer.valueOf(freqs[freq_pos]);
                        for (int i = 0; i < l; i++) {
                            try {
                                Calendar reminder_cal = Calendar.getInstance();
                                int start_day = Integer.parseInt((String) DateFormat.format("dd", selected_date)); // 20
                                int start_monthNumber = Integer.parseInt((String) DateFormat.format("MM", selected_date)); // 06
                                int start_year = Integer.parseInt((String) DateFormat.format("yyyy", selected_date)); // 2013
                                reminder_cal.setTime(CustomCalendarView.final_only_time_format.parse(event_time));
                                reminder_cal.set(start_year, start_monthNumber - 1, start_day);
                                reminder_cal.add(Calendar.MINUTE, -1 * Integer.valueOf(before_time));
                                Date remind_date = reminder_cal.getTime();
                                Reminder reminder = new Reminder(remind_date,
                                        mp3s[ringtone_pos], true, true, true
                                        , true);
                                reminders.add(reminder);

                                event_time = CustomCalendarView.final_only_time_format.format(reminder_cal.getTime());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        event_reminder_adapter_item.notifyDataSetChanged();

                    }
                }, hours, minutes, true);
                timePickerDialog.show();
            }
        });
        set_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Event_Main_Activity.this, "Select events end time", Toast.LENGTH_SHORT).show();

                String[] info = end_event_time.getText().toString().split(":");
                int hours = Integer.parseInt(info[0]);
                int minutes = Integer.parseInt(info[1]);
                TimePickerDialog timePickerDialog = new TimePickerDialog(Event_Main_Activity.this, R.style.Theme_AppCompat_DayNight_Dialog
                        , new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        c.set(Calendar.MINUTE, minute);
                        c.setTimeZone(TimeZone.getDefault());
                        String event_time = CustomCalendarView.final_only_time_format.format(c.getTime());
                        end_event_time.setText(event_time);
                    }
                }, hours, minutes, true);
                timePickerDialog.show();
            }
        });
        add_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO END DATE SORUNUNU ÇÖZ
                //String[] end_dates = end_event_time.getText().toString().split("-");

                try {

                    int child_id = spinner_interval_Type.getSelectedItemPosition();
                    Log.d(TAG, String.valueOf(child_id));
                    int interval_type = 0;
                    int interval_val= 0 ;
                    if(event_name.getText().toString().equals("")){
                        throw  new Exception("Please fill event name ");
                    }
                    String start_time_info = start_event_time.getText().toString();
                    String end_time_info;
                    if (end_event_time.getText().equals(Event_Main_Activity.this.getResources().getString(R.string.select_end_time))) {
                        end_time_info = start_time_info;
                    } else {
                        end_time_info = end_event_time.getText().toString();
                    }
                    Calendar start_days = Calendar.getInstance();

                    String[] date_info = start_event_date.getText().toString().split("-");
                    int year = Integer.parseInt(date_info[0]);
                    //Güncel Yılı alıyoruz
                    int month = Integer.parseInt(date_info[1]);
                    //Güncel Ayı alıyoruz
                    int day = Integer.parseInt(date_info[2]);
                    start_days.set(year, month -1, day);
                    String end_date_info = end_event_date.getText().toString();
                    String start_date_info = CustomCalendarView.final_only_date_format.format(start_days.getTime());
                    if (end_date_info.equals(Event_Main_Activity.this.getString(R.string.select_end_date))) {
                        end_date_info = CustomCalendarView.final_only_date_format.format(start_days.getTime());
                    } else {
                        end_date_info = end_event_date.getText().toString();
                    }
                    Date date_start_day = CustomCalendarView.final_all_time_and_date_format.parse(start_date_info + " " + start_time_info);
                    Date date_end_day = CustomCalendarView.final_all_time_and_date_format.parse(end_date_info + " " + end_time_info);
                    interval_val = 0;
                    if (child_id == 0 ){
                        interval_type = INTERVAL_TYPES.NO_INTERVAL_INDEX;
                    }
                    else if (child_id == 4 && Integer.valueOf(interval_value.getText().toString())==1) {
                        interval_val = Integer.valueOf(interval_value.getText().toString());

                        // Onces in month
                        interval_type = INTERVAL_TYPES.IN_MONTH_INDEX;
                    }
                    else{
                        interval_val = Integer.valueOf(interval_value.getText().toString());
                        interval_type = INTERVAL_TYPES.IN_HOUR_INDEX;
                        interval_val = calculate_interval(interval_val, child_id);
                    }

                    Events event = new Events(event_name.getText().toString(), event_descripton.getText().toString(), event_location.getText().toString(),
                            interval_type,interval_val, spinner_event_type.getSelectedItem().toString(),get_colors().get(selected_color),
                            date_start_day, date_end_day,
                            false, -1);

                    int event_id;
                    if (incoming_event != null){
                        event.setDONE(done.isChecked());
                        event.setEVENT_ID(incoming_event.getEVENT_ID());
                        event_id = event.getEVENT_ID();
                        update_event_to_db(event);
                        delete_and_cancel_all_reminders_for_event(event.getEVENT_ID());
                    }else{
                        event_id = save_event_to_db(event);
                        event.setEVENT_ID(event_id);
                    }
                    for (Reminder reminder : reminders)
                        reminder.setEVENT_ID(event_id);
                    int highest_reminder_id = save_reminders_to_db(reminders);
                    int i;
                    Calendar current_event_start_cal = set_calendar(event.getSTART_DATE());
                    Calendar current_event_end_cal = set_calendar(event.getEND_DATE());
                    ArrayList<Calendar> reminder_cals = new ArrayList<>();
                    int lowest_reminder_id = highest_reminder_id - reminders.size();
                    for (Reminder reminder : reminders) {
                        reminder.setREMIND_ID(++lowest_reminder_id);
                        reminder_cals.add(set_calendar(reminder.getREMIND_DATE()));
                        set_alarm(reminder, reminder_cals.get(reminder_cals.size() - 1), reminder.getREMIND_ID(), interval_type,interval_val);
                    }
                    ArrayList<Events> all_events = new ArrayList<>();
                    final ArrayList<Reminder> all_reminders = new ArrayList<>();
                    int n_interval = sharedPreferences.getInt("number_of_interval", 200);
                    event.setEVENT_ID(event_id);
                    Log.e(TAG, "EVENT VE REMINDARLAR OLUSUYOR");
                    for (i = 0; i < n_interval; i++) {
                        all_events.add(Events.copy(event));
                        for (int j = 0; j < reminders.size(); j++) {
                            all_reminders.add(Reminder.copy(reminders.get(j)));
                            all_reminders.get(j).setREMIND_ID(++highest_reminder_id);
                        }
                    }
                    Log.e(TAG, "E oluştu ");
                    if (interval_type!= INTERVAL_TYPES.NO_INTERVAL_INDEX) {
                        for (i = 0; i < n_interval; i++) {
                            Log.e(TAG, String.valueOf(i));
                            ++event_id;
                            // TODO BİR AY SONRASI EKLEMEYİ HALLET
                            switch (event.getINTERVAL_TYPE()){
                                case INTERVAL_TYPES.IN_HOUR_INDEX: {
                                    current_event_start_cal.add(Calendar.HOUR_OF_DAY, event.getINTERVAL_VALUE());  // NEW EVENT START DATE
                                    current_event_end_cal.add(Calendar.HOUR_OF_DAY, event.getINTERVAL_VALUE());  // NEW EVENT START DATE
                                    break;
                                }
                                case INTERVAL_TYPES.IN_MONTH_INDEX: {
                                    current_event_start_cal.add(Calendar.MONTH, event.getINTERVAL_VALUE());
                                    current_event_end_cal.add(Calendar.MONTH, event.getINTERVAL_VALUE());
                                    break;
                                }
                            }
                            forward_event(all_events.get(i), current_event_start_cal, current_event_end_cal);
                            for (int j = i * reminders.size(); j < (i + 1) * reminders.size(); j++) {
                                all_reminders.get(j).setEVENT_ID(event_id);
                                switch (event.getINTERVAL_TYPE()){
                                    case INTERVAL_TYPES.IN_HOUR_INDEX: {
                                        reminder_cals.get(j % reminders.size()).add(Calendar.HOUR_OF_DAY, event.getINTERVAL_VALUE());
                                        current_event_end_cal.add(Calendar.HOUR_OF_DAY, event.getINTERVAL_VALUE());  // NEW EVENT START DATE
                                        break;
                                    }
                                    case INTERVAL_TYPES.IN_MONTH_INDEX: {
                                        reminder_cals.get(j % reminders.size()).add(Calendar.MONTH, event.getINTERVAL_VALUE());
                                        current_event_end_cal.add(Calendar.MONTH, event.getINTERVAL_VALUE());
                                        break;
                                    }
                                }
                                forward_reminder(all_reminders.get(j), reminder_cals.get(j % reminders.size()));
                            }
                        }
                        Log.e(TAG, "bitti");
                        // HIZLANDIRMA ,İŞLEMİ

                        save_events_to_db(all_events);
                        Log.e(TAG, "DATA BASE DE HAZIR ");
                        save_reminders_to_db(all_reminders);
                        Log.e(TAG, "rEMİNDERLAR DAHAZIR ");

                        Log.e(TAG, "Calenarlar felan hazır ");
                        //Toast.makeText(Event_Main_Activity.this, "Her şey  HAZIR ", Toast.LENGTH_SHORT).show();
                    }
                    CustomCalendarView.set_up_calendar();
                    finish();
                    //radio_interval_type.clearCheck();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(Event_Main_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void delete_and_cancel_all_reminders_for_event(int event_id) {
        ArrayList<Reminder> reminders = CustomCalendarView.get_reminders(event_id,getApplicationContext());
        for(Reminder reminder : reminders){
            cancel_alarm(reminder.getREMIND_ID(), getApplicationContext());
        }
        delete_reminders(event_id);
    }

    private void delete_reminders(int event_id) {
        dbOpenHelper = new DBOpenHelper(Event_Main_Activity.this);
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.delete_reminders(event_id, database);
        dbOpenHelper.close();
        Toast.makeText(Event_Main_Activity.this, "Event Updated", Toast.LENGTH_SHORT).show();
    }


    private void initialize_layout() {
        current_intent = getIntent();
        // Initialize layout and add click listeners
        selected_date = (Date) current_intent.getSerializableExtra("selected_date");
        int event_id = current_intent.getIntExtra("event_id", -1); // if -1 come , this event is a new event
        initialize_views();
        initialize_listeners();
        if (event_id != -1) {
            incoming_event = CustomCalendarView.get_event(event_id, getApplicationContext());
            fill_the_fields();
        }
    }

    private void fill_the_fields() {
        add_event.setText("Update Event");
        done.setVisibility(View.VISIBLE);
        done.setChecked(incoming_event.getDONE());
        event_name.setText(incoming_event.getEVENT_NAME());
        event_location.setText(incoming_event.getLOCATION());
        event_descripton.setText(incoming_event.getEVENT_DESCRIPTION());
        Date start_date = incoming_event.getSTART_DATE();
        start_event_date.setText(CustomCalendarView.final_only_date_format.format(start_date));
        start_event_time.setText(CustomCalendarView.final_only_time_format.format(start_date));
        Date end_date = incoming_event.getEND_DATE();
        end_event_date.setText(CustomCalendarView.final_only_date_format.format(end_date));
        end_event_time.setText(CustomCalendarView.final_only_time_format.format(end_date));
        select_color.setBackgroundColor(Color.parseColor(incoming_event.getEVENT_COLOR()));
        selected_color = get_colors().indexOf(incoming_event.getEVENT_COLOR());
        int interval_value_val = incoming_event.getINTERVAL_VALUE();
        int interval_type = incoming_event.getINTERVAL_TYPE();
        int i = 0;
        String[] types = getApplicationContext().getResources().getStringArray(R.array.interval_types);
        int int_val= 0;
        switch (interval_type){
            case INTERVAL_TYPES.NO_INTERVAL_INDEX:{
               i=0;
                break;
            }
            case INTERVAL_TYPES.IN_HOUR_INDEX:{
                if (interval_value_val % INTERVAL_TYPES.YEAR == 0) {
                    for (i = 0; i < types.length; i++) if (types[i].equals("Years")) break;
                    int_val = interval_value_val / INTERVAL_TYPES.YEAR;
                } else if (interval_value_val % INTERVAL_TYPES.MONTH == 0) {
                    for (i = 0; i < types.length; i++) if (types[i].equals("Months")) break;
                    int_val = interval_value_val / INTERVAL_TYPES.MONTH;

                } else if (interval_value_val % INTERVAL_TYPES.WEEK == 0) {
                    for (i = 0; i < types.length; i++) if (types[i].equals("Weeks")) break;
                    int_val = interval_value_val / INTERVAL_TYPES.WEEK;

                } else if (interval_value_val % INTERVAL_TYPES.DAY == 0) {
                    for (i = 0; i < types.length; i++) if (types[i].equals("Days")) break;
                    int_val = interval_value_val / INTERVAL_TYPES.DAY;

                } else {
                    for (i = 0; i < types.length; i++) if (types[i].equals("Hours")) break;
                    int_val = interval_value_val;
                }
                break;
            }
            case INTERVAL_TYPES.IN_MONTH_INDEX:{ // ONCE In Month
                for (i = 0; i < types.length; i++) if (types[i].equals("Months")) break;
                int_val= 1;
                break;
            }
        }
        spinner_interval_Type.setSelection(i);
        interval_value.setText(String.valueOf(int_val));
        interval_value_text.setText(types[i]);

        String event_type = incoming_event.getEVENT_TYPE();
        String[] event_types = getApplicationContext().getResources().getStringArray(R.array.event_types_spinner);
        for (i = 0; i < event_types.length; i++) if (event_types[i].equals(event_type)) break;
        spinner_event_type.setSelection(i);
        ArrayList<Reminder> event_reminders = CustomCalendarView.get_reminders(incoming_event.getEVENT_ID(), getApplicationContext());
        reminders.addAll(event_reminders);
        event_reminder_adapter_item.notifyDataSetChanged();
    }

    private void forward_reminder(Reminder reminder, Calendar re) {
        reminder.setREMIND_DATE(re.getTime());

    }

    private void forward_event(Events event, Calendar st, Calendar en) {
        event.setSTART_DATE(st.getTime());
        event.setEND_DATE(en.getTime());
    }

    private String get_reminder_before_time(SharedPreferences sharedPreferences) {
        final int time = sharedPreferences.getInt("time", 1);
        String[] timess = Event_Main_Activity.this.getResources().getStringArray(R.array.default_Re_time_Array);
        return timess[time];
    }

    private Calendar set_calendar(Date start_day) {
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(start_day);
        return calendar2;
    }

    private void reminder_show(final ArrayList<Reminder> reminders, final Context context1, final Reminder_Recyler_Adapter adapter_items,
                               final String event_start_time , final String event_start_date) {
        final Context context = Event_Main_Activity.this;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        final View add_view_2 = LayoutInflater.from(context1).inflate(R.layout.add_new_reminder, null);
        final ImageButton reminder_Set_start_time = add_view_2.findViewById(R.id.set_reminder_time);
        ImageButton reminder_Set_start_date = add_view_2.findViewById(R.id.set_reminder_date);
        final TextView reminder_time = add_view_2.findViewById(R.id.reminder_start_time);
        final TextView reminder_date = add_view_2.findViewById(R.id.reminder_start_date);

        final Spinner spinner2 = add_view_2.findViewById(R.id.spinner_reminder_mp3s);
        final SharedPreferences sharedPreferences = add_view_2.getContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        // Create an ArrayAdapter using the string array and a default spinner_event_type layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context1,
                R.array.reminder_mp3s, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter_event_type to the spinner_event_type
        spinner2.setAdapter(adapter);
        final int pos = sharedPreferences.getInt("mp3", 0);
        spinner2.setSelection(pos);

        reminder_date.setText(event_start_date);
        reminder_time.setText(event_start_time);

        try {
            Calendar cs = Calendar.getInstance();
            cs.setTimeZone(TimeZone.getDefault());
            cs.setTime(CustomCalendarView.final_only_time_format.parse(reminder_time.getText().toString()));
            cs.add(Calendar.MINUTE, -1 * Integer.valueOf(before_time));
            reminder_time.setText(CustomCalendarView.final_only_time_format.format(cs.getTime()));
            Toast.makeText(getApplicationContext(),"Reminder autoset "+before_time+" minute before event start date",Toast.LENGTH_SHORT).show();
        } catch (ParseException e) {
            e.printStackTrace();
        }



        Log.i("Shar", String.valueOf(sharedPreferences));
        final int time = sharedPreferences.getInt("time", 0);
        String[] times = context.getResources().getStringArray(R.array.default_Re_time_Array);
        final String before_time = times[time];
        Toast.makeText(add_view_2.getContext(), "Reminder set " + before_time + "minute before event time ", Toast.LENGTH_SHORT).show();

        spinner2.setPrompt("Please Select MP3");

        //TODO VARSAYILAN MP3 TÜRÜNÜ AL
        final CheckBox show_not = add_view_2.findViewById(R.id.reminder_show_not);
        final CheckBox show_ui = add_view_2.findViewById(R.id.reminder_show_ui);
        final CheckBox use_vib = add_view_2.findViewById(R.id.reminder_use_vib);
        final CheckBox play_Ring = add_view_2.findViewById(R.id.reminder_use_mp3);
        spinner2.setVisibility(View.VISIBLE);
        play_Ring.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             if (play_Ring.isChecked()) {
                                                 spinner2.setVisibility(View.VISIBLE);
                                             } else {
                                                 spinner2.setVisibility(View.GONE);
                                             }
                                         }
                                     }
        );
        final Button add_reminder = add_view_2.findViewById(R.id.add_reminder);
        reminder_Set_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] info = reminder_time.getText().toString().split(":");
                int hours = Integer.parseInt(info[0]);
                int minutes = Integer.parseInt(info[1]);
                String[] date_info = reminder_date.getText().toString().split("-");
                final int year = Integer.parseInt(date_info[0]);
                //Güncel Yılı alıyoruz
                final int month = Integer.parseInt(date_info[1]);
                //Güncel Ayı alıyoruz
                final int day = Integer.parseInt(date_info[2]);

                TimePickerDialog timePickerDialog = new TimePickerDialog(add_view_2.getContext(), R.style.Theme_AppCompat_DayNight_Dialog
                        , new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.YEAR, year);
                        c.set(Calendar.DAY_OF_MONTH, day);
                        c.set(Calendar.MONTH, month);
                        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        c.set(Calendar.MINUTE, minute);
                        c.setTimeZone(TimeZone.getDefault());
                        String event_time = CustomCalendarView.final_only_time_format.format(c.getTime());
                        reminder_time.setText(event_time);
                    }
                }, hours, minutes, true);
                timePickerDialog.show();
            }
        });
        reminder_Set_start_date.setOnClickListener(new View.OnClickListener() {
            //tarihButona Click Listener ekliyoruz
            @Override
            public void onClick(View v) {

                String[] date_info = start_event_date.getText().toString().split("-");
                int year = Integer.parseInt(date_info[0]);
                //Güncel Yılı alıyoruz
                final int month = Integer.parseInt(date_info[1]);
                //Güncel Ayı alıyoruz
                final int day = Integer.parseInt(date_info[2]);

                final DatePickerDialog datePicker;//Datepicker objemiz
                datePicker = new DatePickerDialog(add_view_2.getContext(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        String months = "", days = "";
                        if (monthOfYear <= 9)
                            months += "0";
                        if (dayOfMonth <= 9)
                            days += "0";
                        // TODO Auto-generated method stub
                        start_event_date.setText(year + "-" + months + monthOfYear + "-" + days + dayOfMonth);//Ayarla butonu tıklandığında textview'a yazdırıyoruz
                    }
                }, year, month, day);//başlarken set edilcek değerlerimizi atıyoruz
                datePicker.setTitle("Select Date");
                datePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "set", datePicker);
                datePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "cancel", datePicker);
                datePicker.show();
            }
        });
        add_reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Date remind_Date = CustomCalendarView.final_all_time_and_date_format.parse(reminder_date.getText().toString() + " " + reminder_time.getText().toString());
                    Reminder reminder = new Reminder(remind_Date,
                            spinner2.getSelectedItem().toString(), use_vib.isChecked(), show_not.isChecked(), show_ui.isChecked()
                            , play_Ring.isChecked());
                    reminders.add(reminder);
                    adapter_items.notifyDataSetChanged();
                    alertDialog.dismiss();
                } catch (Exception e) {
                    Toast.makeText(context1, "HATA :" + e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
        builder.setView(add_view_2);
        alertDialog = builder.create();
        alertDialog.show();
    }

    private int calculate_interval(int inteval, int child_id) {
        int val = 0;
        switch (child_id) {
            case 1: {
                val = INTERVAL_TYPES.HOUR;
                break;
            }
            case 2: {
                val = INTERVAL_TYPES.DAY;
                break;
            }
            case 3: {
                val = INTERVAL_TYPES.WEEK;
                break;
            }
            /*
            MONTHS DAY INTERVAL CAN BE CHANGED FOR EXAMPLE a month can 31 day , another month can take 28 day
            case 4: {
                val = INTERVAL_TYPES.MONTH;
                break;
            }
            */
            case 5: {
                val = INTERVAL_TYPES.YEAR;
            }
        }
        return inteval * val;
    }

    private void update_event_to_db(Events event){
        dbOpenHelper = new DBOpenHelper(Event_Main_Activity.this);
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.update_event(event, database);
        dbOpenHelper.close();
        Toast.makeText(Event_Main_Activity.this, "Event Updated", Toast.LENGTH_SHORT).show();
    }
    private int save_event_to_db(Events event) {
        dbOpenHelper = new DBOpenHelper(Event_Main_Activity.this);
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.save_Event(event, database);
        int event_id = dbOpenHelper.getHighestID(database);
        dbOpenHelper.close();
        Toast.makeText(Event_Main_Activity.this, "Event Saved", Toast.LENGTH_SHORT).show();
        return event_id;
    }

    private void save_events_to_db(ArrayList<Events> events) {
        dbOpenHelper = new DBOpenHelper(Event_Main_Activity.this);
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        for (Events event : events) {
            dbOpenHelper.save_Event(event, database);
        }
        dbOpenHelper.close();
        Toast.makeText(Event_Main_Activity.this, "Events are Saved", Toast.LENGTH_SHORT).show();
    }

    private int save_reminders_to_db(ArrayList<Reminder> reminders) {
        dbOpenHelper = new DBOpenHelper(Event_Main_Activity.this);
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        for (Reminder reminder : reminders) {
            dbOpenHelper.save_Reminder(reminder, database);
        }
        int remind_id = dbOpenHelper.getHighestID(database);
        dbOpenHelper.close();
        //Toast.makeText(context, "Reminders are Saved", Toast.LENGTH_SHORT).show();
        return remind_id;
    }

    public void set_alarm(Reminder reminder, Calendar reminder_cal, int request_code, int interval_type , int interval_value ) {
        Intent intent = new Intent(Event_Main_Activity.this, Alarm_Receiver.class);
        intent.putExtra("reminder_id", reminder.getREMIND_ID());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(Event_Main_Activity.this, request_code, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmManager = (AlarmManager) Event_Main_Activity.this.getSystemService(Context.ALARM_SERVICE);
        Log.e(TAG, reminder_cal.get(Calendar.HOUR_OF_DAY) + ":" + reminder_cal.get(Calendar.MINUTE) + "--" + reminder_cal.get(Calendar.DAY_OF_MONTH) +"/" + (1+reminder_cal.get(Calendar.MONTH)) + "/" + reminder_cal.get(Calendar.YEAR));
        assert alarmManager != null;
        switch (interval_type){
            case INTERVAL_TYPES.NO_INTERVAL_INDEX:{
                Log.e(TAG, reminder_cal.toString());
                alarmManager.set(AlarmManager.RTC_WAKEUP,reminder_cal.getTimeInMillis() ,pendingIntent);
                break;
            }
            case INTERVAL_TYPES.IN_HOUR_INDEX:{
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, reminder_cal.getTimeInMillis(), AlarmManager.INTERVAL_HOUR * interval_value, pendingIntent);
                break;
            }
                case INTERVAL_TYPES.IN_MONTH_INDEX:{ // ONCE In Month
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, reminder_cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY * reminder_cal.getActualMaximum(Calendar.DAY_OF_MONTH), pendingIntent);
                break;
            }

        }
        //alarmManager.set(AlarmManager.RTC_WAKEUP,reminder_cal.getTimeInMillis() ,pendingIntent);
    }
    private void cancel_alarm(int request_code , Context context) {
        Intent intent = new Intent(context.getApplicationContext(), Alarm_Receiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, request_code, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmManager = (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_MAP_INFO) {
            try {
                if (data != null) {
                    Snackbar.make(scrollView,"Location information added",Snackbar.LENGTH_SHORT).show();
                    event_location.setText(data.getStringExtra("location_info"));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public class INTERVAL_TYPES {
        public static final int HOUR = 1;
        public static final int DAY = 24;
        public static final int WEEK = 24*7;
        public static final int MONTH = 24*30;
        public static final int YEAR = 24*365;
        public static final int NO_INTERVAL_INDEX = 0 ;
        public static final int IN_HOUR_INDEX= 1;
        public static final int IN_MONTH_INDEX= 2;
        private INTERVAL_TYPES() {
        }  // Prevents instantiation
    }
}


