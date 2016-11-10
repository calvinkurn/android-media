package com.tokopedia.core.selling.orderReject.model;

import com.tokopedia.core.product.model.productdetail.ProductDetailData;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Toped10 on 7/11/2016.
 */
@Parcel
public class ResponseGetProduct {

    /**
     * status : OK
     * config : null
     * server_process_time : 0.652828
     * data : {"is_success":1}
     */

    String status;
    String config;
    String server_process_time;
    List<String> message_error;
    /**
     * is_success : 1
     */

    ProductDetailData data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public ProductDetailData getData() {
        return data;
    }

    public void setData(ProductDetailData data) {
        this.data = data;
    }

    public List<String> getMessage_error() {
        return message_error;
    }

    public void setMessage_error(List<String> message_error) {
        this.message_error = message_error;
    }
}
