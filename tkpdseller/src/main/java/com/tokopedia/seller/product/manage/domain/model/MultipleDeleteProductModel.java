package com.tokopedia.seller.product.manage.domain.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 10/5/17.
 */

public class MultipleDeleteProductModel {
    private List<String> productIdDeletedList;
    private List<String> productIdFailedToDeleteList;

    public MultipleDeleteProductModel (){
        productIdDeletedList = new ArrayList<>();
        productIdFailedToDeleteList = new ArrayList<>();
    }

    public List<String> getProductIdDeletedList() {
        return productIdDeletedList;
    }

    public void setProductIdDeletedList(List<String> productIdDeletedList) {
        this.productIdDeletedList = productIdDeletedList;
    }

    public List<String> getProductIdFailedToDeleteList() {
        return productIdFailedToDeleteList;
    }

    public void setProductIdFailedToDeleteList(List<String> productIdFailedToDeleteList) {
        this.productIdFailedToDeleteList = productIdFailedToDeleteList;
    }

    public void addProductIdFailedToDelete(String productId) {
        productIdFailedToDeleteList.add(productId);
    }

    public void addProductIdDeleted(String productId) {
        productIdDeletedList.add(productId);
    }
}
