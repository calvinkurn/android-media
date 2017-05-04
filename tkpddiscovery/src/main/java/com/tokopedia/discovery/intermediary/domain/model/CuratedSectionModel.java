package com.tokopedia.discovery.intermediary.domain.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alifa on 3/24/17.
 */

public class CuratedSectionModel {

    private String title = "";
    private List<ProductModel> products = new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ProductModel> getProducts() {
        return products;
    }

    public void setProducts(List<ProductModel> products) {
        this.products = products;
    }
}
