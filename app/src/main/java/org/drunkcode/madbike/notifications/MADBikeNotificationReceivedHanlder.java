package org.drunkcode.madbike.notifications;

import com.onesignal.OSNotification;
import com.onesignal.OneSignal;

import org.json.JSONObject;

public class MADBikeNotificationReceivedHanlder implements OneSignal.NotificationReceivedHandler {

    @Override
    public void notificationReceived(OSNotification notification) {
        JSONObject data = notification.payload.additionalData;
        if(data != null){

        }
    }
}
