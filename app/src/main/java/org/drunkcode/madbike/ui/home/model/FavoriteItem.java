package org.drunkcode.madbike.ui.home.model;

import com.google.gson.annotations.SerializedName;

import org.drunkcode.madbike.utils.JSONUtils;

public class FavoriteItem {

    @SerializedName(JSONUtils.ID)
    String id;

    public FavoriteItem(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
