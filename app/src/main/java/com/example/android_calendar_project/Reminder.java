package com.example.android_calendar_project;

import java.io.Serializable;
import java.util.Date;

public class Reminder implements Serializable {
    private Date REMIND_DATE;
    private String RING_TONE;
    private Boolean SHOW_NOTIFICATION, SHOW_ALERT_DIAOLOG, VIBRATION;
    private Boolean PLAY_RINGTONE;
    private Boolean in_db = false;
    private int REMIND_ID, EVENT_ID;

    public Reminder(Date REMIND_DATE, String RING_TONE, Boolean SHOW_NOTIFICATION, Boolean SHOW_ALERT_DIAOLOG, Boolean VIBRATION, Boolean PLAY_RINGTONE, Boolean in_db, int REMIND_ID, int EVENT_ID) {
        this.REMIND_DATE = REMIND_DATE;
        this.RING_TONE = RING_TONE;
        this.SHOW_NOTIFICATION = SHOW_NOTIFICATION;
        this.SHOW_ALERT_DIAOLOG = SHOW_ALERT_DIAOLOG;
        this.VIBRATION = VIBRATION;
        this.PLAY_RINGTONE = PLAY_RINGTONE;
        this.in_db = in_db;
        this.REMIND_ID = REMIND_ID;
        this.EVENT_ID = EVENT_ID;
    }

    public Reminder(Date REMIND_DATE, String RING_TONE, Boolean SHOW_NOTIFICATION, Boolean SHOW_ALERT_DIAOLOG, Boolean VIBRATION, Boolean PLAY_RINGTONE) {
        this.REMIND_DATE = REMIND_DATE;
        this.RING_TONE = RING_TONE;
        this.SHOW_NOTIFICATION = SHOW_NOTIFICATION;
        this.SHOW_ALERT_DIAOLOG = SHOW_ALERT_DIAOLOG;
        this.VIBRATION = VIBRATION;
        this.PLAY_RINGTONE = PLAY_RINGTONE;
    }

    public Reminder() {

    }

    public static Reminder copy(Reminder o) {
        Reminder n = new Reminder();
        n.setEVENT_ID(o.getEVENT_ID());

        n.setREMIND_DATE(o.getREMIND_DATE());


        n.setSHOW_ALERT_DIAOLOG(o.getSHOW_ALERT_DIAOLOG());
        n.setSHOW_NOTIFICATION(o.getSHOW_NOTIFICATION());
        n.setVIBRATION(o.getVIBRATION());
        n.setPLAY_RINGTONE(o.getPLAY_RINGTONE());
        n.setRING_TONE(o.getRING_TONE());
        n.setIn_db(o.getIn_db());


        //... etc.
        return n;
    }

    public Date getREMIND_DATE() {
        return REMIND_DATE;
    }

    public void setREMIND_DATE(Date REMIND_DATE) {
        this.REMIND_DATE = REMIND_DATE;
    }

    public Boolean getIn_db() {
        return in_db;
    }

    public void setIn_db(Boolean in_db) {
        this.in_db = in_db;
    }


    public String getRING_TONE() {
        return RING_TONE;
    }

    public void setRING_TONE(String RING_TONE) {
        this.RING_TONE = RING_TONE;
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

    public Boolean getVIBRATION() {
        return VIBRATION;
    }

    public void setVIBRATION(Boolean VIBRATION) {
        this.VIBRATION = VIBRATION;
    }

    public Boolean getPLAY_RINGTONE() {
        return PLAY_RINGTONE;
    }

    public void setPLAY_RINGTONE(Boolean PLAY_RINGTONE) {
        this.PLAY_RINGTONE = PLAY_RINGTONE;
    }

    public int getREMIND_ID() {
        return REMIND_ID;
    }

    public void setREMIND_ID(int REMIND_ID) {
        this.REMIND_ID = REMIND_ID;
    }

    public int getEVENT_ID() {
        return EVENT_ID;
    }

    public void setEVENT_ID(int EVENT_ID) {
        this.EVENT_ID = EVENT_ID;
    }


    public String get_time() {
        String time = "";
        time += String.valueOf(this.getREMIND_DATE());
        time += ":";
        return time;
    }


}
