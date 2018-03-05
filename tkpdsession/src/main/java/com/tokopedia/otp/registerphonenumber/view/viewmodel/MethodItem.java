package com.tokopedia.otp.registerphonenumber.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.session.R;

/**
 * @author by yfsx on 5/3/17.
 */

public class MethodItem implements Parcelable {

    private int type;
    private int iconResId;
    private String methodText;

    public MethodItem(int type, int iconResId, String methodText) {
        this.type = type;
        this.iconResId = iconResId;
        this.methodText = methodText;
    }

    protected MethodItem(Parcel in) {
        type = in.readInt();
        iconResId = in.readInt();
        methodText = in.readString();
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

    public int getType() {
        return type;
    }

    public int getIconResId() {
        return iconResId;
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
        dest.writeInt(type);
        dest.writeInt(iconResId);
        dest.writeString(methodText);
    }

    public static String getSmsMethodText(String phoneNumber) {
        return MainApplication.getAppContext().getString(R.string.verification_sms_to) + " " +
                phoneNumber;
    }

    public static String getCallMethodText(String phoneNumber) {
        return MainApplication.getAppContext().getString(R.string.verification_call_to) + " " +
                phoneNumber;
    }
}
