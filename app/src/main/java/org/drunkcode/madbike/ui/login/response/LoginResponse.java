package org.drunkcode.madbike.ui.login.response;

import com.google.gson.annotations.SerializedName;

import org.drunkcode.madbike.base.BaseResponse;
import org.drunkcode.madbike.ui.login.model.UserModel;
import org.drunkcode.madbike.utils.JSONUtils;

public class LoginResponse extends BaseResponse {

    @SerializedName(JSONUtils.USER)
    private UserModel[] userModel;

    public LoginResponse(int success, String message) {
        super(success, message);
    }

    public UserModel[] getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel[] userModel) {
        this.userModel = userModel;
    }
}
