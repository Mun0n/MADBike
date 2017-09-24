package org.drunkcode.madbike.ui.station.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.drunkcode.madbike.R;
import org.drunkcode.madbike.base.BaseActivity;
import org.drunkcode.madbike.common.dialogs.loading.LoadingManager;
import org.drunkcode.madbike.ui.favorite.activity.FavoriteActivity;
import org.drunkcode.madbike.ui.home.model.NewStation;
import org.drunkcode.madbike.ui.station.model.StationInfo;
import org.drunkcode.madbike.ui.station.presenter.StationPresenter;
import org.drunkcode.madbike.ui.station.response.StationResponse;
import org.drunkcode.madbike.ui.station.view.StationView;
import org.drunkcode.madbike.utils.BranchManager;
import org.drunkcode.madbike.utils.tracking.AnalyticsManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;

public class StationActivity extends BaseActivity implements OnMapReadyCallback, StationView, Branch.BranchLinkCreateListener {

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
    @InjectView(R.id.mainLayout)
    CoordinatorLayout mainLayout;

    private GoogleMap map;
    private StationPresenter presenter;
    private LoadingManager loadingManager;
    private StationPresenter stationPresenter;
    private LatLng madrid = new LatLng(40.4324421, -3.6967743);

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_station;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AnalyticsManager.getInstance().trackContentView("StationScreen");
        MapsInitializer.initialize(this);
        initManagers();
        initMap(savedInstanceState);
    }

    private void initManagers() {
        stationPresenter = new StationPresenter(this);
        loadingManager = new LoadingManager(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        stationPresenter.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        stationPresenter.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
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
        map.getUiSettings().setScrollGesturesEnabled(false);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(madrid, 15f));
        try {
            stationPresenter.getStationgInfo(getIntent().getExtras().getString(FavoriteActivity.EXTRA_STATION_ID));
        } catch (Throwable throwable) {
            //throwable.printStackTrace();
        }
    }

    @Override
    public void getStationResponse(StationResponse stationResponse) {
        if (stationResponse.getSuccess() == 1) {
            Gson gson = new Gson();
            StationInfo stationInfo = gson.fromJson(stationResponse.getStationData(), StationInfo.class);
            NewStation station = stationInfo.getStationList().get(0);

            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(station.getName());
            }

            nameTextView.setText(station.getNumber() + " " + station.getName());
            streetTextView.setText(station.getAddress());

            totalTextView.setText(getString(R.string.bases) + "\n" + station.getTotalBases());
            freeTextView.setText(getString(R.string.bases_free) + "\n" + station.getFreeBases());
            engagedTextView.setText(getString(R.string.bases_engaged) + "\n" + station.getDockBases());

            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(station.getLatitude()), Double.parseDouble(station.getLongitude())), 15f));
            map.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(station.getLatitude()), Double.parseDouble(station.getLongitude()))));
        } else {
            finish();
        }
    }

    @Override
    public void showLoading(int idTitle, int idMessage) {
        loadingManager.showLoadingDialog(idTitle, idMessage);
    }

    @Override
    public void hideLoading() {
        loadingManager.hideLoading();
    }

    @Override
    public void showError() {
        Snackbar snackbar = Snackbar.make(mainLayout, getString(R.string.station_error), Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_station, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_share:
                AnalyticsManager.getInstance().trackShare();
                shareStation();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareStation() {
        BranchManager.getInstance().createStationLink(getIntent().getStringExtra(FavoriteActivity.EXTRA_STATION_ID), this);
    }

    @Override
    public void onLinkCreate(String url, BranchError error) {
        if (error == null) {
            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                i.putExtra(Intent.EXTRA_TEXT, String.format("%s %s", getString(R.string.share_text), url));
                startActivity(Intent.createChooser(i, getString(R.string.share_with)));
            } catch (Exception e) {
                //e.toString();
            }
        }
    }


}
