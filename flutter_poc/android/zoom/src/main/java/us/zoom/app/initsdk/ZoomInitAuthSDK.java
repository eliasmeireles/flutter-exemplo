package us.zoom.app.initsdk;

import android.util.Log;

import us.zoom.sdk.ZoomSDK;
import us.zoom.sdk.ZoomSDKInitParams;
import us.zoom.sdk.ZoomSDKInitializeListener;

/**
 * Init and auth zoom sdk first before using SDK interfaces
 */
public class ZoomInitAuthSDK implements AuthConstants, ZoomSDKInitializeListener {

    private final static String TAG = "InitAuthSDKHelper";

    private static ZoomInitAuthSDK mZoomInitAuthSDK;

    private ZoomSDK mZoomSDK;

    private InitAuthSDKCallback mInitAuthSDKCallback;

    private ZoomInitAuthSDK() {
        mZoomSDK = ZoomSDK.getInstance();
    }

    public synchronized static ZoomInitAuthSDK getInstance() {
        mZoomInitAuthSDK = new ZoomInitAuthSDK();
        return mZoomInitAuthSDK;
    }

    public void initialize(InitAuthSDKCallback callback) {
        if (mZoomSDK.isInitialized()) {
            callback.onZoomSDKInitializeResult(0, 0);
            return;
        }

        mInitAuthSDKCallback = callback;

        ZoomSDKInitParams initParams = new ZoomSDKInitParams();

//        initParams.jwtToken = callback.getMeetingConfig().getJwtToken();
        initParams.appKey = "vPpsPHah5Sa4dI9976Okke2RZgx8BLKt8BA0";
        initParams.appSecret = "5K5e5taSXLZbWlaNtvCIxrCVk5T1TtoQdLvx";
        
//        initParams.enableLog = callback.getMeetingConfig().isEnableLog();
//        initParams.enableGenerateDump = callback.getMeetingConfig().isEnableGenerateDump();
//        initParams.logSize = callback.getMeetingConfig().getLogSize();
        initParams.domain = AuthConstants.WEB_DOMAIN;
//        initParams.videoRawDataMemoryMode = ZoomSDKRawDataMemoryMode.ZoomSDKRawDataMemoryModeStack;
        mZoomSDK.initialize(callback.getContext(), this, initParams);
    }

    /**
     * init sdk callback
     *
     * @param errorCode         defined in {@link us.zoom.sdk.ZoomError}
     * @param internalErrorCode Zoom internal error code
     */
    @Override
    public void onZoomSDKInitializeResult(int errorCode, int internalErrorCode) {
        Log.i(TAG, "onZoomSDKInitializeResult, errorCode=" + errorCode + ", internalErrorCode=" + internalErrorCode);

        if (mInitAuthSDKCallback != null) {
            mInitAuthSDKCallback.onZoomSDKInitializeResult(errorCode, internalErrorCode);
        }
    }

    @Override
    public void onZoomAuthIdentityExpired() {
        Log.e(TAG, "onZoomAuthIdentityExpired in init");
    }

    public void reset() {
        mInitAuthSDKCallback = null;
    }
}
