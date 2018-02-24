package com.tokopedia.shop.product.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.shop.ShopComponentInstance;
import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.product.view.fragment.ShopProductFilterFragment;
import com.tokopedia.shop.product.view.listener.ShopProductFilterFragmentListener;

/**
 * Created by normansyahputa on 2/23/18.
 */

public class ShopProductFilterActivity extends BaseSimpleActivity implements HasComponent<ShopComponent>, ShopProductFilterFragmentListener {
    private ShopComponent component;
    private String sortName;
    public static final String SORT_NAME = "SORT_NAME";
    public static final String SORT_ID = "SORT_ID";

    @Override
    protected Fragment getNewFragment() {
        return new ShopProductFilterFragment();
    }

    @Override
    public ShopComponent getComponent() {
        if (component == null) {
            component = ShopComponentInstance.getComponent(getApplication());
        }
        return component;
    }

    @Override
    public void select(String sortId, String sortName) {
        Intent intent = new Intent();
        intent.putExtra(SORT_ID, sortId);
        intent.putExtra(SORT_NAME, sortName);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
