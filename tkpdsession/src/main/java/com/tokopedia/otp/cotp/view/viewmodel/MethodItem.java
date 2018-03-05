package com.tokopedia.otp.cotp.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.session.R;

/**
 * @author by nisie on 11/30/17.
 */

public class MethodItem implements Parcelable {

    private String modeName;
    private int iconResId;
    private String methodText;
    private String imageUrl;
    private String verificationText;

    public MethodItem(String mode, String imageUrl, String methodText, String verificationText) {
        this.modeName = mode;
        this.iconResId = 0;
        this.imageUrl = imageUrl;
        this.methodText = methodText;
        this.verificationText = verificationText;
    }

    protected MethodItem(Parcel in) {
        modeName = in.readString();
        iconResId = in.readInt();
        methodText = in.readString();
        imageUrl = in.readString();
        verificationText = in.readString();
    }

    public static final Creator<MethodItem> CREATOR = new Creator<MethodItem>() {
        @Override
        public MethodItem createFromParcel(Parcel in) {
            return new MethodItem(in);
        }

        @Override
        public MethodItem[] newArray(int size) {
            return new MethodItem[size];
        }
    };

    public String getModeName() {
        return modeName;
    }

    public String getMethodText() {
        return methodText;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(modeName);
        dest.writeInt(iconResId);
        dest.writeString(methodText);
        dest.writeString(imageUrl);
        dest.writeString(verificationText);
    }

    public static String getSmsMethodText(String phoneNumber) {
        return MainApplication.getAppContext().getString(R.string.verification_sms_to) + " " +
                phoneNumber;
    }

    public static String getCallMethodText(String phoneNumber) {
        return MainApplication.getAppContext().getString(R.string.verification_call_to) + " " +
                phoneNumber;
    }

    public static String getMaskedPhoneNumber(String phone) {
        phone = phone.substring(phone.length() - 4);
        return String.format(("**** - **** - %s"), phone);
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getVerificationText() {
        return verificationText;
    }
}
