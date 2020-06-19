package com.example.githubuser.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.example.githubuser.R;
import com.example.githubuser.provider.NotificationBroadcast;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationActivity extends AppCompatActivity {
    private static final int DAILY_NOTIFICATION_ID = 100;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferenceEditor;

    @BindView(R.id.notification_title) TextView txtNotificationTitle;
    @BindView(R.id.notification_switch) SwitchCompat scNotificationSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Notification Settings");
        }

        sharedPreferences = getSharedPreferences("reminder", Context.MODE_PRIVATE);

        scNotificationSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            sharedPreferenceEditor = sharedPreferences.edit();
            sharedPreferenceEditor.putBoolean("daily_notification", b);
            sharedPreferenceEditor.apply();

            if (b) {
                enableDailyNotification();
            } else {
                disableDailyNotification();
            }
        });

        boolean check = sharedPreferences.getBoolean("daily_notification", false);

        scNotificationSwitch.setChecked(check);
    }

    public void enableDailyNotification() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(getApplicationContext(), NotificationBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, DAILY_NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (alarmManager != null)
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public void disableDailyNotification() {
        Intent intent = new Intent(this, NotificationBroadcast.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, DAILY_NOTIFICATION_ID, intent, 0);
        pendingIntent.cancel();

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        if (alarmManager != null)
            alarmManager.cancel(pendingIntent);
    }
}
