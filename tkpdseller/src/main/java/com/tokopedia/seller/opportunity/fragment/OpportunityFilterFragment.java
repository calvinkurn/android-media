package com.tokopedia.seller.opportunity.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.view.View;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.opportunity.activity.OpportunityFilterActivity;
import com.tokopedia.seller.opportunity.adapter.OpportunityFilterAdapter;
import com.tokopedia.seller.opportunity.viewmodel.FilterViewModel;
import com.tokopedia.seller.opportunity.viewmodel.OptionViewModel;

import java.util.ArrayList;

/**
 * Created by nisie on 3/15/17.
 */

public class OpportunityFilterFragment extends BasePresenterFragment
        implements OpportunityFilterActivity.FilterListener {


    private static final String ARGS_FILTER_DATA = "ARGS_FILTER_DATA";
    RecyclerView categoryList;
    OpportunityFilterAdapter adapter;
    FilterViewModel filterViewModel;
    SearchView searchView;

    public OpportunityFilterFragment() {
    }

    public static Fragment createInstance(FilterViewModel filterViewModel) {
        OpportunityFilterFragment fragment = new OpportunityFilterFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGS_FILTER_DATA, filterViewModel);
        fragment.setArguments(bundle);
        return fragment;
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
        searchView = (SearchView) view.findViewById(R.id.search);
        categoryList = (RecyclerView) view.findViewById(R.id.list);
        categoryList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = OpportunityFilterAdapter.createInstance((OpportunityFilterActivity) getActivity());
        categoryList.setAdapter(adapter);
        setSearchVisibility();
    }

    private void setSearchVisibility() {
        if (filterViewModel != null
                && filterViewModel.getSearchViewModel() != null
                && filterViewModel.getSearchViewModel().isSearchable())
            searchView.setVisibility(View.VISIBLE);
        else
            searchView.setVisibility(View.GONE);
    }

    @Override
    protected void setViewListener() {
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.length() > 0) {
                    ArrayList<OptionViewModel> tempListOption = new ArrayList<OptionViewModel>();
                    tempListOption.addAll(searchFilter(newText, filterViewModel.getListChild()));
                    FilterViewModel tempViewModel = new FilterViewModel();
                    tempViewModel.setActive(filterViewModel.isActive());
                    tempViewModel.setListChild(tempListOption);
                    tempViewModel.setSelected(filterViewModel.isSelected());
                    tempViewModel.setName(filterViewModel.getName());
                    tempViewModel.setSearchViewModel(filterViewModel.getSearchViewModel());
                    tempViewModel.setPosition(filterViewModel.getPosition());
                    adapter.setData(tempViewModel);
                } else {
                    adapter.setData(filterViewModel);
                }

                return false;
            }
        });
    }

    private ArrayList<OptionViewModel> searchFilter(String newText, ArrayList<OptionViewModel> listFilter) {
        ArrayList<OptionViewModel> newList = new ArrayList<>();
        for (OptionViewModel viewModel : listFilter) {
            if (viewModel.getName().contains(newText)
                    && viewModel.getListChild().size() == 0) {
                newList.add(viewModel);
            }

            if (viewModel.getListChild().size() > 0 && !viewModel.isExpanded()) {
                newList.addAll(searchFilter(newText, viewModel.getListChild()));
            }
        }
        return newList;
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
        if (searchView != null)
            searchView.setQuery("", true);
    }
}
