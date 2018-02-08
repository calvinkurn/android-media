package com.tokopedia.topads.dashboard.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.view.model.RadioButtonItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathaniel on 1/31/2017.
 */
public abstract class TopAdsFilterStatusFragment extends TopAdsFilterRadioButtonFragment {

    private int selectedStatus;

    public abstract String[] getStatusValueList();

    public abstract String[] getStatusNameList();

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
        String[] statusValueList = getStatusValueList();
        String[] statusNameList = getStatusNameList();
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
        if (selectedAdapterPosition > 0) { // has been updated for first time only
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