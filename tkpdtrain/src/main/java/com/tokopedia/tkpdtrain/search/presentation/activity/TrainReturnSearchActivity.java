package com.tokopedia.tkpdtrain.search.presentation.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.tkpdtrain.homepage.presentation.model.TrainSearchPassDataViewModel;
import com.tokopedia.tkpdtrain.search.presentation.fragment.TrainReturnSearchFragment;

/**
 * Created by nabillasabbaha on 3/19/18.
 */

public class TrainReturnSearchActivity extends TrainSearchActivity {

    public static final String EXTRA_SEARCH_ID_SCHEDULE = "id_schedule";

    public static Intent getCallingIntent(Activity activity, TrainSearchPassDataViewModel viewModel, String idSchedule) {
        Intent intent = new Intent(activity, TrainReturnSearchActivity.class);
        intent.putExtra(EXTRA_SEARCH_PASS_DATA, viewModel);
        intent.putExtra(EXTRA_SEARCH_ID_SCHEDULE, idSchedule);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return TrainReturnSearchFragment.newInstance(trainSearchPassDataViewModel,
                getIntent().getStringExtra(EXTRA_SEARCH_ID_SCHEDULE));
    }

    @Override
    protected String getTitleTrainToolbar() {
        return "Perjalanan Pulang";
    }

    @Override
    protected String getDepartureDate() {
        return trainSearchPassDataViewModel.getReturnDate();
    }
}
