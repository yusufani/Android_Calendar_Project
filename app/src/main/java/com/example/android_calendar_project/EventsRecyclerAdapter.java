package com.example.android_calendar_project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

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
            delete_event(event.getEVENT_ID(),event.getPARENT_EVENT_ID());
            arrayList.remove(position);
            notifyDataSetChanged();
        }
    });
    String interval = String.valueOf(event.getFREQUENCY());
    if (interval == "0.00"){
        interval = "No Interval Selected";
    }else{
        interval += " Day Interval";
    }
    holder.interval.setText(interval);
    if (event.getEND_TIME()!="null"){
        holder.end_date.setText( event.getEND_DATE() );
        holder.end_time.setText( event.getEND_TIME() );

        LinearLayout linearLayout = (LinearLayout) holder.end_time.getParent();
        linearLayout.setWeightSum(0);
    }
    holder.start_time.setText(event.getSTART_TIME());
    holder.start_date.setText(event.getSTART_DATE());

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
            start_date = itemView.findViewById(R.id.single_event_start_date);
            start_time = itemView.findViewById(R.id.single_start_event_time);
            event_name = itemView.findViewById(R.id.single_event_name);
            end_date = itemView.findViewById(R.id.single_event_end_date);
            end_time = itemView.findViewById(R.id.single_end_event_time);
            interval = itemView.findViewById(R.id.single_event_interval);
            event_type = itemView.findViewById(R.id.single_event_type);
            delete = itemView.findViewById(R.id.delete_event);
            edit = itemView.findViewById(R.id.edit_event);


        }

    }
    private void delete_event(int id , int parent_id ){
        dbOpenHelper= new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.delete_event(id,parent_id,database);
        dbOpenHelper.close();
    }
}

