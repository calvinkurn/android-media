
package com.tokopedia.events.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Package {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("product_id")
    @Expose
    private Integer productId;
    @SerializedName("product_schedule_id")
    @Expose
    private Integer productScheduleId;
    @SerializedName("product_group_id")
    @Expose
    private Integer productGroupId;
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
    private Integer convenienceFee;
    @SerializedName("mrp")
    @Expose
    private Integer mrp;
    @SerializedName("commission")
    @Expose
    private Integer commission;
    @SerializedName("commission_type")
    @Expose
    private String commissionType;
    @SerializedName("sales_price")
    @Expose
    private Integer salesPrice;
    @SerializedName("sold")
    @Expose
    private Integer sold;
    @SerializedName("booked")
    @Expose
    private Integer booked;
    @SerializedName("available")
    @Expose
    private Integer available;
    @SerializedName("min_qty")
    @Expose
    private Integer minQty;
    @SerializedName("max_qty")
    @Expose
    private Integer maxQty;
    @SerializedName("status")
    @Expose
    private Integer status;
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
    private Integer startDate;
    @SerializedName("end_date")
    @Expose
    private Integer endDate;
    @SerializedName("fetch_section_url")
    @Expose
    private String fetchSectionUrl;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getProductScheduleId() {
        return productScheduleId;
    }

    public void setProductScheduleId(Integer productScheduleId) {
        this.productScheduleId = productScheduleId;
    }

    public Integer getProductGroupId() {
        return productGroupId;
    }

    public void setProductGroupId(Integer productGroupId) {
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

    public Integer getConvenienceFee() {
        return convenienceFee;
    }

    public void setConvenienceFee(Integer convenienceFee) {
        this.convenienceFee = convenienceFee;
    }

    public Integer getMrp() {
        return mrp;
    }

    public void setMrp(Integer mrp) {
        this.mrp = mrp;
    }

    public Integer getCommission() {
        return commission;
    }

    public void setCommission(Integer commission) {
        this.commission = commission;
    }

    public String getCommissionType() {
        return commissionType;
    }

    public void setCommissionType(String commissionType) {
        this.commissionType = commissionType;
    }

    public Integer getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(Integer salesPrice) {
        this.salesPrice = salesPrice;
    }

    public Integer getSold() {
        return sold;
    }

    public void setSold(Integer sold) {
        this.sold = sold;
    }

    public Integer getBooked() {
        return booked;
    }

    public void setBooked(Integer booked) {
        this.booked = booked;
    }

    public Integer getAvailable() {
        return available;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }

    public Integer getMinQty() {
        return minQty;
    }

    public void setMinQty(Integer minQty) {
        this.minQty = minQty;
    }

    public Integer getMaxQty() {
        return maxQty;
    }

    public void setMaxQty(Integer maxQty) {
        this.maxQty = maxQty;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
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

    public Integer getStartDate() {
        return startDate;
    }

    public void setStartDate(Integer startDate) {
        this.startDate = startDate;
    }

    public Integer getEndDate() {
        return endDate;
    }

    public void setEndDate(Integer endDate) {
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
