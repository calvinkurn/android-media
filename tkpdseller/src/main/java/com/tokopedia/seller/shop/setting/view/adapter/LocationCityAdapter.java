package com.tokopedia.seller.shop.setting.view.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import com.tokopedia.seller.R;
import com.tokopedia.seller.shop.setting.view.model.RecommendationDistrictViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sebastianuskh on 3/22/17.
 */

public class LocationCityAdapter extends ArrayAdapter<String> {

    private List<RecommendationDistrictViewModel> datas;
    private int selected = -1;

    public LocationCityAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LocationDistrictViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.shop_setting_location_district_autocomplete, parent, false);
            viewHolder = new LocationDistrictViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (LocationDistrictViewHolder) convertView.getTag();
        }
        viewHolder.bindData(datas.get(position));
        return convertView;
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

    public void addDistrictModel(List<RecommendationDistrictViewModel> viewModels) {
        datas = viewModels;
        notifyDataSetChanged();
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public RecommendationDistrictViewModel getSelected() throws Exception {
        return datas.get(selected);
    }
}
