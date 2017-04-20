package com.tokopedia.seller.product.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.view.dialog.AddWholeSaleDialog;
import com.tokopedia.seller.product.view.dialog.TextPickerDialogListener;
import com.tokopedia.seller.product.view.fragment.ProductAddFragment;
import com.tokopedia.seller.product.view.holder.ProductDetailViewHolder;
import com.tokopedia.seller.product.view.model.wholesale.WholesaleModel;

/**
 * Created by nathan on 4/3/17.
 */

public class ProductAddActivity extends TActivity
        implements HasComponent<AppComponent>, TextPickerDialogListener
        , AddWholeSaleDialog.WholeSaleDialogListener, ProductDetailViewHolder.Listener {
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
    public void addWholesaleItem(WholesaleModel item) {
        if (getProductAddFragment() != null && getProductAddFragment().isVisible()) {
            getProductAddFragment().addWholesaleItem(item);
        }
    }

    public ProductAddFragment getProductAddFragment() {
        Fragment fragmentByTag = getSupportFragmentManager().findFragmentByTag(ProductAddFragment.class.getSimpleName());
        if (fragmentByTag != null && fragmentByTag instanceof ProductAddFragment) {
            return (ProductAddFragment) fragmentByTag;
        }
        return null;
    }

    @Override
    public void startAddWholeSaleDialog(WholesaleModel baseValue) {
        AddWholeSaleDialog addWholeSaleDialog = AddWholeSaleDialog.newInstance(baseValue);
        addWholeSaleDialog.show(getSupportFragmentManager(), AddWholeSaleDialog.TAG);
    }
}