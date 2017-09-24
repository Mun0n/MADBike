package org.drunkcode.madbike.ui.favorite.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.drunkcode.madbike.R;
import org.drunkcode.madbike.ui.favorite.listener.AlarmListener;
import org.drunkcode.madbike.ui.favorite.model.AlarmItem;
import org.drunkcode.madbike.ui.favorite.adapter.AlarmAdapter.ViewHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AlarmAdapter extends RecyclerView.Adapter<ViewHolder> {

    private final Context context;
    private List<AlarmItem> alarms;
    private LayoutInflater layoutInflater;
    private AlarmListener alarmListener;

    public AlarmAdapter(Context context, final Collection<AlarmItem> alarmItems, AlarmListener alarmListener) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        alarms = new ArrayList<>(alarmItems);
        this.alarmListener = alarmListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.item_alarm, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        AlarmItem alarmItem = getItem(position);

        holder.nameTextView.setText(alarmItem.getName());
        String diary;
        if (alarmItem.isDiary()) {
            diary = context.getString(R.string.recurrence_diary);
        } else {
            diary = context.getString(R.string.recurrence_never);
        }
        holder.subtitleTextView.setText(String.format("%s, %s", alarmItem.getTime(), diary));
    }

    private AlarmItem getItem(int position) {
        return alarms.get(position);
    }

    @Override
    public int getItemCount() {
        return alarms.size();
    }

    public void setAlarms(Collection<AlarmItem> alarmItems) {
        alarms.clear();
        alarms.addAll(alarmItems);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView nameTextView;
        private final TextView subtitleTextView;
        private final ImageButton deleteImageButton;

        public ViewHolder(View itemView) {
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.nameAlarm);
            subtitleTextView = (TextView) itemView.findViewById(R.id.subtitleAlarm);
            deleteImageButton = (ImageButton) itemView.findViewById(R.id.deleteAlarm);
            deleteImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alarmListener.onAlarmDeleteClicked(alarms.get(getAdapterPosition()), getAdapterPosition());
                }
            });
        }

    }
}
