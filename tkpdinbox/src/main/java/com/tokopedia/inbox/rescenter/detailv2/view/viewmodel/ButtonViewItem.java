package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yfsx on 27/11/17.
 */

public class ButtonViewItem implements Parcelable {
    public String label;
    public String type;
    public int order;

    public ButtonViewItem(String label, String type, int order) {
        this.label = label;
        this.type = type;
        this.order = order;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.label);
        dest.writeString(this.type);
        dest.writeInt(this.order);
    }

    protected ButtonViewItem(Parcel in) {
        this.label = in.readString();
        this.type = in.readString();
        this.order = in.readInt();
    }

    public static final Parcelable.Creator<ButtonViewItem> CREATOR = new Parcelable.Creator<ButtonViewItem>() {
        @Override
        public ButtonViewItem createFromParcel(Parcel source) {
            return new ButtonViewItem(source);
        }

        @Override
        public ButtonViewItem[] newArray(int size) {
            return new ButtonViewItem[size];
        }
    };
}
