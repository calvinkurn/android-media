package com.tokopedia.seller.myproduct.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.Arrays;

/**
 * Created by m.normansyah on 29/12/2015.
 */
@Parcel
public class ProductSubmitModel {

    /**
     * this is for parcelable
     */
    public ProductSubmitModel(){}

    @SerializedName("message_error")
    @Expose
    String[] message_error;

    @SerializedName("status")
    @Expose
    String status;

    @SerializedName("data")
    @Expose
    Data data;

    @SerializedName("config")
    @Expose
    String config;

    @SerializedName("server_process_time")
    @Expose
    String server_process_time;

    @Parcel
    public static class Data{

        /**
         * this is for parcelable
         */
        public Data(){}

        @SerializedName("product_id")
        @Expose
        String productId;

        @SerializedName("product_etalase")
        @Expose
        String productEtalase;

        @SerializedName("product_name")
        @Expose
        String productName;

        @SerializedName("product_desc")
        @Expose
        String productDesc;

        @SerializedName("product_dest")
        @Expose
        String productDest;

        @SerializedName("product_url")
        @Expose
        String productUrl;

        @SerializedName("product_primary_pic")
        @Expose
        String productPrimaryPic;

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getProductEtalase() {
            return productEtalase;
        }

        public void setProductEtalase(String productEtalase) {
            this.productEtalase = productEtalase;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getProductDesc() {
            return productDesc;
        }

        public void setProductDesc(String productDesc) {
            this.productDesc = productDesc;
        }

        public String getProductDest() {
            return productDest;
        }

        public void setProductDest(String productDest) {
            this.productDest = productDest;
        }

        public String getProductUrl() {
            return productUrl;
        }

        public void setProductUrl(String productUrl) {
            this.productUrl = productUrl;
        }

        public String getProductPrimaryPic() {
            return productPrimaryPic;
        }

        public void setProductPrimaryPic(String productPrimaryPic) {
            this.productPrimaryPic = productPrimaryPic;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "productId='" + productId + '\'' +
                    ", productEtalase='" + productEtalase + '\'' +
                    ", productName='" + productName + '\'' +
                    ", productDesc='" + productDesc + '\'' +
                    ", productDest='" + productDest + '\'' +
                    ", productUrl='" + productUrl + '\'' +
                    ", productPrimaryPic='" + productPrimaryPic + '\'' +
                    '}';
        }
    }

    public String[] getMessage_error() {
        return message_error;
    }

    public void setMessage_error(String[] message_error) {
        this.message_error = message_error;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getServer_process_time() {
        return server_process_time;
    }

    public void setServer_process_time(String server_process_time) {
        this.server_process_time = server_process_time;
    }

    @Override
    public String toString() {
        return "ProductSubmitModel{" +
                "message_error=" + Arrays.toString(message_error) +
                ", status='" + status + '\'' +
                ", data=" + data +
                ", config='" + config + '\'' +
                ", server_process_time='" + server_process_time + '\'' +
                '}';
    }
}
