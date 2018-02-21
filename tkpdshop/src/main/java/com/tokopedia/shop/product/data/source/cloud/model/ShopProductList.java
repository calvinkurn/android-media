
package com.tokopedia.shop.product.data.source.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopProductList {

    @SerializedName("paging")
    @Expose
    private Paging paging;
    @SerializedName("total_data")
    @Expose
    private long totalData;
    @SerializedName("list")
    @Expose
    private java.util.List<ShopProduct> list = null;

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    public long getTotalData() {
        return totalData;
    }

    public void setTotalData(long totalData) {
        this.totalData = totalData;
    }

    public java.util.List<ShopProduct> getList() {
        return list;
    }

    public void setList(java.util.List<ShopProduct> list) {
        this.list = list;
    }

    public static class Paging {

        @SerializedName("uri_previous")
        @Expose
        private String uriPrevious;
        @SerializedName("uri_next")
        @Expose
        private String uriNext;

        /**
         *
         * @return
         *     The uriPrevious
         */
        public String getUriPrevious() {
            return uriPrevious;
        }

        /**
         *
         * @param uriPrevious
         *     The uri_previous
         */
        public void setUriPrevious(String uriPrevious) {
            this.uriPrevious = uriPrevious;
        }

        /**
         *
         * @return
         *     The uriNext
         */
        public String getUriNext() {
            return uriNext;
        }

        /**
         *
         * @param uriNext
         *     The uri_next
         */
        public void setUriNext(String uriNext) {
            this.uriNext = uriNext;
        }

    }

}
