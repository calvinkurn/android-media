package com.tokopedia.seller.opportunity.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.seller.R;
import com.tokopedia.seller.opportunity.viewmodel.CategoryViewModel;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.view.fragment.TopAdsFilterRadioButtonFragment;
import com.tokopedia.seller.topads.view.model.RadioButtonItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nisie on 3/15/17.
 */

public class OpportunityCategoryFragment extends TopAdsFilterRadioButtonFragment {

    public static final String SELECTED_POSITION = "CATEGORY_SELECTED_POSITION";

    private int selectedCategoryValue;

    public static OpportunityCategoryFragment createInstance(int status) {
        OpportunityCategoryFragment fragment = new OpportunityCategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(SELECTED_POSITION, status);
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
        selectedCategoryValue = bundle.getInt(SELECTED_POSITION, selectedCategoryValue);
    }


    @Override
    public String getTitle(Context context) {
        return context.getString(R.string.filter);
    }

    @Override
    public Intent addResult(Intent intent) {
        return intent;
    }

    @Override
    protected List<RadioButtonItem> getRadioButtonList() {

        ArrayList<CategoryViewModel> listCategory = new ArrayList<>();
        CategoryViewModel cat1 = new CategoryViewModel();
        cat1.setCategoryId(1);
        cat1.setCategoryName("Category 1");
        cat1.setTreeLevel(1);

        CategoryViewModel cat2 = new CategoryViewModel();
        cat2.setCategoryId(2);
        cat2.setCategoryName("Category 2");
        cat2.setTreeLevel(2);

        ArrayList<CategoryViewModel> list2 = new ArrayList<>();
        CategoryViewModel cat11 = new CategoryViewModel();
        cat11.setCategoryId(11);
        cat11.setCategoryName("Category 1-1");
        cat11.setTreeLevel(2);
        CategoryViewModel cat12 = new CategoryViewModel();
        cat12.setCategoryId(12);
        cat12.setCategoryName("Category 1-2");
        cat12.setTreeLevel(2);

        list2.add(cat11);
        list2.add(cat12);

        cat1.setListChild(list2);

        listCategory.add(cat1);
        listCategory.add(cat2);

        List<RadioButtonItem> radioButtonItemList = new ArrayList<>();
        for (int i = 0; i < listCategory.size(); i++) {
            RadioButtonItem radioButtonItem = new RadioButtonItem();
            radioButtonItem.setName(listCategory.get(i).getCategoryName());
            radioButtonItem.setValue(String.valueOf(listCategory.get(i).getCategoryId()));
            radioButtonItem.setPosition(i);
            radioButtonItemList.add(radioButtonItem);
        }
        updateSelectedPosition(radioButtonItemList);
        return radioButtonItemList;
    }

    private void updateSelectedPosition(List<RadioButtonItem> radioButtonItemList) {
        if (selectedCategoryValue == 0) { // default to all etalase
            selectedAdapterPosition = 0;
            return;
        }
        if (selectedAdapterPosition > 0) { // has been updated for first time only
            return;
        }
        for (int i = 0; i < radioButtonItemList.size(); i++) {
            RadioButtonItem radioButtonItem = radioButtonItemList.get(i);
            if (Integer.valueOf(radioButtonItem.getValue()) == selectedCategoryValue) {
                selectedAdapterPosition = i;
                break;
            }
        }
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
}
