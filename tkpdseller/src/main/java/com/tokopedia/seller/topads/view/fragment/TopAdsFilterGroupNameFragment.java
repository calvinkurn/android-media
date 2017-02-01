package com.tokopedia.seller.topads.view.fragment;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.model.other.RadioButtonItem;
import com.tokopedia.seller.topads.view.adapter.TopAdsBasicRadioButtonAdapter;
import com.tokopedia.seller.topads.view.widget.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathaniel on 1/31/2017.
 */

public class TopAdsFilterGroupNameFragment extends TopAdsFilterRadioButtonFragment {

    @Override
    public String getTitle(Context context) {
        return context.getString(R.string.label_top_ads_promo_group);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_filter_content_group_name;
    }

    @Override
    protected List<RadioButtonItem> getRadioButtonList() {
        List<RadioButtonItem> radioButtonItemList = new ArrayList<>();
        String[] statusValueList = getResources().getStringArray(R.array.filter_group_name_list_values);
        String[] statusNameList = getResources().getStringArray(R.array.filter_group_name_list_names);
        for (int i = 0; i < statusNameList.length; i++) {
            RadioButtonItem radioButtonItem = new RadioButtonItem();
            radioButtonItem.setName(statusNameList[i]);
            radioButtonItem.setValue(Integer.valueOf(statusValueList[i]));
            radioButtonItemList.add(radioButtonItem);
        }
        return radioButtonItemList;
    }
}