package com.tokopedia.shop.address.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.shop.ShopComponentInstance;
import com.tokopedia.shop.address.view.fragment.ShopAddressListFragment;
import com.tokopedia.shop.common.constant.ShopParamConstant;
import com.tokopedia.shop.common.di.component.ShopComponent;

/**
 * Created by normansyahputa on 2/8/18.
 */

public class ShopAddressListActivity extends BaseSimpleActivity implements HasComponent<ShopComponent> {

    private String shopId;

    public static Intent createIntent(Context context, String shopId) {
        Intent intent = new Intent(context, ShopAddressListActivity.class);
        intent.putExtra(ShopParamConstant.SHOP_ID, shopId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        shopId = getIntent().getStringExtra(ShopParamConstant.SHOP_ID);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment getNewFragment() {
        return ShopAddressListFragment.createInstance(shopId);
    }

    @Override
    public ShopComponent getComponent() {
        return ShopComponentInstance.getComponent(getApplication());
    }
}
