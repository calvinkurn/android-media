package com.tokopedia.seller.product.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.constant.ProductExtraConstant;
import com.tokopedia.seller.product.di.component.DaggerProductScoringComponent;
import com.tokopedia.seller.product.di.component.ProductScoringComponent;
import com.tokopedia.seller.product.di.module.ProductScoringModule;
import com.tokopedia.seller.product.view.fragment.ProductAddFragment;
import com.tokopedia.seller.product.view.fragment.ProductScoringDetailFragment;
import com.tokopedia.seller.product.view.model.scoringproduct.ValueIndicatorScoreModel;

/**
 * Created by zulfikarrahman on 4/12/17.
 */

public class ProductScoringDetailActivity extends TActivity implements HasComponent<AppComponent> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_detail_product_score);

        ValueIndicatorScoreModel valueIndicatorScoreModel = getIntent().getParcelableExtra(ProductExtraConstant.VALUE_PRODUCT_SCORING_EXTRA);

        getSupportFragmentManager().beginTransaction().disallowAddToBackStack()
                .replace(R.id.container, ProductScoringDetailFragment.createInstance(valueIndicatorScoreModel),
                        ProductScoringDetailFragment.class.getSimpleName())
                .commit();
    }



    public static Intent createIntent(Context context, ValueIndicatorScoreModel valueIndicatorScoreModel) {
        Intent intent = new Intent(context, ProductScoringDetailActivity.class);
        intent.putExtra(ProductExtraConstant.VALUE_PRODUCT_SCORING_EXTRA, valueIndicatorScoreModel);
        return intent;
    }

    @Override
    public AppComponent getComponent() {
        return getApplicationComponent();
    }
}
