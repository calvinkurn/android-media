package com.tokopedia.tkpdtrain.search.presentation;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.tkpdtrain.common.di.utils.TrainComponentUtils;
import com.tokopedia.tkpdtrain.common.presentation.TrainBaseActivity;
import com.tokopedia.tkpdtrain.homepage.presentation.model.TrainSearchPassDataViewModel;
import com.tokopedia.tkpdtrain.search.di.DaggerTrainSearchComponent;
import com.tokopedia.tkpdtrain.search.di.TrainSearchComponent;

/**
 * @author by alvarisi on 3/8/18.
 */

public class TrainSearchActivity extends TrainBaseActivity implements HasComponent<TrainSearchComponent> {
    private static final String EXTRA_SEARCH_PASS_DATA = "EXTRA_SEARCH_PASS_DATA";
    private TrainSearchComponent trainSearchComponent;

    public static Intent getCallingIntent(Activity activity, TrainSearchPassDataViewModel viewModel) {
        Intent intent = new Intent(activity, TrainSearchActivity.class);
        intent.putExtra(EXTRA_SEARCH_PASS_DATA, viewModel);
        return intent;
    }

    public static Intent getCallingIntent(Activity activity) {
        Intent intent = new Intent(activity, TrainSearchActivity.class);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return TrainSearchFragment.newInstance();
    }

    @Override
    public TrainSearchComponent getComponent() {
        if (trainSearchComponent == null) {
            trainSearchComponent = DaggerTrainSearchComponent.builder()
                    .trainComponent(TrainComponentUtils.getTrainComponent(getApplication())).build();
        }
        return trainSearchComponent;
    }
}
