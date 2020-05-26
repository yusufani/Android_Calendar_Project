package com.example.android_calendar_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class DBOpenHelper extends SQLiteOpenHelper {
    private final static String CREATE_EVENTS_TABLE = "CREATE TABLE "+DBStructure.EVENT_TABLE_NAME+"  (ID INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE," +
            DBStructure.EVENT_PARENT_ID+ " INTEGER NOT NULL ,"+
            DBStructure.EVENT_NAME+" TEXT NOT NULL,"+
            DBStructure.EVENT_DESCRIPTION+" TEXT ,"+
            DBStructure.EVENT_LOCATION +" TEXT ,"+
            DBStructure.EVENT_INTERVAL_TYPE + " INT NOT NULL," +
            DBStructure.EVENT_INTERVAL_VALUE + " INT NOT NULL," +
            DBStructure.EVENT_TYPE+" TEXT NOT NULL,"+
            DBStructure.EVENT_COLOR+" TEXT NOT NULL,"+
            DBStructure.EVENT_START_DATE + " DATE NOT NULL," +
            DBStructure.EVENT_END_DATE + " INTEGER ," +
            DBStructure.EVENT_DONE +" INTEGER NOT NULL"+
            ")";
    private final static String CREATE_REMINDERS_TABLE = "CREATE TABLE "+DBStructure.REMINDER_TABLE_NAME+"  (ID INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE," +
            DBStructure.EVENT_ID+ " INTEGER REFERENCES " +DBStructure.EVENT_TABLE_NAME+ String.format("(ID) ON DELETE CASCADE ,")+
            DBStructure.REMIND_DATE + " DATE NOT NULL," +
            DBStructure.REMIND_RING_TONE +" TEXT ,"+
            DBStructure.REMIND_PLAY_RINGTONE +" INTEGER NOT NULL ," +
            DBStructure.REMIND_VIBRATION +" INTEGER NOT NULL,"+
            DBStructure.REMIND_SHOW_NOTIFICATION +" INTEGER NOT NULL,"+
            DBStructure.REMIND_SHOW_ALERT_DIAOLOG +" INTEGER NOT NULL"+
            ")";
    private final static String DROP_EVENTS_TABLE = "DROP TABLE IF EXISTS "+DBStructure.EVENT_TABLE_NAME;
    String TAG = " DB OPEN HELPER";
    private final static String DROP_REMINDERS_TABLE = "DROP TABLE IF EXISTS "+DBStructure.REMINDER_TABLE_NAME;

    public DBOpenHelper(@Nullable Context context) {
        super(context, DBStructure.DB_NAME, null, DBStructure.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_EVENTS_TABLE);
        db.execSQL(CREATE_REMINDERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_EVENTS_TABLE);
        db.execSQL(DROP_REMINDERS_TABLE);
        onCreate(db);
    }

    public int integer_maker(Boolean bool) {
        if (bool) return 1;
        return 0;
    }
    public void save_Reminder(Reminder reminder, SQLiteDatabase database){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBStructure.REMIND_DATE, CustomCalendarView.final_all_time_and_date_format.format(reminder.getREMIND_DATE()));
        contentValues.put( DBStructure.REMIND_RING_TONE ,reminder.getRING_TONE() );
        contentValues.put(DBStructure.REMIND_PLAY_RINGTONE, integer_maker(reminder.getPLAY_RINGTONE()));
        contentValues.put(DBStructure.REMIND_SHOW_ALERT_DIAOLOG, integer_maker(reminder.getSHOW_ALERT_DIAOLOG()));
        contentValues.put(DBStructure.REMIND_SHOW_NOTIFICATION, integer_maker(reminder.getSHOW_NOTIFICATION()));
        contentValues.put(DBStructure.REMIND_VIBRATION, integer_maker(reminder.getVIBRATION()));
        contentValues.put(DBStructure.EVENT_ID, reminder.getEVENT_ID());
        database.insert(DBStructure.REMINDER_TABLE_NAME, null, contentValues);
    }
    public void save_Event(Events event  ,SQLiteDatabase database){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBStructure.EVENT_NAME, event.getEVENT_NAME());
        contentValues.put(DBStructure.EVENT_INTERVAL_TYPE, event.getINTERVAL_TYPE());
        contentValues.put(DBStructure.EVENT_INTERVAL_VALUE, event.getINTERVAL_VALUE());
        contentValues.put(DBStructure.EVENT_TYPE, event.getEVENT_TYPE());
        contentValues.put( DBStructure.EVENT_COLOR ,event.getEVENT_COLOR() );
        contentValues.put(DBStructure.EVENT_START_DATE, CustomCalendarView.final_all_time_and_date_format.format(event.getSTART_DATE()));
        contentValues.put(DBStructure.EVENT_END_DATE, CustomCalendarView.final_all_time_and_date_format.format(event.getEND_DATE()));
        contentValues.put(DBStructure.EVENT_DESCRIPTION, event.getEVENT_DESCRIPTION());
        contentValues.put(DBStructure.EVENT_DONE, event.getDONE());
        contentValues.put(DBStructure.EVENT_LOCATION, event.getLOCATION());
        contentValues.put(DBStructure.EVENT_PARENT_ID, event.getPARENT_EVENT_ID());
        database.insert(DBStructure.EVENT_TABLE_NAME, null,contentValues);
    }

    public void update_event(Events event, SQLiteDatabase database) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBStructure.EVENT_NAME, event.getEVENT_NAME());
        contentValues.put(DBStructure.EVENT_INTERVAL_TYPE, event.getINTERVAL_TYPE());
        contentValues.put(DBStructure.EVENT_INTERVAL_VALUE, event.getINTERVAL_VALUE());
        contentValues.put(DBStructure.EVENT_TYPE, event.getEVENT_TYPE());
        contentValues.put( DBStructure.EVENT_COLOR ,event.getEVENT_COLOR() );
        contentValues.put(DBStructure.EVENT_START_DATE, CustomCalendarView.final_all_time_and_date_format.format(event.getSTART_DATE()));
        contentValues.put(DBStructure.EVENT_END_DATE, CustomCalendarView.final_all_time_and_date_format.format(event.getEND_DATE()));
        contentValues.put(DBStructure.EVENT_DESCRIPTION, event.getEVENT_DESCRIPTION());
        contentValues.put(DBStructure.EVENT_DONE, event.getDONE());
        contentValues.put(DBStructure.EVENT_LOCATION, event.getLOCATION());
        contentValues.put(DBStructure.EVENT_PARENT_ID, event.getPARENT_EVENT_ID());
        String selection = "ID =?";
        String[] selectionArgs = {String.valueOf(event.getEVENT_ID())};
        database.update(DBStructure.EVENT_TABLE_NAME, contentValues, selection, selectionArgs);
    }

    public void delete_event(int id , SQLiteDatabase database){
        String selection = "ID =? OR "+DBStructure.EVENT_PARENT_ID +"=?";
        String[] selectionArgs = {String.valueOf(id), String.valueOf(id)};
        database.delete(DBStructure.EVENT_TABLE_NAME,selection,selectionArgs);
     }

     public void delete_reminders(int event_id , SQLiteDatabase database){
         String selection = DBStructure.EVENT_ID +"=?";
         String[] selectionArgs = {String.valueOf(event_id)};
         database.delete(DBStructure.REMINDER_TABLE_NAME,selection,selectionArgs);
     }

    public Cursor read_reminders(String id , SQLiteDatabase database){
        String[] Projections = get_reminder_projects();
        String Selections = DBStructure.EVENT_ID+"=?";
        String[] SelectionArgs = {id};
        return database.query(DBStructure.REMINDER_TABLE_NAME, Projections, Selections, SelectionArgs, null, null, null);
    }

    public Cursor read_spesific_reminder(String id, SQLiteDatabase database) {
        String[] Projections = get_reminder_projects();
        String Selections = "ID=?";
        String[] SelectionArgs = {id};
        return database.query(DBStructure.REMINDER_TABLE_NAME, Projections, Selections, SelectionArgs, null, null, null);
    }

    public Cursor read_spesific_event(String id, SQLiteDatabase database) {
        String[] Projections = get_event_projects();
        String Selections = "ID=?";
        String []  SelectionArgs = {id};
        return database.query(DBStructure.EVENT_TABLE_NAME, Projections,Selections,SelectionArgs,null,null,null);
    }

    public Cursor ReadEventperDate(Date date, SQLiteDatabase database) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        Date begin = c.getTime();
        c.add(Calendar.DAY_OF_MONTH, 1);
        Date end = c.getTime();
        String[] Projections = get_event_projects();
        String Selections = DBStructure.EVENT_START_DATE + ">? AND " + DBStructure.EVENT_START_DATE + "<?";
        String[] SelectionArgs = {CustomCalendarView.final_all_time_and_date_format.format(begin), CustomCalendarView.final_all_time_and_date_format.format(end)};
        Log.v(TAG, Selections.toLowerCase() + SelectionArgs[0].toLowerCase() + SelectionArgs[1].toLowerCase());
        return database.query(DBStructure.EVENT_TABLE_NAME, Projections,Selections,SelectionArgs,null,null,null);
    }

    public Cursor ReadEventperYear(Date date, SQLiteDatabase database) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        String[] Projections = get_event_projects();
        String Selections = "strftime('%Y'," + DBStructure.EVENT_START_DATE + ")" + "=?";
        String[] SelectionArgs = {String.valueOf(c.get(Calendar.YEAR))};
        return database.query(DBStructure.EVENT_TABLE_NAME, Projections,Selections,SelectionArgs,null,null,null);
    }

    public Cursor ReadEventperMonth(Date date, SQLiteDatabase database) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        String[] Projections = get_event_projects();
        String Selections = "strftime('%m'," + DBStructure.EVENT_START_DATE + ")" + "=? AND " + "strftime('%Y'," + DBStructure.EVENT_START_DATE + ")" + "=?";
        String val = "";
        int month = c.get(Calendar.MONTH) + 1;

        if (month < 10) val += "0";
        val += String.valueOf(month);
        String[] SelectionArgs = {val, String.valueOf(c.get(Calendar.YEAR))};
        Log.v(TAG, Selections.toLowerCase() + SelectionArgs[0].toLowerCase() + SelectionArgs[1].toLowerCase());
        return database.query(DBStructure.EVENT_TABLE_NAME, Projections, Selections, SelectionArgs, null, null, null);
    }

    public Cursor ReadEventperWeek(Date start_date, Date end_date, SQLiteDatabase database) {

        String[] Projections = get_event_projects();
        String Selections = DBStructure.EVENT_START_DATE + "<=? AND " + DBStructure.EVENT_START_DATE + ">=?";
        String ST = CustomCalendarView.final_all_time_and_date_format.format(start_date);
        String ED = CustomCalendarView.final_all_time_and_date_format.format(end_date);
        String[] SelectionArgs = {ED,ST };
        Log.v(TAG, Selections.toLowerCase() + SelectionArgs[0].toLowerCase() + SelectionArgs[1].toLowerCase());
        return database.query(DBStructure.EVENT_TABLE_NAME, Projections, Selections, SelectionArgs, null, null, null);
    }

    public String[] get_event_projects() {
        String[] projects = {"ID", DBStructure.EVENT_NAME, DBStructure.EVENT_PARENT_ID, DBStructure.EVENT_INTERVAL_TYPE,DBStructure.EVENT_INTERVAL_VALUE, DBStructure.EVENT_TYPE,DBStructure.EVENT_COLOR, DBStructure.EVENT_START_DATE, DBStructure.EVENT_END_DATE, DBStructure.EVENT_DESCRIPTION, DBStructure.EVENT_DONE, DBStructure.EVENT_LOCATION};
        return projects;
    }

    public String[] get_reminder_projects() {
        String[] projections = {"ID", DBStructure.EVENT_ID, DBStructure.REMIND_DATE, DBStructure.REMIND_PLAY_RINGTONE, DBStructure.REMIND_VIBRATION, DBStructure.REMIND_RING_TONE, DBStructure.REMIND_SHOW_ALERT_DIAOLOG, DBStructure.REMIND_SHOW_NOTIFICATION};
        return projections;
    }
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.setForeignKeyConstraintsEnabled (true) ; // Foreign key support
    }

    public int getHighestID(SQLiteDatabase database) {
        final String MY_QUERY = "SELECT last_insert_rowid()";
        Cursor cur = database.rawQuery(MY_QUERY, null);
        cur.moveToFirst();
        int ID = cur.getInt(0);
        cur.close();
        return ID;
    }

    public Events getEventFromCursor(Cursor cursor) {
        int EVENT_ID = cursor.getInt(cursor.getColumnIndex("ID"));
        String EVENT_NAME = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT_NAME));
        int EVENT_PARENT_ID = cursor.getInt(cursor.getColumnIndex(DBStructure.EVENT_PARENT_ID));
        String EVENT_DESCRIPTION = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT_DESCRIPTION));
        String LOCATION = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT_LOCATION));
        int INTERVAL_TYPE = cursor.getInt(cursor.getColumnIndex(DBStructure.EVENT_INTERVAL_TYPE));
        int INTERVAL_VALUE = cursor.getInt(cursor.getColumnIndex(DBStructure.EVENT_INTERVAL_VALUE));
        String EVENT_TYPE = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT_TYPE));
        String START_DATE_s = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT_START_DATE));
        String END_DATE_s = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT_END_DATE));
        String EVENT_COLOR = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT_COLOR));
        Date START_DATE = null, END_DATE = null;
        try {
            START_DATE = CustomCalendarView.final_all_time_and_date_format.parse(START_DATE_s);
            END_DATE = CustomCalendarView.final_all_time_and_date_format.parse(END_DATE_s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int done = cursor.getInt(cursor.getColumnIndex(DBStructure.EVENT_DONE));
        boolean DONE = false;
        if (done == 1)
            DONE = true;
        return new Events(EVENT_ID, EVENT_NAME, EVENT_COLOR,EVENT_DESCRIPTION, LOCATION, INTERVAL_TYPE,INTERVAL_VALUE ,EVENT_TYPE,
                START_DATE, END_DATE,
                DONE, EVENT_PARENT_ID);
    }

    public Reminder getReminderFromCursor(Cursor cursor) {
        Reminder reminder;
        int REMIND_ID = cursor.getInt(cursor.getColumnIndex("ID"));
        int EVENT_ID = cursor.getInt(cursor.getColumnIndex(DBStructure.EVENT_ID));
        String REMIND_date_s = cursor.getString(cursor.getColumnIndex(DBStructure.REMIND_DATE));
        Date REMIND_DATE = null;
        try {
            REMIND_DATE = CustomCalendarView.final_all_time_and_date_format.parse(REMIND_date_s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String RING_TONE = cursor.getString(cursor.getColumnIndex(DBStructure.REMIND_RING_TONE));
        Boolean SHOW_NOTIFICATION = CustomCalendarView.get_bool(cursor.getColumnIndex(DBStructure.REMIND_SHOW_NOTIFICATION));
        Boolean SHOW_ALERT_DIAOLOG = CustomCalendarView.get_bool(cursor.getColumnIndex(DBStructure.REMIND_SHOW_ALERT_DIAOLOG));
        Boolean VIBRATION = CustomCalendarView.get_bool(cursor.getColumnIndex(DBStructure.REMIND_VIBRATION));
        Boolean PLAY_RINGTONE = CustomCalendarView.get_bool(cursor.getColumnIndex(DBStructure.REMIND_PLAY_RINGTONE));
        reminder = new Reminder(REMIND_DATE,
                RING_TONE, SHOW_NOTIFICATION, SHOW_ALERT_DIAOLOG, VIBRATION, PLAY_RINGTONE, true, REMIND_ID, EVENT_ID);

        return reminder;
    }

    public Cursor read_all_events(SQLiteDatabase database) {
        String[] Projections = get_event_projects();
        String Selections = "";
        String[] SelectionArgs = {};
        String order = DBStructure.EVENT_START_DATE + " ASC";
        return database.query(DBStructure.EVENT_TABLE_NAME, Projections, Selections, SelectionArgs, null, null, order);
    }
    public int get_number_of_events(SQLiteDatabase db){
        String countQuery = "SELECT  * FROM " + DBStructure.EVENT_TABLE_NAME;
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
    public int get_number_of_reminders(SQLiteDatabase db){
        String countQuery = "SELECT  * FROM " + DBStructure.REMINDER_TABLE_NAME;
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
    public int get_number_of_completed_events(SQLiteDatabase db){
        String[] Projections = get_event_projects();
        String Selections = DBStructure.EVENT_DONE + "=?";
        String[] SelectionArgs = {"1"};
        Cursor cursor =  db.query(DBStructure.EVENT_TABLE_NAME, Projections, Selections, SelectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public Cursor get_most_common_color_of_events(SQLiteDatabase db){
        String[] Projections = { DBStructure.EVENT_COLOR  , "Count("+ DBStructure.EVENT_COLOR+"  )"} ;
        String group = DBStructure.EVENT_COLOR;
        String order =  "Count("+ DBStructure.EVENT_COLOR+"  )"+"DESC";
        return db.query(DBStructure.EVENT_TABLE_NAME, Projections, null, null, group, null, order);
    }
    public Cursor get_most_common_mp3_of_reminders(SQLiteDatabase db){
        String[] Projections = { DBStructure.REMIND_RING_TONE  , "Count("+ DBStructure.REMIND_RING_TONE+"  )"} ;
        String group = DBStructure.REMIND_RING_TONE;
        String order = "Count("+ DBStructure.REMIND_RING_TONE+"  )"+"DESC";
        return db.query(DBStructure.REMINDER_TABLE_NAME, Projections, null, null, group, null, order);
    }
    public int get_interval_rate(SQLiteDatabase db){
        String[] Projections = get_event_projects();
        String Selections = DBStructure.EVENT_INTERVAL_TYPE + "=?";
        String[] SelectionArgs = {"0"};
        Cursor cursor =  db.query(DBStructure.EVENT_TABLE_NAME, Projections, Selections, SelectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

}
