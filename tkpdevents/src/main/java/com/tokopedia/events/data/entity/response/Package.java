
package com.tokopedia.events.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Package {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("product_id")
    @Expose
    private int productId;
    @SerializedName("product_schedule_id")
    @Expose
    private int productScheduleId;
    @SerializedName("product_group_id")
    @Expose
    private int productGroupId;
    @SerializedName("provider_schedule_id")
    @Expose
    private String providerScheduleId;
    @SerializedName("provider_ticket_id")
    @Expose
    private String providerTicketId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("display_name")
    @Expose
    private String displayName;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("tnc")
    @Expose
    private String tnc;
    @SerializedName("convenience_fee")
    @Expose
    private int convenienceFee;
    @SerializedName("mrp")
    @Expose
    private int mrp;
    @SerializedName("commission")
    @Expose
    private int commission;
    @SerializedName("commission_type")
    @Expose
    private String commissionType;
    @SerializedName("sales_price")
    @Expose
    private int salesPrice;
    @SerializedName("sold")
    @Expose
    private int sold;
    @SerializedName("booked")
    @Expose
    private int booked;
    @SerializedName("available")
    @Expose
    private int available;
    @SerializedName("min_qty")
    @Expose
    private int minQty;
    @SerializedName("max_qty")
    @Expose
    private int maxQty;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("color")
    @Expose
    private String color;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("provider_meta_data")
    @Expose
    private String providerMetaData;
    @SerializedName("provider_status")
    @Expose
    private String providerStatus;
    @SerializedName("venue_detail")
    @Expose
    private String venueDetail;
    @SerializedName("start_date")
    @Expose
    private int startDate;
    @SerializedName("end_date")
    @Expose
    private int endDate;
    @SerializedName("fetch_section_url")
    @Expose
    private String fetchSectionUrl;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

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

    public int getProductGroupId() {
        return productGroupId;
    }

    public void setProductGroupId(int productGroupId) {
        this.productGroupId = productGroupId;
    }

    public String getProviderScheduleId() {
        return providerScheduleId;
    }

    public void setProviderScheduleId(String providerScheduleId) {
        this.providerScheduleId = providerScheduleId;
    }

    public String getProviderTicketId() {
        return providerTicketId;
    }

    public void setProviderTicketId(String providerTicketId) {
        this.providerTicketId = providerTicketId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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

    public int getConvenienceFee() {
        return convenienceFee;
    }

    public void setConvenienceFee(int convenienceFee) {
        this.convenienceFee = convenienceFee;
    }

    public int getMrp() {
        return mrp;
    }

    public void setMrp(int mrp) {
        this.mrp = mrp;
    }

    public int getCommission() {
        return commission;
    }

    public void setCommission(int commission) {
        this.commission = commission;
    }

    public String getCommissionType() {
        return commissionType;
    }

    public void setCommissionType(String commissionType) {
        this.commissionType = commissionType;
    }

    public int getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(int salesPrice) {
        this.salesPrice = salesPrice;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

    public int getBooked() {
        return booked;
    }

    public void setBooked(int booked) {
        this.booked = booked;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public int getMinQty() {
        return minQty;
    }

    public void setMinQty(int minQty) {
        this.minQty = minQty;
    }

    public int getMaxQty() {
        return maxQty;
    }

    public void setMaxQty(int maxQty) {
        this.maxQty = maxQty;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getProviderMetaData() {
        return providerMetaData;
    }

    public void setProviderMetaData(String providerMetaData) {
        this.providerMetaData = providerMetaData;
    }

    public String getProviderStatus() {
        return providerStatus;
    }

    public void setProviderStatus(String providerStatus) {
        this.providerStatus = providerStatus;
    }

    public String getVenueDetail() {
        return venueDetail;
    }

    public void setVenueDetail(String venueDetail) {
        this.venueDetail = venueDetail;
    }

    public int getStartDate() {
        return startDate;
    }

    public void setStartDate(int startDate) {
        this.startDate = startDate;
    }

    public int getEndDate() {
        return endDate;
    }

    public void setEndDate(int endDate) {
        this.endDate = endDate;
    }

    public String getFetchSectionUrl() {
        return fetchSectionUrl;
    }

    public void setFetchSectionUrl(String fetchSectionUrl) {
        this.fetchSectionUrl = fetchSectionUrl;
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

}
