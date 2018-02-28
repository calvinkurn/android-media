package com.tokopedia.seller.product.edit.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.seller.R;
import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.product.edit.di.component.DaggerProductDraftComponent;
import com.tokopedia.seller.product.edit.di.module.ProductDraftModule;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;
import com.tokopedia.seller.product.edit.view.model.upload.intdef.ProductStatus;
import com.tokopedia.seller.product.edit.view.presenter.ProductDraftPresenter;

/**
 * Created by zulfikarrahman on 4/27/17.
 */

public class ProductDraftEditFragment extends BaseProductDraftAddEditFragment<ProductDraftPresenter> {

    public static final String SAVED_PRODUCT_ID = "svd_prd_id";

    private String productId;

    public static Fragment createInstance(String productDraftId) {
        ProductDraftEditFragment fragment = new ProductDraftEditFragment();
        Bundle args = new Bundle();
        args.putString(DRAFT_PRODUCT_ID, productDraftId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getStatusUpload() {
        return ProductStatus.EDIT;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            productId = savedInstanceState.getString(SAVED_PRODUCT_ID);
        }
    }

    @Override
    public void initInjector() {
        DaggerProductDraftComponent
                .builder()
                .productComponent(getComponent(ProductComponent.class))
                .productDraftModule(new ProductDraftModule())
                .build()
                .inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view != null) {
            view.findViewById(R.id.button_save_and_add).setVisibility(View.GONE);
            view.findViewById(R.id.label_switch_share).setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void onSuccessLoadProduct(ProductViewModel model) {
        hideLoading();
        productId = String.valueOf(model.getProductId());
        if (!model.isProductNameEditable()) {
            productInfoViewHolder.setNameEnabled(false);
        }
        super.onSuccessLoadProduct(model);
    }

    @Override
    protected ProductViewModel collectDataFromView() {
        ProductViewModel viewModel = super.collectDataFromView();
        viewModel.setProductId(productId);
        return viewModel;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SAVED_PRODUCT_ID, productId);
    }
}
