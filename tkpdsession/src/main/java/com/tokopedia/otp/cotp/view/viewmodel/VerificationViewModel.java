package com.tokopedia.otp.cotp.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by nisie on 11/30/17.
 */

public class VerificationViewModel implements Parcelable {

    private String appScreen;
    private int type;
    private int iconResId;
    private String message;

    public VerificationViewModel(int type, int iconResId, String message,
                                 String appScreen) {
        this.type = type;
        this.iconResId = iconResId;
        this.message = message;
        this.appScreen = appScreen;
    }

    protected VerificationViewModel(Parcel in) {
        appScreen = in.readString();
        type = in.readInt();
        iconResId = in.readInt();
        message = in.readString();
    }

    public static final Creator<VerificationViewModel> CREATOR = new Creator<VerificationViewModel>() {
        @Override
        public VerificationViewModel createFromParcel(Parcel in) {
            return new VerificationViewModel(in);
        }

        @Override
        public VerificationViewModel[] newArray(int size) {
            return new VerificationViewModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(appScreen);
        dest.writeInt(type);
        dest.writeInt(iconResId);
        dest.writeString(message);
    }

    public String getAppScreen() {
        return appScreen;
    }

    public int getType() {
        return type;
    }

    public int getIconResId() {
        return iconResId;
    }

    public String getMessage() {
        return message;
    }

}
