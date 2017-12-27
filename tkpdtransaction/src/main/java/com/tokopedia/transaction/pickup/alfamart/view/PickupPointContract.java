package com.tokopedia.transaction.pickup.alfamart.view;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;

/**
 * Created by Irfan Khoirul on 22/12/17.
 */

public interface PickupPointContract {

    interface View extends CustomerView {

    }

    interface Presenter extends CustomerPresenter<View> {

    }

    interface Constant {
        String INTENT_DATA_PARAMS = "params";
    }
}
