
package com.tokopedia.inbox.rescenter.inbox.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResolutionCustomer implements Parcelable {

    @SerializedName("customer_url")
    @Expose
    private String customerUrl;
    @SerializedName("customer_id")
    @Expose
    private Integer customerId;
    @SerializedName("customer_name")
    @Expose
    private String customerName;
    @SerializedName("customer_image")
    @Expose
    private String customerImage;

    /**
     * 
     * @return
     *     The customerUrl
     */
    public String getCustomerUrl() {
        return customerUrl;
    }

    /**
     * 
     * @param customerUrl
     *     The customer_url
     */
    public void setCustomerUrl(String customerUrl) {
        this.customerUrl = customerUrl;
    }

    /**
     * 
     * @return
     *     The customerId
     */
    public Integer getCustomerId() {
        return customerId;
    }

    /**
     * 
     * @param customerId
     *     The customer_id
     */
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    /**
     * 
     * @return
     *     The customerName
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * 
     * @param customerName
     *     The customer_name
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * 
     * @return
     *     The customerImage
     */
    public String getCustomerImage() {
        return customerImage;
    }

    /**
     * 
     * @param customerImage
     *     The customer_image
     */
    public void setCustomerImage(String customerImage) {
        this.customerImage = customerImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.customerUrl);
        dest.writeValue(this.customerId);
        dest.writeString(this.customerName);
        dest.writeString(this.customerImage);
    }

    public ResolutionCustomer() {
    }

    protected ResolutionCustomer(Parcel in) {
        this.customerUrl = in.readString();
        this.customerId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.customerName = in.readString();
        this.customerImage = in.readString();
    }

    public static final Parcelable.Creator<ResolutionCustomer> CREATOR = new Parcelable.Creator<ResolutionCustomer>() {
        @Override
        public ResolutionCustomer createFromParcel(Parcel source) {
            return new ResolutionCustomer(source);
        }

        @Override
        public ResolutionCustomer[] newArray(int size) {
            return new ResolutionCustomer[size];
        }
    };
}
