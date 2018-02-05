
package com.tokopedia.events.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.events.data.entity.response.Form;

import java.util.List;

public class PackageViewModel implements Parcelable {

    private Integer id;
    private Integer productId;
    private Integer productScheduleId;
    private Integer productGroupId;
    private String providerScheduleId;
    private String providerTicketId;
    private String thumbnailApp;
    private String displayName;
    private String title;
    private String description;
    private String tnc;
    private Integer convenienceFee;
    private Integer mrp;
    private Integer commission;
    private String commissionType;
    private Integer salesPrice;
    private Integer sold;
    private Integer booked;
    private Integer available;
    private Integer minQty;
    private Integer maxQty;
    private String providerStatus;
    private int selectedQuantity;
    private String timeRange;
    private String address;
    private Form form;
    private String fetchSectionUrl;
    private List<Form> forms;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    private Integer categoryId;

    public Form getForm() {
        return form;
    }
    public void setForm(Form form) {
        this.form = form;
    }

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


    public String getProviderStatus() {
        return providerStatus;
    }

    public void setProviderStatus(String providerStatus) {
        this.providerStatus = providerStatus;
    }


    public int getSelectedQuantity() {
        return selectedQuantity;
    }

    public void setSelectedQuantity(int selectedQuantity) {
        this.selectedQuantity = selectedQuantity;
    }

    public String getThumbnailApp() {
        return thumbnailApp;
    }

    public void setThumbnailApp(String thumbnailApp) {
        this.thumbnailApp = thumbnailApp;
    }

    public String getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(String timeRange) {
        this.timeRange = timeRange;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFetchSectionUrl() {
        return fetchSectionUrl;
    }

    public void setFetchSectionUrl(String fetchSectionUrl) {
        this.fetchSectionUrl = fetchSectionUrl;
    }

    public List<Form> getForms() {
        return forms;
    }

    public void setForms(List<Form> forms) {
        this.forms = forms;
    }


    public PackageViewModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeValue(this.productId);
        dest.writeValue(this.productScheduleId);
        dest.writeValue(this.productGroupId);
        dest.writeString(this.providerScheduleId);
        dest.writeString(this.providerTicketId);
        dest.writeString(this.thumbnailApp);
        dest.writeString(this.displayName);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.tnc);
        dest.writeValue(this.convenienceFee);
        dest.writeValue(this.mrp);
        dest.writeValue(this.commission);
        dest.writeString(this.commissionType);
        dest.writeValue(this.salesPrice);
        dest.writeValue(this.sold);
        dest.writeValue(this.booked);
        dest.writeValue(this.available);
        dest.writeValue(this.minQty);
        dest.writeValue(this.maxQty);
        dest.writeString(this.providerStatus);
        dest.writeInt(this.selectedQuantity);
        dest.writeString(this.timeRange);
        dest.writeString(this.address);
        dest.writeString(this.fetchSectionUrl);
        dest.writeParcelable(this.form, flags);
        dest.writeTypedList(this.forms);
        dest.writeValue(this.categoryId);
    }

    protected PackageViewModel(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.productId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.productScheduleId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.productGroupId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.providerScheduleId = in.readString();
        this.providerTicketId = in.readString();
        this.thumbnailApp = in.readString();
        this.displayName = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.tnc = in.readString();
        this.convenienceFee = (Integer) in.readValue(Integer.class.getClassLoader());
        this.mrp = (Integer) in.readValue(Integer.class.getClassLoader());
        this.commission = (Integer) in.readValue(Integer.class.getClassLoader());
        this.commissionType = in.readString();
        this.salesPrice = (Integer) in.readValue(Integer.class.getClassLoader());
        this.sold = (Integer) in.readValue(Integer.class.getClassLoader());
        this.booked = (Integer) in.readValue(Integer.class.getClassLoader());
        this.available = (Integer) in.readValue(Integer.class.getClassLoader());
        this.minQty = (Integer) in.readValue(Integer.class.getClassLoader());
        this.maxQty = (Integer) in.readValue(Integer.class.getClassLoader());
        this.providerStatus = in.readString();
        this.selectedQuantity = in.readInt();
        this.timeRange = in.readString();
        this.address = in.readString();
        this.fetchSectionUrl = in.readString();
        this.form = in.readParcelable(Form.class.getClassLoader());
        this.forms = in.createTypedArrayList(Form.CREATOR);
        this.categoryId = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<PackageViewModel> CREATOR = new Creator<PackageViewModel>() {
        @Override
        public PackageViewModel createFromParcel(Parcel source) {
            return new PackageViewModel(source);
        }

        @Override
        public PackageViewModel[] newArray(int size) {
            return new PackageViewModel[size];
        }
    };
}
