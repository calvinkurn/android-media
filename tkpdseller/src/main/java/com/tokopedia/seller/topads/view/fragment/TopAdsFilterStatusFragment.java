package com.tokopedia.seller.topads.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.view.model.RadioButtonItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathaniel on 1/31/2017.
 */

public class TopAdsFilterStatusFragment extends TopAdsFilterRadioButtonFragment {

    private int selectedStatus;

    public static TopAdsFilterStatusFragment createInstance(int status) {
        TopAdsFilterStatusFragment fragment = new TopAdsFilterStatusFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_STATUS, status);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_filter_content_status;
    }

    @Override
    protected void setupArguments(Bundle bundle) {
        super.setupArguments(bundle);
        selectedStatus = bundle.getInt(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_STATUS, selectedStatus);
    }

    @Override
    protected List<RadioButtonItem> getRadioButtonList() {
        List<RadioButtonItem> radioButtonItemList = new ArrayList<>();
        String[] statusValueList = getResources().getStringArray(R.array.filter_status_list_values);
        String[] statusNameList = getResources().getStringArray(R.array.filter_status_list_names);
        for (int i = 0; i < statusNameList.length; i++) {
            RadioButtonItem radioButtonItem = new RadioButtonItem();
            radioButtonItem.setName(statusNameList[i]);
            radioButtonItem.setValue(statusValueList[i]);
            radioButtonItem.setPosition(i);
            radioButtonItemList.add(radioButtonItem);
        }
        updateSelectedPosition(radioButtonItemList);
        return radioButtonItemList;
    }

    private void updateSelectedPosition(List<RadioButtonItem> radioButtonItemList) {
        if (selectedAdapterPosition > -1) {
            return;
        }
        for (int i = 0; i < radioButtonItemList.size(); i++) {
            RadioButtonItem radioButtonItem = radioButtonItemList.get(i);
            if (Integer.valueOf(radioButtonItem.getValue()) == selectedStatus) {
                selectedAdapterPosition = i;
                break;
            }
        }
    }

    @Override
    public String getTitle(Context context) {
        return context.getString(R.string.label_top_ads_status);
    }

    @Override
    public Intent addResult(Intent intent) {
        if (selectedAdapterPosition > -1) {
            intent.putExtra(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_STATUS, Integer.parseInt(getSelectedRadioValue()));
        }
        return intent;
    }
}