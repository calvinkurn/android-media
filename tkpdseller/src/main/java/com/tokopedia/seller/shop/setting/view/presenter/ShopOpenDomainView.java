package com.tokopedia.seller.shop.setting.view.presenter;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.app.BaseDiView;

/**
 * Created by sebastianuskh on 3/17/17.
 */

public interface ShopOpenDomainView extends CustomerView {
    void setDomainCheckResult(boolean existed);
    void setShopCheckResult(boolean existed);
}
