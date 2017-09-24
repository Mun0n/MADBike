package org.drunkcode.madbike.ui.profile.response;

import com.google.gson.annotations.SerializedName;

import org.drunkcode.madbike.base.BaseResponse;
import org.drunkcode.madbike.utils.JSONUtils;

public class ChangePasswordResponse extends BaseResponse{

    @SerializedName(JSONUtils.ID_AUTH)
    private String idAuth;
    @SerializedName(JSONUtils.EMAIL)
    private String email;

    public ChangePasswordResponse(int success, String message) {
        super(success, message);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdAuth() {
        return idAuth;
    }

    public void setIdAuth(String idAuth) {
        this.idAuth = idAuth;
    }
}
