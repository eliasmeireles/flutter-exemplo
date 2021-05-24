package us.zoom.app.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import us.zoom.app.models.MeetingConfig;
import us.zoom.app.initsdk.InitAuthSDKCallback;
import us.zoom.app.inmeetingfunction.customizedmeetingui.other.MeetingCommonCallback;
import us.zoom.app.inmeetingfunction.customizedmeetingui.view.MeetingWindowHelper;
import us.zoom.app.inmeetingfunction.zoommeetingui.ZoomMeetingUISettingHelper;
import us.zoom.sdk.InMeetingAudioController;
import us.zoom.sdk.InMeetingChatMessage;
import us.zoom.sdk.InMeetingEventHandler;
import us.zoom.sdk.InMeetingNotificationHandle;
import us.zoom.sdk.JoinMeetingOptions;
import us.zoom.sdk.JoinMeetingParams;
import us.zoom.sdk.MeetingEndReason;
import us.zoom.sdk.MeetingError;
import us.zoom.sdk.MeetingService;
import us.zoom.sdk.MeetingStatus;
import us.zoom.sdk.ZoomSDK;

public abstract class ZoomSDKMeetingServiceActivity extends AppCompatActivity implements InitAuthSDKCallback, MeetingCommonCallback.CommonEvent {

    protected MeetingConfig meetingConfig;

    protected abstract Class<? extends Activity> getZoomActivityClass();

    private Timer timer;
    private final Handler handler = new Handler();

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println(meetingConfig.toString());
        timeRequestValidation();
    }

    @Override
    protected void onDestroy() {
        reset();
        super.onDestroy();
    }

    private void reset() {
        handler.removeCallbacksAndMessages(null);
        ZoomSDK instance = ZoomSDK.getInstance();
        if (instance.isInitialized() && instance.getMeetingService() != null && instance.getInMeetingService() != null) {
            instance.getMeetingService().removeListener(this);
            instance.getInMeetingService().removeListener(this);
            MeetingCommonCallback.getInstance().removeListener(this);
        }
    }

    private void timeRequestValidation() {
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> {
                    System.out.println("TIME_OUT");
                    errorAlert();
                });
            }
        }, 60000);
    }

    public boolean isInMeeting() {
        MeetingService meetingService = ZoomSDK.getInstance().getMeetingService();
        if (ZoomSDK.getInstance().isInitialized() &&
                meetingService != null) {
            MeetingStatus meetingStatus = meetingService.getMeetingStatus();
            if (meetingStatus != MeetingStatus.MEETING_STATUS_IDLE) {
                return meetingStatus == MeetingStatus.MEETING_STATUS_INMEETING ||
                        meetingStatus == MeetingStatus.MEETING_STATUS_CONNECTING ||
                        meetingStatus == MeetingStatus.MEETING_STATUS_RECONNECTING ||
                        meetingStatus == MeetingStatus.MEETING_STATUS_DISCONNECTING ||
                        meetingStatus == MeetingStatus.MEETING_STATUS_WAITINGFORHOST ||
                        meetingStatus == MeetingStatus.MEETING_STATUS_IN_WAITING_ROOM;
            }
        }
        return false;
    }

    protected void entrarNaReuniao() {
        if (ZoomSDK.getInstance().isInitialized()) {
            JoinMeetingOptions options = ZoomMeetingUISettingHelper.getJoinMeetingOptions();
            JoinMeetingParams joinMeetingParams = new JoinMeetingParams();
            joinMeetingParams.displayName = meetingConfig.getUserName();
            joinMeetingParams.password = meetingConfig.getMeetingPassword();
            joinMeetingParams.meetingNo = meetingConfig.getMeetingNumber();

            ZoomSDK instance = ZoomSDK.getInstance();
            MeetingService meetingService = instance.getMeetingService();
            meetingService.joinMeetingWithParams(this, joinMeetingParams, options);
            MeetingWindowHelper.getInstance().showMeetingWindow(this, false);
//            Toast.makeText(this, "Meeting is ready.", Toast.LENGTH_LONG).show();
        } else {
//            Toast.makeText(this, "Zoom já está inicializado!", Toast.LENGTH_LONG).show();
        }
    }

    protected InMeetingNotificationHandle getHandler() {
        return (context, intent) -> {
            intent = new Intent(context, getZoomActivityClass());
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            context.startActivity(intent);
            return true;
        };
    }


    @Override
    public void onZoomSDKInitializeResult(int statusCode, int internalErrorCode) {
        if (statusCode == 0 && ZoomSDK.getInstance().getInMeetingService() != null) {
            ZoomSDK.getInstance().getMeetingSettingsHelper().setCustomizedMeetingUIEnabled(true);
            ZoomSDK.getInstance().getMeetingService().addListener(this);
            ZoomSDK.getInstance().getInMeetingService().addListener(this);

            MeetingCommonCallback.getInstance().addListener(this);
            ZoomSDK.getInstance().getMeetingSettingsHelper().setCustomizedNotificationData(null, getHandler());
            Toast.makeText(this, "Zoom initialized", Toast.LENGTH_SHORT).show();
            entrarNaReuniao();
        } else {
            Toast.makeText(this, "onZoomSDKInitializeResult(" + statusCode + ", " + internalErrorCode + ")", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMeetingStatusChanged(MeetingStatus meetingStatus, int errorCode, int internalErrorCode) {
        System.out.println("onMeetingStatusChanged(" + errorCode + ", " + internalErrorCode + ")");
        Toast.makeText(this, "onMeetingStatusChanged(" + errorCode + ", " + internalErrorCode + ")", Toast.LENGTH_SHORT).show();

        if (!ZoomSDK.getInstance().isInitialized()) {
            return;
        }
        if (mostrarTelaDaMeeting(meetingStatus)) {
            Intent intent = new Intent(this, getZoomActivityClass());
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.setAction(InMeetingNotificationHandle.ACTION_RETURN_TO_CONF);
            startActivity(intent);
            finish();
        } else if (meetingStatus == MeetingStatus.MEETING_STATUS_DISCONNECTING) {
            finish();
        } else if (errorCode == MeetingError.MEETING_ERROR_USER_FULL) {
            usersInMeetingLimit();
        }
    }

    private boolean mostrarTelaDaMeeting(MeetingStatus meetingStatus) {
        return meetingStatus == MeetingStatus.MEETING_STATUS_INMEETING ||
                meetingStatus == MeetingStatus.MEETING_STATUS_IN_WAITING_ROOM ||
                meetingStatus == MeetingStatus.MEETING_STATUS_CONNECTING ||
                meetingStatus == MeetingStatus.MEETING_STATUS_WAITINGFORHOST;
    }

    public abstract void userKickedByHost();

    public abstract void meetingEndedByHost();

    public abstract void showJoinFailDialog();

    public abstract void errorAlert();

    public abstract void usersInMeetingLimit();

    @Override
    public void onMeetingNeedPasswordOrDisplayName(boolean b, boolean b1, InMeetingEventHandler inMeetingEventHandler) {
        showJoinFailDialog();
        finish();
    }

    @Override
    public void finish() {
        reset();
        super.finish();
    }

    @Override
    public void onWebinarNeedRegister() {

    }

    @Override
    public void onJoinWebinarNeedUserNameAndEmail(InMeetingEventHandler inMeetingEventHandler) {

    }

    @Override
    public void onMeetingNeedColseOtherMeeting(InMeetingEventHandler inMeetingEventHandler) {

    }

    @Override
    public void onMeetingFail(int i, int i1) {
        Log.e("onMeetingFail", "code: " + i + " errorCode: " + i1);
    }

    @Override
    public void onMeetingLeaveComplete(long ret) {
        if (MeetingEndReason.END_BY_SELF == ret) {
            return;
        }

        if (MeetingEndReason.KICK_BY_HOST == ret) {
            userKickedByHost();
        } else if (MeetingEndReason.END_BY_HOST == ret) {
            meetingEndedByHost();
        } else {
            showJoinFailDialog();
        }
        finish();
    }


    @Override
    public void onMeetingUserJoin(List<Long> list) {

    }

    @Override
    public void onMeetingUserLeave(List<Long> list) {

    }

    @Override
    public void onMeetingUserUpdated(long l) {

    }

    @Override
    public void onMeetingHostChanged(long l) {

    }

    @Override
    public void onMeetingCoHostChanged(long l) {

    }

    @Override
    public void onActiveVideoUserChanged(long l) {

    }

    @Override
    public void onActiveSpeakerVideoUserChanged(long l) {

    }

    @Override
    public void onSpotlightVideoChanged(boolean b) {

    }

    @Override
    public void onUserVideoStatusChanged(long l) {

    }

    @Override
    public void onUserNetworkQualityChanged(long l) {

    }

    @Override
    public void onMicrophoneStatusError(InMeetingAudioController.MobileRTCMicrophoneError mobileRTCMicrophoneError) {

    }

    @Override
    public void onUserAudioStatusChanged(long l) {

    }

    @Override
    public void onHostAskUnMute(long l) {

    }

    @Override
    public void onHostAskStartVideo(long l) {

    }

    @Override
    public void onUserAudioTypeChanged(long l) {

    }

    @Override
    public void onMyAudioSourceTypeChanged(int i) {

    }

    @Override
    public void onLowOrRaiseHandStatusChanged(long l, boolean b) {

    }

    @Override
    public void onMeetingSecureKeyNotification(byte[] bytes) {

    }

    @Override
    public void onChatMessageReceived(InMeetingChatMessage inMeetingChatMessage) {

    }

    @Override
    public void onSilentModeChanged(boolean b) {

    }

    @Override
    public void onFreeMeetingReminder(boolean b, boolean b1, boolean b2) {

    }

    @Override
    public void onMeetingActiveVideo(long l) {

    }

    @Override
    public void onSinkAttendeeChatPriviledgeChanged(int i) {

    }

    @Override
    public void onSinkAllowAttendeeChatNotification(int i) {

    }

    @Override
    public void onUserNameChanged(long l, String s) {

    }

    @Override
    public void onZoomAuthIdentityExpired() {

    }
}
