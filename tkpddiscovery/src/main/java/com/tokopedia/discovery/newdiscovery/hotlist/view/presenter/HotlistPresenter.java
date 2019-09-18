package com.tokopedia.discovery.newdiscovery.hotlist.view.presenter;

import com.tokopedia.discovery.newdiscovery.base.DiscoveryPresenter;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetProductUseCase;
import com.tokopedia.discovery.newdiscovery.hotlist.view.activity.HotlistActivity;

/**
 * Created by hangnadi on 9/28/17.
 */

public class HotlistPresenter extends DiscoveryPresenter<HotlistContract.View, HotlistActivity>
        implements HotlistContract.Presenter {

    public HotlistPresenter(GetProductUseCase getProductUseCase) {
        super(getProductUseCase);
    }

}
