package com.tokopedia.seller.product.variant.data.model.variantbyprdold;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hendry on 8/15/2017.
 * {"variant_data":[{"pvd_id":3016392,"status":1,"stock":0,"PvoListString":"{2184381,2184932}","v_code":"2184932:2184381"},{"pvd_id":3016391,"status":1,"stock":0,"PvoListString":"{2184382,2184932}","v_code":"2184932:2184382"}]}
 */
@Deprecated
public class VariantDatum implements Parcelable {

    @SerializedName("pvd_id")
    @Expose
    private int pvdId;

    // 0: not active, 1: active
    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("stock")
    @Expose
    private int stock;

    // combination of variant code
    @SerializedName("v_code")
    @Expose
    private String vCode;

    // combination of variant code in arrayList
    @SerializedName("option_ids")
    @Expose
    private List<Long> optionIdList;

    public int getPvdId() {
        return pvdId;
    }

    public int getStatus() {
        return status;
    }

    public int getStock() {
        return stock;
    }

    public String getvCode() {
        return vCode;
    }

    public List<Long> getOptionIdList() {
        return optionIdList;
    }

    public VariantDatum() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.pvdId);
        dest.writeInt(this.status);
        dest.writeInt(this.stock);
        dest.writeString(this.vCode);
        dest.writeList(this.optionIdList);
    }

    protected VariantDatum(Parcel in) {
        this.pvdId = in.readInt();
        this.status = in.readInt();
        this.stock = in.readInt();
        this.vCode = in.readString();
        this.optionIdList = new ArrayList<>();
        in.readList(this.optionIdList, Long.class.getClassLoader());
    }

    public static final Parcelable.Creator<VariantDatum> CREATOR = new Parcelable.Creator<VariantDatum>() {
        @Override
        public VariantDatum createFromParcel(Parcel source) {
            return new VariantDatum(source);
        }

        @Override
        public VariantDatum[] newArray(int size) {
            return new VariantDatum[size];
        }
    };
}
