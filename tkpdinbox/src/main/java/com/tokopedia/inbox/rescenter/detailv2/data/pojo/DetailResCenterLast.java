package com.tokopedia.inbox.rescenter.detailv2.data.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hangnadi on 3/20/17.
 */

public class DetailResCenterLast {

    @SerializedName("solution")
    private DetailResCenterLastSolution solution;
    @SerializedName("status")
    private String status;
    @SerializedName("address")
    private DetailResCenterLastAddress address;
    @SerializedName("complainedProduct")
    private List<DetailResCenterLastComplainedProduct> complainedProduct;
    @SerializedName("shipping")
    private List<DetailResCenterLastShipping> shipping;

    public DetailResCenterLastSolution getSolution() {
        return solution;
    }

    public void setSolution(DetailResCenterLastSolution solution) {
        this.solution = solution;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DetailResCenterLastAddress getAddress() {
        return address;
    }

    public void setAddress(DetailResCenterLastAddress address) {
        this.address = address;
    }

    public List<DetailResCenterLastComplainedProduct> getComplainedProduct() {
        return complainedProduct;
    }

    public void setComplainedProduct(List<DetailResCenterLastComplainedProduct> complainedProduct) {
        this.complainedProduct = complainedProduct;
    }

    public List<DetailResCenterLastShipping> getShipping() {
        return shipping;
    }

    public void setShipping(List<DetailResCenterLastShipping> shipping) {
        this.shipping = shipping;
    }

}
