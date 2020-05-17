package com.example.android_calendar_project;

public class Events {
    private int EVENT_ID;
    private String EVENT_NAME;
    private String EVENT_DESCRIPTION = null;
    private String LOCATION = null;
    private Double FREQUENCY;
    private String EVENT_TYPE;
    private String START_TIME, START_DATE , START_MONTH , START_YEAR ;
    private String END_TIME=null ,  END_DATE =null ,END_MONTH =null ,END_YEAR = null;
    private Boolean DONE ;
    private int PARENT_EVENT_ID ;
    //TODO yinelenen eventde reminderları da set etmeyi unutma

    public int getPARENT_EVENT_ID() {
        return PARENT_EVENT_ID;
    }

    public void setPARENT_EVENT_ID(int PARENT_EVENT_ID) {
        this.PARENT_EVENT_ID = PARENT_EVENT_ID;
    }

    public Events(int EVENT_ID, String EVENT_NAME, String EVENT_DESCRIPTION, String LOCATION, Double FREQUENCY, String EVENT_TYPE, String START_TIME, String START_DATE, String START_MONTH, String START_YEAR, String END_TIME, String END_DATE, String END_MONTH, String END_YEAR, Boolean DONE, int PARENT_EVENT_ID) {
        this.EVENT_ID = EVENT_ID;
        this.EVENT_NAME = EVENT_NAME;
        this.EVENT_DESCRIPTION = EVENT_DESCRIPTION;
        this.LOCATION = LOCATION;
        this.FREQUENCY = FREQUENCY;
        this.EVENT_TYPE = EVENT_TYPE;
        this.START_TIME = START_TIME;
        this.START_DATE = START_DATE;
        this.START_MONTH = START_MONTH;
        this.START_YEAR = START_YEAR;
        this.END_TIME = END_TIME;
        this.END_DATE = END_DATE;
        this.END_MONTH = END_MONTH;
        this.END_YEAR = END_YEAR;
        this.DONE = DONE;
        this.PARENT_EVENT_ID = PARENT_EVENT_ID;
    }
    public Events( String EVENT_NAME, String EVENT_DESCRIPTION, String LOCATION, Double FREQUENCY, String EVENT_TYPE, String START_TIME, String START_DATE, String START_MONTH, String START_YEAR, String END_TIME, String END_DATE, String END_MONTH, String END_YEAR, Boolean DONE, int PARENT_EVENT_ID) {
        this.EVENT_NAME = EVENT_NAME;
        this.EVENT_DESCRIPTION = EVENT_DESCRIPTION;
        this.LOCATION = LOCATION;
        this.FREQUENCY = FREQUENCY;
        this.EVENT_TYPE = EVENT_TYPE;
        this.START_TIME = START_TIME;
        this.START_DATE = START_DATE;
        this.START_MONTH = START_MONTH;
        this.START_YEAR = START_YEAR;
        this.END_TIME = END_TIME;
        this.END_DATE = END_DATE;
        this.END_MONTH = END_MONTH;
        this.END_YEAR = END_YEAR;
        this.DONE = DONE;
        this.PARENT_EVENT_ID = PARENT_EVENT_ID;
    }

    public Boolean getDONE() {
        return DONE;
    }

    public void setDONE(Boolean DONE) {
        this.DONE = DONE;
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

    public Double getFREQUENCY() {
        return FREQUENCY;
    }

    public void setFREQUENCY(Double FREQUENCY) {
        this.FREQUENCY = FREQUENCY;
    }

    public String getEVENT_TYPE() {
        return EVENT_TYPE;
    }

    public void setEVENT_TYPE(String EVENT_TYPE) {
        this.EVENT_TYPE = EVENT_TYPE;
    }

    public String getSTART_TIME() {
        return START_TIME;
    }

    public void setSTART_TIME(String START_TIME) {
        this.START_TIME = START_TIME;
    }

    public String getSTART_DATE() {
        return START_DATE;
    }

    public void setSTART_DATE(String START_DATE) {
        this.START_DATE = START_DATE;
    }

    public String getSTART_MONTH() {
        return START_MONTH;
    }

    public void setSTART_MONTH(String START_MONTH) {
        this.START_MONTH = START_MONTH;
    }

    public String getSTART_YEAR() {
        return START_YEAR;
    }

    public void setSTART_YEAR(String START_YEAR) {
        this.START_YEAR = START_YEAR;
    }

    public String getEND_TIME() {
        return END_TIME;
    }

    public void setEND_TIME(String END_TIME) {
        this.END_TIME = END_TIME;
    }

    public String getEND_DATE() {
        return END_DATE;
    }

    public void setEND_DATE(String END_DATE) {
        this.END_DATE = END_DATE;
    }

    public String getEND_MONTH() {
        return END_MONTH;
    }

    public void setEND_MONTH(String END_MONTH) {
        this.END_MONTH = END_MONTH;
    }

    public String getEND_YEAR() {
        return END_YEAR;
    }

    public void setEND_YEAR(String END_YEAR) {
        this.END_YEAR = END_YEAR;
    }

    public boolean isDONE() {
        return DONE;
    }

    public void setDONE(boolean DONE) {
        this.DONE = DONE;
    }
}
