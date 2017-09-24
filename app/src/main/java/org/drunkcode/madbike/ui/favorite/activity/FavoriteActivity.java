package org.drunkcode.madbike.ui.favorite.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.onesignal.OneSignal;

import org.drunkcode.madbike.R;
import org.drunkcode.madbike.base.BaseActivity;
import org.drunkcode.madbike.preferences.Preferences;
import org.drunkcode.madbike.ui.home.fragment.FavoritesFragment;
import org.drunkcode.madbike.ui.home.model.FavoriteItem;
import org.drunkcode.madbike.ui.home.model.Station;
import org.drunkcode.madbike.utils.OneSignalUtils;
import org.drunkcode.madbike.utils.tracking.AnalyticsManager;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.InjectView;
import butterknife.OnClick;

public class FavoriteActivity extends BaseActivity implements OnMapReadyCallback {

    public static final String EXTRA_STATION_NAME = "station_name";
    public static final String EXTRA_STATION_ID = "station_id";
    public static final String EXTRA_ALARM_DIARY = "alarm_diary";
    public static final String EXTRA_ALARM_ID = "alarm_id";
    @InjectView(R.id.nameTextView)
    TextView nameTextView;
    @InjectView(R.id.streetTextView)
    TextView streetTextView;
    @InjectView(R.id.freeTextView)
    TextView freeTextView;
    @InjectView(R.id.totalTextView)
    TextView totalTextView;
    @InjectView(R.id.engagedTextView)
    TextView engagedTextView;

    @InjectView(R.id.mapView)
    MapView mapView;
    @InjectView(R.id.fab)
    FloatingActionButton fab;

    private GoogleMap map;


    private Gson gson;
    private LatLng madrid = new LatLng(40.4324421, -3.6967743);
    private Station station;
    private FavoriteItem[] favoriteItems;
    private boolean isFavorite;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_favorite;
    }

    @Override
    protected String getActivityTitle() {
        return "";
    }

    @Override
    protected boolean getActivityHomeAsUpEnabled() {
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_alarm:
                showAlarmConfig();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAlarmConfig() {
        Intent intent = new Intent(FavoriteActivity.this, AlarmActivity.class);
        intent.putExtra(EXTRA_STATION_ID, station.getIdStation());
        intent.putExtra(EXTRA_STATION_NAME, station.getNombre());
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(CommonStatusCodes.SUCCESS);
        MapsInitializer.initialize(this);
        gson = new Gson();
        if (getIntent().getExtras().getString(FavoritesFragment.DATA) != null) {
            station = gson.fromJson(getIntent().getExtras().getString(FavoritesFragment.DATA), Station.class);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(station.getNombre());
            }
            nameTextView.setText(station.getNumberStation() + " " + station.getNombre());
            streetTextView.setText(station.getAddress());

            totalTextView.setText(getString(R.string.bases) + "\n" + station.getNumberBases());
            freeTextView.setText(getString(R.string.bases_free) + "\n" + station.getBasesFree());
            engagedTextView.setText(getString(R.string.bases_engaged) + "\n" + station.getBikeEngaged());

            favoriteItems = gson.fromJson(Preferences.getInstance(this).getIdFav(), FavoriteItem[].class);
            if (favoriteItems != null) {
                for (int i = 0; i < favoriteItems.length; i++) {
                    FavoriteItem f = favoriteItems[i];
                    if (f.getId().equalsIgnoreCase(station.getIdStation())) {
                        fab.setImageResource(R.drawable.ic_favorite);
                        isFavorite = true;
                        break;
                    } else {
                        fab.setImageResource(R.drawable.ic_favorite_empty);
                        isFavorite = true;
                    }
                }
            }

            initMap(savedInstanceState);
        } else {
            finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_alarm, menu);
        return true;
    }

    private void initMap(Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        map.getUiSettings().setMapToolbarEnabled(false);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(station.getLatitude()), Double.parseDouble(station.getLongitude())), 15f));
        map.getUiSettings().setScrollGesturesEnabled(false);
        map.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(station.getLatitude()), Double.parseDouble(station.getLongitude()))));
    }

    @OnClick(R.id.fab)
    public void onFabPressed() {
        AnalyticsManager.getInstance().trackFavorites(false);
        if (favoriteItems != null && isFavorite) {
            ArrayList<FavoriteItem> favoriteArray = new ArrayList<FavoriteItem>(Arrays.asList(favoriteItems));
            for (int i = 0; i < favoriteArray.size(); i++) {
                if (favoriteArray.get(i).getId().equalsIgnoreCase(station.getIdStation())) {
                    favoriteArray.remove(i);
                    favoriteItems = new FavoriteItem[favoriteArray.size()];
                    for (int j = 0; j < favoriteArray.size(); j++) {
                        favoriteItems[j] = favoriteArray.get(j);
                    }
                    OneSignal.deleteTag(String.format("Station%s", station.getIdStation()));
                    Preferences.getInstance(this).setIdFav(gson.toJson(favoriteItems));
                    fab.setImageResource(R.drawable.ic_favorite_empty);
                    isFavorite = false;
                    break;
                }
            }
        } else {
            ArrayList<FavoriteItem> favoriteArray = new ArrayList<FavoriteItem>(Arrays.asList(favoriteItems));
            favoriteArray.add(new FavoriteItem(station.getIdStation()));
            favoriteItems = new FavoriteItem[favoriteArray.size()];
            for (int j = 0; j < favoriteArray.size(); j++) {
                favoriteItems[j] = favoriteArray.get(j);
            }
            Preferences.getInstance(this).setIdFav(gson.toJson(favoriteItems));
            fab.setImageResource(R.drawable.ic_favorite);
            isFavorite = true;
        }
        OneSignalUtils.getInstance().sendOneSignalTags();
    }


}
