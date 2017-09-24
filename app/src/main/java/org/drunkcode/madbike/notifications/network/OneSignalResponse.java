package org.drunkcode.madbike.notifications.network;

import com.google.gson.annotations.SerializedName;

public class OneSignalResponse {

    @SerializedName("success")
    private boolean success;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}