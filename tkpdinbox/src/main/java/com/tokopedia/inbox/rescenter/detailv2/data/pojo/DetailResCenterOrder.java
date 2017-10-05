package com.tokopedia.inbox.rescenter.detailv2.data.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hangnadi on 3/20/17.
 */

public class DetailResCenterOrder  {
    @SerializedName("id")
    private String id;
    @SerializedName("invoice")
    private Invoice invoice;
    @SerializedName("shippingRefNum")
    private String shippingRefNum;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public String getShippingRefNum() {
        return shippingRefNum;
    }

    public void setShippingRefNum(String shippingRefNum) {
        this.shippingRefNum = shippingRefNum;
    }

    public static class Invoice {
        @SerializedName("url")
        private String url;
        @SerializedName("refNum")
        private String refNum;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getRefNum() {
            return refNum;
        }

        public void setRefNum(String refNum) {
            this.refNum = refNum;
        }
    }
}
