package org.drunkcode.madbike.ui.login.presenter;

import android.app.Activity;

import org.drunkcode.madbike.R;
import org.drunkcode.madbike.base.BaseResponse;
import org.drunkcode.madbike.ui.login.interactor.PostLoginInteractor;
import org.drunkcode.madbike.ui.login.interactor.PostRecoverPasswordInteractor;
import org.drunkcode.madbike.ui.login.model.LoginModel;
import org.drunkcode.madbike.ui.login.model.RecoverModel;
import org.drunkcode.madbike.ui.login.response.LoginResponse;
import org.drunkcode.madbike.ui.login.response.RecoverResponse;
import org.drunkcode.madbike.ui.login.view.LoginView;

import de.greenrobot.event.EventBus;

public class LoginPresenter {

    private LoginView loginView;
    private PostLoginInteractor postLoginInteractor;
    private PostRecoverPasswordInteractor postRecoverPasswordInteractor;

    public LoginPresenter(Activity activity) {
        this.loginView = (LoginView) activity;
        this.postLoginInteractor = new PostLoginInteractor(activity);
        postRecoverPasswordInteractor = new PostRecoverPasswordInteractor(activity);
    }

    public void onResume() {
        EventBus.getDefault().register(this);
    }

    public void onPause() {
        EventBus.getDefault().unregister(this);
    }

    public void doLogin(LoginModel loginModel) throws Throwable {
        loginView.showLoading(R.string.dialog_login_title, R.string.dialog_login_message);
        postLoginInteractor.setLoginModel(loginModel);
        postLoginInteractor.execute();
    }

    public void onEvent(LoginResponse response) {
        loginView.hideLoading();
        loginView.getLoginResponse(response);
    }

    public void doRecoverPassword(RecoverModel recoverModel) throws Throwable {
        loginView.showLoading(R.string.dialog_recover_title, R.string.dialog_recover_message);
        postRecoverPasswordInteractor.setRecoverModel(recoverModel);
        postRecoverPasswordInteractor.execute();
    }

    public void onEvent(RecoverResponse recoverResponse){
        loginView.hideLoading();
        loginView.onRecoverResponse(recoverResponse);
    }
}
