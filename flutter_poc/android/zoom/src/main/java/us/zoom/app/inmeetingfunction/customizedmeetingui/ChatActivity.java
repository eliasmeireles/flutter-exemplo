package us.zoom.app.inmeetingfunction.customizedmeetingui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.zipow.videobox.fragment.ConfChatFragment;

import us.zoom.androidlib.app.ZMActivity;
import us.zoom.app.R;
import us.zoom.app.inmeetingfunction.customizedmeetingui.view.MeetingWindowHelper;
import us.zoom.sdk.ZoomSDK;

public class ChatActivity extends ZMActivity {

    private static final int CHAT_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ConfChatFragment.showAsActivity(this, CHAT_REQUEST_CODE, ZoomSDK.getInstance().getInMeetingService().getMyUserID());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHAT_REQUEST_CODE) {
            onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        MeetingWindowHelper.getInstance().messageCount = 0;
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }
}