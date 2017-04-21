package com.tokopedia.seller.product.view.activity;

import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.product.activity.ProductInfoActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.view.dialog.TextPickerDialogListener;
import com.tokopedia.seller.product.view.fragment.ProductAddFragment;
import com.tokopedia.seller.product.view.service.AddProductService;

/**
 * Created by nathan on 4/3/17.
 */

public class ProductAddActivity extends TActivity
        implements HasComponent<AppComponent>,
        TextPickerDialogListener,
        ProductAddFragment.Listener{
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
    public void onTextPickerSubmitted(String newEtalaseName) {

    }

    @Override
    public void startUploadProduct(long productId) {
        startService(AddProductService.getIntent(this, productId));
        finish();
    }

    @Override
    public void startUploadProductWithShare(long productId) {
        startService(AddProductService.getIntent(this, productId));
        startActivity(ProductInfoActivity.createInstance(this));
        finish();
    }
}