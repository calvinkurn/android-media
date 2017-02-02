package com.tokopedia.seller.topads.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.model.other.RadioButtonItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathaniel on 1/31/2017.
 */

public class TopAdsFilterGroupNameFragment extends TopAdsFilterRadioButtonFragment {

    private int selectedGroupId;
    private int currentGroupId;
    private String currentGroupName;

    public static TopAdsFilterGroupNameFragment createInstance(int groupId, int currentGroupId, String currentGroupName) {
        TopAdsFilterGroupNameFragment fragment = new TopAdsFilterGroupNameFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_GROUP_ID, groupId);
        bundle.putInt(TopAdsExtraConstant.EXTRA_FILTER_CURRENT_GROUP_ID, currentGroupId);
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
        selectedGroupId = bundle.getInt(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_GROUP_ID, selectedGroupId);
        currentGroupId = bundle.getInt(TopAdsExtraConstant.EXTRA_FILTER_CURRENT_GROUP_ID, currentGroupId);
        currentGroupName = bundle.getString(TopAdsExtraConstant.EXTRA_FILTER_CURRENT_GROUP_NAME, currentGroupName);
    }

    @Override
    protected List<RadioButtonItem> getRadioButtonList() {
        List<RadioButtonItem> radioButtonItemList = new ArrayList<>();
        int position = 0;
        if (!TextUtils.isEmpty(currentGroupName)) {
            RadioButtonItem radioButtonItem = new RadioButtonItem();
            radioButtonItem.setName(currentGroupName);
            radioButtonItem.setValue(String.valueOf(currentGroupId));
            radioButtonItem.setPosition(position++);
            radioButtonItemList.add(radioButtonItem);
        }
        String[] statusValueList = getResources().getStringArray(R.array.filter_group_name_list_values);
        String[] statusNameList = getResources().getStringArray(R.array.filter_group_name_list_names);
        for (int i = 0; i < statusNameList.length; i++) {
            RadioButtonItem radioButtonItem = new RadioButtonItem();
            radioButtonItem.setName(statusNameList[i]);
            radioButtonItem.setValue(statusValueList[i]);
            radioButtonItem.setPosition(position++);
            radioButtonItemList.add(radioButtonItem);
        }
        updateSelectedPosition(radioButtonItemList);
        return radioButtonItemList;
    }

    private void updateSelectedPosition(List<RadioButtonItem> radioButtonItemList) {
        if (selectedRadioButtonItem != null) {
            return;
        }
        for (int i = 0; i < radioButtonItemList.size(); i++) {
            RadioButtonItem radioButtonItem = radioButtonItemList.get(i);
            if (Integer.valueOf(radioButtonItem.getValue()) == selectedGroupId) {
                selectedRadioButtonItem = radioButtonItem;
                break;
            }
        }
    }

    @Override
    public String getTitle(Context context) {
        return context.getString(R.string.label_top_ads_promo_group);
    }

    @Override
    public Intent addResult(Intent intent) {
        intent.putExtra(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_GROUP_ID, Integer.parseInt(selectedRadioButtonItem.getValue()));
        return intent;
    }
}