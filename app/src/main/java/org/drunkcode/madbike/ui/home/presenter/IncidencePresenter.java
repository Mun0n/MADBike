package org.drunkcode.madbike.ui.home.presenter;

import android.app.Activity;
import android.support.v4.app.Fragment;

import org.drunkcode.madbike.R;
import org.drunkcode.madbike.base.BaseResponse;
import org.drunkcode.madbike.ui.home.interactor.PostIncidenceInteractor;
import org.drunkcode.madbike.ui.home.model.IncidenceModel;
import org.drunkcode.madbike.ui.home.view.IncidenceView;

import de.greenrobot.event.EventBus;

public class IncidencePresenter {

    private IncidenceView incidenceView;
    private PostIncidenceInteractor postIncidenceInteractor;

    public IncidencePresenter(Fragment fragment) {
        this.incidenceView = (IncidenceView) fragment;
        this.postIncidenceInteractor = new PostIncidenceInteractor(fragment.getActivity());
    }

    public void onResume() {
        EventBus.getDefault().register(this);
    }

    public void onPause() {
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(BaseResponse baseResponse) {
        incidenceView.hideLoading();
        incidenceView.getIncidenceResponse(baseResponse);
    }

    public void sendIncidence(IncidenceModel incidenceModel) throws Throwable {
        incidenceView.showLoading(R.string.incidence_dialog_title, R.string.incidence_dialog_message);
        postIncidenceInteractor.setIncidenceModel(incidenceModel);
        postIncidenceInteractor.execute();
    }
}
