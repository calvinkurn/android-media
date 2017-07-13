package com.tokopedia.seller.selling.appwidget.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.seller.selling.appwidget.view.GetNewOrderView;

/**
 * Created by zulfikarrahman on 7/12/17.
 */

public abstract class GetNewOrderPresenter extends BaseDaggerPresenter<GetNewOrderView> {
    public abstract void getNewOrder();

    public abstract void unSubscribe();
}
