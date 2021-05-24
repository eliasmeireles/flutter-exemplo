package com.example.flutter.poc_zoom;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;
import us.zoom.app.models.MeetingConfig;

public class MainActivity extends FlutterActivity {


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);
        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), "ZOOM_CHANNEL")
                .setMethodCallHandler(
                        (call, result) -> {
                            ObjectMapper objectMapper = new ObjectMapper();
                            try {
                                MeetingConfig meetingConfig = objectMapper.readValue(call.arguments.toString(), MeetingConfig.class);
                                Intent intent = new Intent(MainActivity.this, ZoomStart.class);
                                Bundle bundle = new Bundle();
                                bundle.putParcelable(ZoomStart.MEETING_KEY, meetingConfig);
                                intent.putExtras(bundle);
                                startActivity(intent);

                            } catch (JsonProcessingException e) {
                                e.printStackTrace();
                                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                            if (call.method.equals("zoomJoinMeeting")) {
                                result.success("Android is update");
                            } else {
                                result.notImplemented();
                            }
                        }
                );
    }
}