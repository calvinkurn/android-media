package com.tokopedia.seller.product.edit.domain.listener;

/**
 * Created by nathan on 2/28/18.
 */

public abstract class NotificationCountListener {

    private int id;
    private int maxCount;
    private int currentCount;
    private String productName;

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

    public String getProductName() {
        return productName;
    }

    public NotificationCountListener(int id, int maxCount) {
        this.id = id;
        this.maxCount = maxCount;
    }

    public void addProgress() {
        currentCount++;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
