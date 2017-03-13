package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel;

import java.util.List;

/**
 * Created by hangnadi on 3/10/17.
 */

public class ProductData {
    private boolean productRelatedComplaint;
    private List<ProductItem> productList;

    public boolean isProductRelatedComplaint() {
        return productRelatedComplaint;
    }

    public void setProductRelatedComplaint(boolean productRelatedComplaint) {
        this.productRelatedComplaint = productRelatedComplaint;
    }

    public List<ProductItem> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductItem> productList) {
        this.productList = productList;
    }
}
