package com.tokopedia.transaction.checkout.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.transaction.checkout.view.data.ShipmentRecipientModel;
import com.tokopedia.transaction.checkout.view.view.ICartAddressChoiceView;

import java.util.List;

/**
 * Created by Irfan Khoirul on 05/02/18.
 */

public interface ICartAddressChoicePresenter extends CustomerPresenter<ICartAddressChoiceView> {

    void loadAddresses();

    List<ShipmentRecipientModel> getRecipientAddresses();

    void setSelectedRecipientAddress(ShipmentRecipientModel model);

    ShipmentRecipientModel getSelectedRecipientAddress();
}
