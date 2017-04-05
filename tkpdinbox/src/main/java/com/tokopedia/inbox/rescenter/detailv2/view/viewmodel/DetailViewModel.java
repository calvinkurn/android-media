package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hangnadi on 3/9/17.
 */

public class DetailViewModel implements Parcelable {
    private boolean timeOut;
    private boolean success;
    private String messageError;
    private ButtonData buttonData;
    private StatusData statusData;
    private DetailData detailData;
    private ProductData productData;
    private SolutionData solutionData;
    private HistoryData historyData;
    private AwbData awbData;
    private AddressReturData addressReturData;

    public void setTimeOut(boolean timeOut) {
        this.timeOut = timeOut;
    }

    public boolean isTimeOut() {
        return timeOut;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setMessageError(String messageError) {
        this.messageError = messageError;
    }

    public String getMessageError() {
        return messageError;
    }

    public ButtonData getButtonData() {
        return buttonData;
    }

    public void setButtonData(ButtonData buttonData) {
        this.buttonData = buttonData;
    }

    public StatusData getStatusData() {
        return statusData;
    }

    public void setStatusData(StatusData statusData) {
        this.statusData = statusData;
    }

    public DetailData getDetailData() {
        return detailData;
    }

    public void setDetailData(DetailData detailData) {
        this.detailData = detailData;
    }

    public ProductData getProductData() {
        return productData;
    }

    public void setProductData(ProductData productData) {
        this.productData = productData;
    }

    public SolutionData getSolutionData() {
        return solutionData;
    }

    public void setSolutionData(SolutionData solutionData) {
        this.solutionData = solutionData;
    }

    public HistoryData getHistoryData() {
        return historyData;
    }

    public void setHistoryData(HistoryData historyData) {
        this.historyData = historyData;
    }

    public AwbData getAwbData() {
        return awbData;
    }

    public void setAwbData(AwbData awbData) {
        this.awbData = awbData;
    }

    public AddressReturData getAddressReturData() {
        return addressReturData;
    }

    public void setAddressReturData(AddressReturData addressReturData) {
        this.addressReturData = addressReturData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.timeOut ? (byte) 1 : (byte) 0);
        dest.writeByte(this.success ? (byte) 1 : (byte) 0);
        dest.writeString(this.messageError);
        dest.writeParcelable(this.buttonData, flags);
        dest.writeParcelable(this.statusData, flags);
        dest.writeParcelable(this.detailData, flags);
        dest.writeParcelable(this.productData, flags);
        dest.writeParcelable(this.solutionData, flags);
        dest.writeParcelable(this.historyData, flags);
        dest.writeParcelable(this.awbData, flags);
        dest.writeParcelable(this.addressReturData, flags);
    }

    public DetailViewModel() {
    }

    protected DetailViewModel(Parcel in) {
        this.timeOut = in.readByte() != 0;
        this.success = in.readByte() != 0;
        this.messageError = in.readString();
        this.buttonData = in.readParcelable(ButtonData.class.getClassLoader());
        this.statusData = in.readParcelable(StatusData.class.getClassLoader());
        this.detailData = in.readParcelable(DetailData.class.getClassLoader());
        this.productData = in.readParcelable(ProductData.class.getClassLoader());
        this.solutionData = in.readParcelable(SolutionData.class.getClassLoader());
        this.historyData = in.readParcelable(HistoryData.class.getClassLoader());
        this.awbData = in.readParcelable(AwbData.class.getClassLoader());
        this.addressReturData = in.readParcelable(AddressReturData.class.getClassLoader());
    }

    public static final Parcelable.Creator<DetailViewModel> CREATOR = new Parcelable.Creator<DetailViewModel>() {
        @Override
        public DetailViewModel createFromParcel(Parcel source) {
            return new DetailViewModel(source);
        }

        @Override
        public DetailViewModel[] newArray(int size) {
            return new DetailViewModel[size];
        }
    };
}
