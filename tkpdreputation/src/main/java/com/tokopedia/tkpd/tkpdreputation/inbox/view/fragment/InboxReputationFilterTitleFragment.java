package com.tokopedia.tkpd.tkpdreputation.inbox.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.activity.InboxReputationFilterActivity;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.InboxReputationFilterTitleAdapter;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.FilterViewModel;

import java.util.ArrayList;

/**
 * @author by nisie on 8/21/17.
 */

public class InboxReputationFilterTitleFragment extends BasePresenterFragment
        implements InboxReputationFilterActivity.FilterTitleListener{

    private static final String ARGS_LIST_FILTER = "ARGS_LIST_FILTER";
    RecyclerView opportunityFilter;
    InboxReputationFilterTitleAdapter adapter;
    ArrayList<FilterViewModel> listFilter;

    public InboxReputationFilterTitleFragment() {
        this.listFilter = new ArrayList<>();
    }

    public static InboxReputationFilterTitleFragment createInstance(ArrayList<FilterViewModel> listTitle) {
        InboxReputationFilterTitleFragment fragment = new InboxReputationFilterTitleFragment();
        Bundle bundle = new Bundle();
        for (FilterViewModel filterViewModel : listTitle) {
            filterViewModel.setSelected(false);
        }
        listTitle.get(0).setSelected(true);
        bundle.putParcelableArrayList(ARGS_LIST_FILTER, listTitle);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {
        if (getArguments().getParcelableArrayList(ARGS_LIST_FILTER) != null)
            listFilter = getArguments().getParcelableArrayList(ARGS_LIST_FILTER);
        adapter.setList(listFilter);
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
        return R.layout.fragment_inbox_reputation_filter_title;
    }

    @Override
    protected void initView(View view) {
        opportunityFilter = (RecyclerView) view.findViewById(R.id.filter);
        opportunityFilter.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = InboxReputationFilterTitleAdapter.createInstance(getActivity(),
                (InboxReputationFilterActivity) getActivity());
        opportunityFilter.setAdapter(adapter);
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
    public void updateData(ArrayList<FilterViewModel> listFilter) {
        this.listFilter = listFilter;
        if (adapter != null) {
            adapter.setList(listFilter);
            adapter.notifyDataSetChanged();
        }
    }
}
