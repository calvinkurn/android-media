package com.tokopedia.inbox.attachproduct.domain.model;

import com.tokopedia.inbox.attachproduct.data.model.DataProductResponse;

import java.util.List;

/**
 * Created by Hendri on 13/02/18.
 */

public class AttachProductDomainModel {
    private List<DataProductResponse> products;

    public List<DataProductResponse> getProducts() {
        return products;
    }

    public void setProducts(List<DataProductResponse> products) {
        this.products = products;
    }
}
