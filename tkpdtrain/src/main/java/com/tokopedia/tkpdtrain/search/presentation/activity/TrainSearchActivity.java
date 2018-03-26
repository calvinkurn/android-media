package com.tokopedia.tkpdtrain.search.presentation.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.tokopedia.design.R;
import com.tokopedia.tkpdtrain.common.presentation.TrainBaseActivity;
import com.tokopedia.tkpdtrain.common.util.TrainDateUtil;
import com.tokopedia.tkpdtrain.homepage.presentation.model.TrainSearchPassDataViewModel;

/**
 * @author by alvarisi on 3/8/18.
 */

public abstract class TrainSearchActivity extends TrainBaseActivity {
    public static final String EXTRA_SEARCH_PASS_DATA = "EXTRA_SEARCH_PASS_DATA";
    protected TrainSearchPassDataViewModel trainSearchPassDataViewModel;
    private String dateString;
    private String adultPassanger;
    private String infantPassanger;
    private int infantTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initializeDataFromIntent();
        super.onCreate(savedInstanceState);
        setupTrainToolbar();
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    protected void initializeDataFromIntent() {
        trainSearchPassDataViewModel = getIntent().getParcelableExtra(EXTRA_SEARCH_PASS_DATA);
        dateString = TrainDateUtil.formatDate(TrainDateUtil.DEFAULT_FORMAT,
                TrainDateUtil.DEFAULT_VIEW_LOCAL_DETAIL, getDepartureDate());
        adultPassanger = trainSearchPassDataViewModel.getAdult() + " Dewasa";
        infantPassanger = trainSearchPassDataViewModel.getInfant() + " Infant";
        infantTotal = trainSearchPassDataViewModel.getInfant();
    }

    private void setupTrainToolbar() {
        toolbar.setContentInsetStartWithNavigation(0);
        toolbar.setSubtitleTextColor(ContextCompat.getColor(this, R.color.grey_500));
        String subtitle = dateString + " | " + adultPassanger + ", " + infantPassanger;
        String subtitleNonInfant = dateString + " | " + adultPassanger;
        updateTitle(getTitleTrainToolbar(), infantTotal == 0 ? subtitleNonInfant : subtitle);
    }

    protected abstract String getTitleTrainToolbar();

    protected abstract String getDepartureDate();
}
