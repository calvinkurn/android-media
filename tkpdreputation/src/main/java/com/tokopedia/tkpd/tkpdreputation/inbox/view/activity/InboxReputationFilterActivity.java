package com.tokopedia.tkpd.tkpdreputation.inbox.view.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.GetFirstTimeInboxReputationUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.InboxReputationFilterAdapter;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.InboxReputationFilterTitleAdapter;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.fragment.InboxReputationFilterFragment;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.fragment.InboxReputationFilterTitleFragment;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.FilterPass;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.FilterPassModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.FilterViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.OptionViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by nisie on 8/21/17.
 */

public class InboxReputationFilterActivity extends BasePresenterActivity
        implements InboxReputationFilterTitleAdapter.FilterListener, InboxReputationFilterAdapter.FilterListener {

    public static final String CACHE_INBOX_REPUTATION_FILTER = "CACHE_INBOX_REPUTATION_FILTER";
    public static final String PARAM_FILTER_VIEW_MODEL = "PARAM_FILTER_VIEW_MODEL";
    public static final String PARAM_SELECTED_FILTER = "PARAM_SELECTED_FILTER";

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, InboxReputationFilterActivity.class);
        return intent;
    }

    public interface FilterTitleListener {
        void updateData(ArrayList<FilterViewModel> listFilter);
    }

    public interface FilterListener {
        void updateData(FilterViewModel viewModel);
    }

    View saveButton;
    View resetButton;
    private List<Fragment> listFragment;
    private ArrayList<FilterViewModel> listFilter;
    private ArrayList<FilterPass> listPass;
    private FilterPassModel filterPassModel;
    private GlobalCacheManager cacheManager;

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
    protected int getLayoutId() {
        return R.layout.inbox_reputation_filter_activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        cacheManager = new GlobalCacheManager();

        if (savedInstanceState != null) {
            listFilter = savedInstanceState.getParcelableArrayList(PARAM_FILTER_VIEW_MODEL);
            listPass = savedInstanceState.getParcelableArrayList(PARAM_SELECTED_FILTER);
        } else {
            FilterPassModel filterPassModel =
                    cacheManager.getConvertObjData(CACHE_INBOX_REPUTATION_FILTER,
                            FilterPassModel.class);

            listFilter = filterPassModel.getListFilter();
            listPass = new ArrayList<>();
        }
        super.onCreate(savedInstanceState);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_close, menu);
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
    protected void initView() {
        saveButton = findViewById(R.id.save_button);
        resetButton = findViewById(R.id.reset_button);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        InboxReputationFilterTitleFragment fragment =
                (InboxReputationFilterTitleFragment) getFragmentManager().findFragmentById(R.id.filter);
        if (fragment == null)
            fragment = InboxReputationFilterTitleFragment.createInstance(listFilter);
        fragmentTransaction.replace(R.id.filter, fragment);

        listFragment = new ArrayList<>();
        listFilter.get(0).setPosition(0);
        listFragment.add(InboxReputationFilterFragment.createInstance(listFilter.get(0)));

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
            if (optionViewModel.isSelected()) {
                optionViewModel.setSelected(false);
            }
        }
    }

    private View.OnClickListener onSaveClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterPassModel = new FilterPassModel();
                filterPassModel.setListFilter(listFilter);
                filterPassModel.setListPass(getSelectedFilterList());

                cacheManager.setKey(CACHE_INBOX_REPUTATION_FILTER);
                cacheManager.setValue(CacheUtil.convertModelToString(filterPassModel,
                        new TypeToken<FilterPassModel>() {
                        }.getType()));
                cacheManager.store();

                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        };
    }

    private ArrayList<FilterPass> getSelectedFilterList() {
        ArrayList<FilterPass> list = new ArrayList<>();

        for (FilterViewModel filterViewModel : listFilter) {
            if (filterViewModel.isActive()) {
                addSelectedFilterToList(list, filterViewModel.getListChild());
            }
        }
        return list;
    }

    private void addSelectedFilterToList(ArrayList<FilterPass> list,
                                         ArrayList<OptionViewModel> listChild) {
        for (OptionViewModel optionViewModel : listChild) {
            if (optionViewModel.isSelected()) {
                list.add(new FilterPass(optionViewModel.getKey(), optionViewModel.getValue()));
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
    public void onTitleClicked(int pos) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        if (listFragment.size() > pos &&
                listFragment.get(pos) != null
                && listFragment.get(pos) instanceof FilterListener) {
            ((FilterListener) listFragment.get(pos)).updateData(listFilter.get(pos));
        } else {
            listFilter.get(pos).setPosition(pos);
            listFragment.add(InboxReputationFilterFragment.createInstance(listFilter.get(pos)));
        }

        fragmentTransaction.replace(R.id.container, listFragment.get(pos));
        fragmentTransaction.commit();
    }


    private void updateFilterTitleFragment() {
        ((InboxReputationFilterTitleFragment) getFragmentManager().findFragmentById(R.id.filter))
                .updateData(listFilter);
    }

    @Override
    public void onFilterSelected(int selectedFilterPosition, String selectedOption) {
        setSelected(selectedOption, listFilter.get(selectedFilterPosition).getListChild());
        listFilter.get(selectedFilterPosition).setActive(
                checkIsActive(listFilter.get(selectedFilterPosition).getListChild()));
        updateFilterTitleFragment();
    }


    private boolean checkIsActive(ArrayList<OptionViewModel> listOption) {
        boolean isActive = false;
        for (OptionViewModel optionViewModel : listOption) {
            if (optionViewModel.isSelected()) {
                isActive = true;
            }
        }
        return isActive;
    }

    private void setSelected(String selectedOption, ArrayList<OptionViewModel> listOption) {
        for (OptionViewModel optionViewModel : listOption) {
            if (optionViewModel.getName().equals(selectedOption)) {
                optionViewModel.setSelected(!optionViewModel.isSelected());
            }
        }
    }
}
