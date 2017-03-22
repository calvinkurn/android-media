package com.tokopedia.seller.shop.setting.view.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import com.tokopedia.seller.shop.setting.view.model.RecommendationDistrictViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sebastianuskh on 3/22/17.
 */

public class LocationCityAdapter extends ArrayAdapter<String>{

    private List<RecommendationDistrictViewModel> datas;

    public LocationCityAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return datas.get(position).getDistrictString();
    }

    private List<String> getStringList(List<RecommendationDistrictViewModel> datas) {
        List<String> strings = new ArrayList<>();
        for(RecommendationDistrictViewModel viewModel : datas){
            strings.add(viewModel.getDistrictString());
        }
        return strings;
    }

    public void addDistrictModel(List<RecommendationDistrictViewModel> viewModels) {
        datas = viewModels;
        notifyDataSetChanged();
    }
}
