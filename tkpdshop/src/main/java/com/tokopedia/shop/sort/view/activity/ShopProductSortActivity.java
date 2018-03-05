package com.tokopedia.shop.sort.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.shop.ShopComponentInstance;
import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.sort.view.fragment.ShopProductSortFragment;
import com.tokopedia.shop.sort.view.listener.ShopProductSortFragmentListener;

/**
 * Created by normansyahputa on 2/23/18.
 */

public class ShopProductSortActivity extends BaseSimpleActivity implements HasComponent<ShopComponent>, ShopProductSortFragmentListener {
    public static final String SORT_NAME = "SORT_NAME";
    public static final String SORT_ID = "SORT_ID";
    private ShopComponent component;
    private String sortName;

    public static Intent createIntent(Context context, String sortName){
           Intent intent = new Intent(context, ShopProductSortActivity.class);
           intent.putExtra(SORT_NAME, sortName);
           return intent;

    }

    @Override
    protected Fragment getNewFragment() {
        return ShopProductSortFragment.createInstance(sortName);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(getIntent() != null && savedInstanceState == null){
            sortName = getIntent().getStringExtra(SORT_NAME);

            if (sortName.equalsIgnoreCase(Integer.toString(Integer.MIN_VALUE))) {
                sortName = null;
            }
        }
        super.onCreate(savedInstanceState);
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
