package com.tokopedia.seller.topads.view.models;

import android.os.Parcel;

/**
 * @author normansyahputa on 2/20/17.
 */

public class TopAdsProductViewModel extends GenericClass implements Comparable<TopAdsProductViewModel> {
    public static final Creator<TopAdsProductViewModel> CREATOR = new Creator<TopAdsProductViewModel>() {
        @Override
        public TopAdsProductViewModel createFromParcel(Parcel source) {
            return new TopAdsProductViewModel(source);
        }

        @Override
        public TopAdsProductViewModel[] newArray(int size) {
            return new TopAdsProductViewModel[size];
        }
    };
    private int id;
    private String name;
    private String imageUrl;
    private boolean isPromoted;
    private int adId;
    private String groupName;

    public TopAdsProductViewModel() {
        super(TopAdsProductViewModel.class.getName());
    }

    protected TopAdsProductViewModel(Parcel in) {
        super(in);
        this.id = in.readInt();
        this.name = in.readString();
        this.imageUrl = in.readString();
        this.isPromoted = in.readByte() != 0;
        this.adId = in.readInt();
        this.groupName = in.readString();
        setClassName(in.readString());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isPromoted() {
        return isPromoted;
    }

    public void setPromoted(boolean promoted) {
        isPromoted = promoted;
    }

    public int getAdId() {
        return adId;
    }

    public void setAdId(int adId) {
        this.adId = adId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.imageUrl);
        dest.writeByte(this.isPromoted ? (byte) 1 : (byte) 0);
        dest.writeInt(this.adId);
        dest.writeString(this.groupName);
        dest.writeString(super.getClassName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TopAdsProductViewModel that = (TopAdsProductViewModel) o;

        if (id != that.id) return false;
        return adId == that.adId;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + adId;
        return result;
    }

    @Override
    public String toString() {
        return "TopAdsProductViewModel{" +
                "adId=" + adId +
                ", id=" + id +
                '}';
    }

    @Override
    public int compareTo(TopAdsProductViewModel o) {
        return getId() - o.getId();
    }
}
