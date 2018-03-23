package com.tokopedia.seller.product.edit.domain.listener;

import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;

/**
 * Created by nathan on 2/28/18.
 */

public abstract class ProductSubmitNotificationListener {

    private int id;
    private int maxCount;
    private int currentCount;
    private int submitStatus;
    private ProductViewModel productViewModel;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public int getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }

    public int getSubmitStatus() {
        return submitStatus;
    }

    public void setSubmitStatus(int submitStatus) {
        this.submitStatus = submitStatus;
    }

    public ProductViewModel getProductViewModel() {
        return productViewModel;
    }

    public void setProductViewModel(ProductViewModel productViewModel) {
        this.productViewModel = productViewModel;
    }

    public ProductSubmitNotificationListener(int id, int submitStatus) {
        this.id = id;
        this.submitStatus = submitStatus;
    }

    public void addProgress() {
        currentCount++;
    }
}
