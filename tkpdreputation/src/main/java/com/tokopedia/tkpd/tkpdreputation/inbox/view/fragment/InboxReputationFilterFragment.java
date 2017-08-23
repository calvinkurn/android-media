package com.tokopedia.tkpd.tkpdreputation.inbox.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.GetFirstTimeInboxReputationUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.InboxReputationFilterAdapter;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.OptionViewModel;

import java.util.ArrayList;

/**
 * @author by nisie on 8/21/17.
 */

public class InboxReputationFilterFragment extends BaseDaggerFragment
        implements InboxReputationFilterAdapter.FilterListener {

    private static final String FILTER_ALL_TIME = "1";
    private static final String FILTER_LAST_WEEK = "2";
    private static final String FILTER_THIS_MONTH = "3";
    private static final String FILTER_LAST_3_MONTH = "4";
    public static final String SELECTED_TIME_FILTER = "SELECTED_TIME_FILTER";

    RecyclerView list;
    InboxReputationFilterAdapter adapter;
    ArrayList<OptionViewModel> listOption;

    public static Fragment createInstance(String timeFilter) {
        InboxReputationFilterFragment fragment = new InboxReputationFilterFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SELECTED_TIME_FILTER, timeFilter);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        listOption = createListFilterTime();
        setSelected(listOption);
    }

    private void setSelected(ArrayList<OptionViewModel> listOption) {
        if (!getArguments().getString(SELECTED_TIME_FILTER, "").equals("")) {
            for (OptionViewModel optionViewModel : listOption) {
                if (optionViewModel.getValue().equals(
                        getArguments().getString(SELECTED_TIME_FILTER))) {
                    optionViewModel.setSelected(true);
                }
            }
        }
    }

    private ArrayList<OptionViewModel> createListFilterTime() {
        ArrayList<OptionViewModel> list = new ArrayList<OptionViewModel>();
        list.add(new OptionViewModel(getString(R.string.filter_all_time),
                GetFirstTimeInboxReputationUseCase.PARAM_TIME_FILTER, FILTER_ALL_TIME, list.size
                ()));
        list.add(new OptionViewModel(getString(R.string.filter_last_7_days),
                GetFirstTimeInboxReputationUseCase.PARAM_TIME_FILTER, FILTER_LAST_WEEK, list.size
                ()));
        list.add(new OptionViewModel(getString(R.string.filter_this_month),
                GetFirstTimeInboxReputationUseCase.PARAM_TIME_FILTER, FILTER_THIS_MONTH, list.size
                ()));
        list.add(new OptionViewModel(getString(R.string.filter_last_3_month),
                GetFirstTimeInboxReputationUseCase.PARAM_TIME_FILTER, FILTER_LAST_3_MONTH, list.size
                ()));
        return list;
    }

    @Override
    protected void initInjector() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        View parentView = inflater.inflate(R.layout.fragment_inbox_reputation_filter, container,
                false);
        list = (RecyclerView) parentView.findViewById(R.id.list);
        prepareView();
        return parentView;

    }

    private void prepareView() {
        list.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        adapter = InboxReputationFilterAdapter.createInstance(this, listOption);
        list.setAdapter(adapter);
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_INBOX_REPUTATION_FILTER;
    }

    @Override
    public void onFilterSelected(OptionViewModel optionViewModel) {

        Intent data = new Intent();
        data.putExtra(SELECTED_TIME_FILTER, optionViewModel.getValue());
        getActivity().setResult(Activity.RESULT_OK, data);
        getActivity().finish();
    }
}