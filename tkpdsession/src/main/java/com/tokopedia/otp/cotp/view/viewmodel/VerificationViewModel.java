package com.tokopedia.otp.cotp.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by nisie on 11/30/17.
 */

public class VerificationViewModel implements Parcelable {

    private String imageUrl;
    private String appScreen;
    private String mode;
    private int iconResId;
    private String message;

    public VerificationViewModel(String mode, int iconResId, String imageUrl, String message,
                                 String appScreen) {
        this.mode = mode;
        this.iconResId = iconResId;
        this.message = message;
        this.appScreen = appScreen;
        this.imageUrl = imageUrl;
    }

    protected VerificationViewModel(Parcel in) {
        appScreen = in.readString();
        mode = in.readString();
        iconResId = in.readInt();
        message = in.readString();
        imageUrl = in.readString();
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
        dest.writeString(mode);
        dest.writeInt(iconResId);
        dest.writeString(message);
        dest.writeString(imageUrl);
    }

    public String getAppScreen() {
        return appScreen;
    }

    public String getType() {
        return mode;
    }

    public int getIconResId() {
        return iconResId;
    }

    public String getMessage() {
        return message;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
