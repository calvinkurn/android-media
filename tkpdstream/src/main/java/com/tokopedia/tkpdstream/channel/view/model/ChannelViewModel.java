package com.tokopedia.tkpdstream.channel.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.tkpdstream.channel.view.adapter.typefactory.ChannelTypeFactory;

/**
 * @author by StevenFredian on 13/02/18.
 **/

public class ChannelViewModel implements Parcelable, Visitable<ChannelTypeFactory> {

    private String channelUrl;
    private String id;
    private String adminName;
    private String image;
    private String adminPicture;
    private String title;
    private String description;
    private String participant;

    protected ChannelViewModel(Parcel in) {
        channelUrl = in.readString();
        id = in.readString();
        adminName = in.readString();
        image = in.readString();
        adminPicture = in.readString();
        title = in.readString();
        description = in.readString();
        participant = in.readString();
    }

    public static final Creator<ChannelViewModel> CREATOR = new Creator<ChannelViewModel>() {
        @Override
        public ChannelViewModel createFromParcel(Parcel in) {
            return new ChannelViewModel(in);
        }

        @Override
        public ChannelViewModel[] newArray(int size) {
            return new ChannelViewModel[size];
        }
    };

    @Override
    public int type(ChannelTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public ChannelViewModel() {
    }

    public ChannelViewModel(String id, String adminName, String image, String adminPicture,
                            String title, String description, String participant, String
                                    channelUrl) {
        this.id = id;
        this.adminName = adminName;
        this.image = image;
        this.adminPicture = adminPicture;
        this.title = title;
        this.description = description;
        this.participant = participant;
        this.channelUrl = channelUrl;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(channelUrl);
        dest.writeString(id);
        dest.writeString(adminName);
        dest.writeString(image);
        dest.writeString(adminPicture);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(participant);
    }
}

