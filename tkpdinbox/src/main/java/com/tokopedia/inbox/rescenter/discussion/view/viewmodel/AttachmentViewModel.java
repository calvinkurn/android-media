package com.tokopedia.inbox.rescenter.discussion.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nisie on 3/31/17.
 */

public class AttachmentViewModel implements Parcelable{

    public static final int FILE_VIDEO = 2;
    public static final int FILE_IMAGE = 1;
    public static final int UNKNOWN = 0;
    String imgThumb;
    String imgLarge;
    String url;
    private String fileLoc;
    private int fileType;

    public AttachmentViewModel() {
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    protected AttachmentViewModel(Parcel in) {
        this.imgThumb = in.readString();
        this.imgLarge = in.readString();
        this.url = in.readString();
        this.fileLoc = in.readString();
        this.fileType = in.readInt();
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
        dest.writeString(this.imgThumb);
        dest.writeString(this.imgLarge);
        dest.writeString(this.url);
        dest.writeString(this.fileLoc);
        dest.writeInt(this.fileType);
    }
}
