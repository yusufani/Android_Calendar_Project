package com.example.android_calendar_project;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeoutException;

public class GridAdapter extends ArrayAdapter {
    List<Date> dates;
    Calendar currentDate;
    List<Events> events;
    LayoutInflater layoutInflater;

    @Nullable
    @Override
    public Object getItem(int position) {
        return dates.get(position);
    }

    @Override
    public int getPosition(@Nullable Object item) {
        return dates.indexOf(item);
    }

    @Override
    public int getCount() {
        return dates.size();
    }

    public GridAdapter(@NonNull Context context, List<Date> dates, Calendar currentDate, List<Events> events) {
        super(context, R.layout.single_cell_layout);
        this.dates = dates;
        this.currentDate = currentDate;
        this.events=events;
        layoutInflater= LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Date month_date = dates.get(position);
        Calendar date_calendar = Calendar.getInstance();
        date_calendar.setTime(month_date);
        int DayNo = date_calendar.get(Calendar.DAY_OF_MONTH);
        int displayMonth = date_calendar.get(Calendar.MONTH)+1;
        int displayYear= date_calendar.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH)+1;
        int currentYear = currentDate.get(Calendar.YEAR);
        View view = convertView;
        if (view == null){
            view = layoutInflater.inflate(R.layout.single_cell_layout, parent,false );

        }
        if (displayMonth == currentMonth && displayYear == currentYear){
            view.setBackgroundColor(getContext().getColor(R.color.green));
        }else{
            view.setBackgroundColor(Color.parseColor("#cccccc"));
        }
        TextView day_number = view.findViewById(R.id.calendar_day);
        TextView eventNumber = view.findViewById(R.id.events_id);
        day_number.setText(String.valueOf(DayNo));
        Calendar eventCalender = Calendar.getInstance();
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i= 0 ; i< events.size() ; i++){
            eventCalender.setTime(converStringToDate(events.get(i).getSTART_DATE()));
            if(DayNo == eventCalender.get(Calendar.DAY_OF_MONTH) && displayMonth == eventCalender.get(Calendar.MONTH)+1  &&
            displayYear == eventCalender.get(Calendar.YEAR)){
                arrayList.add(events.get(i).getEVENT_NAME());
                eventNumber.setText(arrayList.size() + "Events");
            }
        }
        return view;
    }
    private Date converStringToDate(String evenDate){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd" , Locale.ENGLISH) ;
        Date date = null;
        try{
            date = format.parse(evenDate);

        }catch (ParseException e ){
            e.printStackTrace();
        }
        return date;
    }
}
