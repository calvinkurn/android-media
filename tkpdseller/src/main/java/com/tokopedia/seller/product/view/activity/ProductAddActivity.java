package com.tokopedia.seller.product.view.activity;

import android.os.Bundle;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.view.dialog.YoutubeAddUrlDialogListener;
import com.tokopedia.seller.product.view.fragment.ProductAddFragment;

/**
 * Created by nathan on 4/3/17.
 */

public class ProductAddActivity extends TActivity
        implements HasComponent<AppComponent>, YoutubeAddUrlDialogListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_product_add);
        getSupportFragmentManager().beginTransaction().disallowAddToBackStack()
                .add(R.id.container, ProductAddFragment.createInstance(), ProductAddFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public AppComponent getComponent() {
        return getApplicationComponent();
    }

    @Override
    public void addYoutubeUrl(String youtubeUrl) {

    }

    public void openAddYoutubeDialog() {

    }
}