package org.drunkcode.madbike.base;

import com.google.gson.annotations.SerializedName;

import org.drunkcode.madbike.utils.JSONUtils;

public class BaseResponse {

    @SerializedName(JSONUtils.SUCCESS)
    private int success;
    @SerializedName(JSONUtils.MESSAGE)
    private String message;

    public BaseResponse() {
    }

    public BaseResponse(int success, String message) {
        this.success = success;
        this.message = message;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
