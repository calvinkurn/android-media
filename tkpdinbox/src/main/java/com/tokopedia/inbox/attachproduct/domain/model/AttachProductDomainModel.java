package com.tokopedia.inbox.attachproduct.domain.model;

import com.tokopedia.core.shopinfo.models.productmodel.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hendri on 13/02/18.
 */

public class AttachProductDomainModel {
    List<Product> products;

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
