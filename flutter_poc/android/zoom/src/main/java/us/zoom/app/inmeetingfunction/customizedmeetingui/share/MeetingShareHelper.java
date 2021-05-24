package us.zoom.app.inmeetingfunction.customizedmeetingui.share;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import us.zoom.app.inmeetingfunction.customizedmeetingui.view.ZoomCustomUIMeetingActivity;
import us.zoom.sdk.InMeetingService;
import us.zoom.sdk.InMeetingShareController;
import us.zoom.sdk.MobileRTCSDKError;
import us.zoom.sdk.MobileRTCShareView;
import us.zoom.sdk.ZoomSDK;
import us.zoom.app.R;
import us.zoom.app.inmeetingfunction.customizedmeetingui.AndroidAppUtil;

import us.zoom.app.inmeetingfunction.customizedmeetingui.view.adapter.SimpleMenuAdapter;
import us.zoom.app.inmeetingfunction.customizedmeetingui.view.adapter.SimpleMenuItem;

public class MeetingShareHelper {

    private final static String TAG = MeetingShareHelper.class.getSimpleName();

    private final int MENU_SHARE_SCREEN = 0;
    private final int MENU_SHARE_IMAGE = 1;
    private final int MENU_SHARE_WEBVIEW = 2;
    private final int MENU_WHITE_BOARD = 3;
    public boolean shareWhiteBord = false;
    public interface MeetingShareUICallBack {
        void showShareMenu(PopupWindow popupWindow);

        MobileRTCShareView getShareView();

        void cantShare();

        void showOtherSharingTip();
    }

    private InMeetingShareController mInMeetingShareController;

    private InMeetingService mInMeetingService;

    private MeetingShareUICallBack callBack;

    private Activity activity;

    public MeetingShareHelper(Activity activity, MeetingShareUICallBack callBack) {
        mInMeetingShareController = ZoomSDK.getInstance().getInMeetingService().getInMeetingShareController();
        mInMeetingService = ZoomSDK.getInstance().getInMeetingService();
        this.activity = activity;
        this.callBack = callBack;
    }

    public void onClickShare() {
        if (mInMeetingShareController.isOtherSharing()) {
            callBack.showOtherSharingTip();
            return;
        }
        if (!mInMeetingShareController.isSharingOut()) {
            if (mInMeetingShareController.isShareLocked()) {
                callBack.cantShare();
            } else {
                showShareActionPopupWindow();
            }
        } else {
            stopShare();
        }
    }

    public boolean isSenderSupportAnnotation(long userId) {
        return mInMeetingShareController.isSenderSupportAnnotation(userId);
    }

    public boolean isSharingScreen() {
        return mInMeetingShareController.isSharingScreen();
    }

    public boolean isOtherSharing() {
        return mInMeetingShareController.isOtherSharing();
    }

    public boolean isSharingOut() {

        return mInMeetingShareController.isSharingOut();
    }

    public MobileRTCSDKError startShareScreenSession(Intent intent) {
        return mInMeetingShareController.startShareScreenSession(intent);
    }


    public void stopShare() {
        mInMeetingShareController.stopShareScreen();
        if (null != callBack) {
            MobileRTCShareView shareView = callBack.getShareView();
            if (shareView != null) {
                mInMeetingShareController.stopShareView();
                shareView.setVisibility(View.GONE);
            }
        }
    }


    public void onShareActiveUser(long currentShareUserId, long userId) {
        if (currentShareUserId > 0 && mInMeetingService.isMyself(currentShareUserId)) {
            if (userId < 0 || !mInMeetingService.isMyself(userId)) { //My share stopped or other start share and stop my share
                mInMeetingShareController.stopShareView();
                mInMeetingShareController.stopShareScreen();
                return;
            }
        }
        if (mInMeetingService.isMyself(userId)) {
            if (mInMeetingShareController.isSharingOut()) {
                if (mInMeetingShareController.isSharingScreen()) {
                    mInMeetingShareController.startShareScreenContent();
                } else {
                    if (null != callBack) {
                        mInMeetingShareController.startShareViewContent(callBack.getShareView());
                    }
                }
            }
        }
    }


    public void showShareActionPopupWindow() {

        final SimpleMenuAdapter menuAdapter = new SimpleMenuAdapter(activity);

        if (Build.VERSION.SDK_INT >= 21) {
            menuAdapter.addItem(new SimpleMenuItem(MENU_SHARE_SCREEN, activity.getString(R.string.zm_btn_share_screen)));
        }
        menuAdapter.addItem(new SimpleMenuItem(MENU_SHARE_IMAGE, activity.getString(R.string.zm_btn_share_image)));
        menuAdapter.addItem(new SimpleMenuItem(MENU_SHARE_WEBVIEW, activity.getString(R.string.zm_btn_share_url)));
        menuAdapter.addItem(new SimpleMenuItem(MENU_WHITE_BOARD, activity.getString(R.string.zm_btn_share_whiteboard)));

        View popupWindowLayout = LayoutInflater.from(activity).inflate(R.layout.popupwindow, null);

        ListView shareActions = (ListView) popupWindowLayout.findViewById(R.id.actionListView);
        final PopupWindow window = new PopupWindow(popupWindowLayout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.bg_transparent));
        shareActions.setAdapter(menuAdapter);

        shareActions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mInMeetingShareController.isOtherSharing()) {
                    callBack.showOtherSharingTip();
                    window.dismiss();
                    return;
                }

                SimpleMenuItem item = (SimpleMenuItem) menuAdapter.getItem(position);
                if (item.getAction() == MENU_SHARE_WEBVIEW) {
                    startShareWebUrl();
                } else if (item.getAction() == MENU_SHARE_IMAGE) {
                    startShareImage();
                } else if (item.getAction() == MENU_SHARE_SCREEN) {
                    askScreenSharePermission();
                } else if (item.getAction() == MENU_WHITE_BOARD) {
                    startShareWhiteBoard();
                }
                window.dismiss();
            }
        });

        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.update();
        if (null != callBack) {
            callBack.showShareMenu(window);
        }
    }

    @SuppressLint("NewApi")
    protected void askScreenSharePermission() {
        if (Build.VERSION.SDK_INT < 21) {
            return;
        }
        if (mInMeetingShareController.isOtherSharing()) {
            callBack.showOtherSharingTip();
            return;
        }
        MediaProjectionManager mgr = (MediaProjectionManager) activity.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        if (mgr != null) {
            Intent intent = mgr.createScreenCaptureIntent();
            if (AndroidAppUtil.hasActivityForIntent(activity, intent)) {
                try {
                    activity.startActivityForResult(mgr.createScreenCaptureIntent(), ZoomCustomUIMeetingActivity.REQUEST_SHARE_SCREEN_PERMISSION);
                } catch (Exception e) {
                    Log.e(TAG, "askScreenSharePermission failed");
                }
            }
        }
    }

    private void startShareImage() {
        if (mInMeetingShareController.isOtherSharing()) {
            callBack.showOtherSharingTip();
            return;
        }
        boolean success = (mInMeetingShareController.startShareViewSession() == MobileRTCSDKError.SDKERR_SUCCESS);
        if (!success) {
            Log.i(TAG, "startShare is failed");
            return;
        }
        if (null == callBack) {
            return;
        }
        shareWhiteBord = false;
        MobileRTCShareView shareView = callBack.getShareView();
        shareView.setVisibility(View.VISIBLE);
        shareView.setShareImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.zoom_intro1_share));
    }

    private void startShareWebUrl() {
        if (mInMeetingShareController.isOtherSharing()) {
            callBack.showOtherSharingTip();
            return;
        }
        boolean success = (mInMeetingShareController.startShareViewSession() == MobileRTCSDKError.SDKERR_SUCCESS);
        if (!success) {
            Log.i(TAG, "startShare is failed");
            return;
        }
        if (null == callBack) {
            return;
        }
        shareWhiteBord = false;
        MobileRTCShareView shareView = callBack.getShareView();
        shareView.setVisibility(View.VISIBLE);
        shareView.setShareWebview("www.zoom.us");
    }

    private void startShareWhiteBoard() {

        if (mInMeetingShareController.isOtherSharing()) {
            callBack.showOtherSharingTip();
            return;
        }
        if (null == callBack) {
            return;
        }

        shareWhiteBord = true;
        MobileRTCShareView shareView = callBack.getShareView();

        boolean success = (mInMeetingShareController.startShareViewSession() == MobileRTCSDKError.SDKERR_SUCCESS);
        if (!success) {
            Log.i(TAG, "startShare is failed");
            return;
        }
        shareView.setVisibility(View.VISIBLE);
        shareView.setShareWhiteboard();
    }

}
