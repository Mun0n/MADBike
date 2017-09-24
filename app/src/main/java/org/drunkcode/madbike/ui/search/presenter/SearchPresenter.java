package org.drunkcode.madbike.ui.search.presenter;


import android.app.Activity;

import com.google.android.gms.maps.model.LatLng;

import org.drunkcode.madbike.R;
import org.drunkcode.madbike.ui.search.interactor.PostSearchInteractor;
import org.drunkcode.madbike.ui.search.response.SearchResponse;
import org.drunkcode.madbike.ui.search.view.SearchActivityView;

import de.greenrobot.event.EventBus;

public class SearchPresenter {

    private SearchActivityView searchActivityView;
    private PostSearchInteractor postSearchInteractor;

    public SearchPresenter(Activity activity) {
        this.searchActivityView = (SearchActivityView) activity;
        this.postSearchInteractor = new PostSearchInteractor(activity);
    }

    public void onResume() {
        EventBus.getDefault().register(this);
    }

    public void onPause() {
        EventBus.getDefault().unregister(this);
    }

    public void getNearStations(LatLng latLng) throws Throwable {
        searchActivityView.showLoading(R.string.search_title, R.string.search_message);
        postSearchInteractor.setLatLng(latLng);
        postSearchInteractor.execute();
    }

    public void onEvent(SearchResponse searchResponse) {
        searchActivityView.hideLoading();
        searchActivityView.getSearchResponse(searchResponse);
    }

}
