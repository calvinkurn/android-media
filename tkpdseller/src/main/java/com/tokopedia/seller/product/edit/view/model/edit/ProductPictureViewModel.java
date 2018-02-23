
package com.tokopedia.seller.product.edit.view.model.edit;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductPictureViewModel implements Parcelable{

    public static final int ACTIVE_STATUS = 1;

    @SerializedName(value="product_pic_id", alternate={"id"})
    @Expose
    private long id;

    // currently not used in UI, only for backend
    //0 -> deleted
    //1 -> active
    //2 -> primary
    //-3 -> sizechart
    @SerializedName("status")
    @Expose
    private long status = ACTIVE_STATUS;

    @SerializedName("file_name")
    @Expose
    private String fileName;
    @SerializedName("file_path")
    @Expose
    private String filePath;

    @SerializedName("url_original")
    @Expose
    private String urlOriginal;
    @SerializedName("url_thumbnail")
    @Expose
    private String urlThumbnail;

    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("x")
    @Expose
    private long x;
    @SerializedName("y")
    @Expose
    private long y;

    @SerializedName("from_ig")
    @Expose
    private int fromIg;

    public int getFromIg() {
        return fromIg;
    }

    public void setFromIg(int fromIg) {
        this.fromIg = fromIg;
    }

    public long getId() {
        return id;
    }

    public String getUrlThumbnail() {
        return urlThumbnail;
    }

    public String getUrlOriginal() {
        return urlOriginal;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getX() {
        return x;
    }

    public void setX(long x) {
        this.x = x;
    }

    public long getY() {
        return y;
    }

    public void setY(long y) {
        this.y = y;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeLong(this.status);
        dest.writeString(this.fileName);
        dest.writeString(this.filePath);
        dest.writeString(this.urlOriginal);
        dest.writeString(this.urlThumbnail);
        dest.writeString(this.description);
        dest.writeLong(this.x);
        dest.writeLong(this.y);
        dest.writeInt(this.fromIg);
    }

    public ProductPictureViewModel() {
    }

    protected ProductPictureViewModel(Parcel in) {
        this.id = in.readLong();
        this.status = in.readLong();
        this.fileName = in.readString();
        this.filePath = in.readString();
        this.urlOriginal = in.readString();
        this.urlThumbnail = in.readString();
        this.description = in.readString();
        this.x = in.readLong();
        this.y = in.readLong();
        this.fromIg = in.readInt();
    }

    public static final Creator<ProductPictureViewModel> CREATOR = new Creator<ProductPictureViewModel>() {
        @Override
        public ProductPictureViewModel createFromParcel(Parcel source) {
            return new ProductPictureViewModel(source);
        }

        @Override
        public ProductPictureViewModel[] newArray(int size) {
            return new ProductPictureViewModel[size];
        }
    };
}
