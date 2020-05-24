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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class Event_Main_Activity extends AppCompatActivity {
    int INTENT_MAP_INFO = 30;
    FusedLocationProviderClient mFusedLocationClient;
    AlertDialog alertDialog;
    String TAG = "EVENT MAIN ACTIVITY";
    ArrayList<Reminder> reminders = new ArrayList<>();  // hold reminders
    EditText event_name;
    EditText event_descripton;
    EditText event_location;
    EditText interval_value;
    LinearLayout interval_layout;
    TextView start_event_time;
    TextView end_event_time;
    TextView end_event_date;
    TextView interval_value_text;
    Button add_reminder;
    Button add_event;
    Button add_location;
    ImageButton set_start_time;
    ImageButton set_end_time;
    ImageButton set_end_date;
    SharedPreferences sharedPreferences;
    String before_time;
    int ringtone_pos;
    RecyclerView recycler_view;
    RecyclerView.LayoutManager layoutManager;
    Reminder_Recyler_Adapter adapter_items;
    Spinner spinner_interval_Type;
    ArrayAdapter<CharSequence> adapter4;
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    DBOpenHelper dbOpenHelper;
    Date selected_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event__main_);
        initialize_layout();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

    }

    private void initialize_layout() {
        // Initialize layout and add click listeners
        selected_date = (Date) getIntent().getSerializableExtra("selected_date");
        int event_id  =
        event_name = findViewById(R.id.ADD_EVENT_event_name_edit_text);
        event_descripton = findViewById(R.id.ADD_EVENT_event_desc_edit_text);
        event_location = findViewById(R.id.ADD_EVENT_location_edit_text);
        interval_value = findViewById(R.id.ADD_EVENT_interval_value_edit_text);
        interval_layout = findViewById(R.id.interval_layout);

        start_event_time = findViewById(R.id.ADD_EVENT_event_start_time_text_view);
        end_event_time = findViewById(R.id.ADD_EVENT_select_end_time_text_view);
        end_event_date = findViewById(R.id.ADD_EVENT_select_end_date_text_view);
        interval_value_text = findViewById(R.id.ADD_EVENT_interval_type_text_view);
        add_reminder = findViewById(R.id.ADD_EVENT_create_reminder_button);
        add_event = findViewById(R.id.ADD_EVENT_add_event_button);
        add_location = findViewById(R.id.ADD_EVENT_add_location_button);
        set_start_time = findViewById(R.id.ADD_EVENT_select_event_start_time_image_button);
        set_end_time = findViewById(R.id.ADD_EVENT_select_event_end_time_Image_button);
        set_end_date = findViewById(R.id.ADD_EVENT_select_event_end_date_Image_button);

        //final RadioGroup radio_interval_type = add_view.findViewById(R.id.interval_type);
        sharedPreferences = Event_Main_Activity.this.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        before_time = get_reminder_before_time(sharedPreferences);
        ringtone_pos = sharedPreferences.getInt("mp3", 0);
        recycler_view = findViewById(R.id.ADD_EVENT_reminders_list_recyler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Event_Main_Activity.this);
        recycler_view.setLayoutManager(layoutManager);
        adapter_items = new Reminder_Recyler_Adapter(Event_Main_Activity.this, reminders);
        recycler_view.setAdapter(adapter_items);
        recycler_view.setItemAnimator(new DefaultItemAnimator());

        spinner_interval_Type = findViewById(R.id.ADD_EVENT_interval_type_spinner);
        adapter4 = ArrayAdapter.createFromResource(Event_Main_Activity.this,
                R.array.interval_types, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner_interval_Type.setAdapter(adapter4);
        spinner = findViewById(R.id.ADD_EVENT_event_type_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        adapter = ArrayAdapter.createFromResource(Event_Main_Activity.this,
                R.array.event_types_spinner, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        add_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("info", "new");
                startActivityForResult(intent, INTENT_MAP_INFO);

            }
        });
        add_reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(add_view.getContext(),"Creating Reminder"+start_event_time.getText().toString(), Toast.LENGTH_SHORT).show();
                if (start_event_time.getText().toString().equals(Event_Main_Activity.this.getResources().getString(R.string.start_date_not_set))) {
                    Toast.makeText(Event_Main_Activity.this, "Please select Time first", Toast.LENGTH_SHORT).show();
                    return;
                }
                reminder_show(reminders, Event_Main_Activity.this, adapter_items, start_event_time.getText().toString());
            }
        });
        set_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int year = mcurrentTime.get(Calendar.YEAR);
                //Güncel Yılı alıyoruz
                final int month = mcurrentTime.get(Calendar.MONTH);
                //Güncel Ayı alıyoruz
                final int day = mcurrentTime.get(Calendar.DAY_OF_MONTH);
                //Güncel Günü alıyoruz

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
                        end_event_date.setText(year + "-" + months + month + "-" + days + day);//Ayarla butonu tıklandığında textview'a yazdırıyoruz
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
                Toast.makeText(Event_Main_Activity.this, "Saat seçilecek", Toast.LENGTH_SHORT).show();
                Calendar calendar = Calendar.getInstance();
                int hours = calendar.get(Calendar.HOUR_OF_DAY);
                int minutes = calendar.get(Calendar.MINUTE);
                final int year = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH);
                final int day = calendar.get(Calendar.DAY_OF_MONTH);

                TimePickerDialog timePickerDialog = new TimePickerDialog(Event_Main_Activity.this, R.style.Theme_AppCompat_DayNight_Dialog
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
                        start_event_time.setText(event_time);

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
                        adapter_items.notifyDataSetChanged();

                    }
                }, hours, minutes, true);
                timePickerDialog.show();
            }
        });
        set_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int hours = calendar.get(Calendar.HOUR_OF_DAY);
                int minutes = calendar.get(Calendar.MINUTE);
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
                    int interval = 0;

                    String start_time_info = start_event_time.getText().toString();
                    String end_time_info;
                    if (end_event_time.getText().equals(Event_Main_Activity.this.getResources().getString(R.string.select_end_time))) {
                        end_time_info = start_time_info;
                    } else {
                        end_time_info = end_event_time.getText().toString();
                    }


                    Calendar start_days = Calendar.getInstance();

                    int start_day = Integer.parseInt((String) DateFormat.format("dd", selected_date)); // 20
                    int start_monthNumber = Integer.parseInt((String) DateFormat.format("MM", selected_date)); // 06
                    int start_year = Integer.parseInt((String) DateFormat.format("yyyy", selected_date)); // 2013
                    start_days.set(start_year, start_monthNumber - 1, start_day);
                    String end_date_info = end_event_date.getText().toString();
                    String start_date_info = CustomCalendarView.final_only_date_format.format(start_days.getTime());
                    if (end_date_info.equals(Event_Main_Activity.this.getString(R.string.select_end_date))) {
                        end_date_info = CustomCalendarView.final_only_date_format.format(start_days.getTime());
                    } else {
                        end_date_info = end_event_date.getText().toString();
                    }
                    Date date_start_day = CustomCalendarView.final_all_time_and_date_format.parse(start_date_info + " " + start_time_info);
                    Date date_end_day = CustomCalendarView.final_all_time_and_date_format.parse(end_date_info + " " + end_time_info);

                    if (child_id != 0)
                        interval = calculate_interval(Integer.valueOf(interval_value.getText().toString()), child_id);

                    Events event = new Events(event_name.getText().toString(), event_descripton.getText().toString(), event_location.getText().toString(),
                            interval, spinner.getSelectedItem().toString(),
                            date_start_day, date_end_day,
                            false, -1);

                    int event_id = save_event_to_db(event);
                    event.setEVENT_ID(event_id);
                    for (Reminder reminder : reminders)
                        reminder.setEVENT_ID(event_id);
                    int highest_reminder_id = save_reminders_to_db(reminders);
                    int i;
                    Calendar current_event_start_cal = set_calendar(event.getSTART_DATE());
                    Calendar current_event_end_cal = set_calendar(event.getEND_DATE());
                    ArrayList<Calendar> reminder_cals = new ArrayList<>();
                    int lowest_reminder_id = highest_reminder_id - reminders.size();
                    for (Reminder reminder : reminders) {
                        reminder.setREMIND_ID(lowest_reminder_id);
                        reminder_cals.add(set_calendar(reminder.getREMIND_DATE()));
                        set_alarm(reminder, reminder_cals.get(reminder_cals.size() - 1), reminder.getREMIND_ID(), interval);
                    }
                    ArrayList<Events> all_events = new ArrayList<>();
                    final ArrayList<Reminder> all_reminders = new ArrayList<>();
                    int n_interval = sharedPreferences.getInt("NUMBER_OF_INTERVAL", 1000);
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
                    if (interval != 0) {
                        for (i = 0; i < n_interval; i++) {
                            Log.e(TAG, "by");
                            ++event_id;
                            current_event_start_cal.add(Calendar.HOUR_OF_DAY, event.getINTERVAL());  // NEW EVENT START DATE
                            current_event_end_cal.add(Calendar.HOUR_OF_DAY, event.getINTERVAL());   // NEW EVENT END DATE
                            forward_event(all_events.get(i), current_event_start_cal, current_event_end_cal);
                            for (int j = i * reminders.size(); j < (i + 1) * reminders.size(); j++) {
                                all_reminders.get(j).setEVENT_ID(event_id);
                                reminder_cals.get(j % reminders.size()).add(Calendar.HOUR_OF_DAY, event.getINTERVAL());
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

                        Toast.makeText(Event_Main_Activity.this, "Her şey  HAZIR ", Toast.LENGTH_SHORT).show();


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

    private void reminder_show(final ArrayList<Reminder> reminders, final Context context1, final Reminder_Recyler_Adapter adapter_items, final String event_time) {
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
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context1,
                R.array.reminder_mp3s, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner2.setAdapter(adapter);
        final int pos = sharedPreferences.getInt("mp3", 0);
        spinner2.setSelection(pos);


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
                Toast.makeText(context, "Saat seçilecek", Toast.LENGTH_SHORT).show();
                Calendar calendar = Calendar.getInstance();
                int hours = calendar.get(Calendar.HOUR_OF_DAY);
                int minutes = calendar.get(Calendar.MINUTE);
                final int year = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH);
                final int day = calendar.get(Calendar.DAY_OF_MONTH);
                try {
                    Calendar cs = Calendar.getInstance();
                    cs.setTimeZone(TimeZone.getDefault());
                    cs.setTime(CustomCalendarView.final_only_time_format.parse(event_time));
                    cs.add(Calendar.MINUTE, -1 * Integer.valueOf(before_time));
                    hours = cs.get(Calendar.HOUR_OF_DAY);
                    minutes = cs.get(Calendar.MINUTE);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
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
                }, hours, minutes, false);
                timePickerDialog.show();
            }
        });
        reminder_Set_start_date.setOnClickListener(new View.OnClickListener() {
            //tarihButona Click Listener ekliyoruz

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int year = mcurrentTime.get(Calendar.YEAR);
                //Güncel Yılı alıyoruz
                int month = mcurrentTime.get(Calendar.MONTH);
                //Güncel Ayı alıyoruz
                int day = mcurrentTime.get(Calendar.DAY_OF_MONTH);
                //Güncel Günü alıyoruz

                final DatePickerDialog datePicker;//Datepicker objemiz
                datePicker = new DatePickerDialog(add_view_2.getContext(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        String strDate = CustomCalendarView.final_only_date_format.format(calendar.getTime());
                        // TODO Auto-generated method stub
                        reminder_date.setText(strDate);//Ayarla butonu tıklandığında textview'a yazdırıyoruz
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
                val = 1;
                break;
            }
            case 2: {
                val = 24;
                break;
            }
            case 3: {
                val = 24 * 7;
                break;
            }
            case 4: {
                val = 24 * 30;
                break;
            }
            case 5: {
                val = 365 * 24;
            }
        }
        return inteval * val;
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

    private void set_alarm(Reminder reminder, Calendar reminder_cal, int request_code, int interval) {
        Intent intent = new Intent(Event_Main_Activity.this, Alarm_Receiver.class);
        intent.putExtra("reminder_id", reminder.getREMIND_ID());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(Event_Main_Activity.this, request_code, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmManager = (AlarmManager) Event_Main_Activity.this.getSystemService(Context.ALARM_SERVICE);
        Log.i(TAG, reminder_cal.get(Calendar.HOUR) + ":" + reminder_cal.get(Calendar.MINUTE) + "--" + reminder_cal.get(Calendar.DAY_OF_MONTH) + "/" + reminder_cal.get(Calendar.MONTH) + "/" + reminder_cal.get(Calendar.YEAR));
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, reminder_cal.getTimeInMillis(), AlarmManager.INTERVAL_HOUR * interval, pendingIntent);
        //alarmManager.set(AlarmManager.RTC_WAKEUP,reminder_cal.getTimeInMillis() ,pendingIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_MAP_INFO) {
            try {
                if (data != null) {
                    event_location.setText(data.getStringExtra("location_info"));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


}


