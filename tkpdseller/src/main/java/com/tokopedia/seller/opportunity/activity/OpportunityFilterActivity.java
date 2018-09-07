package com.tokopedia.seller.opportunity.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.seller.R;
import com.tokopedia.seller.opportunity.adapter.OpportunityFilterAdapter;
import com.tokopedia.seller.opportunity.adapter.OpportunityFilterTitleAdapter;
import com.tokopedia.seller.opportunity.analytics.OpportunityTrackingEventLabel;
import com.tokopedia.seller.opportunity.fragment.OpportunityFilterFragment;
import com.tokopedia.seller.opportunity.fragment.OpportunityFilterTitleFragment;
import com.tokopedia.seller.opportunity.viewmodel.FilterViewModel;
import com.tokopedia.seller.opportunity.viewmodel.OpportunityFilterPassModel;
import com.tokopedia.seller.opportunity.viewmodel.OptionViewModel;
import com.tokopedia.seller.opportunity.viewmodel.opportunitylist.FilterPass;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nisie on 3/15/17.
 */

public class OpportunityFilterActivity extends BasePresenterActivity
        implements OpportunityFilterTitleAdapter.FilterListener,
        OpportunityFilterAdapter.CategoryListener {

    public interface FilterTitleListener {
        void updateData(ArrayList<FilterViewModel> listFilter);
    }

    public interface FilterListener {
        void updateData(FilterViewModel viewModel);
    }

    public static final String CACHE_OPPORTUNITY_FILTER = "CACHE_OPPORTUNITY_FILTER";

    View saveButton;
    View resetButton;
    private List<Fragment> listFragment;
    private ArrayList<FilterViewModel> listFilter;
    private ArrayList<FilterPass> listPass;
    private OpportunityFilterPassModel filterPassModel;
    private GlobalCacheManager cacheManager;
    private String trackingEventLabel;

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, OpportunityFilterActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        cacheManager = new GlobalCacheManager();
        trackingEventLabel = "";

        OpportunityFilterPassModel opportunityFilterPassModel =
                cacheManager.getConvertObjData(OpportunityFilterActivity.CACHE_OPPORTUNITY_FILTER,
                        OpportunityFilterPassModel.class);

        listFilter = opportunityFilterPassModel.getListFilter();
        listPass = new ArrayList<>();

        super.onCreate(savedInstanceState);

    }


    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filter, menu);
        return true;
    }

    @Override


    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_close) {
            finish();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_opportunity_filter;

    }

    @Override
    protected void initView() {
        saveButton = findViewById(R.id.save_button);
        resetButton = findViewById(R.id.reset_button);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        OpportunityFilterTitleFragment fragment =
                (OpportunityFilterTitleFragment) getFragmentManager().findFragmentById(R.id.filter);
        if (fragment == null)
            fragment = OpportunityFilterTitleFragment.createInstance(listFilter);
        fragmentTransaction.replace(R.id.filter, fragment);

        listFragment = new ArrayList<>();
        listFilter.get(0).setPosition(0);
        listFragment.add(OpportunityFilterFragment.createInstance(listFilter.get(0)));

        fragmentTransaction.replace(R.id.container, listFragment.get(0));

        fragmentTransaction.commit();
    }

    @Override
    protected void setViewListener() {
        saveButton.setOnClickListener(onSaveClicked());
        resetButton.setOnClickListener(onResetClicked());
    }

    private View.OnClickListener onResetClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetFilter(listFilter);
                updateFilterTitleFragment();
            }
        };
    }

    private void resetFilter(List<FilterViewModel> listFilter) {
        for (FilterViewModel filter : listFilter) {
            if (filter.isActive()) {
                filter.setActive(false);
                resetOption(filter.getListChild());
                if (filter.getPosition() < listFragment.size()) {
                    ((FilterListener) listFragment.get(filter.getPosition()))
                            .updateData(listFilter.get(filter.getPosition()));
                }
            }
        }
    }

    private void resetOption(ArrayList<OptionViewModel> listChild) {
        for (OptionViewModel optionViewModel : listChild) {
            if (optionViewModel.getListChild().size() > 0) {
                resetOption(optionViewModel.getListChild());
            } else if (optionViewModel.isSelected()) {
                optionViewModel.setSelected(false);
            }
        }
    }

    private View.OnClickListener onSaveClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                filterPassModel = new OpportunityFilterPassModel();
                filterPassModel.setListFilter(listFilter);
                filterPassModel.setListPass(getSelectedFilterList());

                cacheManager.setKey(CACHE_OPPORTUNITY_FILTER);
                cacheManager.setValue(CacheUtil.convertModelToString(filterPassModel,
                        new TypeToken<OpportunityFilterPassModel>() {
                        }.getType()));
                cacheManager.store();

                if (trackingEventLabel.endsWith("~"))
                    trackingEventLabel = trackingEventLabel.substring(0, trackingEventLabel.length
                            () - 1);
                UnifyTracking.eventOpportunity(
                        OpportunityTrackingEventLabel.EventName.SUBMIT_OPPORTUNITY,
                        OpportunityTrackingEventLabel.EventCategory.OPPORTUNITY_FILTER,
                        AppEventTracking.Action.SUBMIT,
                        trackingEventLabel
                );

                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                finish();

            }
        };
    }

    private ArrayList<FilterPass> getSelectedFilterList() {
        ArrayList<FilterPass> list = new ArrayList<>();

        for (int i = 0; i < listFilter.size(); i++) {
            if (listFilter.get(i).isActive()) {
                addSelectedFilterToList(list, listFilter.get(i).getListChild());
                if (trackingEventLabel.endsWith(";"))
                    trackingEventLabel = trackingEventLabel.substring(0, trackingEventLabel.length
                            () - 1);
                trackingEventLabel += "~";
            }
        }
        return list;
    }

    private void addSelectedFilterToList(ArrayList<FilterPass> list,
                                         ArrayList<OptionViewModel> listChild) {

        for (int i = 0; i < listChild.size(); i++) {
            OptionViewModel optionViewModel = listChild.get(i);
            if (optionViewModel.getListChild().size() > 0 && !optionViewModel.isExpanded()) {
                addSelectedFilterToList(list, optionViewModel.getListChild());
            } else if (optionViewModel.isSelected()) {
                list.add(new FilterPass(
                        optionViewModel.getKey(),
                        optionViewModel.getValue(),
                        optionViewModel.getName()));

                trackingEventLabel += optionViewModel.getKey() + " " + optionViewModel
                        .getValue() + ";";
            }
        }
    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    public void onTitleClicked(int pos) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        if (listFragment.size() > pos &&
                listFragment.get(pos) != null
                && listFragment.get(pos) instanceof FilterListener) {
            ((FilterListener) listFragment.get(pos)).updateData(listFilter.get(pos));
        } else {
            listFilter.get(pos).setPosition(pos);
            listFragment.add(OpportunityFilterFragment.createInstance(listFilter.get(pos)));
        }

        fragmentTransaction.replace(R.id.container, listFragment.get(pos));
        fragmentTransaction.commit();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

    }

    private void updateFilterTitleFragment() {
        ((OpportunityFilterTitleFragment) getFragmentManager().findFragmentById(R.id.filter))
                .updateData(listFilter);
    }

    @Override
    public void onFilterExpanded(int position, FilterViewModel filterViewModel) {
        listFilter.set(filterViewModel.getPosition(), filterViewModel);
    }

    @Override
    public void onFilterSelected(int selectedFilterPosition, String selectedOption) {
        setSelected(selectedOption, listFilter.get(selectedFilterPosition).getListChild());
        listFilter.get(selectedFilterPosition).setActive(checkIsActive(listFilter.get(selectedFilterPosition).getListChild()));
        updateFilterTitleFragment();

    }

    private boolean checkIsActive(ArrayList<OptionViewModel> listOption) {
        boolean isActive = false;
        for (OptionViewModel optionViewModel : listOption) {
            if (optionViewModel.getListChild().size() > 0
                    && !optionViewModel.isExpanded()
                    && checkIsActive(optionViewModel.getListChild())) {
                isActive = true;
            } else if (optionViewModel.isSelected()) {
                isActive = true;
            }
        }
        return isActive;
    }

    private void setSelected(String selectedOption, ArrayList<OptionViewModel> listOption) {
        for (OptionViewModel optionViewModel : listOption) {
            if (optionViewModel.getListChild().size() > 0 && !optionViewModel.isExpanded()) {
                setSelected(selectedOption, optionViewModel.getListChild());
            } else if (optionViewModel.getName().equals(selectedOption)) {
                optionViewModel.setSelected(!optionViewModel.isSelected());
            }
        }
    }
}