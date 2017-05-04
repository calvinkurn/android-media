package com.tokopedia.inbox.rescenter.discussion.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nisie on 3/31/17.
 */

public class AttachmentViewModel implements Parcelable{

    String imgThumb;
    String imgLarge;
    String url;
    private String fileLoc;

    public AttachmentViewModel() {
    }

    protected AttachmentViewModel(Parcel in) {
        imgThumb = in.readString();
        url = in.readString();
        fileLoc = in.readString();
        imgLarge = in.readString();
    }

    public static final Creator<AttachmentViewModel> CREATOR = new Creator<AttachmentViewModel>() {
        @Override
        public AttachmentViewModel createFromParcel(Parcel in) {
            return new AttachmentViewModel(in);
        }

        @Override
        public AttachmentViewModel[] newArray(int size) {
            return new AttachmentViewModel[size];
        }
    };

    public String getImgThumb() {
        return imgThumb;
    }

    public void setImgThumb(String imgThumb) {
        this.imgThumb = imgThumb;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileLoc() {
        return fileLoc;
    }

    public void setFileLoc(String fileLoc) {
        this.fileLoc = fileLoc;
    }

    public String getImgLarge() {
        return imgLarge;
    }

    public void setImgLarge(String imgLarge) {
        this.imgLarge = imgLarge;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imgThumb);
        dest.writeString(url);
        dest.writeString(fileLoc);
        dest.writeString(imgLarge);

    }
}
