package com.example.android_calendar_project;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

public class Events implements Serializable {
    private int EVENT_ID;
    private String EVENT_NAME;
    private String EVENT_DESCRIPTION = null;
    private String LOCATION = null;
    private int INTERVAL;
    private String EVENT_TYPE;
    private Date START_DATE, END_DATE;
    private Boolean DONE;
    private int PARENT_EVENT_ID;

    public Events(String EVENT_NAME, String EVENT_DESCRIPTION, String LOCATION, int INTERVAL, String EVENT_TYPE, Date START_DATE, Date END_DATE, Boolean DONE, int PARENT_EVENT_ID) {
        this.EVENT_NAME = EVENT_NAME;
        this.EVENT_DESCRIPTION = EVENT_DESCRIPTION;
        this.LOCATION = LOCATION;
        this.INTERVAL = INTERVAL;
        this.EVENT_TYPE = EVENT_TYPE;
        this.START_DATE = START_DATE;
        this.END_DATE = END_DATE;
        this.DONE = DONE;
        this.PARENT_EVENT_ID = PARENT_EVENT_ID;
    }

    public Events(int EVENT_ID, String EVENT_NAME, String EVENT_DESCRIPTION, String LOCATION, int INTERVAL, String EVENT_TYPE, Date START_DATE, Date END_DATE, Boolean DONE, int PARENT_EVENT_ID) {
        this.EVENT_ID = EVENT_ID;
        this.EVENT_NAME = EVENT_NAME;
        this.EVENT_DESCRIPTION = EVENT_DESCRIPTION;
        this.LOCATION = LOCATION;
        this.INTERVAL = INTERVAL;
        this.EVENT_TYPE = EVENT_TYPE;
        this.START_DATE = START_DATE;
        this.END_DATE = END_DATE;
        this.DONE = DONE;
        this.PARENT_EVENT_ID = PARENT_EVENT_ID;
    }

    public Events() {

    }

    public static Events copy(Events other) {
        Events newUser = new Events();
        newUser.setEVENT_DESCRIPTION(other.getEVENT_DESCRIPTION());
        newUser.setLOCATION(other.getLOCATION());
        newUser.setINTERVAL(other.getINTERVAL());
        newUser.setEVENT_NAME(other.getEVENT_NAME());
        newUser.setEVENT_TYPE(other.getEVENT_TYPE());

        newUser.setSTART_DATE(other.getSTART_DATE());
        newUser.setEND_DATE(other.getEND_DATE());

        newUser.setPARENT_EVENT_ID(other.getEVENT_ID());
        newUser.setDONE(other.getDONE());

        //... etc.
        return newUser;
    }

    public Date getSTART_DATE() {
        return START_DATE;
    }

    public void setSTART_DATE(Date START_DATE) {
        this.START_DATE = START_DATE;
    }

    public Date getEND_DATE() {
        return END_DATE;
    }
    //TODO yinelenen eventde reminderları da set etmeyi unutma

    public void setEND_DATE(Date END_DATE) {
        this.END_DATE = END_DATE;
    }


    public int getEVENT_ID() {
        return EVENT_ID;
    }

    public void setEVENT_ID(int EVENT_ID) {
        this.EVENT_ID = EVENT_ID;
    }

    public String getEVENT_NAME() {
        return EVENT_NAME;
    }

    public void setEVENT_NAME(String EVENT_NAME) {
        this.EVENT_NAME = EVENT_NAME;
    }

    public String getEVENT_DESCRIPTION() {
        return EVENT_DESCRIPTION;
    }

    public void setEVENT_DESCRIPTION(String EVENT_DESCRIPTION) {
        this.EVENT_DESCRIPTION = EVENT_DESCRIPTION;
    }

    public String getLOCATION() {
        return LOCATION;
    }


    public void setLOCATION(String LOCATION) {
        this.LOCATION = LOCATION;
    }

    public int getINTERVAL() {
        return INTERVAL;
    }

    public void setINTERVAL(int INTERVAL) {
        this.INTERVAL = INTERVAL;
    }

    public String getEVENT_TYPE() {
        return EVENT_TYPE;
    }

    public void setEVENT_TYPE(String EVENT_TYPE) {
        this.EVENT_TYPE = EVENT_TYPE;
    }


    public Boolean getDONE() {
        return DONE;
    }

    public void setDONE(Boolean DONE) {
        this.DONE = DONE;
    }

    public int getPARENT_EVENT_ID() {
        return PARENT_EVENT_ID;
    }

    public void setPARENT_EVENT_ID(int PARENT_EVENT_ID) {
        this.PARENT_EVENT_ID = PARENT_EVENT_ID;
    }

    @NonNull
    @Override
    public String toString() {
        String info = "";
        info += this.getEVENT_TYPE() + " " + this.getEVENT_NAME();
        info += this.getEVENT_DESCRIPTION() + " ( " + this.getLOCATION() + ")";
        info += "Event From " + this.getSTART_DATE() + "to" + this.getEND_DATE() + "\n";
        return info;
    }
    public String share_content(){
        String info = "";
        info +=
    }
}