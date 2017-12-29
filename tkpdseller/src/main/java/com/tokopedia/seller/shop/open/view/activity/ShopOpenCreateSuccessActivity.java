package com.tokopedia.seller.shop.open.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.shop.open.view.fragment.ShopOpenCreateSuccessFragment;

/**
 * Created by nakama on 28/12/17.
 */

public class ShopOpenCreateSuccessActivity extends BaseSimpleActivity {

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, ShopOpenCreateSuccessActivity.class);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return ShopOpenCreateSuccessFragment.newInstance();
    }

}
