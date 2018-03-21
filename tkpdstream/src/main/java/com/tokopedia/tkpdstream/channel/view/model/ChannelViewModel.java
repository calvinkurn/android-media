package com.tokopedia.tkpdstream.channel.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.tkpdstream.channel.view.adapter.typefactory.ChannelTypeFactory;

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
    private String participant;
    private String channelUrl;
    private String partnerName;
    private String partnerImage;

    @Override
    public int type(ChannelTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public ChannelViewModel(String id, String adminName, String image, String adminPicture,
                            String title, String description, String participant, String channelUrl,
                            String partnerName, String partnerImage) {
        this.id = id;
        this.adminName = adminName;
        this.image = image;
        this.adminPicture = adminPicture;
        this.title = title;
        this.description = description;
        this.participant = participant;
        this.channelUrl = channelUrl;
        this.partnerName = partnerName;
        this.partnerImage = partnerImage;
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

    public String getParticipant() {
        return participant;
    }

    public String getAdminPicture() {
        return adminPicture;
    }

    public void setParticipant(String participant) {
        this.participant = participant;
    }

    public String getChannelUrl() {
        return channelUrl;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public String getPartnerImage() {
        return partnerImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.channelUrl);
        dest.writeString(this.id);
        dest.writeString(this.adminName);
        dest.writeString(this.image);
        dest.writeString(this.adminPicture);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.participant);
        dest.writeString(this.partnerName);
        dest.writeString(this.partnerImage);
    }

    protected ChannelViewModel(Parcel in) {
        this.channelUrl = in.readString();
        this.id = in.readString();
        this.adminName = in.readString();
        this.image = in.readString();
        this.adminPicture = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.participant = in.readString();
        this.partnerName = in.readString();
        this.partnerImage = in.readString();
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

