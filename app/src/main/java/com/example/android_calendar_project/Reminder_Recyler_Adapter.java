package com.example.android_calendar_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Reminder_Recyler_Adapter extends RecyclerView.Adapter<Reminder_Recyler_Adapter.ViewHolder> {
    Context context;
    List<Reminder> reminders;

    public Reminder_Recyler_Adapter(Context context, List<Reminder> reminders) {
        this.reminders = reminders;
        this.context = context;
    }

    @Override
    public Reminder_Recyler_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_reminder_card, parent, false);
        final ViewHolder view_holder = new ViewHolder(v);
        return view_holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Reminder r = reminders.get(position);
        holder.date_info.setText(r.getREMIND_DATE().toString());
        String infos = "";
        if (r.getPLAY_RINGTONE()) {
            infos += "Play Ringtone with" + r.getRING_TONE() + "\n";
        }
        if (r.getVIBRATION()) {
            infos += "Vibration set\n";
        }
        if (r.getSHOW_ALERT_DIAOLOG()) {
            infos += "UI wiil show";
        }
        holder.reminder_info.setText(infos);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!r.getIn_db()) {
                    reminders.remove(position);
                } else {

                }
                notifyDataSetChanged();

            }
        });

    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView date_info;
        public TextView reminder_info;
        public ImageButton delete;
        public CardView card_view;


        public ViewHolder(View view) {
            super(view);
            card_view = view.findViewById(R.id.reminder_Card);
            date_info = view.findViewById(R.id.reminder_date_info);
            reminder_info = view.findViewById(R.id.reminder_infos);
            delete = view.findViewById(R.id.delete_reminder);
        }
    }

}