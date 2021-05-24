package com.example.flutter.poc_zoom;

import us.zoom.app.inmeetingfunction.customizedmeetingui.view.MeetingWindowHelper;
import us.zoom.app.inmeetingfunction.customizedmeetingui.view.ZoomCustomUIMeetingActivity;

public class ZoomJoinMeetingActivity extends ZoomCustomUIMeetingActivity {

    @Override
    protected void onResume() {
        super.onResume();
        MeetingWindowHelper.getInstance().setListenerActivity(ZoomJoinMeetingActivity.class);
        MeetingWindowHelper.getInstance().hiddenMeetingWindow(false);
    }


    @Override
    public void cantShare() {

    }

    @Override
    public void showOtherSharingTip() {

    }

    @Override
    public void showCantUnMuteAudio() {

    }

    @Override
    public void cantStartVideo() {

    }

    @Override
    protected void showLeaveMeetingDialog() {
        leaveMeeting();
    }

    @Override
    public void userKickedByHost() {

    }

    @Override
    public void meetingEndedByHost() {

    }

    @Override
    public void showJoinFailDialog() {

    }

    @Override
    public void onMeetingFailed() {

    }

    @Override
    public void usersInMeetingLimit() {

    }

    @Override
    public void requestWidowModePermission() {

    }
}