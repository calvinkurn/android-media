package com.tokopedia.transaction.orders.orderdetails.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MetaDataInfo {

    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("entity_address")
    @Expose
    private EntityAddress entityAddress;
    @SerializedName("entity_packages")
    @Expose
    private List<EntityPackage> entityPackages;
    @SerializedName("entity_brand_name")
    @Expose
    private String entityBrandName;
    @SerializedName("entity_category_id")
    @Expose
    private int entityCategoryId;
    @SerializedName("entity_image")
    @Expose
    private String entityImage;
    @SerializedName("entity_product_id")
    @Expose
    private int entityProductId;
    @SerializedName("entity_product_name")
    @Expose
    private String entityProductName;
    @SerializedName("entity_provider_id")
    @Expose
    private int entityProviderId;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("total_ticket_count")
    @Expose
    private int totalTicketCount;
    @SerializedName("total_ticket_price")
    @Expose
    private int totalTicketPrice;

    @SerializedName("entity_passengers")
    @Expose
    private List<EntityPessenger> entityPessengers;

    @SerializedName("is_hiburan")
    @Expose
    private int isHiburan;

    @SerializedName("seo_url")
    @Expose
    private String seoUrl;

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public EntityAddress getEntityAddress() {
        return entityAddress;
    }

    public void setEntityAddress(EntityAddress entityAddress) {
        this.entityAddress = entityAddress;
    }

    public List<EntityPackage> getEntityPackages() {
        return entityPackages;
    }

    public void setEntityPackages(List<EntityPackage> entityPackages) {
        this.entityPackages = entityPackages;
    }

    public String getEntityBrandName() {
        return entityBrandName;
    }

    public void setEntityBrandName(String entityBrandName) {
        this.entityBrandName = entityBrandName;
    }

    public int getEntityCategoryId() {
        return entityCategoryId;
    }

    public void setEntityCategoryId(int entityCategoryId) {
        this.entityCategoryId = entityCategoryId;
    }

    public String getEntityImage() {
        return entityImage;
    }

    public void setEntityImage(String entityImage) {
        this.entityImage = entityImage;
    }

    public int getEntityProductId() {
        return entityProductId;
    }

    public void setEntityProductId(int entityProductId) {
        this.entityProductId = entityProductId;
    }

    public String getEntityProductName() {
        return entityProductName;
    }

    public void setEntityProductName(String entityProductName) {
        this.entityProductName = entityProductName;
    }

    public int getEntityProviderId() {
        return entityProviderId;
    }

    public void setEntityProviderId(int entityProviderId) {
        this.entityProviderId = entityProviderId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public int getTotalTicketCount() {
        return totalTicketCount;
    }

    public void setTotalTicketCount(int totalTicketCount) {
        this.totalTicketCount = totalTicketCount;
    }

    public int getTotalTicketPrice() {
        return totalTicketPrice;
    }

    public void setTotalTicketPrice(int totalTicketPrice) {
        this.totalTicketPrice = totalTicketPrice;
    }

    public EntityAddress getEntityaddress() {
        return entityAddress;
    }

    public void setEntity_address(EntityAddress entityaddress) {
        this.entityAddress = entityaddress;
    }

    public List<EntityPessenger> getEntityPessengers() {
        return entityPessengers;
    }

    public void setEntityPessengers(List<EntityPessenger> entityPessengers) {
        this.entityPessengers = entityPessengers;
    }

    public int getIsHiburan() {
        return isHiburan;
    }

    public void setIsHiburan(int isHiburan) {
        this.isHiburan = isHiburan;
    }

    public String getSeoUrl() {
        return seoUrl;
    }
}
