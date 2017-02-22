package com.tokopedia.seller.topads.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.domain.model.other.RadioButtonItem;

import java.util.ArrayList;
import java.util.List;

public class TopAdsFilterEtalaseFragment extends TopAdsFilterRadioButtonFragment {

    private int selectedEtalaseId;

    public static TopAdsFilterEtalaseFragment createInstance(int etalaseID) {
        TopAdsFilterEtalaseFragment fragment = new TopAdsFilterEtalaseFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_ETALASE, etalaseID);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_filter_content_etalase;
    }

    @Override
    protected void setupArguments(Bundle bundle) {
        super.setupArguments(bundle);
        selectedEtalaseId = bundle.getInt(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_ETALASE, selectedEtalaseId);
    }

    @Override
    protected List<RadioButtonItem> getRadioButtonList() {
        List<RadioButtonItem> radioButtonItemList = new ArrayList<>();
        //TODO will update from API etalase
        // dummy
        String[] statusValueList = getResources().getStringArray(R.array.filter_status_promo_list_values);
        String[] statusNameList = getResources().getStringArray(R.array.filter_status_promo_list_names);
        for (int i = 0; i < statusNameList.length; i++) {
            RadioButtonItem radioButtonItem = new RadioButtonItem();
            radioButtonItem.setName(statusNameList[i]);
            radioButtonItem.setValue(statusValueList[i] + 1);
            radioButtonItem.setPosition(i);
            radioButtonItemList.add(radioButtonItem);
        }
        // all etalase filter option
        RadioButtonItem radioButtonItem = new RadioButtonItem();
        radioButtonItem.setName(getString(R.string.title_all_etalase));
        radioButtonItem.setValue("0");
        radioButtonItem.setPosition(0);
        radioButtonItemList.add(0, radioButtonItem);

        updateSelectedPosition(radioButtonItemList);

//        String[] statusValueList = getResources().getStringArray(R.array.filter_status_list_values);
//        String[] statusNameList = getResources().getStringArray(R.array.filter_status_list_names);
//        for (int i = 0; i < statusNameList.length; i++) {
//            RadioButtonItem radioButtonItem = new RadioButtonItem();
//            radioButtonItem.setName(statusNameList[i]);
//            radioButtonItem.setValue(statusValueList[i]);
//            radioButtonItem.setPosition(i);
//            radioButtonItemList.add(radioButtonItem);
//        }
//        updateSelectedPosition(radioButtonItemList);
        return radioButtonItemList;
    }

    private void updateSelectedPosition(List<RadioButtonItem> radioButtonItemList) {
        if (selectedRadioButtonItem != null) {
            return;
        }
        for (int i = 0; i < radioButtonItemList.size(); i++) {
            RadioButtonItem radioButtonItem = radioButtonItemList.get(i);
            if (Integer.valueOf(radioButtonItem.getValue()) == selectedEtalaseId) {
                selectedRadioButtonItem = radioButtonItem;
                break;
            }
        }
    }

    @Override
    public String getTitle(Context context) {
        return context.getString(R.string.label_top_ads_etalase);
    }

    @Override
    public Intent addResult(Intent intent) {
        if (selectedRadioButtonItem != null) {
            intent.putExtra(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_ETALASE,
                    Integer.parseInt(selectedRadioButtonItem.getValue()));
        }
        return intent;
    }
}