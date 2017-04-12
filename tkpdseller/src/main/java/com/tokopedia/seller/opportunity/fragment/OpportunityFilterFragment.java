package com.tokopedia.seller.opportunity.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.opportunity.activity.OpportunityFilterActivity;
import com.tokopedia.seller.opportunity.adapter.OpportunityFilterAdapter;
import com.tokopedia.seller.opportunity.viewmodel.FilterItemViewModel;
import com.tokopedia.seller.opportunity.viewmodel.OpportunityFilterActivityViewModel;

import java.util.ArrayList;

/**
 * Created by nisie on 4/7/17.
 */

public class OpportunityFilterFragment extends BasePresenterFragment implements OpportunityFilterActivity.FilterListener {

    private static final String ARGS_LIST_TITLE = "ARGS_LIST_TITLE";
    RecyclerView opportunityFilter;
    OpportunityFilterAdapter adapter;
    ArrayList<FilterItemViewModel> listTitle;

    public OpportunityFilterFragment() {
        this.listTitle = new ArrayList<>();
    }

    public static OpportunityFilterFragment createInstance(ArrayList<FilterItemViewModel> listTitle) {
        OpportunityFilterFragment fragment = new OpportunityFilterFragment();
        Bundle bundle = new Bundle();
        listTitle.get(0).setSelected(true);
        bundle.putParcelableArrayList(ARGS_LIST_TITLE, listTitle);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {
        if (getArguments().getParcelableArrayList(ARGS_LIST_TITLE) != null)
            listTitle = getArguments().getParcelableArrayList(ARGS_LIST_TITLE);
        adapter.setList(listTitle);
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
        return R.layout.fragment_opportunity_filter;
    }

    @Override
    protected void initView(View view) {
        opportunityFilter = (RecyclerView) view.findViewById(R.id.opportunity_filter);
        opportunityFilter.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = OpportunityFilterAdapter.createInstance(getActivity(), (OpportunityFilterActivity) getActivity());
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
    public void updateData(OpportunityFilterActivityViewModel viewModel) {
        adapter.setList(viewModel.getListTitle());
    }
}
