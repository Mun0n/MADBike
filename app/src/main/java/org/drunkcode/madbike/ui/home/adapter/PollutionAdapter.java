package org.drunkcode.madbike.ui.home.adapter;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;

import org.drunkcode.madbike.R;
import org.drunkcode.madbike.ui.home.model.DataPollution;

/**
 * Created by mun0n on 8/6/16.
 */
public class PollutionAdapter implements GoogleMap.InfoWindowAdapter {

    private View customView;
    private Activity activity;
    private TextView titleTextView;
    private TextView bodyTextView;
    private TextView levelTextView;

    public PollutionAdapter(Activity activity) {
        this.activity = activity;
        customView = activity.getLayoutInflater().inflate(R.layout.item_pollution, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        Gson gson = new Gson();
        DataPollution station = gson.fromJson(marker.getTitle(), DataPollution.class);
        titleTextView = (TextView) customView.findViewById(R.id.titleTextView);
        bodyTextView = (TextView) customView.findViewById(R.id.bodyTextView);
        levelTextView = (TextView) customView.findViewById(R.id.levelTextView);

        if (station != null) {
            titleTextView.setText(station.getTitle());
            bodyTextView.setText(station.getBody());
            switch (station.getLevel()) {
                case 1:
                    levelTextView.setText(activity.getString(R.string.level_1));
                    levelTextView.setTextColor(ContextCompat.getColor(activity, android.R.color.holo_green_dark));
                    break;
                case 2:
                    levelTextView.setText(activity.getString(R.string.level_2));
                    levelTextView.setTextColor(ContextCompat.getColor(activity, R.color.yellow_dark));
                    break;
                case 3:
                    levelTextView.setText(activity.getString(R.string.level_3));
                    levelTextView.setTextColor(ContextCompat.getColor(activity, android.R.color.holo_orange_dark));
                    break;
                case 4:
                    levelTextView.setText(activity.getString(R.string.level_4));
                    levelTextView.setTextColor(ContextCompat.getColor(activity, android.R.color.holo_red_dark));
                    break;
            }

            return customView;
        } else {
            return null;
        }
    }
}
