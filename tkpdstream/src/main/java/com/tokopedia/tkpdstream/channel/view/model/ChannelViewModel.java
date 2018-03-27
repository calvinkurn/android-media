package com.tokopedia.tkpdstream.channel.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.tkpdstream.channel.view.adapter.typefactory.ChannelTypeFactory;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.ChannelPartnerViewModel;

import java.util.List;

/**
 * @author by StevenFredian on 13/02/18.
 **/

public class ChannelViewModel implements Parcelable, Visitable<ChannelTypeFactory> {

    private String id;
    private String adminName;
    private String image;
    private String adminPicture;
    private String title;
    private String description;
    private String totalView;
    private String channelUrl;
    private List<ChannelPartnerViewModel> channelPartnerViewModels;

    @Override
    public int type(ChannelTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public ChannelViewModel(String id, String adminName, String image, String adminPicture,
                            String title, String description, String totalView, String channelUrl,
                            List<ChannelPartnerViewModel> channelPartnerViewModels) {
        this.id = id;
        this.adminName = adminName;
        this.image = image;
        this.adminPicture = adminPicture;
        this.title = title;
        this.description = description;
        this.totalView = totalView;
        this.channelUrl = channelUrl;
        this.channelPartnerViewModels = channelPartnerViewModels;
    }

    public String getId() {
        return id;
    }

    public String getAdminName() {
        return adminName;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getTotalView() {
        return totalView;
    }

    public String getAdminPicture() {
        return adminPicture;
    }

    public void setTotalView(String totalView) {
        this.totalView = totalView;
    }

    public String getChannelUrl() {
        return channelUrl;
    }

    public List<ChannelPartnerViewModel> getChannelPartnerViewModels() {
        return channelPartnerViewModels;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.adminName);
        dest.writeString(this.image);
        dest.writeString(this.adminPicture);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.totalView);
        dest.writeString(this.channelUrl);
        dest.writeTypedList(this.channelPartnerViewModels);
    }

    protected ChannelViewModel(Parcel in) {
        this.id = in.readString();
        this.adminName = in.readString();
        this.image = in.readString();
        this.adminPicture = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.totalView = in.readString();
        this.channelUrl = in.readString();
        this.channelPartnerViewModels = in.createTypedArrayList(ChannelPartnerViewModel.CREATOR);
    }

    public static final Creator<ChannelViewModel> CREATOR = new Creator<ChannelViewModel>() {
        @Override
        public ChannelViewModel createFromParcel(Parcel source) {
            return new ChannelViewModel(source);
        }

        @Override
        public ChannelViewModel[] newArray(int size) {
            return new ChannelViewModel[size];
        }
    };
}

