package com.tokopedia.discovery.newdiscovery.hotlist.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.discovery.newdiscovery.hotlist.view.adapter.factory.HotlistAdapterTypeFactory;

import java.util.List;

/**
 * Created by hangnadi on 10/8/17.
 */

public class HotlistHeaderViewModel implements Visitable<HotlistAdapterTypeFactory>,Parcelable {

    private String imageUrl;
    private String desc;
    private List<HotlistHashTagViewModel> hashTags;
    private HotlistPromo hotlistPromo;
    private String hotlistTitle;

    public HotlistHeaderViewModel() {
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setHashTags(List<HotlistHashTagViewModel> hashTags) {
        this.hashTags = hashTags;
    }

    public List<HotlistHashTagViewModel> getHashTags() {
        return hashTags;
    }

    @Override
    public int type(HotlistAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public void setHotlistPromo(HotlistPromo hotlistPromo) {
        this.hotlistPromo = hotlistPromo;
    }

    public HotlistPromo getHotlistPromo() {
        return hotlistPromo;
    }

    public void setHotlistTitle(String hotlistTitle) {
        this.hotlistTitle = hotlistTitle;
    }

    public String getHotlistTitle() {
        return hotlistTitle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imageUrl);
        dest.writeString(this.desc);
        dest.writeTypedList(this.hashTags);
        dest.writeParcelable(this.hotlistPromo, flags);
        dest.writeString(this.hotlistTitle);
    }

    protected HotlistHeaderViewModel(Parcel in) {
        this.imageUrl = in.readString();
        this.desc = in.readString();
        this.hashTags = in.createTypedArrayList(HotlistHashTagViewModel.CREATOR);
        this.hotlistPromo = in.readParcelable(HotlistPromo.class.getClassLoader());
        this.hotlistTitle = in.readString();
    }

    public static final Creator<HotlistHeaderViewModel> CREATOR = new Creator<HotlistHeaderViewModel>() {
        @Override
        public HotlistHeaderViewModel createFromParcel(Parcel source) {
            return new HotlistHeaderViewModel(source);
        }

        @Override
        public HotlistHeaderViewModel[] newArray(int size) {
            return new HotlistHeaderViewModel[size];
        }
    };
}
