package com.tokopedia.tkpdstream.vote.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.tkpdstream.vote.view.adapter.typefactory.VoteTypeFactory;

/**
 * @author by StevenFredian on 21/02/18.
 */

public class VoteViewModel implements Parcelable, Visitable<VoteTypeFactory> {

    public static final int DEFAULT = 0;
    public static final int UNSELECTED = 1;
    public static final int SELECTED = 2;

    public static final String BAR_TYPE = "Bar";
    public static final String IMAGE_TYPE = "Image";

    String option, url, type;
    int percentage, selected;

    public VoteViewModel(String option, int percentage, int selected) {
        this.option = option;
        this.percentage = percentage;
        this.selected = selected;
        type = BAR_TYPE;
    }

    public VoteViewModel(String option, int percentage, int selected, String type) {
        this.option = option;
        this.percentage = percentage;
        this.selected = selected;
        this.type = type;
    }

    public VoteViewModel(String option, String url, int percentage, int selected, String type) {
        this.option = option;
        this.url = url;
        this.percentage = percentage;
        this.selected = selected;
        this.type = type;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int type(VoteTypeFactory typeFactory) {
        return typeFactory.type(this);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.option);
        dest.writeInt(this.percentage);
        dest.writeInt(this.selected);
        dest.writeString(this.type);
        dest.writeString(this.url);
    }

    protected VoteViewModel(Parcel in) {
        this.option = in.readString();
        this.percentage = in.readInt();
        this.selected = in.readInt();
        this.type = in.readString();
        this.url = in.readString();
    }

    public static final Creator<VoteViewModel> CREATOR = new Creator<VoteViewModel>() {
        @Override
        public VoteViewModel createFromParcel(Parcel source) {
            return new VoteViewModel(source);
        }

        @Override
        public VoteViewModel[] newArray(int size) {
            return new VoteViewModel[size];
        }
    };
}
