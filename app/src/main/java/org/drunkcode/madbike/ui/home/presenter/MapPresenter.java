package org.drunkcode.madbike.ui.home.presenter;

import android.support.v4.app.Fragment;

import org.drunkcode.madbike.R;
import org.drunkcode.madbike.ui.home.interactor.PostMapInteractor;
import org.drunkcode.madbike.ui.home.interactor.PostPollutionInteractor;
import org.drunkcode.madbike.ui.home.interactor.PostWeatherInteractor;
import org.drunkcode.madbike.ui.home.response.PollutionResponse;
import org.drunkcode.madbike.ui.home.response.TotemResponse;
import org.drunkcode.madbike.ui.home.response.WeatherResponse;
import org.drunkcode.madbike.ui.home.view.MapFragmentView;

import de.greenrobot.event.EventBus;

public class MapPresenter {

    private MapFragmentView mapView;
    private PostMapInteractor postMapInteractor;
    private PostWeatherInteractor postWeatherInteractor;

    public MapPresenter(Fragment fragment) {
        this.mapView = (MapFragmentView) fragment;
        this.postMapInteractor = new PostMapInteractor(fragment.getActivity());
        this.postWeatherInteractor = new PostWeatherInteractor(fragment.getActivity());
    }

    public void onResume() {
        EventBus.getDefault().register(this);
    }

    public void onPause() {
        EventBus.getDefault().unregister(this);
    }

    public void getStations() throws Throwable {
        mapView.showLoading(R.string.map_dialog_title, R.string.map_dialog_message);
        postMapInteractor.execute();
    }

    public void onEvent(TotemResponse totemResponse) {
        mapView.hideLoading();
        mapView.getTotemsResponse(totemResponse);
    }

    public void getWeather() throws Throwable {
        postWeatherInteractor.execute();
    }

    public void onEvent(WeatherResponse weatherResponse) {
        mapView.getWeatherResponse(weatherResponse);

    }


}
