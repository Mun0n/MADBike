package org.drunkcode.madbike.ui.profile.presenter;

import android.app.Activity;

import org.drunkcode.madbike.R;
import org.drunkcode.madbike.ui.profile.interactor.PostChangePasswordInteractor;
import org.drunkcode.madbike.ui.profile.interactor.PostProfileInteractor;
import org.drunkcode.madbike.ui.profile.model.ChangePasswordModel;
import org.drunkcode.madbike.ui.profile.response.ChangePasswordResponse;
import org.drunkcode.madbike.ui.profile.response.ProfileResponse;
import org.drunkcode.madbike.ui.profile.view.ProfileView;

import de.greenrobot.event.EventBus;

public class ProfilePresenter {

    private ProfileView profileView;
    private PostProfileInteractor postProfileInteractor;
    private PostChangePasswordInteractor postChangePasswordInteractor;

    public ProfilePresenter(Activity activity) {
        this.profileView = (ProfileView) activity;
        this.postProfileInteractor = new PostProfileInteractor(activity);
        this.postChangePasswordInteractor = new PostChangePasswordInteractor(activity);
    }

    public void onResume() {
        EventBus.getDefault().register(this);
    }

    public void onPause() {
        EventBus.getDefault().unregister(this);
    }

    public void getProfileData() throws Throwable {
        profileView.showLoading(R.string.profile_loading_title, R.string.profile_loading_message);
        postProfileInteractor.execute();
    }

    public void onEvent(ProfileResponse profileResponse) {
        profileView.hideLoading();
        profileView.getProfileResponse(profileResponse);
    }

    public void changePassword(ChangePasswordModel changePasswordModel) throws Throwable {
        profileView.showLoading(R.string.change_title, R.string.change_message);
        postChangePasswordInteractor.setChangePasswordModel(changePasswordModel);
        postChangePasswordInteractor.execute();

    }

    public void onEvent(ChangePasswordResponse changePasswordResponse){
        profileView.hideLoading();
        profileView.getChangePasswordResponse(changePasswordResponse);
    }
}
