package com.tokopedia.tkpd.tkpdreputation.inbox.view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.activity.InboxReputationFilterActivity;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.InboxReputationFilterAdapter;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.FilterViewModel;

/**
 * @author by nisie on 8/21/17.
 */

public class InboxReputationFilterFragment extends BasePresenterFragment
        implements InboxReputationFilterActivity.FilterListener {

    private static final String ARGS_FILTER_DATA = "ARGS_FILTER_DATA";
    RecyclerView list;
    InboxReputationFilterAdapter adapter;
    FilterViewModel filterViewModel;

    public static Fragment createInstance(FilterViewModel filterViewModel) {
        InboxReputationFilterFragment fragment = new InboxReputationFilterFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGS_FILTER_DATA, filterViewModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }


    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_inbox_reputation_filter;
    }

    @Override
    protected void initView(View view) {
        list = (RecyclerView) view.findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        adapter = InboxReputationFilterAdapter.createInstance((InboxReputationFilterActivity)
                getActivity());
        list.setAdapter(adapter);
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }


    @Override
    public void onResume() {
        super.onResume();
        if (filterViewModel == null && getArguments().getParcelable(ARGS_FILTER_DATA) != null) {
            filterViewModel = getArguments().getParcelable(ARGS_FILTER_DATA);
        }

        if (adapter != null && filterViewModel != null)
            adapter.setData(filterViewModel);
    }

    @Override
    public void updateData(FilterViewModel viewModel) {
        this.filterViewModel = viewModel;
        if (adapter != null) {
            adapter.setData(viewModel);
            adapter.notifyDataSetChanged();
        }

    }

}