package com.tokopedia.seller.transaction.neworder.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.seller.transaction.neworder.view.appwidget.GetNewOrderView;

/**
 * Created by zulfikarrahman on 7/12/17.
 */

public abstract class GetNewOrderPresenter extends BaseDaggerPresenter<GetNewOrderView> {
    public abstract void getNewOrderAndCount();

    public abstract void getNewOrderAndCountAsync();

    public abstract void unSubscribe();
}
