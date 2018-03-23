package com.tokopedia.tkpdtrain.search.presentation.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tokopedia.tkpdtrain.R;
import com.tokopedia.tkpdtrain.common.di.utils.TrainComponentUtils;
import com.tokopedia.tkpdtrain.common.presentation.TrainBaseActivity;
import com.tokopedia.tkpdtrain.search.di.DaggerTrainSearchComponent;
import com.tokopedia.tkpdtrain.search.domain.FilterSearchData;
import com.tokopedia.tkpdtrain.search.presentation.contract.FilterSearchActionView;
import com.tokopedia.tkpdtrain.search.presentation.contract.TrainFilterSearchContract;
import com.tokopedia.tkpdtrain.search.presentation.fragment.TrainFilterSearchFragment;
import com.tokopedia.tkpdtrain.search.presentation.presenter.TrainFilterSearchPresenter;

import java.util.HashMap;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 3/20/18.
 */

public class TrainFilterSearchActivity extends TrainBaseActivity
        implements TrainFilterSearchContract.View, FilterSearchActionView {

    private static final String MAP_PARAM_SCHEDULE = "map_param_schedule";
    private static final String SCHEDULE_VARIANT = "schedule_variant";

    @Inject
    TrainFilterSearchPresenter presenter;

    private ProgressBar progressBar;
    private LinearLayout containerLayout;
    private FilterSearchData filterSearchData;
    private Button filterButton;

    public static Intent getCallingIntent(Activity activity, HashMap<String, Object> mapParam, int scheduleVariant) {
        Intent intent = new Intent(activity, TrainFilterSearchActivity.class);
        intent.putExtra(MAP_PARAM_SCHEDULE, mapParam);
        intent.putExtra(SCHEDULE_VARIANT, scheduleVariant);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressBar = findViewById(R.id.progress_bar);
        containerLayout = findViewById(R.id.container_layout);
        filterButton =  findViewById(R.id.button_filter);

        updateToolbarBackIcon();
        updateTitle("Filter");

        DaggerTrainSearchComponent.builder()
                .trainComponent(TrainComponentUtils.getTrainComponent(getApplication()))
                .build()
                .inject(this);
        presenter.attachView(this);
        HashMap<String, Object> mapParam = (HashMap<String, Object>) getIntent().getSerializableExtra(MAP_PARAM_SCHEDULE);
        presenter.getFilterSearchData(mapParam, getIntent().getIntExtra(SCHEDULE_VARIANT, 0));
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_train_filter_search;
    }

    @Override
    protected Fragment getNewFragment() {
        return filterSearchData == null? null : TrainFilterSearchFragment.newInstance();
    }

    @Override
    public void showCountSchedule(int totalSchedule) {
        filterButton.setText("Terdapat " + totalSchedule + " perjalanan");
    }

    @Override
    public void renderFilterSearchData(FilterSearchData filterSearchData) {
        this.filterSearchData = filterSearchData;
        presenter.getCountScheduleAvailable(filterSearchData);
        inflateFragment();
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        containerLayout.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
        containerLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_train_filter_reset, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();
        if (menuId == R.id.action_reset) {
            //TODO make menu reset here
            Toast.makeText(getApplicationContext(), "RESET", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void updateToolbarBackIcon() {
        getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(this,
                com.tokopedia.abstraction.R.drawable.ic_close_default));
    }

    @Override
    public FilterSearchData getFilterSearchData() {
        return filterSearchData;
    }

    @Override
    public void onChangeFilterSearchData(FilterSearchData filterSearchData) {
        this.filterSearchData = filterSearchData;
        presenter.getCountScheduleAvailable(filterSearchData);
    }
}
