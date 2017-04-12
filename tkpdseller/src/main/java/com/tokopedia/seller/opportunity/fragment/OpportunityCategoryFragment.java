package com.tokopedia.seller.opportunity.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.opportunity.activity.OpportunityFilterActivity;
import com.tokopedia.seller.opportunity.adapter.OpportunityCategoryAdapter;
import com.tokopedia.seller.opportunity.viewmodel.CategoryViewModel;
import com.tokopedia.seller.opportunity.viewmodel.OpportunityFilterActivityViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nisie on 3/15/17.
 */

public class OpportunityCategoryFragment extends BasePresenterFragment implements OpportunityFilterActivity.FilterListener {


    private static final String ARGS_CATEGORY_LIST = "ARGS_CATEGORY_LIST";
    RecyclerView categoryList;
    OpportunityCategoryAdapter adapter;
    ArrayList<CategoryViewModel> listCategory;

    public OpportunityCategoryFragment() {
        this.listCategory = new ArrayList<>();
    }

    public static Fragment createInstance(List<CategoryViewModel> listShipping) {
        OpportunityCategoryFragment fragment = new OpportunityCategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ARGS_CATEGORY_LIST, new ArrayList<>(listShipping));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_opportunity_category;
    }

    @Override
    protected void initView(View view) {
        categoryList = (RecyclerView) view.findViewById(R.id.list);
        categoryList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = OpportunityCategoryAdapter.createInstance((OpportunityFilterActivity) getActivity());
        categoryList.setAdapter(adapter);
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
    public void onResume() {
        super.onResume();
        if ((listCategory == null || listCategory.size() == 0)
                && getArguments().getParcelableArrayList(ARGS_CATEGORY_LIST) != null)
            listCategory = getArguments().getParcelableArrayList(ARGS_CATEGORY_LIST);
        adapter.setList(listCategory);
    }

    @Override
    public void updateData(OpportunityFilterActivityViewModel viewModel) {
        this.listCategory = new ArrayList<>(viewModel.getListCategory());
        if (adapter != null)
            adapter.setList(listCategory);
    }

}
