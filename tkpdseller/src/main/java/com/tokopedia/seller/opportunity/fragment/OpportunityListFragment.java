package com.tokopedia.seller.opportunity.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.opportunity.adapter.OpportunityListAdapter;

/**
 * Created by nisie on 3/1/17.
 */

public class OpportunityListFragment extends BasePresenterFragment{

    RecyclerView opportunityList;
    OpportunityListAdapter adapter;
    TextView headerInfo;

    public static Fragment newInstance() {
        return new OpportunityListFragment();
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
        return R.layout.fragment_opportunity_list;
    }

    @Override
    protected void initView(View view) {
        headerInfo = (TextView)view.findViewById(R.id.header_info);
        opportunityList = (RecyclerView)view.findViewById(R.id.opportunity_list);
        adapter = OpportunityListAdapter.createInstance();
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
}
