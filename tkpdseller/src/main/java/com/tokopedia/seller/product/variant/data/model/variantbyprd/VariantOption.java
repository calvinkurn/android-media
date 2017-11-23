package com.tokopedia.seller.product.variant.data.model.variantbyprd;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hendry on 8/15/2017.
 */

/**
 * {"variant_option":[{"pv_id":656228,"name":"Warna","identifier":"colour","unit_name":"","v_id":1,"vu_id":0,"status":2,"position":1,"optionList":[{"pvo_id":2184932,"v_id":1,"vu_id":0,"vuv_id":0,"value":"custom merah","status":1,"hex":"","picture":null},{"pvo_id":2184931,"v_id":1,"vu_id":0,"vuv_id":0,"value":"custom biru","status":1,"hex":"","picture":null}]},{"pv_id":656229,"name":"Ukuran Pakaian","identifier":"size","unit_name":"UK","v_id":6,"vu_id":9,"status":1,"position":2,"optionList":[{"pvo_id":2184933,"v_id":6,"vu_id":0,"vuv_id":0,"value":"custom 15","status":1,"hex":"","picture":null},{"pvo_id":2184383,"v_id":6,"vu_id":0,"vuv_id":37,"value":"6","status":1,"hex":"","picture":null}]}]}
 */
public class VariantOption implements Parcelable {

    @SerializedName("pv_id")
    @Expose
    private int pvId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("identifier")
    @Expose
    private String identifier;
    @SerializedName("unit_name")
    @Expose
    private String unitName;
    @SerializedName("v_id")
    @Expose
    private int vId;
    @SerializedName("vu_id")
    @Expose
    private int vuId;
    @SerializedName("position")
    @Expose
    private int position;
    @SerializedName("option")
    @Expose
    private List<Option> optionList = null;

    public Integer getPvId() {
        return pvId;
    }

    public String getName() {
        return name;
    }

    public String getUnitName() {
        return unitName;
    }

    public List<Option> getOptionList() {
        return optionList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getvId() {
        return vId;
    }

    public int getVuId() {
        return vuId;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.pvId);
        dest.writeString(this.name);
        dest.writeString(this.identifier);
        dest.writeString(this.unitName);
        dest.writeInt(this.vId);
        dest.writeInt(this.vuId);
        dest.writeInt(this.position);
        dest.writeTypedList(this.optionList);
    }

    public VariantOption() {
    }

    protected VariantOption(Parcel in) {
        this.pvId = in.readInt();
        this.name = in.readString();
        this.identifier = in.readString();
        this.unitName = in.readString();
        this.vId = in.readInt();
        this.vuId = in.readInt();
        this.position = in.readInt();
        this.optionList = in.createTypedArrayList(Option.CREATOR);
    }

    public static final Parcelable.Creator<VariantOption> CREATOR = new Parcelable.Creator<VariantOption>() {
        @Override
        public VariantOption createFromParcel(Parcel source) {
            return new VariantOption(source);
        }

        @Override
        public VariantOption[] newArray(int size) {
            return new VariantOption[size];
        }
    };
}
