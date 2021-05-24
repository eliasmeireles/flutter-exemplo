package com.example.flutter.poc_zoom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import us.zoom.app.models.MeetingConfig;
import us.zoom.app.initsdk.ZoomInitAuthSDK;
import us.zoom.app.ui.ZoomSDKMeetingServiceActivity;

public class ZoomStart extends ZoomSDKMeetingServiceActivity {

    public static final String MEETING_KEY = "MEETING_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_start);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(MEETING_KEY)) {
            meetingConfig = intent.getParcelableExtra(MEETING_KEY);
        }

//        Toast.makeText(this, meetingConfig.toString(), Toast.LENGTH_LONG).show();
        ZoomInitAuthSDK.getInstance().initialize(this);
    }

    @Override
    protected Class<? extends Activity> getZoomActivityClass() {
        return ZoomJoinMeetingActivity.class;
    }

    @Override
    public void userKickedByHost() {
        System.out.println("userKickedByHost");
    }

    @Override
    public void meetingEndedByHost() {
        System.out.println("meetingEndedByHost");
    }

    @Override
    public void showJoinFailDialog() {
        System.out.println("showJoinFailDialog");
    }

    @Override
    public void errorAlert() {
        System.out.println("errorAlert");
    }

    @Override
    public void usersInMeetingLimit() {
        System.out.println("usersInMeetingLimit");
    }

    @Override
    public MeetingConfig getMeetingConfig() {
        return this.meetingConfig;
    }

    @Override
    public Context getContext() {
        return this;
    }
}