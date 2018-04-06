package com.tokopedia.inbox.attachinvoice.view.resultmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.inbox.attachinvoice.view.model.InvoiceViewModel;

/**
 * Created by Hendri on 27/03/18.
 */
public class SelectedInvoice implements Parcelable {
    private Long invoiceId;
    private String invoiceNo;
    private String invoiceTypeStr;
    private Integer invoiceType;
    private String topProductName;
    private String topProductImage;
    private String description;
    private String amount;
    private String date;
    private String invoiceUrl;
    private String status;
    private int statusId;

    public SelectedInvoice(Long invoiceId, String invoiceNo, String invoiceTypeStr, Integer
            invoiceType, String topProductName, String topProductImage, String description,
                           String amount, String date, String invoiceUrl, String status, int
                                   statusId) {
        this.invoiceId = invoiceId;
        this.invoiceNo = invoiceNo;
        this.invoiceTypeStr = invoiceTypeStr;
        this.invoiceType = invoiceType;
        this.topProductName = topProductName;
        this.topProductImage = topProductImage;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.invoiceUrl = invoiceUrl;
        this.status = status;
        this.statusId = statusId;
    }

    public SelectedInvoice(InvoiceViewModel viewModel) {
        this.invoiceId = viewModel.getInvoiceId();
        this.invoiceNo = viewModel.getInvoiceNumber();
        this.invoiceTypeStr = viewModel.getInvoiceTypeStr();
        this.invoiceType = viewModel.getInvoiceType();
        this.topProductName = viewModel.getProductTopName();
        this.topProductImage = viewModel.getProductTopImage();
        this.description = viewModel.getDescription();
        this.amount = viewModel.getTotal();
        this.date = viewModel.getDate();
        this.invoiceUrl = viewModel.getInvoiceUrl();
        this.status = viewModel.getStatus();
        this.statusId = viewModel.getStatusId();
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getInvoiceTypeStr() {
        return invoiceTypeStr;
    }

    public void setInvoiceTypeStr(String invoiceTypeStr) {
        this.invoiceTypeStr = invoiceTypeStr;
    }

    public Integer getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(Integer invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getTopProductName() {
        return topProductName;
    }

    public void setTopProductName(String topProductName) {
        this.topProductName = topProductName;
    }

    public String getTopProductImage() {
        return topProductImage;
    }

    public void setTopProductImage(String topProductImage) {
        this.topProductImage = topProductImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceUrl() {
        return invoiceUrl;
    }

    public void setInvoiceUrl(String invoiceUrl) {
        this.invoiceUrl = invoiceUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.invoiceId);
        dest.writeString(this.invoiceNo);
        dest.writeString(this.invoiceTypeStr);
        dest.writeValue(this.invoiceType);
        dest.writeString(this.topProductName);
        dest.writeString(this.topProductImage);
        dest.writeString(this.description);
        dest.writeString(this.amount);
        dest.writeString(this.date);
        dest.writeString(this.invoiceUrl);
        dest.writeString(this.status);
        dest.writeInt(this.statusId);
    }

    protected SelectedInvoice(Parcel in) {
        this.invoiceId = in.readLong();
        this.invoiceNo = in.readString();
        this.invoiceTypeStr = in.readString();
        this.invoiceType = (Integer) in.readValue(Integer.class.getClassLoader());
        this.topProductName = in.readString();
        this.topProductImage = in.readString();
        this.description = in.readString();
        this.amount = in.readString();
        this.date = in.readString();
        this.invoiceUrl = in.readString();
        this.status = in.readString();
        this.statusId = in.readInt();
    }

    public static final Creator<SelectedInvoice> CREATOR = new Creator<SelectedInvoice>() {
        @Override
        public SelectedInvoice createFromParcel(Parcel source) {
            return new SelectedInvoice(source);
        }

        @Override
        public SelectedInvoice[] newArray(int size) {
            return new SelectedInvoice[size];
        }
    };
}
