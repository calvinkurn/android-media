package com.tokopedia.events.data.entity.response.verifyresponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EntityPackagesItem {

    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("area_code")
    @Expose
    private List<String> areaCode = null;
    @SerializedName("barcode")
    @Expose
    private List<String> barcode = null;
    @SerializedName("area_id")
    @Expose
    private String areaId;
    @SerializedName("base_price")
    @Expose
    private int basePrice;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("commision")
    @Expose
    private float commision;
    @SerializedName("commision_type")
    @Expose
    private String commisionType;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("dimension")
    @Expose
    private String dimension;
    @SerializedName("display_name")
    @Expose
    private String displayName;
    @SerializedName("error_message")
    @Expose
    private String errorMessage;
    @SerializedName("group_id")
    @Expose
    private int groupId;
    @SerializedName("group_name")
    @Expose
    private String groupName;
    @SerializedName("invoice_expiry")
    @Expose
    private String invoiceExpiry;
    @SerializedName("invoice_status")
    @Expose
    private String invoiceStatus;
    @SerializedName("package_id")
    @Expose
    private int packageId;
    @SerializedName("package_price")
    @Expose
    private int packagePrice;
    @SerializedName("payment_type")
    @Expose
    private String paymentType;
    @SerializedName("price_per_seat")
    @Expose
    private int pricePerSeat;
    @SerializedName("product_id")
    @Expose
    private int productId;
    @SerializedName("provider_group_id")
    @Expose
    private String providerGroupId;
    @SerializedName("provider_invoice_code")
    @Expose
    private String providerInvoiceCode;
    @SerializedName("provider_invoice_indentifier")
    @Expose
    private String providerInvoiceIndentifier;
    @SerializedName("provider_order_id")
    @Expose
    private String providerOrderId;
    @SerializedName("provider_schedule_id")
    @Expose
    private String providerScheduleId;
    @SerializedName("provider_ticket_id")
    @Expose
    private String providerTicketId;
    @SerializedName("provider_unhash_ticket_id")
    @Expose
    private String providerUnhashTicketId;
    @SerializedName("quantity")
    @Expose
    private int quantity;
    @SerializedName("schedule_id")
    @Expose
    private int scheduleId;
    @SerializedName("seat_ids")
    @Expose
    private List<String> seatIds = null;
    @SerializedName("seat_physical_row_ids")
    @Expose
    private List<String> seatPhysicalRowIds = null;
    @SerializedName("seat_row_ids")
    @Expose
    private List<String> seatRowIds = null;
    @SerializedName("show_date")
    @Expose
    private String showDate;
    @SerializedName("tkp_invoice_id")
    @Expose
    private int tkpInvoiceId;
    @SerializedName("tkp_invoice_item_id")
    @Expose
    private int tkpInvoiceItemId;
    @SerializedName("total_ticket_count")
    @Expose
    private int totalTicketCount;
    @SerializedName("venue_detail")
    @Expose
    private String venueDetail;
    @SerializedName("vouchers")
    @Expose
    private Object vouchers;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(List<String> areaCode) {
        this.areaCode = areaCode;
    }

    public List<String> getBarcode() {
        return barcode;
    }

    public void setBarcode(List<String> barcode) {
        this.barcode = barcode;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public int getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(int basePrice) {
        this.basePrice = basePrice;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public float getCommision() {
        return commision;
    }

    public void setCommision(float commision) {
        this.commision = commision;
    }

    public String getCommisionType() {
        return commisionType;
    }

    public void setCommisionType(String commisionType) {
        this.commisionType = commisionType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getInvoiceExpiry() {
        return invoiceExpiry;
    }

    public void setInvoiceExpiry(String invoiceExpiry) {
        this.invoiceExpiry = invoiceExpiry;
    }

    public String getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(String invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public int getPackagePrice() {
        return packagePrice;
    }

    public void setPackagePrice(int packagePrice) {
        this.packagePrice = packagePrice;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public int getPricePerSeat() {
        return pricePerSeat;
    }

    public void setPricePerSeat(int pricePerSeat) {
        this.pricePerSeat = pricePerSeat;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProviderGroupId() {
        return providerGroupId;
    }

    public void setProviderGroupId(String providerGroupId) {
        this.providerGroupId = providerGroupId;
    }

    public String getProviderInvoiceCode() {
        return providerInvoiceCode;
    }

    public void setProviderInvoiceCode(String providerInvoiceCode) {
        this.providerInvoiceCode = providerInvoiceCode;
    }

    public String getProviderInvoiceIndentifier() {
        return providerInvoiceIndentifier;
    }

    public void setProviderInvoiceIndentifier(String providerInvoiceIndentifier) {
        this.providerInvoiceIndentifier = providerInvoiceIndentifier;
    }

    public String getProviderOrderId() {
        return providerOrderId;
    }

    public void setProviderOrderId(String providerOrderId) {
        this.providerOrderId = providerOrderId;
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

    public String getProviderUnhashTicketId() {
        return providerUnhashTicketId;
    }

    public void setProviderUnhashTicketId(String providerUnhashTicketId) {
        this.providerUnhashTicketId = providerUnhashTicketId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public List<String> getSeatIds() {
        return seatIds;
    }

    public void setSeatIds(List<String> seatIds) {
        this.seatIds = seatIds;
    }

    public List<String> getSeatPhysicalRowIds() {
        return seatPhysicalRowIds;
    }

    public void setSeatPhysicalRowIds(List<String> seatPhysicalRowIds) {
        this.seatPhysicalRowIds = seatPhysicalRowIds;
    }

    public List<String> getSeatRowIds() {
        return seatRowIds;
    }

    public void setSeatRowIds(List<String> seatRowIds) {
        this.seatRowIds = seatRowIds;
    }

    public String getShowDate() {
        return showDate;
    }

    public void setShowDate(String showDate) {
        this.showDate = showDate;
    }

    public int getTkpInvoiceId() {
        return tkpInvoiceId;
    }

    public void setTkpInvoiceId(int tkpInvoiceId) {
        this.tkpInvoiceId = tkpInvoiceId;
    }

    public int getTkpInvoiceItemId() {
        return tkpInvoiceItemId;
    }

    public void setTkpInvoiceItemId(int tkpInvoiceItemId) {
        this.tkpInvoiceItemId = tkpInvoiceItemId;
    }

    public int getTotalTicketCount() {
        return totalTicketCount;
    }

    public void setTotalTicketCount(int totalTicketCount) {
        this.totalTicketCount = totalTicketCount;
    }

    public String getVenueDetail() {
        return venueDetail;
    }

    public void setVenueDetail(String venueDetail) {
        this.venueDetail = venueDetail;
    }

    public Object getVouchers() {
        return vouchers;
    }

    public void setVouchers(Object vouchers) {
        this.vouchers = vouchers;
    }

}