package com.tokopedia.transaction.checkout.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.transaction.checkout.view.data.ShipmentRecipientModel;
import com.tokopedia.transaction.checkout.view.view.ICartAddressChoiceView;

/**
 * Created by Irfan Khoirul on 05/02/18.
 */

public interface ICartAddressChoicePresenter extends CustomerPresenter<ICartAddressChoiceView> {

    void setRecipientAddress(ShipmentRecipientModel recipientAddress);

    ShipmentRecipientModel getRecipientAddress();
}
