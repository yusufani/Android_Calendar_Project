package com.example.android_calendar_project;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GridAdapter extends ArrayAdapter {
    List<Date> dates;
    Calendar currentDate;
    List<Events> events;
    LayoutInflater layoutInflater;


    public GridAdapter(@NonNull Context context, List<Date> dates, Calendar currentDate, List<Events> events) {
        super(context, R.layout.single_cell_layout);
        this.dates = dates;
        this.currentDate = currentDate;
        this.events = events;
        layoutInflater = LayoutInflater.from(context);
    }

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

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String TAG="SINGLE CELL";
        Date month_date = dates.get(position);
        Calendar date_calendar = Calendar.getInstance();
        date_calendar.setTime(month_date);
        int DayNo = date_calendar.get(Calendar.DAY_OF_MONTH);
        int displayMonth = date_calendar.get(Calendar.MONTH)+1;
        int displayYear= date_calendar.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH)+1;
        int currentYear = currentDate.get(Calendar.YEAR);
        int current_day = currentDate.get(Calendar.DAY_OF_MONTH);
        View view = convertView;
        if (view == null){
            view = layoutInflater.inflate(R.layout.single_cell_layout, parent,false );
        }

        view.setMinimumHeight(CustomCalendarView.month_height_pixel / 6);
        TextView day_number = view.findViewById(R.id.calendar_day);
        LinearLayout event_colors = view.findViewById(R.id.event_color_layout);
        event_colors.removeAllViews(); // 1 Extra view contains bug fix
        TextView events_number = view.findViewById(R.id.number_of_event);
        day_number.setText(String.valueOf(DayNo));
        Calendar eventCalender = Calendar.getInstance();
        ArrayList<String> arrayList = new ArrayList<>();

        for (int i= 0 ; i< events.size() ; i++){
            Events e = events.get(i);
            eventCalender.setTime(e.getSTART_DATE());
            if(DayNo == eventCalender.get(Calendar.DAY_OF_MONTH) && displayMonth == eventCalender.get(Calendar.MONTH)+1  &&
                displayYear == eventCalender.get(Calendar.YEAR)){
                arrayList.add(events.get(i).getEVENT_COLOR());
            }
        }
        if (arrayList.size() > 0) {
            Log.v(TAG,arrayList.toString());
            for (String color : arrayList) {
                //LinearLayout linearLayout = new LinearLayout(getContext());
                //FrameLayout frameLayout = new FrameLayout(getContext());
                FloatingActionButton floatingActionButton = new FloatingActionButton(getContext());
                floatingActionButton.setCustomSize(20);
                floatingActionButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
                floatingActionButton.setClickable(false);
                floatingActionButton.setFocusable(false);
                //frameLayout.addView(floatingActionButton);
                //linearLayout.addView(frameLayout );
                //floatingActionButton.setClickable(false);
                event_colors.addView(floatingActionButton);
                // TODO RENKLER EKLENECEK VE TEMA İŞİ KALDI  BİR DE EVENTLERİN KAÇ TANESİ DONE
            }
            events_number.setText(arrayList.size() + " Events");
        }
        Calendar today= Calendar.getInstance();
        if (DayNo == today.get(Calendar.DAY_OF_MONTH) && displayMonth == today.get(Calendar.MONTH)+1 && displayYear == today.get(Calendar.YEAR)) {
            view.setBackgroundResource(R.drawable.single_cell);
            view.setBackgroundColor(getContext().getColor(R.color.green));
        } else if (displayMonth == currentMonth && displayYear == currentYear) {
            view.setBackgroundResource(R.drawable.single_cell);
        } else {

            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = getContext().getTheme();
            theme.resolveAttribute(R.attr.deepest, typedValue, true);
            @ColorInt int color = typedValue.data;
            view.setBackgroundColor(color);
            day_number.setTextColor(Color.parseColor("#414141"));
            //eventNumber.setTextColor(Color.parseColor("#414141"));
        }
        return view;
    }

}
