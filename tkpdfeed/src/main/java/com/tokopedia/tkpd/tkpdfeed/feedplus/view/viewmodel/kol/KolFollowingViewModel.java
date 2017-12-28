package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yfsx on 28/12/17.
 */

public class KolFollowingViewModel implements Parcelable {
    private int id;
    private String avatarUrl;
    private boolean isVerified;
    private String name;

    public KolFollowingViewModel(int id, String avatarUrl, boolean isVerified, String name) {
        this.id = id;
        this.avatarUrl = avatarUrl;
        this.isVerified = isVerified;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.avatarUrl);
        dest.writeByte(this.isVerified ? (byte) 1 : (byte) 0);
        dest.writeString(this.name);
    }

    protected KolFollowingViewModel(Parcel in) {
        this.id = in.readInt();
        this.avatarUrl = in.readString();
        this.isVerified = in.readByte() != 0;
        this.name = in.readString();
    }

    public static final Parcelable.Creator<KolFollowingViewModel> CREATOR = new Parcelable.Creator<KolFollowingViewModel>() {
        @Override
        public KolFollowingViewModel createFromParcel(Parcel source) {
            return new KolFollowingViewModel(source);
        }

        @Override
        public KolFollowingViewModel[] newArray(int size) {
            return new KolFollowingViewModel[size];
        }
    };
}
