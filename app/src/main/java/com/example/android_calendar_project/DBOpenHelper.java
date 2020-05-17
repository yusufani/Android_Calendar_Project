package com.example.android_calendar_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.strictmode.SqliteObjectLeakedViolation;

import androidx.annotation.Nullable;

public class DBOpenHelper extends SQLiteOpenHelper {
    private final static String CREATE_EVENTS_TABLE = "CREATE TABLE "+DBStructure.EVENT_TABLE_NAME+"  (ID INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE," +
            DBStructure.EVENT_PARENT_ID+ " INTEGER NOT NULL ,"+
            DBStructure.EVENT_NAME+" TEXT NOT NULL,"+
            DBStructure.EVENT_DESCRIPTION+" TEXT ,"+
            DBStructure.EVENT_LOCATION +" TEXT ,"+
            DBStructure.EVENT_FREQUENCY +" REAL NOT NULL,"+
            DBStructure.EVENT_TYPE+" TEXT NOT NULL,"+
            DBStructure.EVENT_START_TIME +" TEXT NOT NULL,"+
            DBStructure.EVENT_START_DATE +" TEXT NOT NULL,"+
            DBStructure.EVENT_START_MONTH +" TEXT NOT NULL,"+
            DBStructure.EVENT_START_YEAR +" TEXT NOT NULL,"+
            DBStructure.EVENT_END_TIME +" TEXT ,"+
            DBStructure.EVENT_END_DATE +" TEXT ,"+
            DBStructure.EVENT_END_MONTH +" TEXT ,"+
            DBStructure.EVENT_END_YEAR +" TEXT ,"+
            DBStructure.EVENT_DONE +" INTEGER NOT NULL"+
            ")";
    private final static String DROP_EVENTS_TABLE = "DROP TABLE IF EXISTS "+DBStructure.EVENT_TABLE_NAME;
    private final static String CREATE_REMINDERS_TABLE = "CREATE TABLE "+DBStructure.REMINDER_TABLE_NAME+"  (ID INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE," +
            DBStructure.EVENT_ID+ " INTEGER REFERENCES " +DBStructure.EVENT_TABLE_NAME+ String.format("(ID) ON DELETE CASCADE ,")+
            DBStructure.REMIND_TIME+" INTEGER NOT NULL,"+
            DBStructure.REMIND_DATE+" TEXT NOT NULL,"+
            DBStructure.REMIND_MONTH+" TEXT NOT NULL,"+
            DBStructure.REMIND_YEAR+" TEXT NOT NULL,"+
            DBStructure.REMIND_RING_TONE +" TEXT ,"+
            DBStructure.REMIND_PLAY_RINGTONE +" INTEGER NOT NULL ," +
            DBStructure.REMIND_VIBRATION +" INTEGER NOT NULL,"+
            DBStructure.REMIND_SHOW_NOTIFICATION +" INTEGER NOT NULL,"+
            DBStructure.REMIND_SHOW_ALERT_DIAOLOG +" INTEGER NOT NULL"+
            ")";
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
    public void save_Reminder(Reminder reminder, SQLiteDatabase database){
        ContentValues contentValues = new ContentValues();
        contentValues.put( DBStructure.REMIND_DATE ,reminder.getREMIND_DATE());
        contentValues.put( DBStructure.REMIND_MONTH ,reminder.getREMIND_MONTH() );
        contentValues.put( DBStructure.REMIND_RING_TONE ,reminder.getRING_TONE() );
        contentValues.put( DBStructure.REMIND_PLAY_RINGTONE ,reminder.getPLAY_RINGTONE() );
        contentValues.put( DBStructure.REMIND_SHOW_ALERT_DIAOLOG ,reminder.getSHOW_ALERT_DIAOLOG() );
        contentValues.put( DBStructure.REMIND_SHOW_NOTIFICATION ,reminder.getSHOW_NOTIFICATION() );
        contentValues.put( DBStructure.REMIND_TIME ,reminder.getREMIND_TIME() );
        contentValues.put( DBStructure.REMIND_YEAR ,reminder.getREMIND_YEAR() );
        contentValues.put( DBStructure.REMIND_VIBRATION ,reminder.getVIBRATION() );
        database.insert(DBStructure.EVENT_TABLE_NAME, null,contentValues);
    }
    public void save_Event(Events event  ,SQLiteDatabase database){
        ContentValues contentValues = new ContentValues();
        contentValues.put( DBStructure.EVENT_NAME ,event.getEVENT_NAME() );
        contentValues.put( DBStructure.EVENT_FREQUENCY,event.getFREQUENCY() );
        contentValues.put( DBStructure.EVENT_TYPE ,event.getEVENT_TYPE() );
        contentValues.put( DBStructure.EVENT_START_TIME,event.getSTART_TIME() );
        contentValues.put( DBStructure.EVENT_START_DATE, event.getSTART_DATE());
        contentValues.put( DBStructure.EVENT_START_MONTH, event.getSTART_MONTH());
        contentValues.put( DBStructure.EVENT_START_YEAR, event.getSTART_YEAR());
        contentValues.put( DBStructure.EVENT_END_DATE, event.getEND_DATE());
        contentValues.put( DBStructure.EVENT_END_MONTH,event.getEND_MONTH() );
        contentValues.put( DBStructure.EVENT_END_TIME, event.getEND_TIME());
        contentValues.put( DBStructure.EVENT_END_YEAR,event.getEND_YEAR() );
        contentValues.put( DBStructure.EVENT_DESCRIPTION ,event.getEVENT_DESCRIPTION() );
        contentValues.put( DBStructure.EVENT_DONE,event.getDONE() );
        contentValues.put( DBStructure.EVENT_LOCATION, event.getLOCATION());
        contentValues.put(DBStructure.EVENT_PARENT_ID,event.getPARENT_EVENT_ID());
        database.insert(DBStructure.EVENT_TABLE_NAME, null,contentValues);
    }
    public void delete_event(int id , int parent_id, SQLiteDatabase database){
        String selection = "ID =? OR "+DBStructure.EVENT_PARENT_ID +"=?";
        String[] selectionArgs = {String.valueOf(id), String.valueOf(parent_id)};
        database.delete(DBStructure.EVENT_TABLE_NAME,selection,selectionArgs);
     }
    public Cursor read_reminders(String id ,SQLiteDatabase database){
        String [] Projections = {"ID",DBStructure.REMIND_YEAR, DBStructure.REMIND_MONTH , DBStructure.REMIND_DATE ,DBStructure.REMIND_TIME,DBStructure.REMIND_PLAY_RINGTONE,DBStructure.REMIND_VIBRATION, DBStructure.REMIND_RING_TONE, DBStructure.REMIND_SHOW_ALERT_DIAOLOG,DBStructure.REMIND_SHOW_NOTIFICATION};
        String Selections = DBStructure.EVENT_ID+"=?";
        String []  SelectionArgs = {id};
        return database.query(DBStructure.EVENT_TABLE_NAME, Projections,Selections,SelectionArgs,null,null,null);
    }
    public Cursor ReadEventperDate(String date , SQLiteDatabase database){
        String [] Projections =getProjects();
        String Selections = DBStructure.EVENT_START_DATE +"=?";
        String []  SelectionArgs = {date};
        return database.query(DBStructure.EVENT_TABLE_NAME, Projections,Selections,SelectionArgs,null,null,null);
    }
    public Cursor ReadEventperMonth(String month,String year, SQLiteDatabase database){
        String [] Projections =getProjects();
        String Selections = DBStructure.EVENT_START_MONTH +"=? AND "+DBStructure.EVENT_START_YEAR +"=?";
        String []  SelectionArgs = {month,year};
        return database.query(DBStructure.EVENT_TABLE_NAME, Projections,Selections,SelectionArgs,null,null,null);
    }
    public String [] getProjects(){
        String [] projects =  {"ID",DBStructure.EVENT_NAME, DBStructure.EVENT_PARENT_ID, DBStructure.EVENT_FREQUENCY,DBStructure.EVENT_TYPE,DBStructure.EVENT_START_TIME,DBStructure.EVENT_START_DATE,DBStructure.EVENT_START_MONTH, DBStructure.EVENT_START_YEAR, DBStructure.EVENT_START_YEAR, DBStructure.EVENT_END_DATE, DBStructure.EVENT_END_MONTH,DBStructure.EVENT_END_TIME,DBStructure.EVENT_END_YEAR, DBStructure.EVENT_DESCRIPTION ,DBStructure.EVENT_DONE,DBStructure.EVENT_LOCATION} ;
        return projects;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.setForeignKeyConstraintsEnabled (true) ; // Foreign key support
    }
}
