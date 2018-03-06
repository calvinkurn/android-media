package com.tokopedia.transaction.checkout.data.entity.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 05/03/18.
 */

public class DropshipDataCheckoutRequest {
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("telp_no")
    @Expose
    public String telpNo;

    private DropshipDataCheckoutRequest(Builder builder) {
        name = builder.name;
        telpNo = builder.telpNo;
    }


    public static final class Builder {
        private String name;
        private String telpNo;

        public Builder() {
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder telpNo(String val) {
            telpNo = val;
            return this;
        }

        public DropshipDataCheckoutRequest build() {
            return new DropshipDataCheckoutRequest(this);
        }
    }
}
