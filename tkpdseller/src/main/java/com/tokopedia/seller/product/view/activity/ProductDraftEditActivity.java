package com.tokopedia.seller.product.view.activity;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.base.utils.StringUtils;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.view.fragment.ProductDraftAddFragment;
import com.tokopedia.seller.product.view.fragment.ProductDraftEditFragment;
import com.tokopedia.seller.product.view.model.upload.ImageProductInputViewModel;
import com.tokopedia.seller.product.view.model.upload.ProductPhotoListViewModel;
import com.tokopedia.seller.product.view.model.upload.UploadProductInputViewModel;
import com.tokopedia.seller.product.view.presenter.ProductEditPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 4/27/17.
 */

public class ProductDraftEditActivity extends ProductDraftAddActivity  {

    public static Intent createInstance(Context context, String productId){
        Intent intent = new Intent(context, ProductDraftEditActivity.class);
        intent.putExtra(PRODUCT_DRAFT_ID, productId);
        return intent;
    }

    @Override
    protected void setupFragment() {
        inflateView(R.layout.activity_product_add);
        String productId = getIntent().getStringExtra(PRODUCT_DRAFT_ID);
        if (StringUtils.isBlank(productId)){
            throw new RuntimeException("Product id is not selected");
        }
        getSupportFragmentManager().beginTransaction().disallowAddToBackStack()
                .replace(R.id.container, ProductDraftEditFragment.createInstance(productId), ProductDraftEditFragment.class.getSimpleName())
                .commit();
    }

}
