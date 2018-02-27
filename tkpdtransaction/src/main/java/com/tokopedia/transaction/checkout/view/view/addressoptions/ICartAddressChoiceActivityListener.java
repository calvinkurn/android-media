package com.tokopedia.transaction.checkout.view.view.addressoptions;

import com.tokopedia.transaction.checkout.view.data.RecipientAddressModel;

/**
 * @author anggaprasetiyo on 26/02/18.
 */

public interface ICartAddressChoiceActivityListener {

    void finishSendResultActionSelectedAddress(RecipientAddressModel selectedAddressResult);

    void finishSendResultActionToMultipleAddressForm();
}
