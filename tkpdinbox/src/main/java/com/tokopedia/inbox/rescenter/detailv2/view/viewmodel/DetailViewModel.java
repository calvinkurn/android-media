package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel;

/**
 * Created by hangnadi on 3/9/17.
 */

public class DetailViewModel {
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
}
