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
    private String name;
    private String image;
    private String profile;
    private String title;
    private String subtitle;
    private int participant;

    @Override
    public int type(ChannelTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public ChannelViewModel() {
    }

    public ChannelViewModel(String id, String name, String image, String profile, String title, String subtitle, int participant) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.profile = profile;
        this.title = title;
        this.subtitle = subtitle;
        this.participant = participant;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public int getParticipant() {
        return participant;
    }

    public void setParticipant(int participant) {
        this.participant = participant;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.image);
        dest.writeString(this.profile);
        dest.writeString(this.title);
        dest.writeString(this.subtitle);
        dest.writeInt(this.participant);
    }

    protected ChannelViewModel(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.image = in.readString();
        this.profile = in.readString();
        this.title = in.readString();
        this.subtitle = in.readString();
        this.participant = in.readInt();
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

