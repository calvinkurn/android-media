
package com.tokopedia.events.data.entity.response;

import java.util.List;

public class Group {

    private int id;
    private int productId;
    private int productScheduleId;
    private String providerGroupId;
    private String name;
    private String description;
    private String tnc;
    private String providerMetaData;
    private int status;
    private String createdAt;
    private String updatedAt;

    private List<Package> packages = null;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getProductScheduleId() {
        return productScheduleId;
    }

    public void setProductScheduleId(int productScheduleId) {
        this.productScheduleId = productScheduleId;
    }

    public String getProviderGroupId() {
        return providerGroupId;
    }

    public void setProviderGroupId(String providerGroupId) {
        this.providerGroupId = providerGroupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTnc() {
        return tnc;
    }

    public void setTnc(String tnc) {
        this.tnc = tnc;
    }

    public String getProviderMetaData() {
        return providerMetaData;
    }

    public void setProviderMetaData(String providerMetaData) {
        this.providerMetaData = providerMetaData;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Package> getPackages() {
        return packages;
    }

    public void setPackages(List<Package> packages) {
        this.packages = packages;
    }

}
