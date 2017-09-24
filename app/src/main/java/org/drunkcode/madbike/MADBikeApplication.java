package org.drunkcode.madbike;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.karumi.dexter.Dexter;
import com.onesignal.OneSignal;

import org.drunkcode.madbike.notifications.MADBikeNotificationOpenedHandler;
import org.drunkcode.madbike.notifications.MADBikeNotificationReceivedHanlder;

import io.branch.referral.Branch;

public class MADBikeApplication extends Application {

    private static MADBikeApplication sInstance;

    public static MADBikeApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        OneSignal.startInit(this).setNotificationOpenedHandler(new MADBikeNotificationOpenedHandler(this)).init();
        Branch.getAutoInstance(this);
        Dexter.initialize(this);
    }
}
