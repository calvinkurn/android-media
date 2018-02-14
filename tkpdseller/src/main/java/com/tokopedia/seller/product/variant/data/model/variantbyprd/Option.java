package com.tokopedia.seller.product.variant.data.model.variantbyprd;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hendry on 8/15/2017.
 * "option":[{"pvo_id":2184932,"v_id":1,"vu_id":0,"vuv_id":0,"value":"custom merah","status":1,"hex":"","picture":null},{"pvo_id":2184931,"v_id":1,"vu_id":0,"vuv_id":0,"value":"custom biru","status":1,"hex":"","picture":null}]
 */

@Deprecated
public class Option implements Parcelable {

    @SerializedName("pvo_id")
    @Expose
    private int pvoId;
    @SerializedName("v_id")
    @Expose
    private int vId;
    @SerializedName("vu_id")
    @Expose
    private int vuId;
    @SerializedName("vuv_id")
    @Expose
    private int vuvId;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("hex")
    @Expose
    private String hex;
    @SerializedName("picture")
    @Expose
    private List<PictureItem> picture;

    public int getPvoId() {
        return pvoId;
    }

    public String getValue() {
        return value;
    }

    public String getHex() {
        return hex;
    }

    public int getVuvId() {
        return vuvId;
    }

    public List<PictureItem> getPicture() {
        return picture;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.pvoId);
        dest.writeInt(this.vId);
        dest.writeInt(this.vuId);
        dest.writeInt(this.vuvId);
        dest.writeString(this.value);
        dest.writeString(this.hex);
        dest.writeTypedList(this.picture);
    }

    public Option() {
    }

    protected Option(Parcel in) {
        this.pvoId = in.readInt();
        this.vId = in.readInt();
        this.vuId = in.readInt();
        this.vuvId = in.readInt();
        this.value = in.readString();
        this.hex = in.readString();
        this.picture = in.createTypedArrayList(PictureItem.CREATOR);
    }

    public static final Parcelable.Creator<Option> CREATOR = new Parcelable.Creator<Option>() {
        @Override
        public Option createFromParcel(Parcel source) {
            return new Option(source);
        }

        @Override
        public Option[] newArray(int size) {
            return new Option[size];
        }
    };
}