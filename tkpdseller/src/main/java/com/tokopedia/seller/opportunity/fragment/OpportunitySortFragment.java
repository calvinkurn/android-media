package com.tokopedia.seller.opportunity.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.opportunity.adapter.OpportunitySortAdapter;
import com.tokopedia.seller.opportunity.adapter.viewmodel.SimpleCheckListItemModel;
import com.tokopedia.seller.opportunity.analytics.OpportunityTrackingEventLabel;
import com.tokopedia.seller.opportunity.viewmodel.SortingTypeViewModel;
import com.tokopedia.seller.opportunity.viewmodel.opportunitylist.FilterPass;

import java.util.ArrayList;
import java.util.List;

import static com.tokopedia.core.network.v4.NetworkConfig.key;

/**
 * Created by nisie on 3/6/17.
 */

public class OpportunitySortFragment extends BasePresenterFragment {

    public static final String SELECTED_SORT = "SELECTED_SORT";
    public static final String SELECTED_POSITION = "SELECTED_POSITION";
    public static final String ARGS_LIST_SORT = "ARGS_LIST_SORT";

    OpportunitySortAdapter adapter;
    List<SortingTypeViewModel> listSort;
    RecyclerView sortRecyclerView;

    public static OpportunitySortFragment createInstance(Bundle extras) {
        OpportunitySortFragment fragment = new OpportunitySortFragment();
        fragment.setArguments(extras);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)
            listSort = savedInstanceState.getParcelableArrayList(ARGS_LIST_SORT);
        else if (getArguments().getParcelableArrayList(ARGS_LIST_SORT) != null)
            listSort = getArguments().getParcelableArrayList(ARGS_LIST_SORT);
        else {
            listSort = new ArrayList<>();
        }
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(ARGS_LIST_SORT, new ArrayList<Parcelable>(listSort));
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_sort, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_close) {
            getActivity().finish();
            return true;
        } else
            return super.onOptionsItemSelected(item);
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
        return R.layout.fragment_sort_opportunity;
    }

    @Override
    protected void initView(View view) {
        sortRecyclerView = (RecyclerView) view.findViewById(R.id.list);

        adapter = OpportunitySortAdapter.createInstance(getSortList(), onItemSelectedListener());
        sortRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        sortRecyclerView.setAdapter(adapter);

    }

    private ArrayList<SimpleCheckListItemModel> getSortList() {
        ArrayList<SimpleCheckListItemModel> listData = new ArrayList<>();

        for (SortingTypeViewModel sortingTypeViewModel : listSort) {
            SimpleCheckListItemModel itemModel = new SimpleCheckListItemModel();
            itemModel.setSelected(sortingTypeViewModel.isSelected());
            itemModel.setTitle(sortingTypeViewModel.getName());
            itemModel.setValue(String.valueOf(sortingTypeViewModel.getValue()));
            itemModel.setKey(sortingTypeViewModel.getKey());
            listData.add(itemModel);
        }

        return listData;
    }

    private OpportunitySortAdapter.SimpleCheckListListener onItemSelectedListener() {
        return new OpportunitySortAdapter.SimpleCheckListListener() {
            @Override
            public void onItemSelected(int adapterPosition, SimpleCheckListItemModel item) {

                UnifyTracking.eventOpportunity(
                        OpportunityTrackingEventLabel.EventName.SUBMIT_OPPORTUNITY,
                        OpportunityTrackingEventLabel.EventCategory.OPPORTUNITY_FILTER,
                        AppEventTracking.Action.SUBMIT,
                        item.getTitle()
                );

                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                FilterPass filterPass = new FilterPass(item.getKey(), item.getValue(), item
                        .getTitle());
                bundle.putParcelable(SELECTED_SORT, filterPass);
                bundle.putInt(SELECTED_POSITION, adapterPosition);
                intent.putExtras(bundle);
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();

            }
        };
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
