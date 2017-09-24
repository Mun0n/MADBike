package org.drunkcode.madbike.ui.login.view;

import org.drunkcode.madbike.base.BaseResponse;
import org.drunkcode.madbike.base.BaseView;
import org.drunkcode.madbike.ui.login.response.LoginResponse;

public interface LoginView extends BaseView {

    void getLoginResponse(LoginResponse response);

    void onRecoverResponse(BaseResponse baseResponse);
}
