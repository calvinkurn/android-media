package com.tokopedia.flight.search.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.R;
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

    private boolean isReturning;
    private FlightSearchStatisticModel flightSearchStatisticModel;
    private FlightFilterModel flightFilterModel;

    private String currentTag;
    private int count;
    private View vReset;

    private TextView tvToolbarTitle;

    public static Intent createInstance(Context context,
                                        boolean isReturning,
                                        FlightSearchStatisticModel flightSearchStatisticModel,
                                        FlightFilterModel flightFilterModel) {
        Intent intent = new Intent(context, FlightSearchFilterActivity.class);
        intent.putExtra(EXTRA_IS_RETURNING, isReturning);
        intent.putExtra(EXTRA_STAT_MODEL, flightSearchStatisticModel);
        intent.putExtra(EXTRA_FILTER_MODEL, flightFilterModel);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        isReturning = intent.getBooleanExtra(EXTRA_IS_RETURNING, false);
        flightSearchStatisticModel = intent.getParcelableExtra(EXTRA_STAT_MODEL);

        if (savedInstanceState == null) {
            flightFilterModel = intent.getParcelableExtra(EXTRA_FILTER_MODEL);
            currentTag = getTagFragment();
            count = 0;
        } else {
            flightFilterModel = savedInstanceState.getParcelable(EXTRA_FILTER_MODEL);
            currentTag = savedInstanceState.getString(SAVED_TAG);
            count = savedInstanceState.getInt(SAVED_COUNT);
        }

        buttonFilter = (Button) findViewById(R.id.button_filter);
        buttonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonFilterClicked();
            }
        });

        vReset = findViewById(R.id.v_reset);
        vReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment f = getCurrentFragment();
                if (f != null && f instanceof OnFlightBaseFilterListener) {
                    ((OnFlightBaseFilterListener) f).resetFilter();
                }
            }
        });
        tvToolbarTitle = (TextView) findViewById(R.id.tv_toolbar_title);
        tvToolbarTitle.setText(getTitle());

        DaggerFlightSearchComponent.builder()
                .flightComponent(((FlightModuleRouter) getApplication()).getFlightComponent())
                .build()
                .inject(this);
        flightFilterPresenter.attachView(this);
        flightFilterPresenter.getFlightCount(isReturning, true, flightFilterModel);
    }

    private void onButtonFilterClicked() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            Intent intent = new Intent();
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
        if (fragment instanceof OnFlightBaseFilterListener) {
            vReset.setVisibility(View.VISIBLE);
        } else {
            vReset.setVisibility(View.GONE);
        }
    }

    public void setUpTitleByTag(String tag) {
        currentTag = tag;
        if (TextUtils.isEmpty(tag) || getTagFragment().equals(tag)) {
            tvToolbarTitle.setText(getTitle());
        } else if (FlightFilterDepartureFragment.TAG.equals(tag)) {
            tvToolbarTitle.setText(getString(R.string.flight_search_filter_departure_time));
        } else if (FlightFilterTransitFragment.TAG.equals(tag)) {
            tvToolbarTitle.setText(getString(R.string.transit));
        } else if (FlightFilterAirlineFragment.TAG.equals(tag)) {
            tvToolbarTitle.setText(getString(R.string.airline));
        } else if (FlightFilterRefundableFragment.TAG.equals(tag)) {
            tvToolbarTitle.setText(getString(R.string.refundable_policy));
        }
        updateButtonFilter(count);
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
        return flightSearchStatisticModel;
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
