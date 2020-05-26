package com.example.android_calendar_project;
//TODO ZAMAN KALIRSA LAYOUTLAR ARASINA ÇİZGİLER EKLE AYRI OLSUN
// TODO SNACKBAR EKLE
// TODO NOTIFICATIN 15 DK SONRA CALISMIYOR

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.AttributeSet;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.Toolbar;


import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CustomCalendarView extends LinearLayout {
    public static  int month_height_pixel ;
    private static final int MAX_CALENDAR_DAY = 42;
    public static SimpleDateFormat final_all_time_and_date_format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static SimpleDateFormat final_only_date_format = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat final_only_time_format = new SimpleDateFormat("HH:mm");
    static ArrayList<TextView> weeks = new ArrayList<>();
    FloatingActionButton fab ;
    Calendar date;
    static TextView current_date_year, current_date_month;
    static GridView gridView;
    static GridAdapter gridAdapter;
    static SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.ENGLISH);
    static SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);
    static SimpleDateFormat eventDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    static Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
    static DBOpenHelper dbOpenHelper;
    static Context context;
    static List<Date> dates = new ArrayList<>();
    static List<Events> eventsList = new ArrayList<>();
    ArrayList<String> months = get_months();
    TextView week1, week2, week3, week4, week5, week6;
    String TAG = "CUSTOM_CALENDAR_VIEW";
    ImageButton next_button, previous_button;
    static AlertDialog alertDialog;
    LinearLayout all_screen;

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
    }

    public static void set_up_calendar() {
        String currwntDate = monthFormat.format(calendar.getTime());
        current_date_month.setText(currwntDate);
        currwntDate = yearFormat.format(calendar.getTime());
        current_date_year.setText(currwntDate);
        dates.clear();
        Calendar month_Calendar = (Calendar) calendar.clone();
        month_Calendar.set(Calendar.DAY_OF_MONTH, 1);
        int FirstDayOfMont = month_Calendar.get(Calendar.DAY_OF_WEEK) - 1;
        month_Calendar.add(Calendar.DAY_OF_MONTH, -FirstDayOfMont);
        collect_events_per_month(calendar.getTime());
        int j = 0;
        while (dates.size() < MAX_CALENDAR_DAY) {
            if (dates.size() % 7 == 0) {
                weeks.get(j).setText(String.valueOf(month_Calendar.get(Calendar.WEEK_OF_YEAR)));
                j++;
            }
            dates.add(month_Calendar.getTime());
            month_Calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        gridAdapter = new GridAdapter(context, dates, calendar, eventsList);
        gridView.setAdapter(gridAdapter);
    }

    private static void collect_events_per_month(Date date) {
        eventsList.clear();
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadEventperMonth(date, database);
        while (cursor.moveToNext()) {
            Events events = dbOpenHelper.getEventFromCursor(cursor);
            eventsList.add(events);
        }
        cursor.close();
        dbOpenHelper.close();
    }

    public static Events get_event(int event_id, Context context) {
        Events event = null;
        DBOpenHelper dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.read_spesific_event(String.valueOf(event_id), database);
        while (cursor.moveToNext()) {
            event = dbOpenHelper.getEventFromCursor(cursor);
        }
        cursor.close();
        dbOpenHelper.close();
        return event;
    }

    public static Reminder get_remind(int remid_id, Context context) {
        Reminder reminder = null;
        DBOpenHelper dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.read_spesific_reminder(String.valueOf(remid_id), database);
        while (cursor.moveToNext()) {
            reminder = dbOpenHelper.getReminderFromCursor(cursor);
        }
        cursor.close();
        dbOpenHelper.close();
        return reminder;
    }

    public static ArrayList<Reminder> get_reminders(int event_id, Context context) {
        ArrayList<Reminder> reminders = new ArrayList<>();
        DBOpenHelper dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.read_reminders(String.valueOf(event_id), database);
        while (cursor.moveToNext()) {
            reminders.add(dbOpenHelper.getReminderFromCursor(cursor));

        }
        cursor.close();
        dbOpenHelper.close();
        return reminders;
    }

    public static void update_event(Events event, Context context) {
        DBOpenHelper dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        dbOpenHelper.update_event(event, database);
        dbOpenHelper.close();
    }

    public static Boolean get_bool(int val) {
        return val >= 1;
    }

    public static ArrayList<Events> collectsAllevents() {
        ArrayList<Events> arrayList = new ArrayList<>();
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.read_all_events(database);
        while (cursor.moveToNext()) {
            Events event = dbOpenHelper.getEventFromCursor(cursor);
            arrayList.add(event);
        }
        cursor.close();
        dbOpenHelper.close();
        return arrayList;
    }

    private static ArrayList<Events> collectEventsByDate(Date date) {
        ArrayList<Events> arrayList = new ArrayList<>();
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadEventperDate(date, database);
        while (cursor.moveToNext()) {
            Events event = dbOpenHelper.getEventFromCursor(cursor);
            arrayList.add(event);
        }
        cursor.close();
        dbOpenHelper.close();
        return arrayList;
    }

    private void initializeLayout() {
        Log.e(TAG, TAG);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = layoutInflater.inflate(R.layout.monthly_calendar_layout, this);
        all_screen = view.findViewById(R.id.activity_monthly_calendar_layout);
        final LinearLayout linearLayout = view.findViewById(R.id.monthly_linear);
        final OnLayoutChangeListener a  ;
         a = new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                // Vertical fill gridadapter for days
                month_height_pixel = v.getHeight();
                gridAdapter.notifyDataSetChanged();
                for (TextView textView : weeks)
                    textView.setMinimumHeight(month_height_pixel);
                linearLayout.removeOnLayoutChangeListener(this);
                set_listeners();
            }
        };
        linearLayout.addOnLayoutChangeListener(a);
        month_height_pixel = linearLayout.getLayoutParams().height;
        week1 = view.findViewById(R.id.CUSTOM_CAL_week_no_1);
        week2 = view.findViewById(R.id.CUSTOM_CAL_week_no_2);
        week3 = view.findViewById(R.id.CUSTOM_CAL_week_no_3);
        week4 = view.findViewById(R.id.CUSTOM_CAL_week_no_4);
        week5 = view.findViewById(R.id.CUSTOM_CAL_week_no_5);
        week6 = view.findViewById(R.id.CUSTOM_CAL_week_no_6);
        weeks.add(week1);
        weeks.add(week2);
        weeks.add(week3);
        weeks.add(week4);
        weeks.add(week5);
        weeks.add(week6);
        next_button = view.findViewById(R.id.nextBtn);
        previous_button = view.findViewById(R.id.previousBtn);
        current_date_year = view.findViewById(R.id.current_date_year);
        current_date_month = view.findViewById(R.id.current_date_month);
        gridView = view.findViewById(R.id.monthly_grid);
        fab = view.findViewById(R.id.fab);

    }

    private void set_listeners() {
        set_adapter_Static_listeners();




        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Snackbar.make(all_screen,"Please Select New Event Informations",Snackbar.LENGTH_SHORT).show();
                    showDateTimePicker(view);
                }catch (Exception e ){
                    e.printStackTrace();
                }

            }

        });
        previous_button.setOnClickListener(new OnClickListener() {
            @Override
            public  void onClick(View v) {
                Snackbar.make(all_screen,"Previous Month",Snackbar.LENGTH_SHORT).show();

                calendar.add(Calendar.MONTH, -1);
                set_up_calendar();
            }
        });
        next_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(all_screen,"Next Month",Snackbar.LENGTH_SHORT).show();
                calendar.add(Calendar.MONTH, 1);
                set_up_calendar();
            }
        });

        current_date_year.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String date = current_date_year.getText().toString();
                Toast.makeText(getContext(),date+" year's  events are listed ",Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                View showView = LayoutInflater.from(context).inflate(R.layout.show_events, null);
                RecyclerView recyclerView = showView.findViewById(R.id.events_RV);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(showView.getContext());
                recyclerView.setLayoutManager(layoutManager);

                recyclerView.setHasFixedSize(true);
                EventsRecyclerAdapter eventsRecyclerAdapter = null;
                Date year = null;
                try {
                    year = final_only_date_format.parse(date + "-01-01");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                eventsRecyclerAdapter = new EventsRecyclerAdapter(showView.getContext(), collectEventsByYear(year));

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
        });
        current_date_month.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String years = current_date_year.getText().toString();
                int month = get_months().indexOf(current_date_month.getText().toString())+1;
                String months = "";
                if (month < 10) months += "0";
                months += month;

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                View showView = LayoutInflater.from(context).inflate(R.layout.show_events, null);
                RecyclerView recyclerView = showView.findViewById(R.id.events_RV);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(showView.getContext());
                recyclerView.setLayoutManager(layoutManager);

                recyclerView.setHasFixedSize(true);
                EventsRecyclerAdapter eventsRecyclerAdapter = null;
                Date year = null;
                Toast.makeText(getContext(),current_date_month.getText().toString()+" month's events are listed ",Toast.LENGTH_SHORT).show();

                try {
                    year = final_only_date_format.parse(years + "-" + months + "-01");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                eventsRecyclerAdapter = new EventsRecyclerAdapter(showView.getContext(), collects_Events_by_spesific_month(year));

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
        });
        for (final TextView week : weeks)
            week.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    show_all_events_per_week(week.getText().toString());
                    return true;
                }
            });

    }

    private void show_all_events_per_week(String week_no) {
        Toast.makeText(getContext(),"Events of the +" + week_no + "th week of the year are listed",Toast.LENGTH_SHORT).show();

        String year = current_date_year.getText().toString();

        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        cal.set(Calendar.YEAR, Integer.parseInt(year));
        cal.set(Calendar.WEEK_OF_YEAR, Integer.parseInt(week_no));
        int saturday = cal.get(Calendar.DAY_OF_MONTH);
        int saturday_month = cal.get(Calendar.MONTH) + 1;
        int year_end = cal.get(Calendar.YEAR);
        cal.set(Calendar.HOUR_OF_DAY,23);
        cal.set(Calendar.MINUTE,59);

        Date end = new Date();
        end.setTime(cal.getTimeInMillis());
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        cal.set(Calendar.YEAR, Integer.parseInt(year));
        cal.set(Calendar.WEEK_OF_YEAR, Integer.parseInt(week_no)-1);
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        Date start = new Date();
        start.setTime(cal.getTimeInMillis());
        int sunday = cal.get(Calendar.DAY_OF_MONTH);
        int sunday_month = cal.get(Calendar.MONTH) + 1;
        int year_start = cal.get(Calendar.YEAR);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        View showView = LayoutInflater.from(context).inflate(R.layout.show_events, null);
        RecyclerView recyclerView = showView.findViewById(R.id.events_RV);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(showView.getContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);
        EventsRecyclerAdapter eventsRecyclerAdapter = null;
        eventsRecyclerAdapter = new EventsRecyclerAdapter(showView.getContext(), collectEventsperWeek(start, end));
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
    }

    private ArrayList<Events> collectEventsperWeek(Date start_date, Date end_date) {
        ArrayList<Events> arrayList = new ArrayList<>();
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadEventperWeek(start_date, end_date, database);
        while (cursor.moveToNext()) {
            Events events = dbOpenHelper.getEventFromCursor(cursor);
            arrayList.add(events);
        }
        cursor.close();
        dbOpenHelper.close();

        return arrayList;
    }

    private ArrayList<Events> collectEventsByYear(Date date) {
        ArrayList<Events> arrayList = new ArrayList<>();
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadEventperYear(date, database);
        while (cursor.moveToNext()) {
            Events events = dbOpenHelper.getEventFromCursor(cursor);
            arrayList.add(events);
        }
        cursor.close();
        dbOpenHelper.close();
        return arrayList;
    }

    private ArrayList<Events> collects_Events_by_spesific_month(Date date) {
        ArrayList<Events> events = new ArrayList<>();
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadEventperMonth(date, database);
        while (cursor.moveToNext()) {
            Events event = dbOpenHelper.getEventFromCursor(cursor);
            events.add(event);
        }
        cursor.close();
        dbOpenHelper.close();
        return events;
    }

    private ArrayList<String> get_months() {
        ArrayList<String> months = new ArrayList<String>();
        months.add("January");
        months.add("February");
        months.add("March");
        months.add("April");
        months.add("May");
        months.add("June");
        months.add("July");
        months.add("August");
        months.add("September");
        months.add("October");
        months.add("November");
        months.add("December");
        return months;
    }

    public void showDateTimePicker(View view) {
        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();
        new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        Log.v("Date", "The choosen one " + date.getTime());
                        Date send_date = date.getTime();
                        Intent new_inten = new Intent(view.getContext(), Event_Main_Activity.class);
                        new_inten.putExtra("selected_date", send_date);
                        Activity activity = (Activity) context;
                        activity.startActivityForResult(new_inten, 212);

                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }


    public static  void set_adapter_Static_listeners(){
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Intent new_inten = new Intent(context, Event_Main_Activity.class);
                new_inten.putExtra("selected_date", dates.get(position));
                Activity activity = (Activity) context;
                activity.startActivityForResult(new_inten, 212);
            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                                @Override
                                                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                                                    String date = eventDateFormat.format(dates.get(position));
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                                    builder.setCancelable(true);
                                                    View showView = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_events, null);
                                                    RecyclerView recyclerView = showView.findViewById(R.id.events_RV);
                                                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(showView.getContext());
                                                    recyclerView.setLayoutManager(layoutManager);

                                                    recyclerView.setHasFixedSize(true);
                                                    String[] infos = date.split("-");
                                                    EventsRecyclerAdapter eventsRecyclerAdapter = new EventsRecyclerAdapter(showView.getContext(), collectEventsByDate(dates.get(position)));
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
    public static void set_theme(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        boolean dark_mode = sharedPreferences.getBoolean("dark_mode",true);
        if (dark_mode){
            context.setTheme(R.style.dark_theme);
        }else{
            context.setTheme(R.style.AppTheme);
        }
    }
}
