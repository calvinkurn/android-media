package com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.tkpdstream.chatroom.view.adapter.chatroom.typefactory.GroupChatTypeFactory;

/**
 * @author by StevenFredian on 28/03/18.
 */

public class GroupChatPointsViewModel implements Parcelable,
        Visitable<GroupChatTypeFactory> {

    String image;
    String text;
    String span;
    String url;

    public GroupChatPointsViewModel(String text, String url) {
        this.text = text;
        this.url = url;
    }

    public GroupChatPointsViewModel(String text, String span, String url) {
        this.text = text;
        this.span = span;
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSpan() {
        return span;
    }

    public void setSpan(String span) {
        this.span = span;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.image);
        dest.writeString(this.text);
        dest.writeString(this.span);
        dest.writeString(this.url);
    }

    public GroupChatPointsViewModel() {
    }

    protected GroupChatPointsViewModel(Parcel in) {
        this.image = in.readString();
        this.text = in.readString();
        this.span = in.readString();
        this.url = in.readString();
    }

    public static final Creator<GroupChatPointsViewModel> CREATOR = new Creator<GroupChatPointsViewModel>() {
        @Override
        public GroupChatPointsViewModel createFromParcel(Parcel source) {
            return new GroupChatPointsViewModel(source);
        }

        @Override
        public GroupChatPointsViewModel[] newArray(int size) {
            return new GroupChatPointsViewModel[size];
        }
    };

    @Override
    public int type(GroupChatTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
