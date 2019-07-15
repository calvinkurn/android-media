package com.tokopedia.seller.seller.info.view.activity;

import android.support.v4.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.webview.BaseSessionWebViewFragment;

/**
 * @author okasurya on 7/28/18.
 */
@DeepLink(ApplinkConst.SELLER_CENTER)
public class SellerCenterActivity extends BaseSimpleActivity {
    private static final String SELLER_CENTER_URL = "https://seller.tokopedia.com/edu/";

    @Override
    protected Fragment getNewFragment() {
        return BaseSessionWebViewFragment.newInstance(SELLER_CENTER_URL);
    }
}