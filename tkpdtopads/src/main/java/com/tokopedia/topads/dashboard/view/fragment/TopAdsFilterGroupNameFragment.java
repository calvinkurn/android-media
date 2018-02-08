package com.tokopedia.topads.dashboard.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.view.model.RadioButtonItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathaniel on 1/31/2017.
 */
public class TopAdsFilterGroupNameFragment extends TopAdsFilterRadioButtonFragment {

    private long selectedGroupId;
    private long currentGroupId;
    private String currentGroupName;

    public static TopAdsFilterGroupNameFragment createInstance(long groupId, long currentGroupId, String currentGroupName) {
        TopAdsFilterGroupNameFragment fragment = new TopAdsFilterGroupNameFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_GROUP_ID, groupId);
        bundle.putLong(TopAdsExtraConstant.EXTRA_FILTER_CURRENT_GROUP_ID, currentGroupId);
        bundle.putString(TopAdsExtraConstant.EXTRA_FILTER_CURRENT_GROUP_NAME, currentGroupName);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_filter_content_group_name;
    }

    @Override
    protected void setupArguments(Bundle bundle) {
        super.setupArguments(bundle);
        selectedGroupId = bundle.getLong(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_GROUP_ID, selectedGroupId);
        currentGroupId = bundle.getLong(TopAdsExtraConstant.EXTRA_FILTER_CURRENT_GROUP_ID, currentGroupId);
        currentGroupName = bundle.getString(TopAdsExtraConstant.EXTRA_FILTER_CURRENT_GROUP_NAME, currentGroupName);
    }

    @Override
    protected List<RadioButtonItem> getRadioButtonList() {
        List<RadioButtonItem> radioButtonItemList = new ArrayList<>();
        int position = 0;
        String[] statusValueList = getResources().getStringArray(R.array.top_ads_filter_group_name_list_values);
        String[] statusNameList = getResources().getStringArray(R.array.top_ads_filter_group_name_list_entries);
        for (int i = 0; i < statusNameList.length; i++) {
            RadioButtonItem radioButtonItem = new RadioButtonItem();
            radioButtonItem.setName(statusNameList[i]);
            radioButtonItem.setValue(statusValueList[i]);
            radioButtonItem.setPosition(position++);
            radioButtonItemList.add(radioButtonItem);
        }
        if (!TextUtils.isEmpty(currentGroupName)) {
            RadioButtonItem radioButtonItem = new RadioButtonItem();
            radioButtonItem.setName(currentGroupName);
            radioButtonItem.setValue(String.valueOf(currentGroupId));
            radioButtonItem.setPosition(position++);
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
            if (Integer.valueOf(radioButtonItem.getValue()) == selectedGroupId) {
                selectedAdapterPosition = i;
                break;
            }
        }
    }

    @Override
    public String getTitle(Context context) {
        return context.getString(R.string.label_top_ads_groups);
    }

    @Override
    public Intent addResult(Intent intent) {
        if (selectedAdapterPosition >= 0) {
            intent.putExtra(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_GROUP_ID, Long.parseLong(getSelectedRadioValue()));
        }
        return intent;
    }
}