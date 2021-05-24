package us.zoom.app.inmeetingfunction.customizedmeetingui.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.ConfMgr;

import java.util.ArrayList;
import java.util.List;

import us.zoom.app.R;
import us.zoom.app.inmeetingfunction.customizedmeetingui.view.share.OptionMenu;
import us.zoom.app.ui.QAActivity;
import us.zoom.sdk.InMeetingAnnotationController;
import us.zoom.sdk.InMeetingAudioController;
import us.zoom.sdk.InMeetingChatMessage;
import us.zoom.sdk.InMeetingService;
import us.zoom.sdk.InMeetingShareController;
import us.zoom.sdk.InMeetingUserInfo;
import us.zoom.sdk.InMeetingVideoController;
import us.zoom.sdk.InMeetingWebinarController;
import us.zoom.sdk.ZoomSDK;

public class MeetingOptionBar extends FrameLayout implements View.OnClickListener, MeetingWindowHelper.OnNewMessageListener {

    private static final String TAG = "MeetingOptionBar";
    private final int MENU_DISCONNECT_AUDIO = 0;
    private final int MENU_SHOW_CHAT = 4;

    //webinar host&cohost
    private final int MENU_AllOW_PANELIST_START_VIDEO = 5;
    private final int MENU_AllOW_ATTENDEE_CHAT = 6;

    private final int MENU_DISALLOW_PANELIST_START_VIDEO = 7;
    private final int MENU_DISALLOW_ATTENDEE_CHAT = 8;

    private final int MENU_SPEAKER_ON = 9;
    private final int MENU_SPEAKER_OFF = 10;

    private final int MENU_ANNOTATION_OFF = 11;
    private final int MENU_ANNOTATION_ON = 12;
    private final int MENU_ANNOTATION_QA = 13;
    private final int MENU_SWITCH_DOMAIN = 14;
    private final int MENU_CREATE_BO = 15;
    private final int MENU_LOWER_ALL_HANDS = 16;
    private final int MENU_RECLAIM_HOST = 17;
    private final int MENU_WINDOW = 18;
    private final int MENU_SWITCH_AUDIO = 19;
    private final int MENU_LOWER_HAND = 20;
    private final int MENU_RISE_HAND = 21;
    MeetingOptionBarCallBack mCallBack;

    View mContentView;

    View mBottomBar;
    View mTopBar;

    private View mBtnLeave;
    private View mBtnShare;
    private View mBtnCamera;
    private View mBtnAudio;
    private View mBtnSwitchCamera;

    private ImageView mAudioStatusImg;
    private ImageView mVideoStatusImg;
    private ImageView mShareStatusImg;

    private TextView mMeetingAudioText;
    private TextView mMeetingVideoText;
    private TextView mMeetingShareText;
    private View newMessageAlert;

    private InMeetingService mInMeetingService;
    private InMeetingShareController mInMeetingShareController;
    private InMeetingVideoController mInMeetingVideoController;
    private InMeetingAudioController mInMeetingAudioController;
    private InMeetingWebinarController mInMeetingWebinarController;
    private InMeetingAnnotationController meetingAnnotationController;

    private Context mContext;
    private View mMoreOptions;

    @Override
    public void onNewMessageReceived(InMeetingChatMessage inMeetingChatMessage) {
        refreshToolbar();
    }

    public interface MeetingOptionBarCallBack {
        void onClickBack();

        void onClickSwitchCamera();

        void onClickLeave();

        void onClickAudio();

        void onClickVideo();

        void onClickShare();

        void onClickParticipants();

        void onClickChats();

        void onClickDisconnectAudio();

        void onClickSwitchLoudSpeaker();

        void onClickAdminBo();

        void onClickLowerAllHands();

        void onClickReclaimHost();

        void showMoreMenu(List<OptionMenu> optionMenus);

        void onHidden(boolean hidden);

        void onShowMiniWindows();

        void dismiss();

        void lowerRaiseHand(boolean lowerHand);

        void onOptionBarRefresh();
    }


    public MeetingOptionBar(Context context) {
        super(context);
        init(context);
    }

    public MeetingOptionBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public MeetingOptionBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setCallBack(MeetingOptionBarCallBack callBack) {
        this.mCallBack = callBack;
    }

    void init(Context context) {
        MeetingWindowHelper.getInstance().addOnNewMessageListener(this);
        mContext = context;
        mContentView = LayoutInflater.from(context).inflate(R.layout.layout_meeting_option, this, false);
        mContentView.setOnClickListener(null);

        addView(mContentView);

        mInMeetingService = ZoomSDK.getInstance().getInMeetingService();

        mInMeetingShareController = mInMeetingService.getInMeetingShareController();
        mInMeetingVideoController = mInMeetingService.getInMeetingVideoController();
        mInMeetingAudioController = mInMeetingService.getInMeetingAudioController();
        mInMeetingWebinarController = mInMeetingService.getInMeetingWebinarController();
        meetingAnnotationController = mInMeetingService.getInMeetingAnnotationController();


//        mContentView.setOnClickListener(this);
        mBottomBar = findViewById(R.id.bottom_bar);
        mTopBar = findViewById(R.id.top_bar);
        mBtnLeave = findViewById(R.id.btnLeaveZoomMeeting);
        mBtnLeave.setOnClickListener(this);
        mBtnShare = findViewById(R.id.btnShare);
        mBtnShare.setOnClickListener(this);

        mBtnCamera = findViewById(R.id.btnCamera);
        mBtnCamera.setOnClickListener(this);
        mBtnAudio = findViewById(R.id.btnAudio);
        mBtnAudio.setOnClickListener(this);
        findViewById(R.id.btnPlist).setOnClickListener(this);

        mAudioStatusImg = findViewById(R.id.audioStatusImage);
        mVideoStatusImg = findViewById(R.id.videotatusImage);
        mShareStatusImg = findViewById(R.id.shareStatusImage);
        newMessageAlert = findViewById(R.id.newMessageAlert);
        mMeetingAudioText = findViewById(R.id.text_audio);
        mMeetingVideoText = findViewById(R.id.text_video);
        mMeetingShareText = findViewById(R.id.text_share);


        mMoreOptions = findViewById(R.id.moreActionImg);
        mMoreOptions.setOnClickListener(this);

        mBtnSwitchCamera = findViewById(R.id.btnSwitchCamera);
        mBtnSwitchCamera.setOnClickListener(this);


        findViewById(R.id.btnBack).setOnClickListener(this);
    }

    public void hideMoreOptions() {
        mMoreOptions.setVisibility(GONE);
    }

    public void hideShareOption() {
        mBtnShare.setVisibility(GONE);
    }

    Runnable autoHidden = () -> hideOrShowToolbar(true);

    public void hideOrShowToolbar(boolean hidden) {
        removeCallbacks(autoHidden);
        if (hidden) {
            setVisibility(View.INVISIBLE);
        } else {
            refreshToolbar();
            postDelayed(autoHidden, 30000);
            setVisibility(View.VISIBLE);
            bringToFront();
        }
        if (null != mCallBack) {
            mCallBack.onHidden(hidden);
        }
    }


    public int getBottomBarHeight() {
        return mBottomBar.getMeasuredHeight();
    }

    public View getSwitchCameraView() {
        return mBtnSwitchCamera;
    }


    public void refreshToolbar() {
        updateAudioButton();
        updateVideoButton();
        updateShareButton();
        updateSwitchCameraButton();
        updateMessageAlert();
        mCallBack.onOptionBarRefresh();
    }

    private void updateMessageAlert() {
        newMessageAlert.setVisibility(GONE);
        if (MeetingWindowHelper.getInstance().messageCount > 0) {
            newMessageAlert.setVisibility(VISIBLE);
        }
    }

    public void updateAudioButton() {
        if (mInMeetingAudioController.isAudioConnected()) {
            if (mInMeetingAudioController.isMyAudioMuted()) {
                mAudioStatusImg.setImageResource(R.drawable.icon_meeting_audio_mute);
            } else {
                mAudioStatusImg.setImageResource(R.drawable.icon_meeting_audio);
            }
        } else {
            mAudioStatusImg.setImageResource(R.drawable.icon_meeting_noaudio);
        }
    }

    public boolean isMySelfWebinarAttendee() {
        InMeetingUserInfo myUserInfo = mInMeetingService.getMyUserInfo();
        if (myUserInfo != null && mInMeetingService.isWebinarMeeting()) {
            return myUserInfo.getInMeetingUserRole() == InMeetingUserInfo.InMeetingUserRole.USERROLE_ATTENDEE;
        }
        return false;
    }

    public void updateShareButton() {
        if (isMySelfWebinarAttendee()) {
            mBtnShare.setVisibility(View.GONE);
        } else {
            mBtnShare.setVisibility(View.VISIBLE);
            if (mInMeetingShareController.isSharingOut()) {
                mMeetingShareText.setText(R.string.zm_btn_stop_share);
                mShareStatusImg.setImageResource(R.drawable.icon_share_pause);
            } else {
                mMeetingShareText.setText(R.string.zm_btn_share);
                mShareStatusImg.setImageResource(R.drawable.icon_share_resume);
            }
        }
    }

    public void updateVideoButton() {
        if (mInMeetingVideoController.isMyVideoMuted()) {
            mVideoStatusImg.setImageResource(R.drawable.icon_meeting_video_mute);
        } else {
            mVideoStatusImg.setImageResource(R.drawable.icon_meeting_video);
        }
    }

    public boolean isChatEnable() {
        return !mInMeetingService.getInMeetingChatController().isChatDisabled();
    }

    public void updateSwitchCameraButton() {
        if (mInMeetingVideoController.isMyVideoMuted()) {
            mBtnSwitchCamera.setVisibility(View.GONE);
        } else {
            mBtnSwitchCamera.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnBack) {
            if (null != mCallBack) {
                mCallBack.onClickBack();
            }
        } else if (id == R.id.btnLeaveZoomMeeting) {
            if (null != mCallBack) {
                mCallBack.onClickLeave();
            }
        } else if (id == R.id.btnShare) {
            if (null != mCallBack) {
                mCallBack.onClickShare();
            }
        } else if (id == R.id.btnCamera) {
            if (null != mCallBack) {
                mCallBack.onClickVideo();
            }
        } else if (id == R.id.btnAudio) {
            if (null != mCallBack) {
                mCallBack.onClickAudio();
            }
        } else if (id == R.id.btnSwitchCamera) {
            if (null != mCallBack) {
                mCallBack.onClickSwitchCamera();
            }
        } else if (id == R.id.moreActionImg) {
            showMoreMenuPopupWindow();
        } else if (id == R.id.btnPlist) {
            if (null != mCallBack) {
                mCallBack.onClickParticipants();
            }
        } else {
            setVisibility(INVISIBLE);
        }
    }

    public void show() {
        hideOrShowToolbar(isShowing());
    }

    private boolean isMySelfWebinarHostCohost() {
        InMeetingUserInfo myUserInfo = mInMeetingService.getMyUserInfo();
        if (myUserInfo != null && mInMeetingService.isWebinarMeeting()) {
            return myUserInfo.getInMeetingUserRole() == InMeetingUserInfo.InMeetingUserRole.USERROLE_HOST
                    || myUserInfo.getInMeetingUserRole() == InMeetingUserInfo.InMeetingUserRole.USERROLE_COHOST;
        }
        return false;
    }

    private boolean isMySelfMeetingHostBoModerator() {
        InMeetingUserInfo myUserInfo = mInMeetingService.getMyUserInfo();
        if (myUserInfo != null && !mInMeetingService.isWebinarMeeting()) {
            InMeetingUserInfo.InMeetingUserRole role = myUserInfo.getInMeetingUserRole();
            return role == InMeetingUserInfo.InMeetingUserRole.USERROLE_HOST ||
                    role == InMeetingUserInfo.InMeetingUserRole.USERROLE_BREAKOUTROOM_MODERATOR;
        }
        return false;
    }

    private boolean isMySelfHostCohost() {
        InMeetingUserInfo myUserInfo = mInMeetingService.getMyUserInfo();
        if (myUserInfo != null) {
            return myUserInfo.getInMeetingUserRole() == InMeetingUserInfo.InMeetingUserRole.USERROLE_HOST
                    || myUserInfo.getInMeetingUserRole() == InMeetingUserInfo.InMeetingUserRole.USERROLE_COHOST;
        }
        return false;
    }

    private void showMoreMenuPopupWindow() {
        List<OptionMenu> optionMenus = new ArrayList<>();

        if (isChatEnable()) {
            String chatLabel = mContext.getString(R.string.zm_mi_chat);
            if (MeetingWindowHelper.getInstance().messageCount > 0) {
                chatLabel = chatLabel.concat(" (" + (MeetingWindowHelper.getInstance().messageCount
                        < 100 ? MeetingWindowHelper.getInstance().messageCount : "99+") + ")");
            }
            optionMenus.add(buildOptionMeu(R.drawable.zm_btn_plist, chatLabel, MENU_SHOW_CHAT));
        }

        optionMenus.add(buildOptionMeu(R.drawable.icon_shrink_gray, R.string.zm_sip_minimize_85332, MENU_WINDOW));

//        if (mInMeetingAudioController.isAudioConnected()) {
//            if (mInMeetingAudioController.isMyAudioMuted()) {
//                optionMenus.add(buildOptionMeu(R.drawable.zm_audio_off, R.string.zm_mi_mute, MENU_SWITCH_AUDIO));
//            }
//
//            if (!mInMeetingAudioController.isMyAudioMuted()) {
//                optionMenus.add(buildOptionMeu(R.drawable.zm_audio_on, R.string.zm_mi_unmute, MENU_SWITCH_AUDIO));
//            }
//        }

        ConfMgr instance = ConfMgr.getInstance();
        CmmUser myself = instance.getMyself();
        if (myself != null) {
            if (myself.getRaiseHandState()) {
                optionMenus.add(buildOptionMeu(R.drawable.ic_raise_hand, R.string.zm_btn_lower_hand, MENU_LOWER_HAND));
            } else {
                optionMenus.add(buildOptionMeu(R.drawable.ic_raise_hand, R.string.zm_btn_raise_hand, MENU_RISE_HAND));
            }
        }
//        if (mInMeetingAudioController.canSwitchAudioOutput()) {
//            if (mInMeetingAudioController.getLoudSpeakerStatus()) {
//                menuAdapter.addItem(new SimpleMenuItem(MENU_SPEAKER_OFF, "Speak Off"));
//            } else {
//                menuAdapter.addItem(new SimpleMenuItem(MENU_SPEAKER_ON, "Speak On"));
//            }
//        }

        if (meetingAnnotationController.canDisableViewerAnnotation()) {
//            if (!meetingAnnotationController.isViewerAnnotationDisabled()) {
//                menuAdapter.addItem((new SimpleMenuItem(MENU_ANNOTATION_OFF, "Disable Annotation")));
//            } else {
//                menuAdapter.addItem((new SimpleMenuItem(MENU_ANNOTATION_ON, "Enable Annotation")));
//            }
        }

//        if (isMySelfWebinarHostCohost()) {
//            if (mInMeetingWebinarController.isAllowPanellistStartVideo()) {
//                menuAdapter.addItem((new SimpleMenuItem(MENU_DISALLOW_PANELIST_START_VIDEO, "Disallow panelist start video")));
//            } else {
//                menuAdapter.addItem((new SimpleMenuItem(MENU_AllOW_PANELIST_START_VIDEO, "Allow panelist start video")));
//            }
//
//            if (mInMeetingWebinarController.isAllowAttendeeChat()) {
//                menuAdapter.addItem((new SimpleMenuItem(MENU_DISALLOW_ATTENDEE_CHAT, "Disallow attendee chat")));
//            } else {
//                menuAdapter.addItem((new SimpleMenuItem(MENU_AllOW_ATTENDEE_CHAT, "Allow attendee chat")));
//            }
//        }

        if (isMySelfHostCohost()) {
//            optionMenus.add(buildOptionMeu(R.drawable.zm_btn_plist, R.string.zm_btn_chats, ));
//            menuAdapter.addItem((new SimpleMenuItem(MENU_LOWER_ALL_HANDS, "Lower All Hands")));
        }

//        if (mInMeetingService.canReclaimHost()) {
//            menuAdapter.addItem((new SimpleMenuItem(MENU_RECLAIM_HOST, "Reclaim Host")));
//        }
        mCallBack.showMoreMenu(optionMenus);
    }

    private OptionMenu buildOptionMeu(int drawableId, int stringValueId, final int tagValue) {
        final OptionMenu optionMenu = new OptionMenu(mContext, drawableId, stringValueId);
        optionMenu.setOnClickListener(v -> {
            onMenuClick(tagValue);
            mCallBack.dismiss();
        });
        return optionMenu;
    }

    private OptionMenu buildOptionMeu(int drawableId, String text, final int tagValue) {
        final OptionMenu optionMenu = new OptionMenu(mContext, drawableId, text);
        optionMenu.setOnClickListener(v -> {
            onMenuClick(tagValue);
            mCallBack.dismiss();
        });
        return optionMenu;
    }

    private void onMenuClick(int tagValue) {
        switch (tagValue) {
            case MENU_DISCONNECT_AUDIO:
                if (null != mCallBack) {
                    mCallBack.onClickDisconnectAudio();
                }
                break;
            case MENU_SHOW_CHAT:
                if (null != mCallBack) {
                    mCallBack.onClickChats();
                    MeetingWindowHelper.getInstance().messageCount = 0;
                    refreshToolbar();
                }
                break;
            case MENU_SWITCH_AUDIO:
                if (null != mCallBack) {
                    mCallBack.onClickAudio();
                }
                break;
            case MENU_WINDOW:
                if (null != mCallBack) {
                    mCallBack.onShowMiniWindows();
                }
                break;
            case MENU_RISE_HAND:
                if (null != mCallBack) {
                    mCallBack.lowerRaiseHand(false);
                }
                break;

            case MENU_LOWER_HAND:
                if (null != mCallBack) {
                    mCallBack.lowerRaiseHand(true);
                }
                break;
            case MENU_AllOW_ATTENDEE_CHAT:
                mInMeetingWebinarController.allowAttendeeChat();
                break;
            case MENU_AllOW_PANELIST_START_VIDEO:
                mInMeetingWebinarController.allowPanelistStartVideo();
                break;
            case MENU_DISALLOW_ATTENDEE_CHAT:
                mInMeetingWebinarController.disallowAttendeeChat();
                break;
            case MENU_DISALLOW_PANELIST_START_VIDEO:
                mInMeetingWebinarController.disallowPanelistStartVideo();
                break;
            case MENU_SPEAKER_OFF:
            case MENU_SPEAKER_ON: {
                if (null != mCallBack) {
                    mCallBack.onClickSwitchLoudSpeaker();
                }
                break;
            }
            case MENU_ANNOTATION_ON: {
                meetingAnnotationController.disableViewerAnnotation(false);
                break;
            }
            case MENU_ANNOTATION_OFF: {
                meetingAnnotationController.disableViewerAnnotation(true);
                break;
            }
            case MENU_ANNOTATION_QA: {
                mContext.startActivity(new Intent(mContext, QAActivity.class));
                break;
            }
            case MENU_SWITCH_DOMAIN: {
                boolean success = ZoomSDK.getInstance().switchDomain("zoom.us", true);
                Log.d(TAG, "switchDomain:" + success);
                break;
            }
            case MENU_CREATE_BO: {
                if (null != mCallBack) {
                    mCallBack.onClickAdminBo();
                }
                break;
            }
            case MENU_LOWER_ALL_HANDS: {
                if (null != mCallBack) {
                    mCallBack.onClickLowerAllHands();
                }
                break;
            }
            case MENU_RECLAIM_HOST: {
                if (null != mCallBack) {
                    mCallBack.onClickReclaimHost();
                }
                break;
            }
        }
    }

    public boolean isShowing() {
        return getVisibility() == VISIBLE;
    }
}
