package org.drunkcode.madbike.ui.favorite.activity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.google.gson.Gson;

import org.drunkcode.madbike.R;
import org.drunkcode.madbike.base.BaseActivity;
import org.drunkcode.madbike.preferences.Preferences;
import org.drunkcode.madbike.ui.favorite.adapter.AlarmAdapter;
import org.drunkcode.madbike.ui.favorite.listener.AlarmListener;
import org.drunkcode.madbike.ui.favorite.model.AlarmItem;
import org.drunkcode.madbike.ui.favorite.model.AlarmsMainItem;
import org.drunkcode.madbike.utils.AlarmReceiver;
import org.drunkcode.madbike.utils.tracking.AnalyticsManager;
import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.InjectView;

public class AlarmActivity extends BaseActivity implements AlarmListener {

    @InjectView(R.id.closeButton)
    ImageButton closeImageButton;
    @InjectView(R.id.saveButton)
    Button saveButton;
    @InjectView(R.id.hourSubtitle)
    TextView hourSubtitle;
    @InjectView(R.id.repeatSubtitle)
    TextView repeatSubtitle;
    @InjectView(R.id.hourLayout)
    LinearLayout hourLinearLayout;
    @InjectView(R.id.repeatLayout)
    LinearLayout repeatLinearLayout;
    @InjectView(R.id.alarmsRecyclerView)
    RecyclerView alarmsRecyclerView;
    @InjectView(R.id.stationNameTextView)
    TextView titleTextView;

    private String stationId;
    private String stationName;
    private Gson gson;
    private AlarmAdapter alarmAdapter;
    private List<AlarmItem> alarmsList = new ArrayList<>();

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_alarm;
    }

    @Override
    protected String getActivityTitle() {
        return getString(R.string.alarm_title);
    }

    @Override
    protected boolean getActivityHomeAsUpEnabled() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AnalyticsManager.getInstance().trackContentView("AlarmScreen");
        gson = new Gson();
        closeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveButtonPressed();
            }
        });

        if (getIntent().getExtras().getString(FavoriteActivity.EXTRA_STATION_NAME) != null) {
            stationName = getIntent().getExtras().getString(FavoriteActivity.EXTRA_STATION_NAME);
            titleTextView.setText(stationName);
            stationId = getIntent().getExtras().getString(FavoriteActivity.EXTRA_STATION_ID);
        }

        hourLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AlarmActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        hourSubtitle.setText(String.format("%02d", selectedHour) + ":" + String.format("%02d", selectedMinute));
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle(R.string.select_time);
                mTimePicker.show();
            }
        });

        repeatLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(AlarmActivity.this);
                dialog.setTitle(R.string.select_recurrence);

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        AlarmActivity.this,
                        android.R.layout.select_dialog_singlechoice);
                arrayAdapter.add(getString(R.string.recurrence_never));
                arrayAdapter.add(getString(R.string.recurrence_diary));

                dialog.setSingleChoiceItems(arrayAdapter, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        repeatSubtitle.setText(arrayAdapter.getItem(i));
                        dialogInterface.dismiss();
                    }
                });
                dialog.show();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        alarmsRecyclerView.setLayoutManager(linearLayoutManager);
        alarmsRecyclerView.setHasFixedSize(true);
        alarmAdapter = new AlarmAdapter(this, alarmsList, this);
        alarmsRecyclerView.setAdapter(alarmAdapter);

        alarmsRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        paintAlarmList();

    }

    private void paintAlarmList() {
        String alarms = Preferences.getInstance(this).getAlarmList();
        if (alarms != null && !alarms.isEmpty()) {
            AlarmsMainItem alarmsMainItem = gson.fromJson(alarms, AlarmsMainItem.class);
            alarmAdapter.setAlarms(Arrays.asList(alarmsMainItem.getAlarms()));
            alarmsList.addAll(Arrays.asList(alarmsMainItem.getAlarms()));
        }
    }

    private void onSaveButtonPressed() {
        scheduleAlarm(hourSubtitle.getText().toString(), repeatSubtitle.getText().toString().equalsIgnoreCase(getString(R.string.recurrence_diary)));
    }

    private void scheduleAlarm(String hour, boolean isDiary) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour.substring(0, hour.indexOf(":"))));
        calendar.set(Calendar.MINUTE, Integer.parseInt(hour.substring(hour.indexOf(":") + 1)));
        Intent alarmIntent = new Intent(AlarmActivity.this, AlarmReceiver.class);
        int alarmId = createID();
        alarmIntent.setData(Uri.parse("madbike://" + alarmId));
        alarmIntent.setAction(String.valueOf(alarmId));
        alarmIntent.putExtra(FavoriteActivity.EXTRA_STATION_NAME, stationName);
        alarmIntent.putExtra(FavoriteActivity.EXTRA_STATION_ID, stationId);
        alarmIntent.putExtra(FavoriteActivity.EXTRA_ALARM_ID, alarmId);
        alarmIntent.putExtra(FavoriteActivity.EXTRA_ALARM_DIARY, isDiary);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(AlarmActivity.this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) AlarmActivity.this.getSystemService(ALARM_SERVICE);
        if (isDiary) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
        AlarmItem alarmItem = new AlarmItem(alarmId, stationName, hour, isDiary);
        saveAlarmItem(alarmItem);
        Toast.makeText(getApplicationContext(), R.string.alarm_is_set, Toast.LENGTH_SHORT).show();
        finish();
    }

    private void saveAlarmItem(AlarmItem alarmItem) {
        AnalyticsManager.getInstance().trackSaveAlarm();
        String alarmList = Preferences.getInstance(this).getAlarmList();
        if (alarmList != null && !alarmList.isEmpty()) {
            AlarmsMainItem alarmsMainItem = gson.fromJson(alarmList, AlarmsMainItem.class);
            List<AlarmItem> alarmItemList = new ArrayList<>();
            alarmItemList.addAll(Arrays.asList(alarmsMainItem.getAlarms()));
            alarmItemList.add(alarmItem);
            alarmsMainItem.setAlarms(alarmItemList.toArray(new AlarmItem[alarmItemList.size()]));
            alarmList = gson.toJson(alarmsMainItem);
        } else {
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(alarmItem);
            alarmList = gson.toJson(jsonArray);
        }
        Preferences.getInstance(this).setAlarmList(alarmList);
    }

    public int createID() {
        Date now = new Date();
        int id = Integer.parseInt(new SimpleDateFormat("ddHHmmss", Locale.US).format(now));
        return id;
    }

    @Override
    public void onAlarmDeleteClicked(AlarmItem alarmItem, int position) {
        Intent alarmIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
        alarmIntent.setData(Uri.parse("custom://" + alarmItem.getId()));
        alarmIntent.setAction(String.valueOf(alarmItem.getId()));
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent displayIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarmIntent, 0);
        alarmManager.cancel(displayIntent);
        alarmsList.remove(position);
        AlarmsMainItem alarmsMainItem = new AlarmsMainItem();
        alarmsMainItem.setAlarms(alarmsList.toArray(new AlarmItem[alarmsList.size()]));
        String alarmPreferences = gson.toJson(alarmsMainItem);
        Preferences.getInstance(this).setAlarmList(alarmPreferences);
        alarmAdapter.setAlarms(alarmsList);
    }

    class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
        private Drawable mDivider;

        public SimpleDividerItemDecoration(Context context) {
            mDivider = context.getResources().getDrawable(R.drawable.line_divider);
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + mDivider.getIntrinsicHeight();

                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }
}
