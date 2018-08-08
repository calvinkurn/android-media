package com.tokopedia.transaction.cart.model.cartdata;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductPreorder implements Parcelable {

    @SerializedName("update_time")
    @Expose
    private String updateTime = "";
    @SerializedName("end_time")
    @Expose
    private String endTime = "";
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("process_time")
    @Expose
    private String processTime;
    @SerializedName("create_time")
    @Expose
    private String createTime = "";
    @SerializedName("start_time")
    @Expose
    private String startTime = "";
    @SerializedName("process_day")
    @Expose
    private String processDay;
    @SerializedName("max_order")
    @Expose
    private String maxOrder = "";
    @SerializedName("process_time_type")
    @Expose
    private String processTimeType;
    @SerializedName("process_time_type_string")
    @Expose
    private String processTimeTypeString = "";
    @SerializedName("order_limit")
    @Expose
    private String orderLimit = "";

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getProcessTime() {
        return processTime;
    }

    public void setProcessTime(String processTime) {
        this.processTime = processTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getProcessDay() {
        return processDay;
    }

    public void setProcessDay(String processDay) {
        this.processDay = processDay;
    }

    public String getMaxOrder() {
        return maxOrder;
    }

    public void setMaxOrder(String maxOrder) {
        this.maxOrder = maxOrder;
    }

    public String getProcessTimeType() {
        return processTimeType;
    }

    public void setProcessTimeType(String processTimeType) {
        this.processTimeType = processTimeType;
    }

    public String getProcessTimeTypeString() {
        return processTimeTypeString;
    }

    public void setProcessTimeTypeString(String processTimeTypeString) {
        this.processTimeTypeString = processTimeTypeString;
    }

    public String getOrderLimit() {
        return orderLimit;
    }

    public void setOrderLimit(String orderLimit) {
        this.orderLimit = orderLimit;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.updateTime);
        dest.writeString(this.endTime);
        dest.writeInt(this.status);
        dest.writeString(this.processTime);
        dest.writeString(this.createTime);
        dest.writeString(this.startTime);
        dest.writeString(this.processDay);
        dest.writeString(this.maxOrder);
        dest.writeString(this.processTimeType);
        dest.writeString(this.processTimeTypeString);
        dest.writeString(this.orderLimit);
    }

    public ProductPreorder() {
    }

    protected ProductPreorder(Parcel in) {
        this.updateTime = in.readString();
        this.endTime = in.readString();
        this.status = in.readInt();
        this.processTime = in.readString();
        this.createTime = in.readString();
        this.startTime = in.readString();
        this.processDay = in.readString();
        this.maxOrder = in.readString();
        this.processTimeType = in.readString();
        this.processTimeTypeString = in.readString();
        this.orderLimit = in.readString();
    }

    public static final Creator<ProductPreorder> CREATOR = new Creator<ProductPreorder>() {
        @Override
        public ProductPreorder createFromParcel(Parcel source) {
            return new ProductPreorder(source);
        }

        @Override
        public ProductPreorder[] newArray(int size) {
            return new ProductPreorder[size];
        }
    };
}
