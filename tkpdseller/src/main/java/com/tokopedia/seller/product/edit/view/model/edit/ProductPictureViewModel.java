
package com.tokopedia.seller.product.edit.view.model.edit;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductPictureViewModel extends BasePictureViewModel implements Parcelable{

    @SerializedName(value="product_pic_id", alternate={"id"})
    @Expose
    private long id;

    @SerializedName("description")
    @Expose
    private String description;

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.description);
        dest.writeLong(this.status);
        dest.writeString(this.fileName);
        dest.writeString(this.filePath);
        dest.writeString(this.urlOriginal);
        dest.writeString(this.urlThumbnail);
        dest.writeLong(this.x);
        dest.writeLong(this.y);
        dest.writeInt(this.fromIg);
    }

    public ProductPictureViewModel() {
    }

    protected ProductPictureViewModel(Parcel in) {
        this.id = in.readLong();
        this.description = in.readString();
        this.status = in.readLong();
        this.fileName = in.readString();
        this.filePath = in.readString();
        this.urlOriginal = in.readString();
        this.urlThumbnail = in.readString();
        this.x = in.readLong();
        this.y = in.readLong();
        this.fromIg = in.readInt();
    }

    public static final Parcelable.Creator<ProductPictureViewModel> CREATOR = new Parcelable.Creator<ProductPictureViewModel>() {
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
