package org.drunkcode.madbike.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences {

    private static final String ID_AUTH = "id_auth";
    private static final String USER_DATA = "user_data";
    private static final String USER_DNI = "user_dni";
    private static final String EMAIL = "email";
    private static final String ID_FAV = "id_fav";
    private static final String ALL_STATIONS = "all_stations";
    private static final String IS_LOGGED = "is_logged";
    private static final String PUSHES_IMPORTANT_INFORMATION = "pushes_important_info";
    private static final String PUSHES_UNAVAILABLE_STATIONS = "pushes_unavailable_stations";
    private static final String PUSHES_AVAILABLE_STATIONS = "pushes_available_stations";
    private static final String PUSHES_NEW_STATIONS = "pushes_new_stations";
    private static final String PUSHES_AIR_QUALITY_ALERTS = "pushes_air_quality_alerts";
    private static final String ALARM_LIST = "alarm_list";
    private static final String RATE_KEY = "rate_key";
    protected static Preferences instance;

    protected SharedPreferences sharedPreferences;

    protected Preferences(Context context) {
        sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
    }

    public static Preferences getInstance(Context context) {
        if (instance == null) {
            instance = new Preferences(context);
        }
        return instance;
    }

    public void setIdAuth(String idUser) {
        sharedPreferences.edit().putString(ID_AUTH, idUser).apply();
    }

    public String getIdAuth() {
        return sharedPreferences.getString(ID_AUTH, "");
    }

    public void setUserData(String userData) {
        sharedPreferences.edit().putString(USER_DATA, userData).apply();
    }

    public String getUserData() {
        return sharedPreferences.getString(USER_DATA, "");
    }

    public void setUserDni(String userDni) {
        sharedPreferences.edit().putString(USER_DNI, userDni).apply();
    }

    public String getUserDni() {
        return sharedPreferences.getString(USER_DNI, "");
    }

    public void setEmail(String email) {
        sharedPreferences.edit().putString(EMAIL, email).apply();
    }

    public String getEmail() {
        return sharedPreferences.getString(EMAIL, "");
    }

    public void setIdFav(String idFav) {
        sharedPreferences.edit().putString(ID_FAV, idFav).apply();
    }

    public String getIdFav() {
        return sharedPreferences.getString(ID_FAV, "");
    }

    public void setAllStations(String stations) {
        sharedPreferences.edit().putString(ALL_STATIONS, stations).apply();
    }

    public String getAllStations() {
        return sharedPreferences.getString(ALL_STATIONS, "");
    }

    public void setIsLogged(boolean isLogged) {
        sharedPreferences.edit().putBoolean(IS_LOGGED, isLogged).apply();
    }

    public boolean isLogged() {
        return sharedPreferences.getBoolean(IS_LOGGED, false);
    }

    public boolean getImportantInformationPushEnabled() {
        return sharedPreferences.getBoolean(PUSHES_IMPORTANT_INFORMATION, true);
    }

    public void setImportantInformationPushEnabled(boolean importantInformationPushEnabled) {
        sharedPreferences.edit().putBoolean(PUSHES_IMPORTANT_INFORMATION, importantInformationPushEnabled).apply();
    }

    public boolean getUnavailableStationsPushEnabled() {
        return sharedPreferences.getBoolean(PUSHES_UNAVAILABLE_STATIONS, true);
    }

    public void setUnavailableStationsPushEnabled(boolean unavailableStationsPushEnabled) {
        sharedPreferences.edit().putBoolean(PUSHES_UNAVAILABLE_STATIONS, unavailableStationsPushEnabled).apply();
    }

    public boolean getAvailableStationsPushEnabled() {
        return sharedPreferences.getBoolean(PUSHES_AVAILABLE_STATIONS, true);
    }

    public void setAvailableStationsPushEnabled(boolean availableStationsPushEnabled) {
        sharedPreferences.edit().putBoolean(PUSHES_AVAILABLE_STATIONS, availableStationsPushEnabled).apply();
    }

    public boolean getNewStationsPushEnabled() {
        return sharedPreferences.getBoolean(PUSHES_NEW_STATIONS, true);
    }

    public void setNewStationsPushEnabled(boolean newStationsPushEnabled) {
        sharedPreferences.edit().putBoolean(PUSHES_NEW_STATIONS, newStationsPushEnabled).apply();
    }

    public boolean getAirQualityAlertsPushEnabled() {
        return sharedPreferences.getBoolean(PUSHES_AIR_QUALITY_ALERTS, true);
    }

    public void setAirQualityAlertsPushEnabled(boolean airQualityAlertsPushEnabled) {
        sharedPreferences.edit().putBoolean(PUSHES_AIR_QUALITY_ALERTS, airQualityAlertsPushEnabled).apply();
    }

    public String getAlarmList() {
        return sharedPreferences.getString(ALARM_LIST, "");
    }

    public void setAlarmList(String alarmList) {
        sharedPreferences.edit().putString(ALARM_LIST, alarmList).apply();
    }

    public void setRateDone(boolean doneRate) {
        sharedPreferences.edit().putBoolean(RATE_KEY, doneRate).apply();
    }

    public boolean isRateDone() {
        return sharedPreferences.getBoolean(RATE_KEY, false);
    }
}
