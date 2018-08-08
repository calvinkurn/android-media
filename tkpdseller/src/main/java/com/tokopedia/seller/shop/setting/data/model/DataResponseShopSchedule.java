package com.tokopedia.seller.shop.setting.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by ZulfikarRahman on 9/15/2017.
 */
public class DataResponseShopSchedule {
        @SerializedName("is_success")
        @Expose
        int is_success;

        public int getIs_success() {
            return is_success;
        }

        public void setIs_success(int is_success) {
            this.is_success = is_success;
        }
}
