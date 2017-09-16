package com.tokopedia.gm.featured.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.gm.featured.view.listener.FeaturedProductView;

/**
 * Created by normansyahputa on 9/7/17.
 */

public abstract class FeaturedProductPresenter extends BaseDaggerPresenter<FeaturedProductView> {
    public abstract void loadData();

    public abstract void postData(RequestParams requestParams);
}
