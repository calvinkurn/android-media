
package com.tokopedia.seller.product.edit.view.model.edit;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VariantPictureViewModel extends BasePictureViewModel implements Parcelable{

    @SerializedName(value="v_pic_id", alternate={"id"})
    @Expose
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeLong(this.status);
        dest.writeString(this.fileName);
        dest.writeString(this.filePath);
        dest.writeString(this.urlOriginal);
        dest.writeString(this.urlThumbnail);
        dest.writeLong(this.x);
        dest.writeLong(this.y);
        dest.writeInt(this.fromIg);
    }

    public VariantPictureViewModel() {
    }

    protected VariantPictureViewModel(Parcel in) {
        this.id = in.readString();
        this.status = in.readLong();
        this.fileName = in.readString();
        this.filePath = in.readString();
        this.urlOriginal = in.readString();
        this.urlThumbnail = in.readString();
        this.x = in.readLong();
        this.y = in.readLong();
        this.fromIg = in.readInt();
    }

    public static final Creator<VariantPictureViewModel> CREATOR = new Creator<VariantPictureViewModel>() {
        @Override
        public VariantPictureViewModel createFromParcel(Parcel source) {
            return new VariantPictureViewModel(source);
        }

        @Override
        public VariantPictureViewModel[] newArray(int size) {
            return new VariantPictureViewModel[size];
        }
    };
}
