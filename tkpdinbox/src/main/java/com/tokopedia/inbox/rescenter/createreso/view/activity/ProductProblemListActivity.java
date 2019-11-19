package com.tokopedia.inbox.rescenter.createreso.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.inbox.rescenter.createreso.view.fragment.ChooseProductAndProblemFragment;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ComplaintResult;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ProductProblemListViewModel;

import java.util.ArrayList;

/**
 * Created by yoasfs on 14/08/17.
 */

public class ProductProblemListActivity extends BaseSimpleActivity {

    private static final String KEY_PARAM_PASS_DATA = "pass_data";
    public static final String PROBLEM_RESULT_LIST_DATA = "problem_result_list_data";

    public static Intent getInstance(Context context,
                                     ProductProblemListViewModel productProblemListViewModel,
                                     ArrayList<ComplaintResult> complaintResults) {
        Intent intent = new Intent(context, ProductProblemListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_PARAM_PASS_DATA, productProblemListViewModel);
        bundle.putParcelableArrayList(PROBLEM_RESULT_LIST_DATA, complaintResults);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return ChooseProductAndProblemFragment.newInstance(
                (ProductProblemListViewModel) getIntent().getExtras().getParcelable(KEY_PARAM_PASS_DATA),
                getIntent().getExtras().getParcelableArrayList(PROBLEM_RESULT_LIST_DATA));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
