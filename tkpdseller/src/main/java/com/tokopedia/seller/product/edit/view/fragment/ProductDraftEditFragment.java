package com.tokopedia.seller.product.edit.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.seller.R;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;
import com.tokopedia.seller.product.edit.view.model.upload.ProductPhotoListViewModel;
import com.tokopedia.seller.product.edit.view.model.upload.ProductWholesaleViewModel;
import com.tokopedia.seller.product.edit.view.model.upload.intdef.ProductStatus;

import java.util.List;

/**
 * Created by zulfikarrahman on 4/27/17.
 */

public class ProductDraftEditFragment extends ProductDraftAddFragment {

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
    public void onSuccessLoadDraftProduct(ProductViewModel model) {
        hideLoading();
        productId = String.valueOf(model.getProductId());
        if (!model.isProductNameEditable()) {
            productInfoViewHolder.setNameEnabled(false);
        }
        super.onSuccessLoadDraftProduct(model);
    }

    @Override
    protected void getCategoryRecommendation(String productName) {
        // Do nothing
    }

    @Override
    protected ProductViewModel collectDataFromView() {
        ProductViewModel viewModel = super.collectDataFromView();
        viewModel.setProductId(Long.parseLong(productId));
        return viewModel;
    }
}
