package com.tokopedia.inbox.rescenter.createreso.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.inbox.rescenter.createreso.view.fragment.ProductProblemDetailFragment;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ComplaintResult;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ProductProblemViewModel;

/**
 * Created by yoasfs on 14/08/17.
 */

public class ProductProblemDetailActivity extends BaseSimpleActivity {

    public static final String PRODUCT_PROBLEM_DATA = "product_problem_data";
    public static final String PROBLEM_RESULT_DATA = "problem_result_data";

    public static Intent getInstance(Context context, ProductProblemViewModel productProblemViewModel, ComplaintResult problemResult) {
        Intent intent = new Intent(context, ProductProblemDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(PRODUCT_PROBLEM_DATA, productProblemViewModel);
        bundle.putParcelable(PROBLEM_RESULT_DATA, problemResult);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return ProductProblemDetailFragment.newInstance(
                (ProductProblemViewModel) getIntent().getExtras().getParcelable(PRODUCT_PROBLEM_DATA),
                (ComplaintResult) getIntent().getExtras().getParcelable(PROBLEM_RESULT_DATA));
    }



}
