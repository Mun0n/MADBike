package org.drunkcode.madbike.notifications;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.drunkcode.madbike.MADBikeApplication;
import org.drunkcode.madbike.network.OneSignalApi;
import org.drunkcode.madbike.notifications.network.OneSignalBody;
import org.drunkcode.madbike.notifications.network.OneSignalResponse;
import org.drunkcode.madbike.preferences.Preferences;
import org.drunkcode.madbike.ui.home.activity.HomeActivity;
import org.drunkcode.madbike.utils.OneSignalUtils;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MADBikeNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {


    private final MADBikeApplication madBikeApplication;

    public MADBikeNotificationOpenedHandler(MADBikeApplication madBikeApplication) {
        this.madBikeApplication = madBikeApplication;
    }

    @Override
    public void notificationOpened(OSNotificationOpenResult result) {
        sendRequestOpenedPush(result);
        JSONObject data = result.notification.payload.additionalData;
        if (data != null) {
            String madlink = data.optString("madbike_link", null);
            if (madlink != null) {
                processMadLink(madlink);
            }
        }
        OSNotificationAction.ActionType actionType = result.action.type;
        if (actionType == OSNotificationAction.ActionType.ActionTaken) {
            if (result.action.actionID.equalsIgnoreCase("rate")) {
                Preferences.getInstance(madBikeApplication).setRateDone(true);
                OneSignalUtils.getInstance().sendOneSignalTags();
                final String appPackageName = madBikeApplication.getPackageName();
                try {
                    madBikeApplication.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                } catch (android.content.ActivityNotFoundException anfe) {
                    madBikeApplication.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            }
        }
    }

    private void processMadLink(String madlink) {
        Intent intent = new Intent(madBikeApplication, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
        if (madlink.contains("stations") && madlink.contains("id=")) {
            String id = madlink.substring(madlink.indexOf("=") + 1, madlink.length());
            intent.putExtra(HomeActivity.EXTRA_DATA, HomeActivity.SECTION_MAP_STATION);
            intent.putExtra(HomeActivity.STATION_ID, id);
        } else if (madlink.contains("quality")) {
            intent.putExtra(HomeActivity.EXTRA_DATA, HomeActivity.SECTION_AIR_QUALITY);
        } else if (madlink.contains("news")) {
            intent.putExtra(HomeActivity.EXTRA_DATA, HomeActivity.SECTION_NEWS);
        } else if (madlink.contains("proposals")) {
            intent.putExtra(HomeActivity.EXTRA_DATA, HomeActivity.SECTION_PROPOSALS);
        } else if (madlink.contains("review")) {
            intent.putExtra(HomeActivity.EXTRA_DATA, HomeActivity.REVIEW_LINK);
        }
        madBikeApplication.startActivity(intent);
    }

    private void sendRequestOpenedPush(OSNotificationOpenResult result) {
        RestAdapter retrofit = new RestAdapter.Builder()
                .setEndpoint("https://onesignal.com/api/v1/")
                .build();

        OneSignalApi service = retrofit.create(OneSignalApi.class);
        service.openedOneSignalPush(String.valueOf(result.notification.androidNotificationId), new OneSignalBody(), new Callback<OneSignalResponse>() {
            @Override
            public void success(OneSignalResponse oneSignalResponse, Response response) {
                Log.d("RETROFIT", "Is success:" + oneSignalResponse.isSuccess());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("RETROFIT", error.toString());
            }
        });
    }
}
