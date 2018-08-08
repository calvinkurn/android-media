package com.tokopedia.profilecompletion.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by nisie on 8/2/17.
 */

public class ProfileCompletionViewModel implements Parcelable{

    private int gender;
    private String phone;
    private String bday;
    private int completion;
    private boolean phoneVerified;

    public ProfileCompletionViewModel(int gender, String phone, String bday,
                                      int completion, boolean phoneVerified) {
        this.gender = gender;
        this.phone = phone;
        this.bday = bday;
        this.completion = completion;
        this.phoneVerified = phoneVerified;
    }

    protected ProfileCompletionViewModel(Parcel in) {
        gender = in.readInt();
        phone = in.readString();
        bday = in.readString();
        completion = in.readInt();
        phoneVerified = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(gender);
        dest.writeString(phone);
        dest.writeString(bday);
        dest.writeInt(completion);
        dest.writeByte((byte) (phoneVerified ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProfileCompletionViewModel> CREATOR = new Creator<ProfileCompletionViewModel>() {
        @Override
        public ProfileCompletionViewModel createFromParcel(Parcel in) {
            return new ProfileCompletionViewModel(in);
        }

        @Override
        public ProfileCompletionViewModel[] newArray(int size) {
            return new ProfileCompletionViewModel[size];
        }
    };

    public int getGender() {
        return gender;
    }

    public String getPhone() {
        return phone;
    }

    public String getBday() {
        return bday;
    }

    public int getCompletion() {
        return completion;
    }

    public boolean isPhoneVerified() {
        return phoneVerified;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setBday(String bday) {
        this.bday = bday;
    }

    public void setCompletion(int completion) {
        this.completion = completion;
    }

    public void setPhoneVerified(boolean phoneVerified) {
        this.phoneVerified = phoneVerified;
    }
}
