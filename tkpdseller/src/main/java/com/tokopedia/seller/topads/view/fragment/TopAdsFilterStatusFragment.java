package com.tokopedia.seller.topads.view.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.model.other.RadioButtonItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathaniel on 1/31/2017.
 */

public class TopAdsFilterStatusFragment extends TopAdsFilterRadioButtonFragment {

    public static TopAdsFilterStatusFragment createInstance(int status) {
        TopAdsFilterStatusFragment fragment = new TopAdsFilterStatusFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TopAdsExtraConstant.EXTRA_FILTER_STATUS_VALUE, status);
        fragment.setArguments(bundle);
        return fragment;
    }

    private int selectedStatus;

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_filter_content_status;
    }

    @Override
    protected void setupArguments(Bundle bundle) {
        super.setupArguments(bundle);
        selectedStatus = bundle.getInt(TopAdsExtraConstant.EXTRA_FILTER_STATUS_VALUE);
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
            radioButtonItemList.add(radioButtonItem);
            if (selectedStatus == Integer.parseInt(statusValueList[i])) {
                selectedRadioButtonItem = radioButtonItem;
                selectedPosition = i;
            }
        }
        return radioButtonItemList;
    }

    @Override
    public String getTitle(Context context) {
        return context.getString(R.string.label_top_ads_status);
    }

    @Override
    public Intent addResult(Intent intent) {
        intent.putExtra(TopAdsExtraConstant.EXTRA_FILTER_STATUS_VALUE, Integer.parseInt(selectedRadioButtonItem.getValue()));
        return intent;
    }
}