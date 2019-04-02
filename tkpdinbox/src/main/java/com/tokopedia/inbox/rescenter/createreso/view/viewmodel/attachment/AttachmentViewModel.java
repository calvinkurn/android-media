package com.tokopedia.inbox.rescenter.createreso.view.viewmodel.attachment;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nisie on 3/31/17.
 */

public class AttachmentViewModel implements Parcelable {

    public static final int FILE_VIDEO = 2;
    public static final int FILE_IMAGE = 1;
    public static final int UNKNOWN = 0;
    String imgThumb;
    String imgLarge;
    String url;
    private String fileLoc;
    private int fileType;
    private UploadedFileViewModel uploadedFile;
    private int isVideo;
    // start --------------------> New Upload Attachment
    public String attachmentId;
    public String picSrc;
    public String picObj;
    // end   --------------------> New Upload Attachment

    public UploadedFileViewModel getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFileViewModel uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public AttachmentViewModel() {
    }

    public boolean isVideo() {
        return fileType == FILE_VIDEO;
    }

    public boolean isImage() {
        return fileType == FILE_IMAGE;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

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

    public String getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }

    public String getPicSrc() {
        return picSrc;
    }

    public void setPicSrc(String picSrc) {
        this.picSrc = picSrc;
    }

    public String getPicObj() {
        return picObj;
    }

    public void setPicObj(String picObj) {
        this.picObj = picObj;
    }

    public static class UploadedFileViewModel implements Parcelable {

        private String picSrc;
        private String picObj;
        private String imageUrl;

        public void setPicSrc(String picSrc) {
            this.picSrc = picSrc;
        }

        public String getPicSrc() {
            return picSrc;
        }

        public void setPicObj(String picObj) {
            this.picObj = picObj;
        }

        public String getPicObj() {
            return picObj;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.picSrc);
            dest.writeString(this.picObj);
            dest.writeString(this.imageUrl);
        }

        public UploadedFileViewModel() {
        }

        protected UploadedFileViewModel(Parcel in) {
            this.picSrc = in.readString();
            this.picObj = in.readString();
            this.imageUrl = in.readString();
        }

        public static final Creator<UploadedFileViewModel> CREATOR = new Creator<UploadedFileViewModel>() {
            @Override
            public UploadedFileViewModel createFromParcel(Parcel source) {
                return new UploadedFileViewModel(source);
            }

            @Override
            public UploadedFileViewModel[] newArray(int size) {
                return new UploadedFileViewModel[size];
            }
        };
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
        dest.writeParcelable(this.uploadedFile, flags);
        dest.writeInt(this.isVideo);
        dest.writeString(this.attachmentId);
        dest.writeString(this.picSrc);
        dest.writeString(this.picObj);
    }

    protected AttachmentViewModel(Parcel in) {
        this.imgThumb = in.readString();
        this.imgLarge = in.readString();
        this.url = in.readString();
        this.fileLoc = in.readString();
        this.fileType = in.readInt();
        this.uploadedFile = in.readParcelable(UploadedFileViewModel.class.getClassLoader());
        this.isVideo = in.readInt();
        this.attachmentId = in.readString();
        this.picSrc = in.readString();
        this.picObj = in.readString();
    }

    public static final Creator<AttachmentViewModel> CREATOR = new Creator<AttachmentViewModel>() {
        @Override
        public AttachmentViewModel createFromParcel(Parcel source) {
            return new AttachmentViewModel(source);
        }

        @Override
        public AttachmentViewModel[] newArray(int size) {
            return new AttachmentViewModel[size];
        }
    };
}
