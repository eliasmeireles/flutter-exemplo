package us.zoom.app.inmeetingfunction.customizedmeetingui.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

import us.zoom.amdroidlib.util.OverlayHelper;
import us.zoom.androidlib.utils.ZmCommonUtils;
import us.zoom.androidlib.utils.ZmOsUtils;
import us.zoom.app.R;
import us.zoom.sdk.InMeetingAudioController;
import us.zoom.sdk.InMeetingChatMessage;
import us.zoom.sdk.InMeetingEventHandler;
import us.zoom.sdk.InMeetingService;
import us.zoom.sdk.InMeetingServiceListener;
import us.zoom.sdk.InMeetingShareController;
import us.zoom.sdk.MeetingEndReason;
import us.zoom.sdk.MeetingServiceListener;
import us.zoom.sdk.MeetingStatus;
import us.zoom.sdk.MobileRTCVideoUnitRenderInfo;
import us.zoom.sdk.MobileRTCVideoView;
import us.zoom.sdk.ZoomSDK;

public class MeetingWindowHelper implements InMeetingShareController.InMeetingShareListener,
        MeetingServiceListener, InMeetingServiceListener {
    public final static int REQUEST_SYSTEM_ALERT_WINDOW = 1020;
    private Class<? extends Activity> listenerActivity;
    private boolean mbAddedView = false;
    public boolean leaveMeeting = false;
    View windowView;

    int lastX, lastY;

    public int messageCount = 0;

    GestureDetector gestureDetector;

    MobileRTCVideoView mobileRTCVideoView;

    MobileRTCVideoUnitRenderInfo renderInfo;

    private WindowManager mWindowManager;

    private static MeetingWindowHelper instance;

    private SoftReference<Context> refContext;
    private List<OnNewMessageListener> onNewMessagesListeners;

    private MeetingWindowHelper() {

    }

    public static MeetingWindowHelper getInstance() {
        if (null == instance) {
            synchronized (MeetingWindowHelper.class) {
                if (null == instance) {
                    instance = new MeetingWindowHelper();
                    instance.onNewMessagesListeners = new ArrayList<>();
                    ZoomSDK.getInstance().getMeetingService().addListener(instance);
                    ZoomSDK.getInstance().getInMeetingService().addListener(instance);
                }
            }
        }
        return instance;
    }

    public void addOnNewMessageListener(OnNewMessageListener listener) {
        onNewMessagesListeners.add(listener);
    }

    public void removeOnNewMessageListener(OnNewMessageListener listener) {
        for (OnNewMessageListener onNewMessageListener : onNewMessagesListeners) {
            if (onNewMessageListener == listener) {
                onNewMessagesListeners.remove(listener);
                break;
            }
        }
    }

    public void setMessageCount(InMeetingChatMessage inMeetingChatMessage) {
        this.messageCount++;
        for (OnNewMessageListener onNewMessageListener : onNewMessagesListeners) {
            onNewMessageListener.onNewMessageReceived(inMeetingChatMessage);
        }
    }

    public void setListenerActivity(Class<? extends Activity> listenerActivity) {
        this.listenerActivity = listenerActivity;
    }

    public void showMeetingWindow(final Activity context, boolean tryWindowMode) {

        ZoomSDK.getInstance().getInMeetingService().getInMeetingShareController().addListener(this);
        ZoomSDK.getInstance().getInMeetingService().addListener(this);
        List<Long> userList = ZoomSDK.getInstance().getInMeetingService().getInMeetingUserList();
        if (null == userList || userList.size() < 2) {
            //only me
            return;
        }

        if (mbAddedView) {
            windowView.setVisibility(View.VISIBLE);
            addVideoUnit();
            return;
        }

        if (!tryWindowMode) {
            return;
        }
        showMiniMeetingWindow(context);
    }

    public void onActivityResult(int requestCode, Context context) {
        if (refContext != null && refContext.get() != null && refContext.get() == context) {
            if (requestCode == REQUEST_SYSTEM_ALERT_WINDOW) {
                if (OverlayHelper.getInstance().isNeedListenOverlayPermissionChanged()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        OverlayHelper.getInstance().stopListenOverlayPermissionChange(context);
                    }
                }
                if ((ZmOsUtils.isAtLeastN() && !Settings.canDrawOverlays(context)) && (!OverlayHelper.getInstance().isNeedListenOverlayPermissionChanged() || !OverlayHelper.getInstance().ismCanDraw())) {
                    return;
                }
                showMiniMeetingWindow(context);
            }
        }
    }

    @SuppressLint("InflateParams")
    private void showMiniMeetingWindow(final Context context) {
        refContext = new SoftReference<>(context);
        if (null == mWindowManager) {
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }

        if (null == windowView) {
            windowView = LayoutInflater.from(context).inflate(R.layout.layout_meeting_window, null);
            mobileRTCVideoView = (MobileRTCVideoView) windowView.findViewById(R.id.active_video_view);
            renderInfo = new MobileRTCVideoUnitRenderInfo(0, 0, 100, 100);
            renderInfo.is_border_visible = true;
            gestureDetector = new GestureDetector(context, new SingleTapConfirm());
            windowView.setOnTouchListener(onTouchListener);
        }

        mWindowManager.addView(windowView, getLayoutParams(context));
        mbAddedView = true;
        addVideoUnit();
    }

    private void addVideoUnit() {
        InMeetingShareController shareController = ZoomSDK.getInstance().getInMeetingService().getInMeetingShareController();
        mobileRTCVideoView.getVideoViewManager().removeAllVideoUnits();
        final long shareUserId = ZoomCustomUIMeetingActivity.mCurShareUserId;
        if (shareUserId > 0 && (shareController.isOtherSharing() || shareController.isSharingOut())) {
            mobileRTCVideoView.getVideoViewManager().addShareVideoUnit(shareUserId, renderInfo);
        } else {
            mobileRTCVideoView.getVideoViewManager().addActiveVideoUnit(renderInfo);
        }
        mobileRTCVideoView.onResume();
    }

    public void destroy() {
        try {
            this.windowView.setVisibility(View.GONE);
            this.mWindowManager.removeView(this.windowView);
            this.mbAddedView = false;
            this.windowView = null;
            this.mobileRTCVideoView = null;

            ZoomSDK.getInstance().getInMeetingService().removeListener(this);
            ZoomSDK.getInstance().getInMeetingService().getInMeetingShareController().removeListener(this);
        } catch (Exception ignored) {
        }
    }

    @Override
    public void onMeetingStatusChanged(MeetingStatus meetingStatus, int i, int i1) {
//        if (meetingStatus == MeetingStatus.MEETING_STATUS_DISCONNECTING) {
//            destroy();
//        }
    }

    @Override
    public void onMeetingLeaveComplete(long ret) {
        try {
            if (MeetingEndReason.KICK_BY_HOST == ret) {
                Toast.makeText(windowView.getContext(), R.string.mensagem_usuario_removido_pelo_anfitriao, Toast.LENGTH_LONG).show();
            } else if (MeetingEndReason.END_BY_HOST == ret) {
                Toast.makeText(windowView.getContext(), R.string.videoconferencia_enserrada_pelo_anfitriao, Toast.LENGTH_LONG).show();
            }
        } catch (Exception ignore) {
        }
        destroy();
    }

    private class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent event) {

            if (null != refContext && null != refContext.get()) {
                hiddenMeetingWindow(false);
                Context context = refContext.get();
                Intent intent = new Intent(context, listenerActivity);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                context.startActivity(intent);
            }
            return true;

        }
    }


    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            if (gestureDetector.onTouchEvent(event)) {
                return true;
            }
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    lastX = (int) event.getRawX();
                    lastY = (int) event.getRawY();
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    break;
                case MotionEvent.ACTION_MOVE:
                    WindowManager.LayoutParams params = (WindowManager.LayoutParams) view.getLayoutParams();

                    int dx = (int) event.getRawX() - lastX;
                    int dy = (int) event.getRawY() - lastY;
                    int left = params.x + dx;
                    int top = params.y + dy;

                    params.x = left;
                    params.y = top;
                    mWindowManager.updateViewLayout(windowView, params);
                    lastX = (int) event.getRawX();
                    lastY = (int) event.getRawY();
                    break;
            }
            return true;
        }
    };

    private WindowManager.LayoutParams getLayoutParams(Context context) {
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && Settings.canDrawOverlays(context)) {
            lp.type = ZmCommonUtils.getSystemAlertWindowType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        } else {
            lp.type = ZmCommonUtils.getSystemAlertWindowType(WindowManager.LayoutParams.TYPE_TOAST);
        }
        lp.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        int width = mWindowManager.getDefaultDisplay().getWidth();
        int height = mWindowManager.getDefaultDisplay().getHeight();
        lp.format = PixelFormat.RGBA_8888;
        windowView.measure(-1, -1);
        lp.x = width - windowView.getMeasuredWidth() - 40;
        lp.y = 80;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.TOP | Gravity.LEFT;
        return lp;
    }


    public void hiddenMeetingWindow(boolean destroy) {
        if (null == windowView || null == mWindowManager || null == mobileRTCVideoView) {
            return;
        }
        mobileRTCVideoView.getVideoViewManager().removeAllVideoUnits();
        boolean isSharingOut = false;
        InMeetingService inMeetingService = ZoomSDK.getInstance().getInMeetingService();
        if (inMeetingService != null && inMeetingService.getInMeetingShareController() != null) {
            isSharingOut = inMeetingService.getInMeetingShareController().isSharingOut();
        }
        if (!destroy || isSharingOut) {
            windowView.setVisibility(View.GONE);
        } else {
            try {
                mWindowManager.removeView(windowView);
            } catch (Exception ignored) {
            }
            mbAddedView = false;
            windowView = null;
            mobileRTCVideoView = null;
        }
    }

    @Override
    public void onShareActiveUser(long userId) {
        if (mbAddedView && null != mobileRTCVideoView) {
            if (userId < 0) {
                mobileRTCVideoView.getVideoViewManager().removeAllVideoUnits();
                mobileRTCVideoView.getVideoViewManager().addActiveVideoUnit(renderInfo);
            } else {
                mobileRTCVideoView.getVideoViewManager().removeAllVideoUnits();
                MobileRTCVideoUnitRenderInfo renderInfo = new MobileRTCVideoUnitRenderInfo(0, 0, 100, 100);
                mobileRTCVideoView.getVideoViewManager().addShareVideoUnit(userId, renderInfo);
            }
        }
    }

    public interface OnNewMessageListener {
        void onNewMessageReceived(InMeetingChatMessage inMeetingChatMessage);
    }

    @Override
    public void onShareUserReceivingStatus(long userId) {
        Log.d("MeetingWindowHelper", "userId:" + userId);
    }

    @Override
    public void onMeetingNeedPasswordOrDisplayName(boolean b, boolean b1, InMeetingEventHandler inMeetingEventHandler) {

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
        long userID = ZoomSDK.getInstance().getInMeetingService().getMyUserID();
        if (inMeetingChatMessage.isChatToAll() || inMeetingChatMessage.getReceiverUserId() == userID) {
            setMessageCount(inMeetingChatMessage);
        }
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

}
