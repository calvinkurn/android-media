package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yfsx on 02/01/18.
 */

public class ContentProductViewModel implements Parcelable {
    private final String imageUrl;
    private final String applink;
    private final String buttonTitle;
    private final String textHeader;
    private final String textDescription;

    public ContentProductViewModel(String imageUrl, String applink, String buttonTitle, String textHeader, String textDescription) {
        this.imageUrl = imageUrl;
        this.applink = applink;
        this.buttonTitle = buttonTitle;
        this.textHeader = textHeader;
        this.textDescription = textDescription;
    }

    public String getButtonTitle() {
        return buttonTitle;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getApplink() {
        return applink;
    }

    public String getTextHeader() {
        return textHeader;
    }

    public String getTextDescription() {
        return textDescription;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imageUrl);
        dest.writeString(this.applink);
        dest.writeString(this.buttonTitle);
        dest.writeString(this.textHeader);
        dest.writeString(this.textDescription);
    }

    protected ContentProductViewModel(Parcel in) {
        this.imageUrl = in.readString();
        this.applink = in.readString();
        this.buttonTitle = in.readString();
        this.textHeader = in.readString();
        this.textDescription = in.readString();
    }

    public static final Creator<ContentProductViewModel> CREATOR = new Creator<ContentProductViewModel>() {
        @Override
        public ContentProductViewModel createFromParcel(Parcel source) {
            return new ContentProductViewModel(source);
        }

        @Override
        public ContentProductViewModel[] newArray(int size) {
            return new ContentProductViewModel[size];
        }
    };
}
