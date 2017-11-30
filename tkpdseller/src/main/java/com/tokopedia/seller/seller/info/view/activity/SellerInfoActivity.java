package com.tokopedia.seller.seller.info.view.activity;

import android.support.v4.app.Fragment;

import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.seller.info.view.fragment.SellerInfoFragment;

/**
 * Created by normansyahputa on 11/30/17.
 */

public class SellerInfoActivity extends BaseSimpleActivity {
    @Override
    protected Fragment getNewFragment() {
        return SellerInfoFragment.newInstance();
    }
}
