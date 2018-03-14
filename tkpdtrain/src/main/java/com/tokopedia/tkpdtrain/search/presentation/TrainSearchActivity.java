package com.tokopedia.tkpdtrain.search.presentation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.design.R;
import com.tokopedia.tkpdtrain.common.di.utils.TrainComponentUtils;
import com.tokopedia.tkpdtrain.common.presentation.TrainBaseActivity;
import com.tokopedia.tkpdtrain.common.util.TrainDateUtil;
import com.tokopedia.tkpdtrain.homepage.presentation.model.TrainSearchPassDataViewModel;
import com.tokopedia.tkpdtrain.search.di.DaggerTrainSearchComponent;
import com.tokopedia.tkpdtrain.search.di.TrainSearchComponent;

/**
 * @author by alvarisi on 3/8/18.
 */

public class TrainSearchActivity extends TrainBaseActivity implements HasComponent<TrainSearchComponent> {
    public static final String EXTRA_SEARCH_PASS_DATA = "EXTRA_SEARCH_PASS_DATA";
    private TrainSearchPassDataViewModel trainSearchPassDataViewModel;
    private TrainSearchComponent trainSearchComponent;
    private String dateString;
    private String adultPassanger;
    private String infantPassanger;

    public static Intent getCallingIntent(Activity activity, TrainSearchPassDataViewModel viewModel) {
        Intent intent = new Intent(activity, TrainSearchActivity.class);
        intent.putExtra(EXTRA_SEARCH_PASS_DATA, viewModel);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initializeDataFromIntent();
        super.onCreate(savedInstanceState);
        setupTrainToolbar();
    }

    @Override
    protected Fragment getNewFragment() {
        return TrainSearchFragment.newInstance(trainSearchPassDataViewModel);
    }

    @Override
    public TrainSearchComponent getComponent() {
        if (trainSearchComponent == null) {
            trainSearchComponent = DaggerTrainSearchComponent.builder()
                    .trainComponent(TrainComponentUtils.getTrainComponent(getApplication())).build();
        }
        return trainSearchComponent;
    }

    private void initializeDataFromIntent() {
        trainSearchPassDataViewModel = getIntent().getParcelableExtra(EXTRA_SEARCH_PASS_DATA);
        dateString = TrainDateUtil.formatDate(TrainDateUtil.DEFAULT_FORMAT,
                TrainDateUtil.DEFAULT_VIEW_LOCAL_DETAIL, trainSearchPassDataViewModel.getDepartureDate());
        adultPassanger = trainSearchPassDataViewModel.getAdult() + " Dewasa";
        infantPassanger = trainSearchPassDataViewModel.getInfant() + " Infant";
    }

    private void setupTrainToolbar() {
        toolbar.setContentInsetStartWithNavigation(0);
        toolbar.setSubtitleTextColor(ContextCompat.getColor(this, R.color.grey_500));
        String title = "Perjalanan Pergi";
        String subtitle = dateString + " | " + adultPassanger + ", " + infantPassanger;
        updateTitle(title, subtitle);
    }
}
