package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yfsx on 28/12/17.
 */

public class KolFollowingViewModel implements Parcelable {
    private int id;
    private String avatarUrl;
    private String profileUrl;
    private String profileApplink;
    private boolean isInfluencer;
    private String name;
    private boolean isLoadingItem;

    public KolFollowingViewModel(int id, String avatarUrl, String profileApplink, String profileUrl, boolean isInfluencer, String name) {
        this.id = id;
        this.avatarUrl = avatarUrl;
        this.profileApplink = profileApplink;
        this.profileUrl = profileUrl;
        this.isInfluencer = isInfluencer;
        this.name = name;
        this.isLoadingItem = false;
    }

    public KolFollowingViewModel(boolean isLoadingItem) {
        this.isLoadingItem = isLoadingItem;
        this.id = 0;
        this.avatarUrl = "";
        this.profileApplink = "";
        this.isInfluencer = false;
        this.name = "";
    }

    public boolean isLoadingItem() {
        return isLoadingItem;
    }

    public void setLoadingItem(boolean loadingItem) {
        isLoadingItem = loadingItem;
    }

    public String getProfileApplink() {
        return profileApplink;
    }

    public void setProfileApplink(String profileApplink) {
        this.profileApplink = profileApplink;
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

    public boolean isInfluencer() {
        return isInfluencer;
    }

    public void setInfluencer(boolean influencer) {
        isInfluencer = influencer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.avatarUrl);
        dest.writeString(this.profileUrl);
        dest.writeString(this.profileApplink);
        dest.writeByte(this.isInfluencer ? (byte) 1 : (byte) 0);
        dest.writeString(this.name);
        dest.writeByte(this.isLoadingItem ? (byte) 1 : (byte) 0);
    }

    protected KolFollowingViewModel(Parcel in) {
        this.id = in.readInt();
        this.avatarUrl = in.readString();
        this.profileUrl = in.readString();
        this.profileApplink = in.readString();
        this.isInfluencer = in.readByte() != 0;
        this.name = in.readString();
        this.isLoadingItem = in.readByte() != 0;
    }

    public static final Creator<KolFollowingViewModel> CREATOR = new Creator<KolFollowingViewModel>() {
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
