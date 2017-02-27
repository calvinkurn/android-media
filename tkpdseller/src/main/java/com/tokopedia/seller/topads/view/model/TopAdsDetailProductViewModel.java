package com.tokopedia.seller.topads.view.model;

/**
 * Created by Nathaniel on 2/23/2017.
 */

public class TopAdsDetailProductViewModel implements TopAdsDetailAdViewModel {

    private long id;
    private int type;
    private long groupId;
    private long shopId;
    private long itemId;
    private int status;
    private float priceBid;
    private boolean budget;
    private float priceDaily;
    private int stickerId;
    private boolean scheduled;
    private String startDate;
    private String startTime;
    private String endDate;
    private String endTime;
    private String image;
    private String title;

    @Override
    public int getStickerId() {
        return stickerId;
    }

    @Override
    public void setStickerId(int stickerId) {
        this.stickerId = stickerId;
    }

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    @Override
    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    @Override
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public float getPriceBid() {
        return priceBid;
    }

    @Override
    public void setPriceBid(float priceBid) {
        this.priceBid = priceBid;
    }

    @Override
    public boolean isBudget() {
        return budget;
    }

    @Override
    public void setBudget(boolean budget) {
        this.budget = budget;
    }

    @Override
    public float getPriceDaily() {
        return priceDaily;
    }

    @Override
    public void setPriceDaily(float priceDaily) {
        this.priceDaily = priceDaily;
    }

    @Override
    public boolean isScheduled() {
        return scheduled;
    }

    @Override
    public void setScheduled(boolean scheduled) {
        this.scheduled = scheduled;
    }

    @Override
    public String getStartDate() {
        return startDate;
    }

    @Override
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    @Override
    public String getStartTime() {
        return startTime;
    }

    @Override
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @Override
    public String getEndDate() {
        return endDate;
    }

    @Override
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public String getEndTime() {
        return endTime;
    }

    @Override
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
