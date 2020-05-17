package com.example.android_calendar_project;

public class Reminder {
    private String REMIND_ID ,REMIND_TIME, REMIND_DATE, REMIND_MONTH, REMIND_YEAR, EVENT_ID;
    private String RING_TONE, VIBRATION;
    private Boolean SHOW_NOTIFICATION,SHOW_ALERT_DIAOLOG ;
    private Boolean PLAY_RINGTONE;

    public Reminder(String REMIND_TIME, String REMIND_DATE, String REMIND_MONTH, String REMIND_YEAR, String EVENT_ID, String RING_TONE, String VIBRATION, Boolean SHOW_NOTIFICATION, Boolean SHOW_ALERT_DIAOLOG , Boolean play_ring_tone) {
        this.REMIND_TIME = REMIND_TIME;
        this.REMIND_DATE = REMIND_DATE;
        this.REMIND_MONTH = REMIND_MONTH;
        this.REMIND_YEAR = REMIND_YEAR;
        this.EVENT_ID = EVENT_ID;
        this.RING_TONE = RING_TONE;
        this.VIBRATION = VIBRATION;
        this.SHOW_NOTIFICATION = SHOW_NOTIFICATION;
        this.SHOW_ALERT_DIAOLOG = SHOW_ALERT_DIAOLOG;
        this.PLAY_RINGTONE = play_ring_tone;
    }
    public Boolean getSHOW_NOTIFICATION() {
        return SHOW_NOTIFICATION;
    }

    public void setSHOW_NOTIFICATION(Boolean SHOW_NOTIFICATION) {
        this.SHOW_NOTIFICATION = SHOW_NOTIFICATION;
    }

    public Boolean getSHOW_ALERT_DIAOLOG() {
        return SHOW_ALERT_DIAOLOG;
    }

    public void setSHOW_ALERT_DIAOLOG(Boolean SHOW_ALERT_DIAOLOG) {
        this.SHOW_ALERT_DIAOLOG = SHOW_ALERT_DIAOLOG;
    }

    public Boolean getPLAY_RINGTONE() {
        return PLAY_RINGTONE;
    }

    public void setPLAY_RINGTONE(Boolean PLAY_RINGTONE) {
        this.PLAY_RINGTONE = PLAY_RINGTONE;
    }
    public boolean isPLAY_RINGTONE() {
        return PLAY_RINGTONE;
    }

    public void setPLAY_RINGTONE(boolean PLAY_RINGTONE) {
        this.PLAY_RINGTONE = PLAY_RINGTONE;
    }

    public String getREMIND_ID() {
        return REMIND_ID;
    }

    public void setREMIND_ID(String REMIND_ID) {
        this.REMIND_ID = REMIND_ID;
    }

    public String getREMIND_TIME() {
        return REMIND_TIME;
    }

    public void setREMIND_TIME(String REMIND_TIME) {
        this.REMIND_TIME = REMIND_TIME;
    }

    public String getREMIND_DATE() {
        return REMIND_DATE;
    }

    public void setREMIND_DATE(String REMIND_DATE) {
        this.REMIND_DATE = REMIND_DATE;
    }

    public String getREMIND_MONTH() {
        return REMIND_MONTH;
    }

    public void setREMIND_MONTH(String REMIND_MONTH) {
        this.REMIND_MONTH = REMIND_MONTH;
    }

    public String getREMIND_YEAR() {
        return REMIND_YEAR;
    }

    public void setREMIND_YEAR(String REMIND_YEAR) {
        this.REMIND_YEAR = REMIND_YEAR;
    }

    public String getEVENT_ID() {
        return EVENT_ID;
    }

    public void setEVENT_ID(String EVENT_ID) {
        this.EVENT_ID = EVENT_ID;
    }

    public String getRING_TONE() {
        return RING_TONE;
    }

    public void setRING_TONE(String RING_TONE) {
        this.RING_TONE = RING_TONE;
    }

    public String getVIBRATION() {
        return VIBRATION;
    }

    public void setVIBRATION(String VIBRATION) {
        this.VIBRATION = VIBRATION;
    }

    public boolean isSHOW_NOTIFICATION() {
        return SHOW_NOTIFICATION;
    }

    public void setSHOW_NOTIFICATION(boolean SHOW_NOTIFICATION) {
        this.SHOW_NOTIFICATION = SHOW_NOTIFICATION;
    }

    public boolean isSHOW_ALERT_DIAOLOG() {
        return SHOW_ALERT_DIAOLOG;
    }

    public void setSHOW_ALERT_DIAOLOG(boolean SHOW_ALERT_DIAOLOG) {
        this.SHOW_ALERT_DIAOLOG = SHOW_ALERT_DIAOLOG;
    }
}
