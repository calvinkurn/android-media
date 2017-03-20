package com.tokopedia.seller.opportunity.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.model.other.RadioButtonItem;
import com.tokopedia.seller.topads.view.fragment.TopAdsFilterRadioButtonFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nisie on 3/6/17.
 */

public class OpportunitySortFragment extends TopAdsFilterRadioButtonFragment {

    public static final String EXTRA_LIST_SORT = "EXTRA_LIST_SORT";
    public static final String SELECTED_POSITION = "SELECTED_POSITION";

    @Override
    protected List<RadioButtonItem> getRadioButtonList() {
        return convertToRadioButtonItem(getArguments().getStringArrayList(EXTRA_LIST_SORT));
    }

    private List<RadioButtonItem> convertToRadioButtonItem(ArrayList<String> listString) {
        ArrayList<RadioButtonItem> listSort = new ArrayList<>();
        for (int i = 0; i < listString.size(); i++) {
            RadioButtonItem sortItem = new RadioButtonItem();
            sortItem.setName(listString.get(i));
            sortItem.setPosition(i);
            sortItem.setValue(String.valueOf(i));
            listSort.add(sortItem);
        }
        selectedRadioButtonItem = listSort.get(0);
        return listSort;
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
        intent.putExtra(SELECTED_POSITION, position);
        return intent;
    }

    public static OpportunitySortFragment createInstance(Bundle extras) {
        OpportunitySortFragment fragment = new OpportunitySortFragment();
        fragment.setArguments(extras);
        return fragment;
    }
}
