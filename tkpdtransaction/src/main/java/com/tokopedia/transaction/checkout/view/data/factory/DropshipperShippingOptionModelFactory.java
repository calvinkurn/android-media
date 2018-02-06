package com.tokopedia.transaction.checkout.view.data.factory;

import com.tokopedia.transaction.checkout.view.data.DropshipperShippingOptionModel;

/**
 * @author Aghny A. Putra on 31/01/18
 */

public class DropshipperShippingOptionModelFactory {

    public static DropshipperShippingOptionModel getDummyDropshippingOptionModel() {
        return createDropshipperShippingOptionModel(false);
    }

    public static DropshipperShippingOptionModel createDropshipperShippingOptionModel(boolean isDropshipping) {
        DropshipperShippingOptionModel dropshipperShippingOptionModel = new DropshipperShippingOptionModel();
        dropshipperShippingOptionModel.setDropshipping(isDropshipping);

        return dropshipperShippingOptionModel;
    }

}
