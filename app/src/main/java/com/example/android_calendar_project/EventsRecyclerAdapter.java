package com.example.android_calendar_project;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Calendar;

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
    holder.share.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, event.toString());
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            context.startActivity(shareIntent);
        }
    });
    holder.delete.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            delete_event(event.getEVENT_ID());
            arrayList.remove(position);
            Toast.makeText(context,"Event deleted",Toast.LENGTH_SHORT).show();
            notifyDataSetChanged();
        }
    });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent new_intent = new Intent(context, Event_Main_Activity.class);
                new_intent.putExtra("event_id", event.getEVENT_ID());
                context.startActivity(new_intent);
            }
        });
        int interval_type = event.getINTERVAL_TYPE();
        String str= "";
        switch (interval_type) {
            case Event_Main_Activity.INTERVAL_TYPES.NO_INTERVAL_INDEX: {
                str += "Interval Not Selected";
                break;
            }
            case Event_Main_Activity.INTERVAL_TYPES.IN_HOUR_INDEX: {

                str += "Interval in  every " + event.getINTERVAL_VALUE() + " HOURs";
                break;
            }
            case Event_Main_Activity.INTERVAL_TYPES.IN_MONTH_INDEX: { // ONCE In Month
                str += "Interval in every " + event.getINTERVAL_VALUE() + " in MONTHs";
                break;
            }
        }
        holder.interval.setText(str);
        holder.end_time.setText(CustomCalendarView.final_all_time_and_date_format.format(event.getEND_DATE()));

        LinearLayout linearLayout = (LinearLayout) holder.end_time.getParent();
        linearLayout.setWeightSum(0);
        holder.start_time.setText(CustomCalendarView.final_all_time_and_date_format.format(event.getSTART_DATE()));

    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView event_name, start_date ,start_time ,end_date, end_time ,interval, event_type;
        ImageButton delete, edit , share;
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
            share = itemView.findViewById(R.id.SINGLE_EVENT_share_event_button);

        }

    }
    private void delete_event(int id ){
        dbOpenHelper= new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.delete_event(id,database);
        dbOpenHelper.close();
    }
}
