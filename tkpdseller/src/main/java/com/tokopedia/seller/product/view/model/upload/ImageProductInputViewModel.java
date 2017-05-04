package com.tokopedia.seller.product.view.model.upload;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author sebastianuskh on 4/13/17.
 */

public class ImageProductInputViewModel implements Parcelable {
    private String imagePath;
    private String imageDescription;
    private String picId;
    private String url;

    private int imageResolution;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setImageDescription(String imageDescription) {
        this.imageDescription = imageDescription;
    }

    public String getImageDescription() {
        return imageDescription;
    }

    public void setPicId(String picId) {
        this.picId = picId;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPicId() {
        return picId;
    }

    public String getUrl() {
        return url;
    }

    public void setImageResolution(int imageResolution) {
        this.imageResolution = imageResolution;
    }

    public int getImageResolution() {
        return imageResolution;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imagePath);
        dest.writeString(this.imageDescription);
        dest.writeString(this.picId);
        dest.writeString(this.url);
        dest.writeInt(this.imageResolution);
    }

    public ImageProductInputViewModel() {
    }

    protected ImageProductInputViewModel(Parcel in) {
        this.imagePath = in.readString();
        this.imageDescription = in.readString();
        this.picId = in.readString();
        this.url = in.readString();
        this.imageResolution = in.readInt();
    }

    public static final Parcelable.Creator<ImageProductInputViewModel> CREATOR = new Parcelable.Creator<ImageProductInputViewModel>() {
        @Override
        public ImageProductInputViewModel createFromParcel(Parcel source) {
            return new ImageProductInputViewModel(source);
        }

        @Override
        public ImageProductInputViewModel[] newArray(int size) {
            return new ImageProductInputViewModel[size];
        }
    };
}
