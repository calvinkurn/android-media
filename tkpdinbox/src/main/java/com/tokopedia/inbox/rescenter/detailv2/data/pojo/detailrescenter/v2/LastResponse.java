package com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailrescenter.v2;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by yfsx on 07/11/17.
 */
public class LastResponse {

    @SerializedName("sellerAddress")
    private SellerAddressResponse sellerAddress;
    @SerializedName("userAwb")
    private UserAwbResponse userAwb;
    @SerializedName("solution")
    private SolutionResponse solution;
    @SerializedName("problem")
    private String problem;
    @SerializedName("status")
    private String status;
    @SerializedName("complainedProducts")
    private List<ComplainedProductResponse> complainedProducts;

    public SellerAddressResponse getSellerAddress() {
        return sellerAddress;
    }

    public void setSellerAddress(SellerAddressResponse sellerAddress) {
        this.sellerAddress = sellerAddress;
    }

    public UserAwbResponse getUserAwb() {
        return userAwb;
    }

    public void setUserAwb(UserAwbResponse userAwb) {
        this.userAwb = userAwb;
    }

    public SolutionResponse getSolution() {
        return solution;
    }

    public void setSolution(SolutionResponse solution) {
        this.solution = solution;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ComplainedProductResponse> getComplainedProducts() {
        return complainedProducts;
    }

    public void setComplainedProducts(List<ComplainedProductResponse> complainedProducts) {
        this.complainedProducts = complainedProducts;
    }

}
