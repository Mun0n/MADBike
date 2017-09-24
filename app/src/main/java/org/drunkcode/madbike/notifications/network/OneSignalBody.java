package org.drunkcode.madbike.notifications.network;

import com.google.gson.annotations.SerializedName;

import org.drunkcode.madbike.BuildConfig;

public class OneSignalBody {

    @SerializedName("opened")
    private boolean opened;
    @SerializedName("app_id")
    private String appId;

    public OneSignalBody(){
        this.opened = true;
        this.appId = "86a74c4c-d8dc-45d6-9ecc-abab2592ed58";
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
