package us.zoom.app.initsdk;

import android.content.Context;

import us.zoom.app.models.MeetingConfig;
import us.zoom.sdk.InMeetingServiceListener;
import us.zoom.sdk.MeetingServiceListener;
import us.zoom.sdk.ZoomSDKInitializeListener;

public interface InitAuthSDKCallback extends ZoomSDKInitializeListener, MeetingServiceListener, InMeetingServiceListener {

    MeetingConfig getMeetingConfig();

    Context getContext();
}
