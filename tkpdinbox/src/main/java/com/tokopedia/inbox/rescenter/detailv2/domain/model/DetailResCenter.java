package com.tokopedia.inbox.rescenter.detailv2.domain.model;

import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.NextActionDomain;

/**
 * Created by hangnadi on 3/9/17.
 */

public class DetailResCenter {
    private boolean success;
    private String messageError;
    private int errorCode;
    private AddressDomainModel address;
    private ShippingDomainModel shipping;
    private ButtonDomainModel button;
    private ResolutionDomainModel resolution;
    private ResolutionHistoryDomainModel resolutionHistory;
    private ProductDataDomainModel productData;
    private SolutionDomainModel solutionData;
    private NextActionDomain nextAction;

    public DetailResCenter() {

    }

    public DetailResCenter(AddressDomainModel address,
                           ShippingDomainModel shipping,
                           ButtonDomainModel button,
                           ResolutionDomainModel resolution,
                           ResolutionHistoryDomainModel resolutionHistory,
                           ProductDataDomainModel productData,
                           SolutionDomainModel solutionData,
                           NextActionDomain nextAction) {
        this.address = address;
        this.shipping = shipping;
        this.button = button;
        this.resolution = resolution;
        this.resolutionHistory = resolutionHistory;
        this.productData = productData;
        this.solutionData = solutionData;
        this.nextAction = nextAction;
    }

    public NextActionDomain getNextAction() {
        return nextAction;
    }

    public void setNextAction(NextActionDomain nextAction) {
        this.nextAction = nextAction;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessageError() {
        return messageError;
    }

    public void setMessageError(String messageError) {
        this.messageError = messageError;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public AddressDomainModel getAddress() {
        return address;
    }

    public void setAddress(AddressDomainModel address) {
        this.address = address;
    }

    public ShippingDomainModel getShipping() {
        return shipping;
    }

    public void setShipping(ShippingDomainModel shipping) {
        this.shipping = shipping;
    }

    public ButtonDomainModel getButton() {
        return button;
    }

    public void setButton(ButtonDomainModel button) {
        this.button = button;
    }

    public ResolutionDomainModel getResolution() {
        return resolution;
    }

    public void setResolution(ResolutionDomainModel resolution) {
        this.resolution = resolution;
    }

    public ResolutionHistoryDomainModel getResolutionHistory() {
        return resolutionHistory;
    }

    public void setResolutionHistory(ResolutionHistoryDomainModel resolutionHistory) {
        this.resolutionHistory = resolutionHistory;
    }

    public ProductDataDomainModel getProductData() {
        return productData;
    }

    public void setProductData(ProductDataDomainModel productData) {
        this.productData = productData;
    }

    public SolutionDomainModel getSolutionData() {
        return solutionData;
    }

    public void setSolutionData(SolutionDomainModel solutionData) {
        this.solutionData = solutionData;
    }
}
