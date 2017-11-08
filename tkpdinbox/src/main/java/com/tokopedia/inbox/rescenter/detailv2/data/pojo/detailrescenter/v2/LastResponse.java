package com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailrescenter.v2;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by yfsx on 07/11/17.
 */
public class LastResponse {

    @SerializedName("sellerAddressResponse")
    private SellerAddressResponse sellerAddressResponse;
    @SerializedName("userAwb")
    private UserAwbResponse userAwb;
    @SerializedName("solution")
    private SolutionResponse solution;
    @SerializedName("problem")
    private String problem;
    @SerializedName("status")
    private String status;
    @SerializedName("complainedProductResponses")
    private List<ComplainedProductResponse> complainedProductResponses;

    public SellerAddressResponse getSellerAddressResponse() {
        return sellerAddressResponse;
    }

    public void setSellerAddressResponse(SellerAddressResponse sellerAddressResponse) {
        this.sellerAddressResponse = sellerAddressResponse;
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

    public List<ComplainedProductResponse> getComplainedProductResponses() {
        return complainedProductResponses;
    }

    public void setComplainedProductResponses(List<ComplainedProductResponse> complainedProductResponses) {
        this.complainedProductResponses = complainedProductResponses;
    }

}
