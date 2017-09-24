package org.drunkcode.madbike.ui.profile.response;

import com.google.gson.annotations.SerializedName;

import org.drunkcode.madbike.base.BaseResponse;
import org.drunkcode.madbike.ui.profile.model.UserProfileModel;
import org.drunkcode.madbike.utils.JSONUtils;

public class ProfileResponse extends BaseResponse{

    @SerializedName(JSONUtils.USER)
    private UserProfileModel[] userProfileModel;
    @SerializedName(JSONUtils.TITLE)
    private String title;

    public ProfileResponse(int success, String message) {
        super(success, message);
    }

    public UserProfileModel[] getUserProfileModel() {
        return userProfileModel;
    }

    public void setUserProfileModel(UserProfileModel[] userProfileModel) {
        this.userProfileModel = userProfileModel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
