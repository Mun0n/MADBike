package org.drunkcode.madbike.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.google.gson.Gson;

import org.drunkcode.madbike.R;
import org.drunkcode.madbike.preferences.Preferences;
import org.drunkcode.madbike.ui.favorite.activity.FavoriteActivity;
import org.drunkcode.madbike.ui.favorite.model.AlarmItem;
import org.drunkcode.madbike.ui.favorite.model.AlarmsMainItem;
import org.drunkcode.madbike.ui.station.activity.StationActivity;
import org.drunkcode.madbike.utils.tracking.AnalyticsManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        AnalyticsManager.getInstance().trackAlarmNotification();
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, StationActivity.class);
        notificationIntent.putExtra(FavoriteActivity.EXTRA_STATION_ID, intent.getStringExtra(FavoriteActivity.EXTRA_STATION_ID));
        notificationIntent.putExtra(FavoriteActivity.EXTRA_STATION_NAME, intent.getStringExtra(FavoriteActivity.EXTRA_STATION_NAME));
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        Gson gson = new Gson();
        if (!intent.getExtras().getBoolean(FavoriteActivity.EXTRA_ALARM_DIARY, false)) {
            String alarms = Preferences.getInstance(context).getAlarmList();
            if (alarms != null && !alarms.isEmpty()) {
                AlarmsMainItem alarmsMainItem = gson.fromJson(alarms, AlarmsMainItem.class);
                List<AlarmItem> alarmItemList = new ArrayList<>();
                alarmItemList.addAll(Arrays.asList(alarmsMainItem.getAlarms()));
                int position = 0;
                boolean found = false;
                for (int i = 0; i < alarmItemList.size(); i++) {
                    AlarmItem alarmItem = alarmItemList.get(i);
                    if (alarmItem.getId() == intent.getIntExtra(FavoriteActivity.EXTRA_ALARM_ID, 0)) {
                        position = i;
                        found = true;
                        break;
                    }
                }
                if (found) {
                    alarmItemList.remove(position);
                    alarmsMainItem = new AlarmsMainItem();
                    alarmsMainItem.setAlarms(alarmItemList.toArray(new AlarmItem[alarmItemList.size()]));
                    String alarmPreferences = gson.toJson(alarmsMainItem);
                    Preferences.getInstance(context).setAlarmList(alarmPreferences);
                }
            }
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
                context).setSmallIcon(R.drawable.ic_notification)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setContentTitle(context.getString(R.string.alarm_notification_title))
                .setContentText(intent.getExtras().getString(FavoriteActivity.EXTRA_STATION_NAME)).setSound(alarmSound)
                .setAutoCancel(true).setWhen(when)
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{1000, 1000});
        notificationManager.notify(createID(), mNotifyBuilder.build());

    }

    public int createID() {
        Date now = new Date();
        int id = Integer.parseInt(new SimpleDateFormat("ddHHmmss", Locale.US).format(now));
        return id;
    }
}
