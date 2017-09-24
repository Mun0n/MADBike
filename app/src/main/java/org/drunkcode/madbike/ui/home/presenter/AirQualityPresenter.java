package org.drunkcode.madbike.ui.home.presenter;

import android.support.v4.app.Fragment;

import org.drunkcode.madbike.R;
import org.drunkcode.madbike.ui.home.interactor.PostPollutionInteractor;
import org.drunkcode.madbike.ui.home.response.PollutionResponse;
import org.drunkcode.madbike.ui.home.view.AirQualityView;

import de.greenrobot.event.EventBus;

public class AirQualityPresenter{

    private AirQualityView airQualityView;
    private PostPollutionInteractor postPoullutionInteractor;

    public AirQualityPresenter(Fragment fragment){
        this.airQualityView = (AirQualityView) fragment;
        this.postPoullutionInteractor = new PostPollutionInteractor(fragment.getActivity());
    }

    public void onResume(){
        EventBus.getDefault().register(this);
    }

    public void onPause(){
        EventBus.getDefault().unregister(this);
    }

    public void getPollution() throws Throwable {
        airQualityView.showLoading(R.string.pollution_title, R.string.pollution_message);
        postPoullutionInteractor.execute();
    }

    public void onEvent(PollutionResponse pollutionResponse){
        airQualityView.hideLoading();
        airQualityView.getPollutionResponse(pollutionResponse);
    }
}
