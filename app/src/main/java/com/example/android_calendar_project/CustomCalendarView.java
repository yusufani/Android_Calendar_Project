package com.example.android_calendar_project;
//TODO ZAMAN KALIRSA LAYOUTLAR ARASINA ÇİZGİLER EKLE AYRI OLSUN

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class CustomCalendarView extends LinearLayout {
    Spinner spinner;
    ImageButton next_button, previous_button;
    TextView current_date;
    GridView gridView;
    GridAdapter gridAdapter;
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.ENGLISH);
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);
    SimpleDateFormat eventDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    private static final int MAX_CALENDAR_DAY = 42;
    Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
    DBOpenHelper dbOpenHelper;
    AlertDialog alertDialog;
    Context context;
    List<Date> dates = new ArrayList<>();
    List<Events> eventsList = new ArrayList<>();

    public CustomCalendarView(Context context) {
        super(context);
    }

    public CustomCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomCalendarView(final Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initializeLayout();
        set_up_calendar();
        previous_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, -1);
                set_up_calendar();
            }
        });
        next_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, 1);
                set_up_calendar();
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                final View add_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_new_event, null);
                final EditText event_name = add_view.findViewById(R.id.add_event_name);
                final EditText event_descripton = add_view.findViewById(R.id.add_event_desc);
                final EditText event_location = add_view.findViewById(R.id.location_text);
                final LinearLayout interval_layout = add_view.findViewById(R.id.interval_layout);
                final TextView start_event_time = add_view.findViewById(R.id.event_start_time);
                final TextView end_event_time = add_view.findViewById(R.id.event_end_time);
                final EditText times = add_view.findViewById(R.id.get_times);
                final RadioGroup radio_interval_type = add_view.findViewById(R.id.interval_type);
                final EditText interval_value = add_view.findViewById(R.id.interval_value);
                final TextView interval_value_text = add_view.findViewById(R.id.interval_type_text);
                ImageButton set_start_time = add_view.findViewById(R.id.set_event_start_time);
                ImageButton set_end_time = add_view.findViewById(R.id.set_event_end_time);
                final Spinner spinner = add_view.findViewById(R.id.spinner1);
                // Create an ArrayAdapter using the string array and a default spinner layout
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                        R.array.event_types_spinner, android.R.layout.simple_spinner_item);
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                spinner.setAdapter(adapter);
                Button add_event = add_view.findViewById(R.id.add_event);
                radio_interval_type.check(1);
                interval_layout.setVisibility(View.GONE);
                radio_interval_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        checkedId %=6;
                        Toast.makeText(context,"Çocuk"+checkedId , Toast.LENGTH_SHORT).show();
                        if (checkedId == 1 || checkedId==0) {
                            interval_layout.setVisibility(View.GONE);
                        } else {
                            interval_layout.setVisibility(View.VISIBLE);
                            RadioButton r = (RadioButton) radio_interval_type.getChildAt(checkedId-1);

                            interval_value_text.setText(r.getText().toString());
                        }
                    }
                });
                radio_interval_type.check(1);
                set_start_time.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context,"Saat seçilecek", Toast.LENGTH_SHORT).show();
                        Calendar calendar = Calendar.getInstance();
                        int hours = calendar.get(Calendar.HOUR_OF_DAY);
                        int minutes = calendar.get(Calendar.MINUTE);

                        final int year = calendar.get(Calendar.YEAR);
                        final int month = calendar.get(Calendar.MONTH);
                        final int day = calendar.get(Calendar.DAY_OF_MONTH);

                        TimePickerDialog timePickerDialog = new TimePickerDialog(add_view.getContext(), R.style.Theme_AppCompat_DayNight_Dialog
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
                                SimpleDateFormat hformate = new SimpleDateFormat("K:mm a", Locale.ENGLISH);
                                String event_time = hformate.format(c.getTime());
                                start_event_time.setText(event_time);
                            }
                        }, hours, minutes, false);
                        timePickerDialog.show();
                    }
                });
                set_end_time.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Calendar calendar = Calendar.getInstance();
                        int hours = calendar.get(Calendar.HOUR_OF_DAY);
                        int minutes = calendar.get(Calendar.MINUTE);
                        TimePickerDialog timePickerDialog = new TimePickerDialog(add_view.getContext(), R.style.Theme_AppCompat_DayNight_Dialog
                                , new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                Calendar c = Calendar.getInstance();
                                c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                c.set(Calendar.MINUTE, minute);
                                c.setTimeZone(TimeZone.getDefault());
                                SimpleDateFormat hformate = new SimpleDateFormat("K:mm a", Locale.ENGLISH);
                                String event_time = hformate.format(c.getTime());
                                end_event_time.setText(event_time);
                            }
                        }, hours, minutes, false);
                        timePickerDialog.show();
                    }
                });
                add_event.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //TODO END DATE SORUNUNU ÇÖZ
                        //String[] end_dates = end_event_time.getText().toString().split("-");

                            try {
                                int child_id = radio_interval_type.getCheckedRadioButtonId();
                                Log.d("SAD" , String.valueOf(child_id));
                                Double interval = 0.0;

                                String start_date = eventDateFormat.format(dates.get(position));
                                String start_month = monthFormat.format(dates.get(position));
                                String start_year = yearFormat.format(dates.get(position));
                                if (child_id != 1)
                                    interval = calculate_interval(Double.valueOf(times.getText().toString()), Double.valueOf(interval_value.getText().toString()), child_id);
                                Events event = new Events(event_name.getText().toString(),event_descripton.getText().toString(),event_location.getText().toString(),
                                        interval, spinner.getSelectedItem().toString(),
                                        start_event_time.getText().toString(), start_date, start_month, start_year,
                                        start_event_time.getText().toString(), start_date, start_month, start_year,
                                        false,-1);
                                save_event_to_db(event);
                                set_up_calendar();
                                alertDialog.dismiss();

                                //radio_interval_type.clearCheck();
                            }catch (Exception e){
                                e.printStackTrace();
                                Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                    }
                });
                builder.setView(add_view);
                alertDialog = builder.create();
                alertDialog.show();
            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                            @Override
                                            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                                                String date  = eventDateFormat.format(dates.get(position));
                                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                                builder.setCancelable(true);
                                                View showView = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_events, null);
                                                RecyclerView recyclerView = showView.findViewById(R.id.events_RV);
                                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(showView.getContext());
                                                recyclerView.setLayoutManager(layoutManager);
                                                recyclerView.setHasFixedSize(true);
                                                EventsRecyclerAdapter eventsRecyclerAdapter = new EventsRecyclerAdapter(showView.getContext(),collectEventsByDate(date));
                                                recyclerView.setAdapter(eventsRecyclerAdapter);
                                                eventsRecyclerAdapter.notifyDataSetChanged();

                                                builder.setView(showView);
                                                alertDialog = builder.create();
                                                alertDialog.show();

                                                alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                                    @Override
                                                    public void onCancel(DialogInterface dialog) {
                                                        set_up_calendar();
                                                    }
                                                });
                                                return true;
                                            }
                                        }
        );
    }
    private ArrayList<Events> collectEventsByDate(String date){
        ArrayList<Events> arrayList = new ArrayList<>();
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadEventperDate(date,database);
        while (cursor.moveToNext()) {
            int EVENT_ID = cursor.getInt(cursor.getColumnIndex("ID"));
            String EVENT_NAME = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT_NAME));
            int EVENT_PARENT_ID = cursor.getInt(cursor.getColumnIndex(DBStructure.EVENT_PARENT_ID));
            String EVENT_DESCRIPTION = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT_DESCRIPTION));
            String LOCATION = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT_LOCATION));
            Double FREQUENCY = cursor.getDouble(cursor.getColumnIndex(DBStructure.EVENT_FREQUENCY));
            String EVENT_TYPE = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT_TYPE));
            String START_TIME = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT_START_TIME));
            String START_DATE = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT_START_DATE));
            String START_MONTH = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT_START_MONTH));
            String START_YEAR = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT_START_YEAR));
            String END_TIME = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT_END_TIME));
            String END_DATE = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT_END_DATE));
            String END_MONTH = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT_END_MONTH));
            String END_YEAR = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT_END_YEAR));
            int done = cursor.getInt(cursor.getColumnIndex(DBStructure.EVENT_NAME));
            boolean DONE = false;
            if (done == 1 ) DONE= true;
            Events events = new Events(EVENT_ID,EVENT_NAME,EVENT_DESCRIPTION,LOCATION,FREQUENCY,EVENT_TYPE,START_TIME,START_DATE, START_MONTH,START_YEAR,END_TIME,END_DATE,END_MONTH,END_YEAR,DONE,EVENT_PARENT_ID);
            arrayList.add(events);
        }
        cursor.close();
        dbOpenHelper.close();

        return arrayList;
    }

    private void initializeLayout() {
        Log.e("AS", "ASDAS");
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.monthly_calendar_layout, this);
        next_button = view.findViewById(R.id.nextBtn);
        previous_button = view.findViewById(R.id.previousBtn);
        current_date = view.findViewById(R.id.current_date);
        gridView = view.findViewById(R.id.monthly_grid);
    }

    private void set_up_calendar() {
        String currwntDate = dateFormat.format(calendar.getTime());
        current_date.setText(currwntDate);
        dates.clear();
        Calendar month_Calendar = (Calendar) calendar.clone();
        month_Calendar.set(Calendar.DAY_OF_MONTH, 1);
        int FirstDayOfMont = month_Calendar.get(Calendar.DAY_OF_WEEK) - 1;
        month_Calendar.add(Calendar.DAY_OF_MONTH, -FirstDayOfMont);
        collect_events_per_month(monthFormat.format(calendar.getTime()) , yearFormat.format(calendar.getTime()));
        while (dates.size() < MAX_CALENDAR_DAY) {
            dates.add(month_Calendar.getTime());
            month_Calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        gridAdapter = new GridAdapter(context, dates, calendar, eventsList);
        gridView.setAdapter(gridAdapter);

    }

    private Double calculate_interval(Double times, Double inteval, int child_id) {
        double val = 0.00;
        switch (child_id) {
            case 2: {
                val = 1;
                break;
            }
            case 3: {
                val = 7.0;
                break;
            }
            case 4: {
                val = 30;
                break;
            }
            case 5: {
                val = 365;
                break;
            }
        }
        return inteval * val * 24.0 * 60 * 60 * 1000.0 / times;
    }

    private void save_event_to_db(Events event) {
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.save_Event(event, database);
        dbOpenHelper.close();
        Toast.makeText(context, "Event Saved", Toast.LENGTH_SHORT).show();
    }

    private void collect_events_per_month(String month, String year) {
        eventsList.clear();
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadEventperMonth(month, year, database);
        while (cursor.moveToNext()) {
            int EVENT_ID = cursor.getInt(cursor.getColumnIndex("ID"));
            String EVENT_NAME = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT_NAME));
            int EVENT_PARENT_ID = cursor.getInt(cursor.getColumnIndex(DBStructure.EVENT_PARENT_ID));
            String EVENT_DESCRIPTION = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT_DESCRIPTION));
            String LOCATION = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT_LOCATION));
            Double FREQUENCY = cursor.getDouble(cursor.getColumnIndex(DBStructure.EVENT_FREQUENCY));
            String EVENT_TYPE = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT_TYPE));
            String START_TIME = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT_START_TIME));
            String START_DATE = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT_START_DATE));
            String START_MONTH = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT_START_MONTH));
            String START_YEAR = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT_START_YEAR));
            String END_TIME = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT_END_TIME));
            String END_DATE = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT_END_DATE));
            String END_MONTH = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT_END_MONTH));
            String END_YEAR = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT_END_YEAR));
            int done = cursor.getInt(cursor.getColumnIndex(DBStructure.EVENT_NAME));
            boolean DONE = false;
            if (done == 1 ) DONE= true;
            Events events = new Events(EVENT_ID,EVENT_NAME,EVENT_DESCRIPTION,LOCATION,FREQUENCY,EVENT_TYPE,START_TIME,START_DATE, START_MONTH,START_YEAR,END_TIME,END_DATE,END_MONTH,END_YEAR,DONE,EVENT_PARENT_ID);
            eventsList.add(events);
        }
        cursor.close();
        dbOpenHelper.close();
    }

}
