
package com.tokopedia.seller.product.edit.view.model.edit;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VariantPictureViewModel extends BasePictureViewModel implements Parcelable {

    @SerializedName(value = "v_pic_id", alternate = {"id"})
    @Expose
    private long id;

    public long getId() {
        if (id <= 0) {
            return 0;
        } else {
            return id;
        }
    }

    public void setId(long id) {
        this.id = id;
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
        dest.writeLong(this.x);
        dest.writeLong(this.y);
        dest.writeInt(this.fromIg);
    }

    public VariantPictureViewModel() {
    }

    protected VariantPictureViewModel(Parcel in) {
        this.id = in.readLong();
        this.status = in.readLong();
        this.fileName = in.readString();
        this.filePath = in.readString();
        this.urlOriginal = in.readString();
        this.urlThumbnail = in.readString();
        this.x = in.readLong();
        this.y = in.readLong();
        this.fromIg = in.readInt();
    }

    public static final Parcelable.Creator<VariantPictureViewModel> CREATOR = new Parcelable.Creator<VariantPictureViewModel>() {
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
