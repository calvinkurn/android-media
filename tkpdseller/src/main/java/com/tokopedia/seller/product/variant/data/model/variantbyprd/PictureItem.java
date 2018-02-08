package com.tokopedia.seller.product.variant.data.model.variantbyprd;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hendry on 9/5/2017.
 */

public class PictureItem implements Parcelable {
    @SerializedName("v_pic_id")
    @Expose
    private int vPicId;
    @SerializedName("filepath")
    @Expose
    private String filepath;
    @SerializedName("filename")
    @Expose
    private String filename;
    @SerializedName("picture_url")
    @Expose
    private String pictureUrl;
    @SerializedName("picture_url_100")
    @Expose
    private String pictureUrl100;
    @SerializedName("picture_url_300")
    @Expose
    private String pictureUrl300;
    @SerializedName("picture_url_700")
    @Expose
    private String pictureUrl700;
    @SerializedName("pic_status")
    @Expose
    private int picStatus;
    @SerializedName("width")
    @Expose
    private int width;
    @SerializedName("height")
    @Expose
    private int height;

    public int getvPicId() {
        return vPicId;
    }

    public String getFilepath() {
        return filepath;
    }

    public String getFilename() {
        return filename;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public String getPictureUrl100() {
        return pictureUrl100;
    }

    public String getPictureUrl300() {
        return pictureUrl300;
    }

    public String getPictureUrl700() {
        return pictureUrl700;
    }

    public int getPicStatus() {
        return picStatus;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.vPicId);
        dest.writeString(this.filepath);
        dest.writeString(this.filename);
        dest.writeString(this.pictureUrl);
        dest.writeString(this.pictureUrl100);
        dest.writeString(this.pictureUrl300);
        dest.writeString(this.pictureUrl700);
        dest.writeInt(this.picStatus);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
    }

    public PictureItem() {
    }

    protected PictureItem(Parcel in) {
        this.vPicId = in.readInt();
        this.filepath = in.readString();
        this.filename = in.readString();
        this.pictureUrl = in.readString();
        this.pictureUrl100 = in.readString();
        this.pictureUrl300 = in.readString();
        this.pictureUrl700 = in.readString();
        this.picStatus = in.readInt();
        this.width = in.readInt();
        this.height = in.readInt();
    }

    public static final Parcelable.Creator<PictureItem> CREATOR = new Parcelable.Creator<PictureItem>() {
        @Override
        public PictureItem createFromParcel(Parcel source) {
            return new PictureItem(source);
        }

        @Override
        public PictureItem[] newArray(int size) {
            return new PictureItem[size];
        }
    };
}
