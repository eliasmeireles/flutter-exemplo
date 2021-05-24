package us.zoom.app.inmeetingfunction.customizedmeetingui.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.List;

import us.zoom.androidlib.utils.ZmOsUtils;
import us.zoom.androidlib.widget.ZMAlertDialog;
import us.zoom.app.R;
import us.zoom.app.inmeetingfunction.customizedmeetingui.ChatActivity;
import us.zoom.app.inmeetingfunction.customizedmeetingui.SimpleInMeetingBOControllerListener;
import us.zoom.app.inmeetingfunction.customizedmeetingui.audio.MeetingAudioCallback;
import us.zoom.app.inmeetingfunction.customizedmeetingui.audio.MeetingAudioHelper;
import us.zoom.app.inmeetingfunction.customizedmeetingui.other.MeetingCommonCallback;
import us.zoom.app.inmeetingfunction.customizedmeetingui.remotecontrol.MeetingRemoteControlHelper;
import us.zoom.app.inmeetingfunction.customizedmeetingui.share.MeetingShareCallback;
import us.zoom.app.inmeetingfunction.customizedmeetingui.share.MeetingShareHelper;
import us.zoom.app.inmeetingfunction.customizedmeetingui.user.MeetingUserCallback;
import us.zoom.app.inmeetingfunction.customizedmeetingui.video.MeetingVideoCallback;
import us.zoom.app.inmeetingfunction.customizedmeetingui.video.MeetingVideoHelper;
import us.zoom.app.inmeetingfunction.customizedmeetingui.view.adapter.AttenderVideoAdapter;
import us.zoom.app.inmeetingfunction.customizedmeetingui.view.share.AnnotateToolbar;
import us.zoom.app.inmeetingfunction.customizedmeetingui.view.share.CustomShareView;
import us.zoom.app.inmeetingfunction.customizedmeetingui.view.share.OptionMenu;
import us.zoom.app.ui.dialog.CustomBottomSheetDialogFragment;
import us.zoom.sdk.IBOAttendee;
import us.zoom.sdk.IBOData;
import us.zoom.sdk.IBODataEvent;
import us.zoom.sdk.IBOMeeting;
import us.zoom.sdk.IZoomRetrieveSMSVerificationCodeHandler;
import us.zoom.sdk.IZoomVerifySMSVerificationCodeHandler;
import us.zoom.sdk.InMeetingBOController;
import us.zoom.sdk.InMeetingEventHandler;
import us.zoom.sdk.InMeetingService;
import us.zoom.sdk.InMeetingUserInfo;
import us.zoom.sdk.MeetingEndReason;
import us.zoom.sdk.MeetingError;
import us.zoom.sdk.MeetingService;
import us.zoom.sdk.MeetingStatus;
import us.zoom.sdk.MobileRTCRenderInfo;
import us.zoom.sdk.MobileRTCSMSVerificationError;
import us.zoom.sdk.MobileRTCShareView;
import us.zoom.sdk.MobileRTCVideoUnitRenderInfo;
import us.zoom.sdk.MobileRTCVideoView;
import us.zoom.sdk.MobileRTCVideoViewManager;
import us.zoom.sdk.SmsListener;
import us.zoom.sdk.ZoomSDK;
import us.zoom.sdk.ZoomSDKCountryCode;

import static us.zoom.sdk.MobileRTCSDKError.SDKERR_SUCCESS;

public abstract class ZoomCustomUIMeetingActivity extends AppCompatActivity implements View.OnClickListener, MeetingVideoCallback.VideoEvent,
        MeetingAudioCallback.AudioEvent, MeetingShareCallback.ShareEvent,
        MeetingUserCallback.UserEvent, MeetingCommonCallback.CommonEvent, SmsListener {

    private final static String TAG = ZoomCustomUIMeetingActivity.class.getSimpleName();

    public final static int REQUEST_PLIST = 1001;

    public final static int REQUEST_CAMERA_CODE = 1010;

    public final static int REQUEST_AUDIO_CODE = 1011;

    public final static int REQUEST_SHARE_SCREEN_PERMISSION = 1001;

    protected final static int REQUEST_SYSTEM_ALERT_WINDOW = 1002;

    protected final static int REQUEST_SYSTEM_ALERT_WINDOW_FOR_MINIWINDOW = 1003;

    private int from = 0;
    private int currentLayoutType = -1;
    private final int LAYOUT_TYPE_PREVIEW = 0;
    private final int LAYOUT_TYPE_WAITHOST = 1;
    private final int LAYOUT_TYPE_IN_WAIT_ROOM = 2;
    private final int LAYOUT_TYPE_ONLY_MYSELF = 3;
    private final int LAYOUT_TYPE_ONETOONE = 4;
    private final int LAYOUT_TYPE_LIST_VIDEO = 5;
    private final int LAYOUT_TYPE_VIEW_SHARE = 6;
    private final int LAYOUT_TYPE_SHARING_VIEW = 7;
    private final int LAYOUT_TYPE_WEBINAR_ATTENDEE = 8;

    private View mWaitJoinView;
    private View mWaitRoomView;
    private View mConnectingText;

    private boolean mMeetingFailed = false;

    public static long mCurShareUserId = -1;

    private MobileRTCVideoView mDefaultVideoView;
    private MobileRTCVideoViewManager mDefaultVideoViewMgr;

    private MeetingAudioHelper meetingAudioHelper;

    private MeetingVideoHelper meetingVideoHelper;

    private MeetingShareHelper meetingShareHelper;

    private MeetingRemoteControlHelper remoteControlHelper;

    private MeetingService mMeetingService;

    private InMeetingService mInMeetingService;

    private Intent mScreenInfoData;

    private MobileRTCShareView mShareView;
    private AnnotateToolbar mDrawingView;
    private FrameLayout mMeetingVideoView;

    private View mNormalSenceView;

    private CustomShareView customShareView;


    protected MeetingOptionBar meetingOptionBar;

    private GestureDetector gestureDetector;

    private CustomBottomSheetDialogFragment bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mMeetingService = ZoomSDK.getInstance().getMeetingService();
        mInMeetingService = ZoomSDK.getInstance().getInMeetingService();
        if (mMeetingService == null || mInMeetingService == null) {
            finish();
            return;
        }

        if (null != getIntent().getExtras()) {
            from = getIntent().getExtras().getInt("from");
        }
        meetingAudioHelper = new MeetingAudioHelper(audioCallBack);
        meetingVideoHelper = new MeetingVideoHelper(this, videoCallBack);
        meetingShareHelper = new MeetingShareHelper(this, shareCallBack);

        registerListener();

        setContentView(R.layout.activity_zoom_custom_ui_meeting);

        gestureDetector = new GestureDetector(this, new GestureDetectorListener());
        meetingOptionBar = findViewById(R.id.meeting_option_contain);
        meetingOptionBar.setCallBack(callBack);
        mMeetingVideoView = findViewById(R.id.meetingVideoView);
        mShareView = findViewById(R.id.sharingView);
        mDrawingView = findViewById(R.id.drawingView);

        mWaitJoinView = findViewById(R.id.waitJoinView);
        mWaitRoomView = findViewById(R.id.waitingRoom);

        LayoutInflater inflater = getLayoutInflater();

        mNormalSenceView = inflater.inflate(R.layout.layout_meeting_content_normal, null);
        mDefaultVideoView = mNormalSenceView.findViewById(R.id.videoView);

        customShareView = mNormalSenceView.findViewById(R.id.custom_share_view);
        remoteControlHelper = new MeetingRemoteControlHelper(customShareView);
        mMeetingVideoView.addView(mNormalSenceView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mConnectingText = findViewById(R.id.connectingTxt);
        refreshToolbar();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    MeetingVideoHelper.VideoCallBack videoCallBack = new MeetingVideoHelper.VideoCallBack() {
        @Override
        public boolean requestVideoPermission() {

            if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ZoomCustomUIMeetingActivity.this, new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_CODE);
                return false;
            }
            return true;
        }

        @Override
        public void showCameraList(PopupWindow popupWindow) {
            popupWindow.showAsDropDown(meetingOptionBar.getSwitchCameraView(), 0, 20);
        }

        @Override
        public void canStartVideo() {
            ZoomCustomUIMeetingActivity.this.cantStartVideo();
        }
    };

    MeetingAudioHelper.AudioCallBack audioCallBack = new MeetingAudioHelper.AudioCallBack() {
        @Override
        public boolean requestAudioPermission() {
            if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ZoomCustomUIMeetingActivity.this, new String[]{android.Manifest.permission.RECORD_AUDIO}, REQUEST_AUDIO_CODE);
                return false;
            }
            return true;
        }

        @Override
        public void updateAudioButton() {
            meetingOptionBar.updateAudioButton();
        }

        @Override
        public void cantUnMuteAudio() {
            showCantUnMuteAudio();
        }
    };

    MeetingShareHelper.MeetingShareUICallBack shareCallBack = new MeetingShareHelper.MeetingShareUICallBack() {
        @Override
        public void showShareMenu(PopupWindow popupWindow) {
            popupWindow.showAtLocation((View) meetingOptionBar.getParent(), Gravity.BOTTOM | Gravity.CENTER, 0, 150);
        }

        @Override
        public MobileRTCShareView getShareView() {
            return mShareView;
        }

        @Override
        public void cantShare() {
            ZoomCustomUIMeetingActivity.this.cantShare();
        }

        @Override
        public void showOtherSharingTip() {
            ZoomCustomUIMeetingActivity.this.showOtherSharingTip();
        }
    };

    public void onOptionBarRefreshed() {

    }

    public abstract void cantShare();

    public abstract void showOtherSharingTip();

    public abstract void showCantUnMuteAudio();

    public abstract void cantStartVideo();

    AttenderVideoAdapter.ItemClickListener pinVideoListener = new AttenderVideoAdapter.ItemClickListener() {
        @Override
        public void onItemClick(View view, int position, long userId) {
            if (currentLayoutType == LAYOUT_TYPE_VIEW_SHARE || currentLayoutType == LAYOUT_TYPE_SHARING_VIEW) {
                return;
            }
            mDefaultVideoViewMgr.removeAllVideoUnits();
            MobileRTCVideoUnitRenderInfo renderInfo = new MobileRTCVideoUnitRenderInfo(0, 0, 100, 100);
            mDefaultVideoViewMgr.addAttendeeVideoUnit(userId, renderInfo);
        }
    };

    @Override
    public void onClick(View v) {
    }

    class GestureDetectorListener extends GestureDetector.SimpleOnGestureListener {

        public GestureDetectorListener() {
            super();
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {

            if (mDrawingView.isAnnotationStarted() || remoteControlHelper.isEnableRemoteControl()) {
                meetingOptionBar.hideOrShowToolbar(true);
                return true;
            }
            if (mMeetingService.getMeetingStatus() == MeetingStatus.MEETING_STATUS_INMEETING) {
                meetingOptionBar.hideOrShowToolbar(meetingOptionBar.isShowing());
            }
            return true;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        gestureDetector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    private void refreshToolbar() {
        if (mMeetingService.getMeetingStatus() == MeetingStatus.MEETING_STATUS_CONNECTING) {
            mConnectingText.setVisibility(View.VISIBLE);
            meetingOptionBar.refreshToolbar();
        } else {
            mConnectingText.setVisibility(View.GONE);
        }
        meetingOptionBar.hideOrShowToolbar(true);
        onOptionBarRefreshed();
    }


    private void updateAnnotationBar() {
        if (meetingShareHelper.shareWhiteBord) {
            if (mCurShareUserId > 0 && !isMySelfWebinarAttendee()) {
                if (meetingShareHelper.isSenderSupportAnnotation(mCurShareUserId)) {
                    if (mInMeetingService.isMyself(mCurShareUserId) && !meetingShareHelper.isSharingScreen()) {
                        mDrawingView.setVisibility(View.VISIBLE);
                    } else {
                        if (currentLayoutType == LAYOUT_TYPE_VIEW_SHARE) {
                            mDrawingView.setVisibility(View.VISIBLE);
                        } else {
                            mDrawingView.setVisibility(View.GONE);
                        }
                    }
                } else {
                    mDrawingView.setVisibility(View.GONE);
                }

            } else {
                mDrawingView.setVisibility(View.GONE);
            }
        }
    }

    private void checkShowVideoLayout() {
        MeetingService meetingService = ZoomSDK.getInstance().getMeetingService();
        if (meetingService != null && meetingService.getMeetingStatus() == MeetingStatus.MEETING_STATUS_INMEETING) {
            mMeetingVideoView.setVisibility(View.VISIBLE);
        }
        mDefaultVideoViewMgr = mDefaultVideoView.getVideoViewManager();
        if (mDefaultVideoViewMgr != null) {
            int newLayoutType = getNewVideoMeetingLayout();
            if (currentLayoutType != newLayoutType) {
                removeOldLayout(currentLayoutType);
                currentLayoutType = newLayoutType;
                addNewLayout(newLayoutType);
            }
        }
        updateAnnotationBar();
    }

    private int getNewVideoMeetingLayout() {
        int newLayoutType = -1;
        if (mMeetingService.getMeetingStatus() == MeetingStatus.MEETING_STATUS_WAITINGFORHOST) {
            newLayoutType = LAYOUT_TYPE_WAITHOST;
            return newLayoutType;
        }

        if (mMeetingService.getMeetingStatus() == MeetingStatus.MEETING_STATUS_IN_WAITING_ROOM) {
            newLayoutType = LAYOUT_TYPE_IN_WAIT_ROOM;
            return newLayoutType;
        }

        if (mInMeetingService.isWebinarMeeting()) {
            if (isMySelfWebinarAttendee()) {
                newLayoutType = LAYOUT_TYPE_WEBINAR_ATTENDEE;
                return newLayoutType;
            }
        }

        if (meetingShareHelper.isOtherSharing()) {
            newLayoutType = LAYOUT_TYPE_VIEW_SHARE;
        } else if (meetingShareHelper.isSharingOut() && !meetingShareHelper.isSharingScreen()) {
            newLayoutType = LAYOUT_TYPE_SHARING_VIEW;
        } else {
            List<Long> userlist = mInMeetingService.getInMeetingUserList();
            int userCount = 0;
            if (userlist != null) {
                userCount = userlist.size();
            }

            if (userCount > 1) {
                int preCount = userCount;
                for (int i = 0; i < preCount; i++) {
                    InMeetingUserInfo userInfo = mInMeetingService.getUserInfoById(userlist.get(i));
                    if (mInMeetingService.isWebinarMeeting()) {
                        if (userInfo != null && userInfo.getInMeetingUserRole() == InMeetingUserInfo.InMeetingUserRole.USERROLE_ATTENDEE) {
                            userCount--;
                        }
                    }
                }
            }


            if (userCount == 0) {
                newLayoutType = LAYOUT_TYPE_PREVIEW;
            } else if (userCount == 1) {
                newLayoutType = LAYOUT_TYPE_ONLY_MYSELF;
            } else {
                newLayoutType = LAYOUT_TYPE_LIST_VIDEO;
            }
        }
        return newLayoutType;
    }

    private void removeOldLayout(int type) {
        if (type == LAYOUT_TYPE_WAITHOST) {
            mWaitJoinView.setVisibility(View.GONE);
            mMeetingVideoView.setVisibility(View.VISIBLE);
        } else if (type == LAYOUT_TYPE_IN_WAIT_ROOM) {
            mWaitRoomView.setVisibility(View.GONE);
            mMeetingVideoView.setVisibility(View.VISIBLE);
        } else if (type == LAYOUT_TYPE_PREVIEW || type == LAYOUT_TYPE_ONLY_MYSELF || type == LAYOUT_TYPE_ONETOONE) {
            mDefaultVideoViewMgr.removeAllVideoUnits();
        } else if (type == LAYOUT_TYPE_LIST_VIDEO || type == LAYOUT_TYPE_VIEW_SHARE) {
            mDefaultVideoViewMgr.removeAllVideoUnits();
            mDefaultVideoView.setGestureDetectorEnabled(false);
        } else if (type == LAYOUT_TYPE_SHARING_VIEW) {
            mShareView.setVisibility(View.GONE);
            mMeetingVideoView.setVisibility(View.VISIBLE);
        }

        if (type != LAYOUT_TYPE_SHARING_VIEW) {
            if (null != customShareView) {
                customShareView.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void addNewLayout(int type) {
        if (type == LAYOUT_TYPE_WAITHOST) {
            mWaitJoinView.setVisibility(View.VISIBLE);
            refreshToolbar();
            mMeetingVideoView.setVisibility(View.GONE);
        } else if (type == LAYOUT_TYPE_IN_WAIT_ROOM) {
            mWaitRoomView.setVisibility(View.VISIBLE);
            refreshToolbar();
            mMeetingVideoView.setVisibility(View.GONE);
            mDrawingView.setVisibility(View.GONE);
        } else if (type == LAYOUT_TYPE_PREVIEW) {
            showPreviewLayout();
        } else if (type == LAYOUT_TYPE_ONLY_MYSELF || type == LAYOUT_TYPE_WEBINAR_ATTENDEE) {
            showOnlyMeLayout();
        } else if (type == LAYOUT_TYPE_ONETOONE) {
            showOne2OneLayout();
        } else if (type == LAYOUT_TYPE_LIST_VIDEO) {
            showVideoListLayout();
        } else if (type == LAYOUT_TYPE_VIEW_SHARE) {
            showViewShareLayout();
        } else if (type == LAYOUT_TYPE_SHARING_VIEW) {
            showSharingViewOutLayout();
        }
    }

    private void showPreviewLayout() {
        MobileRTCVideoUnitRenderInfo renderInfo1 = new MobileRTCVideoUnitRenderInfo(0, 0, 100, 100);
        mDefaultVideoView.setVisibility(View.VISIBLE);
        mDefaultVideoViewMgr.addPreviewVideoUnit(renderInfo1);
    }

    private void showOnlyMeLayout() {
        mDefaultVideoView.setVisibility(View.VISIBLE);
        MobileRTCVideoUnitRenderInfo renderInfo = new MobileRTCVideoUnitRenderInfo(0, 0, 100, 100);
        InMeetingUserInfo myUserInfo = mInMeetingService.getMyUserInfo();
        if (myUserInfo != null) {
            mDefaultVideoViewMgr.removeAllVideoUnits();
            if (isMySelfWebinarAttendee()) {
                if (mCurShareUserId > 0) {
                    mDefaultVideoViewMgr.addShareVideoUnit(mCurShareUserId, renderInfo);
                } else {
                    mDefaultVideoViewMgr.addActiveVideoUnit(renderInfo);
                }
            } else {
                mDefaultVideoViewMgr.addAttendeeVideoUnit(myUserInfo.getUserId(), renderInfo);
            }
        }
    }


    private void showOne2OneLayout() {
        mDefaultVideoView.setVisibility(View.VISIBLE);
        MobileRTCVideoUnitRenderInfo renderInfo = new MobileRTCVideoUnitRenderInfo(0, 0, 100, 100);
        //options.aspect_mode = MobileRTCVideoUnitAspectMode.VIDEO_ASPECT_PAN_AND_SCAN;
        mDefaultVideoViewMgr.addActiveVideoUnit(renderInfo);

    }

    private void showVideoListLayout() {
        MobileRTCVideoUnitRenderInfo renderInfo = new MobileRTCVideoUnitRenderInfo(0, 0, 100, 100);
        //options.aspect_mode = MobileRTCVideoUnitAspectMode.VIDEO_ASPECT_PAN_AND_SCAN;
        mDefaultVideoViewMgr.addActiveVideoUnit(renderInfo);
    }

    private void showSharingViewOutLayout() {
        mMeetingVideoView.setVisibility(View.GONE);
        mShareView.setVisibility(View.VISIBLE);
    }


    private void showViewShareLayout() {
        if (!isMySelfWebinarAttendee()) {
            mDefaultVideoView.setVisibility(View.VISIBLE);
            mDefaultVideoView.setOnClickListener(null);
            mDefaultVideoView.setGestureDetectorEnabled(true);
            long shareUserId = mInMeetingService.activeShareUserID();
            MobileRTCRenderInfo renderInfo1 = new MobileRTCRenderInfo(0, 0, 100, 100);
            mDefaultVideoViewMgr.addShareVideoUnit(shareUserId, renderInfo1);
            customShareView.setMobileRTCVideoView(mDefaultVideoView);
            remoteControlHelper.refreshRemoteControlStatus();
        } else {
            mDefaultVideoView.setVisibility(View.VISIBLE);
            mDefaultVideoView.setOnClickListener(null);
            mDefaultVideoView.setGestureDetectorEnabled(true);
            long shareUserId = mInMeetingService.activeShareUserID();
            MobileRTCRenderInfo renderInfo1 = new MobileRTCRenderInfo(0, 0, 100, 100);
            mDefaultVideoViewMgr.addShareVideoUnit(shareUserId, renderInfo1);
        }
    }

    private boolean isMySelfWebinarAttendee() {
        InMeetingUserInfo myUserInfo = mInMeetingService.getMyUserInfo();
        if (myUserInfo != null && mInMeetingService.isWebinarMeeting()) {
            return myUserInfo.getInMeetingUserRole() == InMeetingUserInfo.InMeetingUserRole.USERROLE_ATTENDEE;
        }
        return false;
    }

    private boolean isMySelfWebinarHostCohost() {
        InMeetingUserInfo myUserInfo = mInMeetingService.getMyUserInfo();
        if (myUserInfo != null && mInMeetingService.isWebinarMeeting()) {
            return myUserInfo.getInMeetingUserRole() == InMeetingUserInfo.InMeetingUserRole.USERROLE_HOST
                    || myUserInfo.getInMeetingUserRole() == InMeetingUserInfo.InMeetingUserRole.USERROLE_COHOST;
        }
        return false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        meetingVideoHelper.checkVideoRotation(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        checkShowVideoLayout();
        meetingVideoHelper.checkVideoRotation(this);
        mDefaultVideoView.onResume();
        if (MeetingWindowHelper.getInstance().leaveMeeting) {
            MeetingWindowHelper.getInstance().leaveMeeting = false;
            finish();
        }
        onOptionBarRefreshed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDefaultVideoView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        clearSubscribe();
    }

    private void clearSubscribe() {
        if (null != mDefaultVideoViewMgr) {
            mDefaultVideoViewMgr.removeActiveVideoUnit();
        }
        currentLayoutType = -1;
    }

    @Override
    protected void onDestroy() {
        if (null != remoteControlHelper) {
            remoteControlHelper.onDestroy();
        }
        unRegisterListener();
        super.onDestroy();
    }

    MeetingOptionBar.MeetingOptionBarCallBack callBack = new MeetingOptionBar.MeetingOptionBarCallBack() {
        @Override
        public void onClickBack() {
            showMainActivity();
        }

        @Override
        public void onClickSwitchCamera() {
            meetingVideoHelper.switchCamera();
        }

        @Override
        public void onClickLeave() {
            showLeaveMeetingDialog();
        }

        @Override
        public void onClickAudio() {
            meetingAudioHelper.switchAudio();
        }

        @Override
        public void onClickVideo() {
            meetingVideoHelper.switchVideo();
        }

        @Override
        public void onClickShare() {
            meetingShareHelper.onClickShare();
        }

        @Override
        public void onClickParticipants() {
            mInMeetingService.showZoomParticipantsUI(ZoomCustomUIMeetingActivity.this, REQUEST_PLIST);
        }

        @Override
        public void onClickChats() {
            Intent intent = new Intent(ZoomCustomUIMeetingActivity.this, ChatActivity.class);
            startActivity(intent);
        }


        @Override
        public void onClickDisconnectAudio() {
            meetingAudioHelper.disconnectAudio();
        }

        @Override
        public void onClickSwitchLoudSpeaker() {
            meetingAudioHelper.switchLoudSpeaker();
        }

        @Override
        public void onClickAdminBo() {

        }

        @Override
        public void lowerRaiseHand(boolean lowerHand) {
            try {
                if (lowerHand) {
                    long userId = mInMeetingService.getMyUserInfo().getUserId();
                    mInMeetingService.lowerHand(userId);
                } else {
                    mInMeetingService.raiseMyHand();
                }
            } catch (Exception ignored) {
            }
        }

        @Override
        public void onOptionBarRefresh() {
            onOptionBarRefreshed();
        }

        @Override
        public void onClickLowerAllHands() {
            if (mInMeetingService.lowerAllHands() == SDKERR_SUCCESS) {
                ;
            }
        }

        @Override
        public void onClickReclaimHost() {
            if (mInMeetingService.reclaimHost() == SDKERR_SUCCESS) {
                ;
            }
        }

        @Override
        public void showMoreMenu(List<OptionMenu> optionMenus) {
            bottomSheetDialog = CustomBottomSheetDialogFragment.getInstance(optionMenus);
            bottomSheetDialog.show(getSupportFragmentManager(), bottomSheetDialog.getClass().getName());
        }

        @Override
        public void onHidden(boolean hidden) {
        }

        @Override
        public void onShowMiniWindows() {
            showMainActivity();
        }

        @Override
        public void dismiss() {
            if (bottomSheetDialog != null && bottomSheetDialog.isVisible()) {
                bottomSheetDialog.dismiss();
                bottomSheetDialog = null;
            }
        }
    };


    @Override
    public void onBackPressed() {
        showLeaveMeetingDialog();
    }

    private void showMainActivity() {
        if (ZmOsUtils.isAtLeastN() && !Settings.canDrawOverlays(this)) {
            requestWidowModePermission();
        } else {
            mDefaultVideoView.removeAllViews();
            mDefaultVideoView.setVisibility(View.GONE);
            findViewById(R.id.screen_blocked).setVisibility(View.VISIBLE);
            MeetingWindowHelper.getInstance().showMeetingWindow(ZoomCustomUIMeetingActivity.this, true);
            clearSubscribe();
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_SHARE_SCREEN_PERMISSION:
                if (resultCode != RESULT_OK) {
                    if (us.zoom.videomeetings.BuildConfig.DEBUG) {
                        Log.d(TAG, "onActivityResult REQUEST_SHARE_SCREEN_PERMISSION no ok ");
                    }
                    break;
                }
                startShareScreen(data);
                break;
            case REQUEST_SYSTEM_ALERT_WINDOW:
                meetingShareHelper.startShareScreenSession(mScreenInfoData);
                break;
        }
    }

    protected abstract void showLeaveMeetingDialog();

    protected void leaveMeeting() {
        if (meetingShareHelper.isSharingOut()) {
            meetingShareHelper.stopShare();
        }
        mInMeetingService.leaveCurrentMeeting(true);
        finish();
    }

    @SuppressLint("NewApi")
    protected void startShareScreen(Intent data) {
        if (data == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 24 && !Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            mScreenInfoData = data;
            startActivityForResult(intent, REQUEST_SYSTEM_ALERT_WINDOW);
        } else {
            meetingShareHelper.startShareScreenSession(data);
        }
    }

    @Override
    public int checkSelfPermission(String permission) {
        if (permission == null || permission.length() == 0) {
            return PackageManager.PERMISSION_DENIED;
        }
        try {
            return checkPermission(permission, android.os.Process.myPid(), android.os.Process.myUid());
        } catch (Throwable e) {
            return PackageManager.PERMISSION_DENIED;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int i = 0; i < permissions.length; i++) {
            if (Manifest.permission.RECORD_AUDIO.equals(permissions[i])) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    meetingAudioHelper.switchAudio();
                }
            } else if (Manifest.permission.CAMERA.equals(permissions[i])) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    meetingVideoHelper.switchVideo();
                }
            }
        }
    }


    @Override
    public void onUserAudioStatusChanged(long userId) {
        meetingAudioHelper.onUserAudioStatusChanged(userId);
    }

    @Override
    public void onUserAudioTypeChanged(long userId) {
        meetingAudioHelper.onUserAudioTypeChanged(userId);
    }

    @Override
    public void onMyAudioSourceTypeChanged(int type) {
        meetingAudioHelper.onMyAudioSourceTypeChanged(type);
    }

    @Override
    public void onUserVideoStatusChanged(long userId) {
        meetingOptionBar.updateVideoButton();
        meetingOptionBar.updateSwitchCameraButton();
    }

    @Override
    public void onShareActiveUser(long userId) {
        meetingShareHelper.onShareActiveUser(mCurShareUserId, userId);
        mCurShareUserId = userId;
        meetingOptionBar.updateShareButton();
        checkShowVideoLayout();
        onOptionBarRefreshed();
    }

    @Override
    public void onSilentModeChanged(boolean inSilentMode) {
        if (inSilentMode) {
            meetingShareHelper.stopShare();
        }
    }

    @Override
    public void onShareUserReceivingStatus(long userId) {
        Log.i(TAG, "onSpotlightVideoChanged");

    }

    @Override
    public void onMeetingUserJoin(List<Long> userList) {
        checkShowVideoLayout();
    }

    @Override
    public void onMeetingUserLeave(List<Long> userList) {
        checkShowVideoLayout();
    }

    @Override
    public void onWebinarNeedRegister() {
        Log.i(TAG, "onWebinarNeedRegister");
    }

    @Override
    public void onMeetingFail(int errorCode, int internalErrorCode) {
        onMeetingFailed();
//        mMeetingFailed = true;
//        mMeetingVideoView.setVisibility(View.GONE);
//        mConnectingText.setVisibility(View.GONE);
//        ZoomSDK.getInstance().logoutZoom();
//        finish();
    }

    @Override
    public void onMeetingLeaveComplete(long ret) {
        if (MeetingEndReason.END_BY_SELF == ret) {
            leaveMeeting();
        } else if (MeetingEndReason.KICK_BY_HOST == ret) {
            userKickedByHost();
        } else if (MeetingEndReason.END_BY_HOST == ret) {
            meetingEndedByHost();
        } else {
            showJoinFailDialog();
        }
        MeetingWindowHelper.getInstance().leaveMeeting = true;
    }

    public abstract void userKickedByHost();

    public abstract void meetingEndedByHost();

    public abstract void showJoinFailDialog();

    public abstract void onMeetingFailed();

    public abstract void usersInMeetingLimit();

    public abstract void requestWidowModePermission();

    @Override
    public void onMeetingNeedPasswordOrDisplayName(boolean needPassword, boolean needDisplayName, InMeetingEventHandler handler) {
        showJoinFailDialog();
    }

    @Override
    public void onMeetingStatusChanged(MeetingStatus meetingStatus, int errorCode, int internalErrorCode) {
        if (errorCode == MeetingError.MEETING_ERROR_USER_FULL) {
            usersInMeetingLimit();
        } else {
            checkShowVideoLayout();
            refreshToolbar();
        }
    }

    @Override
    public void onMeetingNeedColseOtherMeeting(InMeetingEventHandler inMeetingEventHandler) {
        Log.d(TAG, "onMeetingNeedColseOtherMeeting:");
    }

    @Override
    public void onJoinWebinarNeedUserNameAndEmail(InMeetingEventHandler inMeetingEventHandler) {
        long time = System.currentTimeMillis();
        finish();
//        showWebinarNeedRegisterDialog(inMeetingEventHandler);
//        inMeetingEventHandler.setRegisterWebinarInfo("test", time+"@example.com", false);
    }

    @Override
    public void onFreeMeetingReminder(boolean isOrignalHost, boolean canUpgrade, boolean isFirstGift) {
        Log.d(TAG, "onFreeMeetingReminder:" + isOrignalHost + " " + canUpgrade + " " + isFirstGift);
    }

    @Override
    public void onNeedRealNameAuthMeetingNotification(List<ZoomSDKCountryCode> supportCountryList, String privacyUrl, IZoomRetrieveSMSVerificationCodeHandler handler) {
        Log.d(TAG, "onNeedRealNameAuthMeetingNotification:" + privacyUrl);
        Log.d(TAG, "onNeedRealNameAuthMeetingNotification getRealNameAuthPrivacyURL:" + ZoomSDK.getInstance().getSmsService().getRealNameAuthPrivacyURL());
        RealNameAuthDialog.show(this, handler);
    }

    @Override
    public void onRetrieveSMSVerificationCodeResultNotification(MobileRTCSMSVerificationError result, IZoomVerifySMSVerificationCodeHandler handler) {
        Log.d(TAG, "onRetrieveSMSVerificationCodeResultNotification:" + result);
    }

    @Override
    public void onVerifySMSVerificationCodeResultNotification(MobileRTCSMSVerificationError result) {
        Log.d(TAG, "onVerifySMSVerificationCodeResultNotification:" + result);
    }

    private void unRegisterListener() {
        try {
            MeetingAudioCallback.getInstance().removeListener(this);
            MeetingVideoCallback.getInstance().removeListener(this);
            MeetingShareCallback.getInstance().removeListener(this);
            MeetingUserCallback.getInstance().removeListener(this);
            MeetingCommonCallback.getInstance().removeListener(this);
            ZoomSDK.getInstance().getSmsService().removeListener(this);
            ZoomSDK.getInstance().getInMeetingService().getInMeetingBOController().removeListener(mBOControllerListener);
        } catch (Exception e) {
        }
    }


    private void registerListener() {
        ZoomSDK.getInstance().getSmsService().addListener(this);
        ZoomSDK.getInstance().getInMeetingService().getInMeetingBOController().addListener(mBOControllerListener);
        MeetingAudioCallback.getInstance().addListener(this);
        MeetingVideoCallback.getInstance().addListener(this);
        MeetingShareCallback.getInstance().addListener(this);
        MeetingUserCallback.getInstance().addListener(this);
        MeetingCommonCallback.getInstance().addListener(this);

    }

    private SimpleInMeetingBOControllerListener mBOControllerListener = new SimpleInMeetingBOControllerListener() {

        ZMAlertDialog dialog;

        @Override
        public void onHasAttendeeRightsNotification(final IBOAttendee iboAttendee) {
            super.onHasAttendeeRightsNotification(iboAttendee);
            Log.d(TAG, "onHasAttendeeRightsNotification");
        }

        @Override
        public void onHasDataHelperRightsNotification(IBOData iboData) {
            Log.d(TAG, "onHasDataHelperRightsNotification");
            iboData.setEvent(iboDataEvent);
        }

        @Override
        public void onLostAttendeeRightsNotification() {
            super.onLostAttendeeRightsNotification();
            Log.d(TAG, "onLostAttendeeRightsNotification");
            if (null != dialog && dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    };

    private IBODataEvent iboDataEvent = new IBODataEvent() {
        @Override
        public void onBOInfoUpdated(String strBOID) {
            InMeetingBOController boController = mInMeetingService.getInMeetingBOController();
            IBOData iboData = boController.getBODataHelper();
            if (iboData != null) {
                IBOMeeting iboMeeting = iboData.getBOMeetingByID(strBOID);
            }
        }

        @Override
        public void onUnAssignedUserUpdated() {
            Log.d(TAG, "onUnAssignedUserUpdated");
        }
    };
}

