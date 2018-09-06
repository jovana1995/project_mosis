package com.example.aleksandra.backpack.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aleksandra.backpack.BackpackApplication;
import com.example.aleksandra.backpack.R;
import com.example.aleksandra.backpack.activities.EventActivity;
import com.example.aleksandra.backpack.Models.EventModel;
import com.example.aleksandra.backpack.utils.Utils;

import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {
    private List<EventModel> eventsList;
    private Context context;

    public EventsAdapter(List<EventModel> potsList) {
        this.eventsList = potsList;
    }

    @NonNull
    @Override
    public EventsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new EventsAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_events, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EventsAdapter.ViewHolder holder, int position) {

        holder.name.setText(eventsList.get(position).getName());
        holder.state.setText(eventsList.get(position).getState());
        holder.comment.setText(eventsList.get(position).getComment());
        Utils.loadImageWithGlideCircle(Uri.parse(eventsList.get(position).getPersonImage()), holder.imgPerson);
        holder.when.setText(eventsList.get(position).getWhen());
        holder.where.setText(eventsList.get(position).getWhere());
    }

    @Override
    public int getItemCount() {
        return eventsList != null ? eventsList.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView name, state, comment, when, where;
        private ImageView imgPerson;

        private ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tw_event_creator_name);
            state = itemView.findViewById(R.id.tw_event_creator_state);
            comment = itemView.findViewById(R.id.tw_event_comment);
            when = itemView.findViewById(R.id.tw_when);
            where = itemView.findViewById(R.id.tw_where);
            imgPerson = itemView.findViewById(R.id.iv_event_creator_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent i = new Intent(BackpackApplication.getAppContext(), EventActivity.class);
            context.startActivity(i);
        }
    }
}

