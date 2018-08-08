package com.tokopedia.session.addchangeemail.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by yfsx on 09/03/18.
 */

public class VerificationModel implements Parcelable {

    private int iconResId;
    private String message;
    private String email;

    public VerificationModel(int iconResId, String message, String email) {
        this.iconResId = iconResId;
        this.message = message;
        this.email = email;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.iconResId);
        dest.writeString(this.message);
        dest.writeString(this.email);
    }

    protected VerificationModel(Parcel in) {
        this.iconResId = in.readInt();
        this.message = in.readString();
        this.email = in.readString();
    }

    public static final Parcelable.Creator<VerificationModel> CREATOR = new Parcelable.Creator<VerificationModel>() {
        @Override
        public VerificationModel createFromParcel(Parcel source) {
            return new VerificationModel(source);
        }

        @Override
        public VerificationModel[] newArray(int size) {
            return new VerificationModel[size];
        }
    };
}