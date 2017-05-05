package com.tokopedia.seller.product.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.constant.ProductExtraConstant;
import com.tokopedia.seller.product.di.component.DaggerProductScoringComponent;
import com.tokopedia.seller.product.di.component.ProductScoringComponent;
import com.tokopedia.seller.product.di.module.ProductScoringModule;
import com.tokopedia.seller.product.view.fragment.CategoryPickerFragment;
import com.tokopedia.seller.product.view.fragment.ProductAddFragment;
import com.tokopedia.seller.product.view.fragment.ProductScoringDetailFragment;
import com.tokopedia.seller.product.view.model.scoringproduct.ValueIndicatorScoreModel;

/**
 * Created by zulfikarrahman on 4/12/17.
 */

public class ProductScoringDetailActivity extends TActivity implements HasComponent<AppComponent> {

    public static final String TAG = ProductScoringDetailFragment.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_detail_product_score);

        ValueIndicatorScoreModel valueIndicatorScoreModel = getIntent().getParcelableExtra(ProductExtraConstant.VALUE_PRODUCT_SCORING_EXTRA);

        inflateCategoryFragment(valueIndicatorScoreModel);
    }

    private void inflateCategoryFragment(ValueIndicatorScoreModel valueIndicatorScoreModel) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG);
        if (fragment == null) {
            fragment = ProductScoringDetailFragment.createInstance(valueIndicatorScoreModel);
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment, TAG);
        fragmentTransaction.commit();
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
