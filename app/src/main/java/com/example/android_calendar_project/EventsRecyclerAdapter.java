package com.example.android_calendar_project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EventsRecyclerAdapter extends RecyclerView.Adapter<EventsRecyclerAdapter.MyViewHolder> {
    Context context;

    ArrayList<Events> arrayList;
    DBOpenHelper dbOpenHelper;
    public EventsRecyclerAdapter(Context context, ArrayList<Events> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_event_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
    final Events event = arrayList.get(position);
    holder.event_name.setText(event.getEVENT_NAME());
    holder.event_type.setText(event.getEVENT_TYPE());
    holder.delete.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            delete_event(event.getEVENT_ID());
            arrayList.remove(position);
            notifyDataSetChanged();
        }
    });
        String interval = String.valueOf(event.getINTERVAL());
    if (interval == "0.00"){
        interval = "No Interval Selected";
    }else{
        interval += " Day Interval";
    }
    holder.interval.setText(interval);
        holder.end_date.setText(CustomCalendarView.final_only_date_format.format(event.getEND_DATE()));
        holder.end_time.setText(CustomCalendarView.final_only_time_format.format(event.getEND_DATE()));

        LinearLayout linearLayout = (LinearLayout) holder.end_time.getParent();
        linearLayout.setWeightSum(0);
        holder.start_time.setText(CustomCalendarView.final_only_date_format.format(event.getSTART_DATE()));
        holder.start_date.setText(CustomCalendarView.final_only_time_format.format(event.getSTART_DATE()));

    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView event_name, start_date ,start_time ,end_date, end_time ,interval, event_type;
        ImageButton delete, edit ;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            start_date = itemView.findViewById(R.id.SINGLE_EVENT_start_date);
            start_time = itemView.findViewById(R.id.SINGLE_EVENT_start_time);
            event_name = itemView.findViewById(R.id.SINGLE_EVENT_name);
            end_date = itemView.findViewById(R.id.SINGLE_EVENT_end_date);
            end_time = itemView.findViewById(R.id.SINGLE_EVENT_end_time);
            interval = itemView.findViewById(R.id.SINGLE_EVENT_interval);
            event_type = itemView.findViewById(R.id.SINGLE_EVENT_type);
            delete = itemView.findViewById(R.id.SINGLE_EVENT_delete_IMAGE_BUTTON);
            edit = itemView.findViewById(R.id.SINGLE_EVENT_edit_IMAGE_BUTTON);


        }

    }
    private void delete_event(int id ){
        dbOpenHelper= new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.delete_event(id,database);
        dbOpenHelper.close();
    }
}
