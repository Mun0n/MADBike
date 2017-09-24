package org.drunkcode.madbike.ui.station.presenter;

import android.app.Activity;
import org.drunkcode.madbike.R;
import org.drunkcode.madbike.ui.station.interactor.GetStationInteractor;
import org.drunkcode.madbike.ui.station.response.StationResponse;
import org.drunkcode.madbike.ui.station.view.StationView;

import de.greenrobot.event.EventBus;

public class StationPresenter {

    private StationView view;
    private GetStationInteractor getStationInteractor;

    public StationPresenter(Activity activity) {
        this.view = (StationView) activity;
        this.getStationInteractor = new GetStationInteractor(activity);
    }

    public void onResume() {
        EventBus.getDefault().register(this);
    }

    public void onPause() {
        EventBus.getDefault().unregister(this);
    }

    public void getStationgInfo(String stationId) throws Throwable {
        getStationInteractor.setStationId(stationId);
        view.showLoading(R.string.station_title, R.string.station_message);
        getStationInteractor.execute();
    }

    public void onEvent(StationResponse stationResponse) {
        view.hideLoading();
        view.getStationResponse(stationResponse);
    }

}
