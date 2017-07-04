package com.tokopedia.seller.product.draft.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tokopedia.core.app.TkpdActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.draft.view.fragment.ProductDraftListFragment;

/**
 * Created by User on 6/19/2017.
 */

public class ProductDraftListActivity extends TkpdActivity implements HasComponent<AppComponent>{
    public static final String TAG = ProductDraftListActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_simple_fragment);
        drawer.setDrawerPosition(TkpdState.DrawerPosition.DRAFT_PRODUCT);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, ProductDraftListFragment.newInstance(),
                            ProductDraftListFragment.TAG)
                    .commit();
        }
    }

    @Override
    public AppComponent getComponent() {
        return getApplicationComponent();
    }

}
