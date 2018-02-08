package com.tokopedia.seller.product.edit.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.product.edit.constant.ProductExtraConstant;
import com.tokopedia.seller.product.edit.view.fragment.ProductScoringDetailFragment;
import com.tokopedia.seller.product.edit.view.model.scoringproduct.ValueIndicatorScoreModel;

/**
 * Created by zulfikarrahman on 4/12/17.
 */

public class ProductScoringDetailActivity extends BaseSimpleActivity implements HasComponent<ProductComponent> {

    public static Intent createIntent(Context context, ValueIndicatorScoreModel valueIndicatorScoreModel) {
        Intent intent = new Intent(context, ProductScoringDetailActivity.class);
        intent.putExtra(ProductExtraConstant.VALUE_PRODUCT_SCORING_EXTRA, valueIndicatorScoreModel);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        ValueIndicatorScoreModel valueIndicatorScoreModel = getIntent().getParcelableExtra(ProductExtraConstant.VALUE_PRODUCT_SCORING_EXTRA);
        return ProductScoringDetailFragment.createInstance(valueIndicatorScoreModel);
    }

    @Override
    public ProductComponent getComponent() {
        return ((SellerModuleRouter) getApplication()).getProductComponent();
    }

    @Override
    protected boolean isToolbarWhite() {
        return true;
    }
}