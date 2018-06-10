package com.tokopedia.loyalty.view.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Aghny A. Putra on 27/03/18
 */

public class ShareSocialViewModel implements Parcelable {

    private int resource;
    private String name;

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
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
        dest.writeInt(this.resource);
        dest.writeString(this.name);
    }

    public ShareSocialViewModel() {
    }

    protected ShareSocialViewModel(Parcel in) {
        this.resource = in.readInt();
        this.name = in.readString();
    }

    public static final Creator<ShareSocialViewModel> CREATOR = new Creator<ShareSocialViewModel>() {
        @Override
        public ShareSocialViewModel createFromParcel(Parcel source) {
            return new ShareSocialViewModel(source);
        }

        @Override
        public ShareSocialViewModel[] newArray(int size) {
            return new ShareSocialViewModel[size];
        }
    };
}
