package com.tokopedia.seller.product.variant.data.model.variantbyprd;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hendry on 8/15/2017.
 * {"status":"OK","data":{"product_id":201634008,"variant_option":[{"pv_id":659143,"name":"Warna","identifier":"colour","unit_name":"","v_id":1,"vu_id":0,"status":2,"position":1,"option":[{"pvo_id":2193651,"v_id":1,"vu_id":0,"vuv_id":0,"value":"custom merah","status":1,"hex":"","picture":null},{"pvo_id":2193650,"v_id":1,"vu_id":0,"vuv_id":8,"value":"Maroon","status":1,"hex":"#800000","picture":null},{"pvo_id":2193649,"v_id":1,"vu_id":0,"vuv_id":4,"value":"Navy","status":1,"hex":"#05056b","picture":null},{"pvo_id":2193648,"v_id":1,"vu_id":0,"vuv_id":1,"value":"Putih","status":1,"hex":"#ffffff","picture":null}]},{"pv_id":659144,"name":"Ukuran Pakaian","identifier":"size","unit_name":"US","v_id":6,"vu_id":8,"status":1,"position":2,"option":[{"pvo_id":2193655,"v_id":6,"vu_id":0,"vuv_id":0,"value":"custom 3","status":1,"hex":"","picture":null},{"pvo_id":2193654,"v_id":6,"vu_id":0,"vuv_id":30,"value":"2","status":1,"hex":"","picture":null},{"pvo_id":2193653,"v_id":6,"vu_id":0,"vuv_id":31,"value":"4","status":1,"hex":"","picture":null},{"pvo_id":2193652,"v_id":6,"vu_id":0,"vuv_id":29,"value":"0","status":1,"hex":"","picture":null}]}],"variant_data":[{"pvd_id":3026306,"status":1,"stock":0,"v_code":"2193648:2193652"},{"pvd_id":3026307,"status":1,"stock":0,"v_code":"2193648:2193653"},{"pvd_id":3026308,"status":1,"stock":0,"v_code":"2193648:2193654"},{"pvd_id":3026309,"status":1,"stock":0,"v_code":"2193648:2193655"},{"pvd_id":3026310,"status":1,"stock":0,"v_code":"2193649:2193652"},{"pvd_id":3026311,"status":1,"stock":0,"v_code":"2193649:2193653"},{"pvd_id":3026312,"status":1,"stock":0,"v_code":"2193650:2193652"}]}}
 * Maroon 0, Navy 0 4, putih 0 2 4 custom 3
 */
@Deprecated
public class ProductVariantByPrdModel implements Parcelable {
    @SerializedName("product_id")
    @Expose
    private long productId;
    @SerializedName("variant_option")
    @Expose
    private List<VariantOption> variantOptionList = null;
    @SerializedName("variant_data")
    @Expose
    private List<VariantDatum> variantDataList = null;

    public long getProductId() {
        return productId;
    }

    public List<VariantOption> getVariantOptionList() {
        return variantOptionList;
    }

    public List<VariantDatum> getVariantDataList() {
        return variantDataList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.productId);
        dest.writeList(this.variantOptionList);
        dest.writeTypedList(this.variantDataList);
    }

    public ProductVariantByPrdModel() {
    }

    protected ProductVariantByPrdModel(Parcel in) {
        this.productId = in.readLong();
        this.variantOptionList = new ArrayList<VariantOption>();
        in.readList(this.variantOptionList, VariantOption.class.getClassLoader());
        this.variantDataList = in.createTypedArrayList(VariantDatum.CREATOR);
    }

    public static final Parcelable.Creator<ProductVariantByPrdModel> CREATOR = new Parcelable.Creator<ProductVariantByPrdModel>() {
        @Override
        public ProductVariantByPrdModel createFromParcel(Parcel source) {
            return new ProductVariantByPrdModel(source);
        }

        @Override
        public ProductVariantByPrdModel[] newArray(int size) {
            return new ProductVariantByPrdModel[size];
        }
    };

}
