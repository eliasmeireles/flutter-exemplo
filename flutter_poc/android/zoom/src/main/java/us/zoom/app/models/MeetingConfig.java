package us.zoom.app.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MeetingConfig implements Parcelable {

    @JsonProperty("jwtToken")
    private String jwtToken;
    @JsonProperty("appKey")
    private String appKey;
    @JsonProperty("appSecret")
    private String appSecret;
    @JsonProperty("userName")
    private String userName;
    @JsonProperty("meetingNumber")
    private String meetingNumber;
    @JsonProperty("meetingPassword")
    private String meetingPassword;
    @JsonProperty("enableLog")
    private boolean enableLog;
    @JsonProperty("enableGenerateDump")
    private boolean enableGenerateDump;
    @JsonProperty("logSize")
    private int logSize;


    public MeetingConfig(String jwtToken, String appKey, String appSecret, String userName, String meetingNumber, String meetingPassword) {
        this.jwtToken = jwtToken;
        this.appKey = appKey;
        this.appSecret = appSecret;
        this.userName = userName;
        this.meetingNumber = meetingNumber;
        this.meetingPassword = meetingPassword;
    }

    public MeetingConfig(String jwtToken, String appKey, String appSecret, String userName, String meetingNumber, String meetingPassword, boolean enableLog, boolean enableGenerateDump, int logSize) {
        this.jwtToken = jwtToken;
        this.appKey = appKey;
        this.appSecret = appSecret;
        this.userName = userName;
        this.meetingNumber = meetingNumber;
        this.meetingPassword = meetingPassword;
        this.enableLog = enableLog;
        this.enableGenerateDump = enableGenerateDump;
        this.logSize = logSize;
    }

    public MeetingConfig() {
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMeetingNumber() {
        return meetingNumber;
    }

    public void setMeetingNumber(String meetingNumber) {
        this.meetingNumber = meetingNumber;
    }

    public String getMeetingPassword() {
        return meetingPassword;
    }

    public void setMeetingPassword(String meetingPassword) {
        this.meetingPassword = meetingPassword;
    }

    public boolean isEnableLog() {
        return enableLog;
    }

    public void setEnableLog(boolean enableLog) {
        this.enableLog = enableLog;
    }

    public boolean isEnableGenerateDump() {
        return enableGenerateDump;
    }

    public void setEnableGenerateDump(boolean enableGenerateDump) {
        this.enableGenerateDump = enableGenerateDump;
    }

    public int getLogSize() {
        return logSize;
    }

    public void setLogSize(int logSize) {
        this.logSize = logSize;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.jwtToken);
        dest.writeString(this.appKey);
        dest.writeString(this.appSecret);
        dest.writeString(this.userName);
        dest.writeString(this.meetingNumber);
        dest.writeString(this.meetingPassword);
        dest.writeByte(this.enableLog ? (byte) 1 : (byte) 0);
        dest.writeByte(this.enableGenerateDump ? (byte) 1 : (byte) 0);
        dest.writeInt(this.logSize);
    }

    protected MeetingConfig(Parcel in) {
        this.jwtToken = in.readString();
        this.appKey = in.readString();
        this.appSecret = in.readString();
        this.userName = in.readString();
        this.meetingNumber = in.readString();
        this.meetingPassword = in.readString();
        this.enableLog = in.readByte() != 0;
        this.enableGenerateDump = in.readByte() != 0;
        this.logSize = in.readInt();
    }

    public static final Creator<MeetingConfig> CREATOR = new Creator<MeetingConfig>() {
        @Override
        public MeetingConfig createFromParcel(Parcel source) {
            return new MeetingConfig(source);
        }

        @Override
        public MeetingConfig[] newArray(int size) {
            return new MeetingConfig[size];
        }
    };

    @Override
    public String toString() {
        return "MeetingConfig{" +
                "jwtToken='" + jwtToken + '\'' +
                ", appKey='" + appKey + '\'' +
                ", appSecret='" + appSecret + '\'' +
                ", userName='" + userName + '\'' +
                ", meetingNumber='" + meetingNumber + '\'' +
                ", meetingPassword='" + meetingPassword + '\'' +
                ", enableLog=" + enableLog +
                ", enableGenerateDump=" + enableGenerateDump +
                ", logSize=" + logSize +
                '}';
    }
}
