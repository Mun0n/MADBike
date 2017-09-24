package org.drunkcode.madbike.utils.tracking;

import android.content.Context;
import android.os.Bundle;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.crashlytics.android.answers.LoginEvent;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;

import org.drunkcode.madbike.MADBikeApplication;

public class AnalyticsManager {

    private static final String SIGN_UP_EVENT = "SignUp";
    private static final String LOGIN_EVENT = "Login";
    private static final String LOGOUT_EVENT = "Logout";
    private static final String CHANGE_PASSWORD_EVENT = "ChangePassword";
    private static final String CONTENT_VIEW_EVENT = "%sView";
    private static final String SEARCH_EVENT = "Search";
    private static final String FAVORITES_DIALOG_EVENT = "FavoritesDialog";
    private static final String FAVORITES_SCREEN_EVENT = "FavoritesScreen";
    private static final String SEND_INCIDENCE_EVENT = "SendIncidence";
    private static final String SAVE_ALARM_EVENT = "SaveAlarm";
    private static final String SHOW_ALARM_EVENT = "ShowAlarmNotification";
    private static final String SHARE_STATION_EVENT = "ShareStation";

    private final Answers answers;
    private AppEventsLogger logger;
    private Context applicationContext;

    public AnalyticsManager(MADBikeApplication instance) {
        this.applicationContext = instance;
        answers = Answers.getInstance();
        logger = AppEventsLogger.newLogger(applicationContext);
    }

    public void trackSignUp() {
        logger.logEvent(SIGN_UP_EVENT);
        answers.logCustom(new CustomEvent(SIGN_UP_EVENT));
    }

    public void trackLoginResult(boolean loginSuccess) {
        Bundle bundle = new Bundle();
        if (loginSuccess) {
            bundle.putString(AppEventsConstants.EVENT_PARAM_VALUE_YES, String.valueOf(loginSuccess));
        } else {
            bundle.putString(AppEventsConstants.EVENT_PARAM_VALUE_NO, String.valueOf(loginSuccess));
        }
        logger.logEvent(LOGIN_EVENT, bundle);
        answers.logLogin(new LoginEvent().putMethod("Digits").putSuccess(loginSuccess));
    }

    public void trackLogout() {
        logger.logEvent(LOGOUT_EVENT);
        answers.logCustom(new CustomEvent(LOGOUT_EVENT));
    }

    public void trackChangePassword() {
        logger.logEvent(CHANGE_PASSWORD_EVENT);
        answers.logCustom(new CustomEvent(CHANGE_PASSWORD_EVENT));
    }

    public void trackContentView(String contentViewName) {
        logger.logEvent(String.format(CONTENT_VIEW_EVENT, contentViewName));
        answers.logCustom(new CustomEvent(String.format(CONTENT_VIEW_EVENT, contentViewName)));
    }

    public void trackSearch() {
        logger.logEvent(SEARCH_EVENT);
        answers.logCustom(new CustomEvent(SEARCH_EVENT));
    }

    public void trackFavorites(boolean fromDialog) {
        if (fromDialog) {
            logger.logEvent(FAVORITES_DIALOG_EVENT);
            answers.logCustom(new CustomEvent(FAVORITES_DIALOG_EVENT));
        } else {
            logger.logEvent(FAVORITES_SCREEN_EVENT);
            answers.logCustom(new CustomEvent(FAVORITES_SCREEN_EVENT));
        }
    }

    public void trackSendIncidence() {
        logger.logEvent(SEND_INCIDENCE_EVENT);
        answers.logCustom(new CustomEvent(SEND_INCIDENCE_EVENT));
    }

    public void trackSaveAlarm() {
        logger.logEvent(SAVE_ALARM_EVENT);
        answers.logCustom(new CustomEvent(SAVE_ALARM_EVENT));
    }

    public void trackAlarmNotification() {
        logger.logEvent(SHOW_ALARM_EVENT);
        answers.logCustom(new CustomEvent(SHOW_ALARM_EVENT));
    }

    public void trackShare() {
        logger.logEvent(SHARE_STATION_EVENT);
        answers.logCustom(new CustomEvent(SHARE_STATION_EVENT));
    }

    private static class Singleton {
        private static final AnalyticsManager INSTANCE = new AnalyticsManager(MADBikeApplication.getInstance());
    }

    public static AnalyticsManager getInstance() {
        return Singleton.INSTANCE;
    }
}
