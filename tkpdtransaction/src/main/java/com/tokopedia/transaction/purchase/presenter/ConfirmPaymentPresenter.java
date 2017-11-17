package com.tokopedia.transaction.purchase.presenter;

import android.content.Context;

import com.tokopedia.transaction.purchase.activity.ConfirmPaymentActivity;
import com.tokopedia.transaction.purchase.model.ConfirmPaymentData;
import com.tokopedia.transaction.purchase.model.ConfirmationData;
import com.tokopedia.transaction.purchase.model.response.formconfirmpayment.Form;
import com.tokopedia.transaction.purchase.model.response.formconfirmpayment.FormEdit;

/**
 * @author Angga.Prasetiyo on 20/06/2016.
 */
public interface ConfirmPaymentPresenter {
    void processChooseBank(Context context,
                           ConfirmPaymentActivity.OnNewAccountBankSelected listener);

    void processGetEditPaymentForm(Context context, String confirmationId);

    void processGetConfirmPaymentForm(Context context, String confirmationId);

    void processSubmitConfirmation(Context context, ConfirmPaymentData data,
                                   Form formData, FormEdit formEditData);

    void onDestroyView();

}
