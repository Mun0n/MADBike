package org.drunkcode.madbike.ui.home.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import org.drunkcode.madbike.R;
import org.drunkcode.madbike.ui.home.model.Station;

public class IconRendered extends DefaultClusterRenderer<Station> {

    public IconRendered(Context context, GoogleMap map, ClusterManager<Station> clusterManager) {
        super(context, map, clusterManager);
    }

    @Override
    protected void onBeforeClusterItemRendered(Station item, MarkerOptions markerOptions) {
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(item.getBitmapMarker()));
//        markerOptions.snippet(item.getAddress());
//        markerOptions.title(item.getName());
        super.onBeforeClusterItemRendered(item, markerOptions);
    }


}