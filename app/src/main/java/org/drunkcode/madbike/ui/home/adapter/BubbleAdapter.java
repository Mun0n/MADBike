package org.drunkcode.madbike.ui.home.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;

import org.drunkcode.madbike.R;
import org.drunkcode.madbike.ui.home.model.Station;

public class BubbleAdapter implements GoogleMap.InfoWindowAdapter {

    private View customView;
    private Activity activity;
    private TextView titleTextView;
    private TextView addressTextView;
    private TextView totalTextView;
    private TextView freeTextView;
    private TextView engagedTextView;
    private ImageButton favImageButton;

    public BubbleAdapter(Activity activity) {
        this.activity = activity;
        customView = activity.getLayoutInflater().inflate(R.layout.item_map, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        Gson gson = new Gson();
        Station station = gson.fromJson(marker.getTitle(), Station.class);
        titleTextView = (TextView) customView.findViewById(R.id.nameTextView);
        addressTextView = (TextView) customView.findViewById(R.id.streetTextView);

        totalTextView = (TextView) customView.findViewById(R.id.totalTextView);
        freeTextView = (TextView) customView.findViewById(R.id.freeTextView);
        engagedTextView = (TextView) customView.findViewById(R.id.engagedTextView);

        if (station != null) {
            titleTextView.setText(station.getNumberStation() + " " + station.getNombre());
            addressTextView.setText(station.getAddress());

            totalTextView.setText(activity.getString(R.string.bases) + "\n" + Integer.parseInt(station.getNumberBases()));
            freeTextView.setText(activity.getString(R.string.bases_free) + "\n" + Integer.parseInt(station.getBasesFree()));
            engagedTextView.setText(activity.getString(R.string.bases_engaged) + "\n" + Integer.parseInt(station.getBikeEngaged()));

            return customView;
        } else {
            return null;
        }
    }

}
