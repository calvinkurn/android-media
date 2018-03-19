package com.tokopedia.shop.analytic.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Click {

    @SerializedName("actionField")
    @Expose
    private ActionField actionField;
    @SerializedName("products")
    @Expose
    private List<Product> products = null;

    public ActionField getActionField() {
        return actionField;
    }

    public void setActionField(ActionField actionField) {
        this.actionField = actionField;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

}
