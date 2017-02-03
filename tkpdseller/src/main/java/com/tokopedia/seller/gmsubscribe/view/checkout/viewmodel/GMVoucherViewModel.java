package com.tokopedia.seller.gmsubscribe.view.checkout.viewmodel;

import com.tokopedia.seller.gmsubscribe.domain.cart.model.GMVoucherCheckDomainModel;

/**
 * Created by sebastianuskh on 1/31/17.
 */
public class GMVoucherViewModel {
    private boolean success;
    private String message;

    public static GMVoucherViewModel mapFromDomain(GMVoucherCheckDomainModel gmVoucherCheckDomainModel) {
        GMVoucherViewModel viewModel = new GMVoucherViewModel();
        viewModel.setSuccess(true);
        viewModel.setMessage(gmVoucherCheckDomainModel.getMessage());
        return viewModel;
    }

    public static GMVoucherViewModel generateClassWithError(String message) {
        GMVoucherViewModel viewModel = new GMVoucherViewModel();
        viewModel.setSuccess(false);
        viewModel.setMessage(message);
        return viewModel;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }



}
