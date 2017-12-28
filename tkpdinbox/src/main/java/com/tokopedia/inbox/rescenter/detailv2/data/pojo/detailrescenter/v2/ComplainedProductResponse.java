package com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailrescenter.v2;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yfsx on 08/11/17.
 */
public class ComplainedProductResponse {

    @SerializedName("id")
    private int id;
    @SerializedName("count")
    private int count;
    @SerializedName("product")
    private ProductResponse product;
    @SerializedName("trouble")
    private TroubleResponse trouble;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ProductResponse getProduct() {
        return product;
    }

    public void setProduct(ProductResponse product) {
        this.product = product;
    }

    public TroubleResponse getTrouble() {
        return trouble;
    }

    public void setTrouble(TroubleResponse trouble) {
        this.trouble = trouble;
    }

}
