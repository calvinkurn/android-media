package com.tokopedia.transaction.checkout.view.view;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.transaction.checkout.view.data.ShipmentRecipientModel;

import java.util.List;

/**
 * Created by Irfan Khoirul on 05/02/18.
 */

public interface ICartAddressChoiceView extends CustomerView {
    void renderRecipientData();
}
