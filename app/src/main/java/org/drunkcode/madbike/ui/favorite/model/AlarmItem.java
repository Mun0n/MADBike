package org.drunkcode.madbike.ui.favorite.model;

import com.google.gson.annotations.SerializedName;

import org.drunkcode.madbike.utils.JSONUtils;

public class AlarmItem {

    @SerializedName(JSONUtils.ID)
    private int id;
    @SerializedName(JSONUtils.NAME_EN)
    private String name;
    @SerializedName(JSONUtils.TIME)
    private String time;
    @SerializedName(JSONUtils.IS_DIARY)
    private boolean isDiary;

    public AlarmItem() {
    }

    public AlarmItem(int id, String name, String time, boolean isDiary) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.isDiary = isDiary;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isDiary() {
        return isDiary;
    }

    public void setDiary(boolean diary) {
        isDiary = diary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
