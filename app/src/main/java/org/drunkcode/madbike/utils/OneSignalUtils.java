package org.drunkcode.madbike.utils;

import android.content.Context;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.onesignal.OneSignal;

import org.drunkcode.madbike.MADBikeApplication;
import org.drunkcode.madbike.preferences.Preferences;
import org.drunkcode.madbike.ui.home.model.FavoriteItem;
import org.json.JSONException;
import org.json.JSONObject;

public class OneSignalUtils {

    private Context applicationContext;

    private static class Singleton {
        private static final OneSignalUtils INSTANCE = new OneSignalUtils(MADBikeApplication.getInstance());
    }

    public static OneSignalUtils getInstance() {
        return Singleton.INSTANCE;
    }

    public OneSignalUtils(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void sendOneSignalTags() {
        try {
            JSONObject tags = new JSONObject();
            tags.put("ImportantInformation", PreferenceManager.getDefaultSharedPreferences(applicationContext).getBoolean("important_switch", true));
            tags.put("UnavailableStations", PreferenceManager.getDefaultSharedPreferences(applicationContext).getBoolean("unavailable_switch", true));
            tags.put("AvailableStations", PreferenceManager.getDefaultSharedPreferences(applicationContext).getBoolean("available_switch", true));
            tags.put("NewStations", PreferenceManager.getDefaultSharedPreferences(applicationContext).getBoolean("new_stations_switch", true));
            tags.put("AirQualityAlerts", PreferenceManager.getDefaultSharedPreferences(applicationContext).getBoolean("air_quality_switch", true));
            tags.put("rate", Preferences.getInstance(applicationContext).isRateDone());
            Gson gson = new Gson();
            FavoriteItem[] favorites = gson.fromJson(Preferences.getInstance(applicationContext).getIdFav(), FavoriteItem[].class);
            if (favorites != null && favorites.length > 0) {
                for (int i = 0; i < favorites.length; i++) {
                    String favText = String.format("Station%s", favorites[i].getId());
                    tags.put(favText, true);
                }
            }
            OneSignal.sendTags(tags);
        } catch (JSONException e) {
            //e.printStackTrace();
        }
    }
}
