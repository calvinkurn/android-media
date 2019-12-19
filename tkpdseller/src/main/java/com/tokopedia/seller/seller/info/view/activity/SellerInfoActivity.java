package com.tokopedia.seller.seller.info.view.activity;

import androidx.fragment.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.seller.info.view.fragment.SellerInfoFragment;

/**
 * Created by normansyahputa on 11/30/17.
 */
@DeepLink(ApplinkConst.SELLER_INFO)
public class SellerInfoActivity extends BaseSimpleActivity implements HasComponent<AppComponent> {

    @Override
    protected Fragment getNewFragment() {
        return SellerInfoFragment.newInstance();
    }

    @Override
    public AppComponent getComponent() {
        return getApplicationComponent();
    }
}
