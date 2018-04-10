package com.tokopedia.transaction.purchase.listener;

import android.view.View;

import com.tokopedia.transaction.common.base.IBaseView;
import com.tokopedia.transaction.purchase.model.ConfirmationData;
import com.tokopedia.transaction.purchase.model.response.formconfirmpayment.FormConfPaymentData;
import com.tokopedia.transaction.purchase.model.response.formconfirmpayment.FormEditPaymentData;

/**
 * @author Angga.Prasetiyo on 20/06/2016.
 */
public interface ConfirmPaymentViewListener extends IBaseView {
    void renderFormConfirmation(FormConfPaymentData data);

    void renderErrorPaymentMethod(String message);

    void renderErrorSysBank(String message);

    void renderErrorAccountBank(String message);

    void renderErrorAccountName(String message);

    void renderErrorAccountNumber(String message);

    void renderErrorAccountBranch(String message);

    void renderErrorChooseBank(String message);

    void renderErrotDepositorName(String message);

    void renderErrorDepositorPassword(String message);

    void renderErrorPaymentAmount(String message);

    void renderErrorDate(String message);

    void requestFocusError(View view);

    void renderConfirmationSuccess(ConfirmationData data);

    void renderFormEdit(FormEditPaymentData data);

    void renderConfirmationError(String errorMsg);

    void renderConfirmationTimeout(String errorMsg);

    void renderEditTimeout(String message);

    void renderConfirmationNoConnection(String message);

    void renderErrorFormConfirmation(String message);

    void renderNoConnectionFormConfirmation(String message);

    void renderErrorFormEdit(String message);

    void renderNoConnectionFormEdit(String message);
}
