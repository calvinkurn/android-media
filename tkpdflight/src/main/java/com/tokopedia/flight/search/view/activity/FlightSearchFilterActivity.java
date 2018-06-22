package com.tokopedia.flight.search.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.flight.FlightComponentInstance;
import com.tokopedia.flight.R;
import com.tokopedia.flight.common.util.FlightErrorUtil;
import com.tokopedia.flight.search.di.DaggerFlightSearchComponent;
import com.tokopedia.flight.search.presenter.FlightFilterPresenter;
import com.tokopedia.flight.search.view.FlightFilterCountView;
import com.tokopedia.flight.search.view.fragment.FlightFilterAirlineFragment;
import com.tokopedia.flight.search.view.fragment.FlightFilterDepartureFragment;
import com.tokopedia.flight.search.view.fragment.FlightFilterRefundableFragment;
import com.tokopedia.flight.search.view.fragment.FlightFilterTransitFragment;
import com.tokopedia.flight.search.view.fragment.FlightSearchFilterFragment;
import com.tokopedia.flight.search.view.fragment.flightinterface.OnFlightBaseFilterListener;
import com.tokopedia.flight.search.view.model.filter.FlightFilterModel;
import com.tokopedia.flight.search.view.model.resultstatistics.FlightSearchStatisticModel;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by nathan on 10/27/17.
 */

public class FlightSearchFilterActivity extends BaseSimpleActivity
        implements FlightSearchFilterFragment.OnFlightSearchFilterFragmentListener, FlightFilterCountView {

    public static final String EXTRA_IS_RETURNING = "is_return";
    public static final String EXTRA_STAT_MODEL = "stat_model";
    public static final String EXTRA_FILTER_MODEL = "filter_model";

    public static final String SAVED_TAG = "svd_tag";
    public static final String SAVED_COUNT = "svd_count";

    @Inject
    FlightFilterPresenter flightFilterPresenter;

    private Button buttonFilter;
    private LinearLayout containerLayout;
    private RelativeLayout loadingLayout;

    private boolean isReturning;
    private FlightSearchStatisticModel flightSearchStaatisticModel;
    private FlightFilterModel flightFilterModel;

    private String currentTag;
    private int count;
    private boolean isCloseButton = true;

    public static Intent createInstance(Context context,
                                        boolean isReturning,
                                        FlightFilterModel flightFilterModel) {
        Intent intent = new Intent(context, FlightSearchFilterActivity.class);
        intent.putExtra(EXTRA_IS_RETURNING, isReturning);
        intent.putExtra(EXTRA_FILTER_MODEL, flightFilterModel);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        isReturning = intent.getBooleanExtra(EXTRA_IS_RETURNING, false);

        if (savedInstanceState == null) {
            flightFilterModel = intent.getParcelableExtra(EXTRA_FILTER_MODEL);
            currentTag = getTagFragment();
            count = 0;
        } else {
            flightFilterModel = savedInstanceState.getParcelable(EXTRA_FILTER_MODEL);
            currentTag = savedInstanceState.getString(SAVED_TAG);
            count = savedInstanceState.getInt(SAVED_COUNT);
        }

        containerLayout = (LinearLayout) findViewById(R.id.container_layout);
        loadingLayout = (RelativeLayout) findViewById(R.id.loading_layout);
        buttonFilter = (Button) findViewById(R.id.button_filter);
        buttonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonFilterClicked();
            }
        });

        DaggerFlightSearchComponent.builder()
                .flightComponent(FlightComponentInstance.getFlightComponent(getApplication()))
                .build()
                .inject(this);
        flightFilterPresenter.attachView(this);
        flightFilterPresenter.getFilterStatisticData();

    }

    @Override
    protected boolean isShowCloseButton() {
        return isCloseButton;
    }

    @Override
    protected void setupFragment(Bundle savedInstance) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (flightSearchStaatisticModel != null) {
            getMenuInflater().inflate(R.menu.menu_filter_reset, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_reset) {
            onResetClicked();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void onResetClicked() {
        Fragment f = getCurrentFragment();
        if (f != null && f instanceof OnFlightBaseFilterListener) {
            ((OnFlightBaseFilterListener) f).resetFilter();
        }
    }

    private void onButtonFilterClicked() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            Intent intent = new Intent();
            flightFilterModel.setHasFilter(flightSearchStaatisticModel);
            intent.putExtra(EXTRA_FILTER_MODEL, flightFilterModel);
            setResult(Activity.RESULT_OK, intent);
        }
        this.onBackPressed(true);
    }

    private Fragment getCurrentFragment() {
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        for (int i = 0, sizei = fragmentList.size(); i < sizei; i++) {
            Fragment fragment = fragmentList.get(i);
            if (fragment.isAdded() && fragment.isVisible()) {
                return fragment;
            }
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        this.onBackPressed(false);
    }

    private void onBackPressed(boolean submitFilter) {
        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            if (!submitFilter) {
                backFilterToOriginal();
            }
            int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
            if (backStackCount == 1) {
                setUpTitleByTag(getTagFragment()); // set default
            } else { //2 or more
                setUpTitleByTag(getSupportFragmentManager()
                        .getBackStackEntryAt(backStackCount - 2)
                        .getName());
            }
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    private void backFilterToOriginal() {
        Fragment f = getCurrentFragment();
        if (f != null && f instanceof OnFlightBaseFilterListener) {
            ((OnFlightBaseFilterListener) f).changeFilterToOriginal();
        }
    }

    public void replaceFragment(Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(R.id.parent_view, fragment, tag).addToBackStack(tag).commit();
        setUpTitleByTag(tag);
    }

    public void setUpTitleByTag(String tag) {
        currentTag = tag;
        if (TextUtils.isEmpty(tag) || getTagFragment().equals(tag)) {
            isCloseButton = true;
            toolbar.setTitle(getTitle());
        } else if (FlightFilterDepartureFragment.TAG.equals(tag)) {
            toolbar.setTitle(getString(R.string.flight_search_filter_departure_time));
            isCloseButton = false;
        } else if (FlightFilterTransitFragment.TAG.equals(tag)) {
            toolbar.setTitle(getString(R.string.transit));
            isCloseButton = false;
        } else if (FlightFilterAirlineFragment.TAG.equals(tag)) {
            toolbar.setTitle(getString(R.string.airline));
            isCloseButton = false;
        } else if (FlightFilterRefundableFragment.TAG.equals(tag)) {
            toolbar.setTitle(getString(R.string.refundable_policy));
            isCloseButton = false;
        }
        updateToolbarBackIcon();
        updateButtonFilter(count);
    }

    private void updateToolbarBackIcon() {
        if (getSupportActionBar() != null && isShowCloseButton()) {
            getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(this, com.tokopedia.abstraction.R.drawable.ic_close_default));
        } else {
            getSupportActionBar().setHomeAsUpIndicator(null);
        }
    }

    @Override
    protected Fragment getNewFragment() {
        return FlightSearchFilterFragment.newInstance();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_flight_filter;
    }

    @Override
    public void onTransitLabelClicked() {
        replaceFragment(FlightFilterTransitFragment.newInstance(), FlightFilterTransitFragment.TAG);
    }

    @Override
    public void onAirlineLabelClicked() {
        replaceFragment(FlightFilterAirlineFragment.newInstance(), FlightFilterAirlineFragment.TAG);
    }

    @Override
    public void onRefundLabelClicked() {
        replaceFragment(FlightFilterRefundableFragment.newInstance(), FlightFilterRefundableFragment.TAG);
    }

    @Override
    public void onDepartureLabelClicked() {
        replaceFragment(FlightFilterDepartureFragment.newInstance(), FlightFilterDepartureFragment.TAG);
    }

    @Override
    public FlightSearchStatisticModel getFlightSearchStatisticModel() {
        return flightSearchStaatisticModel;
    }

    @Override
    public FlightFilterModel getFlightFilterModel() {
        return flightFilterModel;
    }

    @Override
    public void onFilterModelChanged(FlightFilterModel flightFilterModel) {
        this.flightFilterModel = flightFilterModel;
        flightFilterPresenter.getFlightCount(isReturning, true, flightFilterModel);
    }

    @Override
    public void onErrorGetCount(Throwable throwable) {
        // no op
    }

    @Override
    public void onSuccessGetCount(int count) {
        this.count = count;
        updateButtonFilter(count);
    }

    @Override
    public boolean isReturning() {
        return isReturning;
    }

    @Override
    public void showErrorGetFilterStatistic(Throwable e) {
        Toast.makeText(this, FlightErrorUtil.getMessageFromException(this, e), Toast.LENGTH_SHORT).show();
        ;
    }

    @Override
    public void onSuccessGetStatistic(FlightSearchStatisticModel statisticModel) {
        this.flightSearchStaatisticModel = statisticModel;
        flightFilterPresenter.getFlightCount(isReturning, true, flightFilterModel);
        containerLayout.setVisibility(View.VISIBLE);
        inflateFragment();
        invalidateOptionsMenu();
    }

    @Override
    public void showGetFilterStatisticLoading() {
        loadingLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideGetFilterStatisticLoading() {
        loadingLayout.setVisibility(View.GONE);
    }

    private void updateButtonFilter(int count) {
        if (currentTag.equals(getTagFragment())) {
            buttonFilter.setText(getString(R.string.flight_there_has_x_flights, count));
        } else {
            buttonFilter.setText(getString(R.string.flight_save_x_flights, count));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_FILTER_MODEL, flightFilterModel);
        outState.putString(SAVED_TAG, currentTag);
        outState.putInt(SAVED_COUNT, count);
    }
}
