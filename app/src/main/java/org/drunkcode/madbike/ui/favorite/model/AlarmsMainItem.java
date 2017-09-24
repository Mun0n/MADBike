package org.drunkcode.madbike.ui.favorite.model;

import com.google.gson.annotations.SerializedName;

public class AlarmsMainItem {

    @SerializedName("values")
    private AlarmItem[] alarms;

    public AlarmItem[] getAlarms() {
        return alarms;
    }

    public void setAlarms(AlarmItem[] alarms) {
        this.alarms = alarms;
    }
}
