package org.drunkcode.madbike.ui.home.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.drunkcode.madbike.R;
import org.drunkcode.madbike.base.BaseFragment;
import org.drunkcode.madbike.common.dialogs.loading.LoadingManager;
import org.drunkcode.madbike.ui.home.adapter.PollutionAdapter;
import org.drunkcode.madbike.ui.home.model.DataPollution;
import org.drunkcode.madbike.ui.home.model.PollutionStation;
import org.drunkcode.madbike.ui.home.presenter.AirQualityPresenter;
import org.drunkcode.madbike.ui.home.response.PollutionResponse;
import org.drunkcode.madbike.ui.home.view.AirQualityView;
import org.drunkcode.madbike.utils.tracking.AnalyticsManager;

import butterknife.InjectView;

public class AirQualityFragment extends BaseFragment implements AirQualityView, OnMapReadyCallback {

    private AirQualityPresenter airQualityPresenter;
    private LoadingManager loadingManager;

    @InjectView(R.id.mainLayout)
    LinearLayout mainLinearLayout;
    @InjectView(R.id.mapView)
    MapView mapView;

    private GoogleMap map;
    private LatLng madrid;
    private PollutionStation[] stations;

    public static AirQualityFragment newInstance(Bundle arguments) {
        AirQualityFragment f = new AirQualityFragment();
        if (arguments != null) {
            f.setArguments(arguments);
        }
        return f;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_air_quality;
    }

    @Override
    public void onResume() {
        super.onResume();
        airQualityPresenter.onResume();
        mapView.onResume();
        try {
            airQualityPresenter.getPollution();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private void initManagers() {
        airQualityPresenter = new AirQualityPresenter(this);
        loadingManager = new LoadingManager(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        airQualityPresenter.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView != null) {
            mapView.onDestroy();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MapsInitializer.initialize(getActivity());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AnalyticsManager.getInstance().trackContentView("AirQuality");
        madrid = new LatLng(40.4324421, -3.6967743);
        initManagers();
        initMap(savedInstanceState);
    }

    private void initMap(Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void getPollutionResponse(PollutionResponse pollutionResponse) {
        if (pollutionResponse.getSuccess() != 0) {
            stations = pollutionResponse.getStations();
            for (int i = 0; i < pollutionResponse.getStations().length; i++) {
                MarkerOptions markerOptions = createMarker(stations[i]);
                if (markerOptions != null) {
                    map.addMarker(markerOptions);
                }
            }
        } else {
            showError();
        }
    }

    @NonNull
    private MarkerOptions createMarker(PollutionStation station) {
        MarkerOptions marker = new MarkerOptions();
        marker.position(new LatLng(station.getLatitude(), station.getLongitude()));
        double noValue = 0.0;
        String noText = "";
        double oValue = 0.0;
        String oText = "";
        double pm10Value = 0.0;
        String pm10Text = "";
        double soValue = 0.0;
        String soText = "";
        double coValue = 0.0;
        String coText = "";
        String bodyText = "";
        for (int i = 0; i < station.getMetrics().length; i++) {
            if (station.getMetrics()[i].getFormula().equalsIgnoreCase("NO₂")) {
                noValue = station.getMetrics()[i].getValues()[0];
                noText = station.getMetrics()[i].getFormula() + ": " + noValue + " " + station.getMetrics()[i].getUnit();
                bodyText = formatBody(noText, bodyText);
            }
            if (station.getMetrics()[i].getFormula().equalsIgnoreCase("O₃")) {
                oValue = station.getMetrics()[i].getValues()[0];
                oText = station.getMetrics()[i].getFormula() + ": " + oValue + " " + station.getMetrics()[i].getUnit();
                bodyText = formatBody(oText, bodyText);
            }
            if (station.getMetrics()[i].getFormula().equalsIgnoreCase("PM10")) {
                pm10Value = station.getMetrics()[i].getValues()[0];
                pm10Text = station.getMetrics()[i].getFormula() + ": " + pm10Value + " " + station.getMetrics()[i].getUnit();
                bodyText = formatBody(pm10Text, bodyText);
            }
            if (station.getMetrics()[i].getFormula().equalsIgnoreCase("SO₂")) {
                soValue = station.getMetrics()[i].getValues()[0];
                soText = station.getMetrics()[i].getFormula() + ": " + soValue + " " + station.getMetrics()[i].getUnit();
                bodyText = formatBody(soText, bodyText);
            }
            if (station.getMetrics()[i].getFormula().equalsIgnoreCase("CO")) {
                coValue = station.getMetrics()[i].getValues()[0];
                coText = station.getMetrics()[i].getFormula() + ": " + coValue + " " + station.getMetrics()[i].getUnit();
                bodyText = formatBody(coText, bodyText);
            }
        }
        int level = 0;
        if (pm10Value <= 50.0 && soValue <= 175.0 && noValue <= 100.0 && coValue <= 5.0 && oValue <= 90.0) {
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.art_1));
            level = 1;
        } else if ((pm10Value > 50.0 && pm10Value < 90.0) || (soValue > 175.0 && soValue < 350.0) || (noValue > 100.0 && noValue < 200.0) || (coValue > 5.0 && coValue < 10.0) || (oValue > 90.0 && oValue < 180.0)) {
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.art_2));
            level = 2;
        } else if ((pm10Value > 90.0 && pm10Value < 150.0) || (soValue > 355.0 && soValue < 525.0) || (noValue > 200.0 && noValue < 300.0) || (coValue > 10.0 && coValue < 15.0) || (oValue > 180.0 && oValue < 240.0)) {
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.art_3));
            level = 3;
        } else if (pm10Value >= 150.0 || soValue >= 525.0 || noValue >= 300.0 || coValue >= 15.0 || oValue >= 240.0) {
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.art_4));
            level = 4;
        }
        Gson gson = new Gson();
        marker.title(gson.toJson(new DataPollution(station.getName(), bodyText, level)));
        return marker;
    }

    private String formatBody(String noText, String bodyText) {
        if (bodyText.isEmpty()) {
            bodyText = noText;
        } else {
            bodyText = bodyText + "\n" + noText;
        }
        return bodyText;
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
        Snackbar snackbar = Snackbar
                .make(mainLinearLayout, getActivity().getString(R.string.pollution_error), Snackbar.LENGTH_LONG);

        snackbar.show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        map.setInfoWindowAdapter(new PollutionAdapter(getActivity()));
        map.getUiSettings().setMapToolbarEnabled(false);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(madrid, 11.5f));
    }
}
