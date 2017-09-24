package org.drunkcode.madbike.common.notification;

import android.app.Activity;
import android.support.design.widget.Snackbar;

public class NotificationManager implements NotificationBuilder{

    private Activity context;

    public NotificationManager(Activity context) {
        this.context = context;
    }

    @Override
    public void showMessage(String message) {
        Snackbar.make(context.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }
}
