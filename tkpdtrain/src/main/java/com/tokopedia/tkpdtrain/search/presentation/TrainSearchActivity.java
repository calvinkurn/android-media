package com.tokopedia.tkpdtrain.search.presentation;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.tkpdtrain.common.presentation.TrainBaseActivity;
import com.tokopedia.tkpdtrain.homepage.presentation.model.TrainSearchPassDataViewModel;

/**
 * @author by alvarisi on 3/8/18.
 */

public class TrainSearchActivity extends TrainBaseActivity {
    private static final String EXTRA_SEARCH_PASS_DATA = "EXTRA_SEARCH_PASS_DATA";

    public static Intent getCallingIntent(Activity activity, TrainSearchPassDataViewModel viewModel) {
        Intent intent = new Intent(activity, TrainSearchActivity.class);
        intent.putExtra(EXTRA_SEARCH_PASS_DATA, viewModel);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return TrainSearchFragment.newInstance();
    }
}
