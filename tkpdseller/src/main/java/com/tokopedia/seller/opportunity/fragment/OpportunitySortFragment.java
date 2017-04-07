package com.tokopedia.seller.opportunity.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.tokopedia.seller.R;
import com.tokopedia.seller.opportunity.viewmodel.SortingTypeViewModel;
import com.tokopedia.seller.topads.view.fragment.TopAdsFilterRadioButtonFragment;
import com.tokopedia.seller.topads.view.model.RadioButtonItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nisie on 3/6/17.
 */

public class OpportunitySortFragment extends TopAdsFilterRadioButtonFragment {

    public static final String EXTRA_LIST_SORT = "EXTRA_LIST_SORT";
    public static final String SELECTED_VALUE = "SELECTED_VALUE";
    private static final String ARGS_LIST_SORT = "ARGS_LIST_SORT";
    List<SortingTypeViewModel> listSort;

    @Override
    protected List<RadioButtonItem> getRadioButtonList() {
        if (getArguments().getParcelableArrayList(EXTRA_LIST_SORT) != null)
            listSort = getArguments().getParcelableArrayList(EXTRA_LIST_SORT);
        return convertToRadioButtonItem(listSort);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)
            listSort = savedInstanceState.getParcelableArrayList(ARGS_LIST_SORT);
        else
            listSort = new ArrayList<>();
    }

    private List<RadioButtonItem> convertToRadioButtonItem(List<SortingTypeViewModel> listSort) {
        ArrayList<RadioButtonItem> listRadio = new ArrayList<>();
        for (int i = 0; i < listSort.size(); i++) {
            RadioButtonItem sortItem = new RadioButtonItem();
            sortItem.setName(listSort.get(i).getSortingTypeName());
            sortItem.setPosition(i);
            sortItem.setValue(String.valueOf(listSort.get(i).getSortingTypeId()));
            listRadio.add(sortItem);
        }
        selectedAdapterPosition = 0;
        return listRadio;
    }

    @Override
    public String getTitle(Context context) {
        return getString(R.string.title_sort_by);
    }

    @Override
    public Intent addResult(Intent intent) {
        return intent;
    }

    @Override
    public void onItemSelected(RadioButtonItem radioButtonItem, int position) {
        super.onItemSelected(radioButtonItem, position);
        getActivity().setResult(Activity.RESULT_OK, getResultIntent(radioButtonItem, position));
        getActivity().finish();
    }

    private Intent getResultIntent(RadioButtonItem radioButtonItem, int position) {
        Intent intent = new Intent();
        intent.putExtra(SELECTED_VALUE, listSort.get(position).getSortingTypeId());
        return intent;
    }

    public static OpportunitySortFragment createInstance(Bundle extras) {
        OpportunitySortFragment fragment = new OpportunitySortFragment();
        fragment.setArguments(extras);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(ARGS_LIST_SORT, new ArrayList<Parcelable>(listSort));
        super.onSaveInstanceState(outState);
    }
}
