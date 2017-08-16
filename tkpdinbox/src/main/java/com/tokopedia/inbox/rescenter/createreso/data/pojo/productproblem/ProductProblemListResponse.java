package com.tokopedia.inbox.rescenter.createreso.data.pojo.productproblem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoasfs on 16/08/17.
 */

public class ProductProblemListResponse {
    @SerializedName("data")
    @Expose
    List<ProductProblemResponse> productProblemResponseList = new ArrayList<>();

    public List<ProductProblemResponse> getProductProblemResponseList() {
        return productProblemResponseList;
    }

    public void setProductProblemResponseList(List<ProductProblemResponse> productProblemResponseList) {
        this.productProblemResponseList = productProblemResponseList;
    }

    @Override
    public String toString() {
        return "ProductProblemListResponse{" +
                "data='" + productProblemResponseList.toString() + '\'' +
                '}';
    }

}
