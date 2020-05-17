package com.example.android_calendar_project;

public class DBStructure {
    public static  final  String DB_NAME ="CALENDAR_DB";
    public static  final int DB_VERSION = 1;

    public static final String EVENT_TABLE_NAME = "eventstable";

    public static final String EVENT_NAME ="EVENT_NAME";
    public static final String EVENT_DESCRIPTION = "EVENT_DESCRIPTION";
    public static final String EVENT_LOCATION = "EVENT_LOCATION";
    public static final String EVENT_FREQUENCY = "EVENT_FREQUENCY";
    public static final String EVENT_TYPE = "EVENT_TYPE";
    public static final String EVENT_START_TIME ="EVENT_START_TIME";
    public static final String EVENT_START_DATE = "EVENT_START_DATE";
    public static final String EVENT_START_MONTH = "EVENT_START_MONTH";
    public static final String EVENT_START_YEAR = "EVENT_START_YEAR";
    public static final String EVENT_END_TIME = "EVENT_END_TIME";
    public static final String EVENT_END_DATE = "EVENT_END_DATE";
    public static final String EVENT_END_MONTH = "EVENT_END_MONTH";
    public static final String EVENT_END_YEAR = "EVENT_END_YEAR";
    public static final String EVENT_DONE = "EVENT_DONE";
    public static final String EVENT_PARENT_ID= "EVENT_PARENT_ID";

    public static final String EVENT_ID = "EVENT_ID"; // Yabancı Key


    public static final String REMINDER_TABLE_NAME = "reminderstable";
    public static final String REMIND_ID = "REMIND_ID";
    public static final String REMIND_TIME = "REMIND_TIME";
    public static final String REMIND_DATE = "REMIND_DATE";
    public static final String REMIND_MONTH = "REMIND_MONTH";
    public static final String REMIND_YEAR = "REMIND_YEAR";
    public static final String REMIND_RING_TONE = "REMIND_RING_TONE";
    public static final String REMIND_VIBRATION = "REMIND_VIBRATION";
    public static final String REMIND_SHOW_NOTIFICATION = "REMIND_SHOW_NOTIFICATION";
    public static final String REMIND_SHOW_ALERT_DIAOLOG = "REMIND_SHOW_ALERT_DIAOLOG";
    public static final String REMIND_PLAY_RINGTONE = "REMIND_PLAY_RINGTONE";


}
