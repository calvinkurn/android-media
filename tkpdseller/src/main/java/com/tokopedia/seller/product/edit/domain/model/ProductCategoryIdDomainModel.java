package com.tokopedia.seller.product.edit.domain.model;

/**
 * Created by Hendry on 5/6/2017.
 */

public class ProductCategoryIdDomainModel {
    private int id;
    private String name;

    public ProductCategoryIdDomainModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
