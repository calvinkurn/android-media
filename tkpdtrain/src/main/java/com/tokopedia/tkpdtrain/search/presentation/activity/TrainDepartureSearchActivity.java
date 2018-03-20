package com.tokopedia.tkpdtrain.search.presentation.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.tkpdtrain.homepage.presentation.model.TrainSearchPassDataViewModel;
import com.tokopedia.tkpdtrain.search.presentation.fragment.TrainDepartureSearchFragment;

/**
 * Created by nabillasabbaha on 3/19/18.
 */

public class TrainDepartureSearchActivity extends TrainSearchActivity {

    public static Intent getCallingIntent(Activity activity, TrainSearchPassDataViewModel viewModel) {
        Intent intent = new Intent(activity, TrainDepartureSearchActivity.class);
        intent.putExtra(EXTRA_SEARCH_PASS_DATA, viewModel);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return TrainDepartureSearchFragment.newInstance(trainSearchPassDataViewModel);
    }

    @Override
    protected String getTitleTrainToolbar() {
        return "Perjalanan Pergi";
    }

    @Override
    protected String getDepartureDate() {
        return trainSearchPassDataViewModel.getDepartureDate();
    }
}
