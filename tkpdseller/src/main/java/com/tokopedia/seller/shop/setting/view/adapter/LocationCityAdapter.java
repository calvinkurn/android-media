package com.tokopedia.seller.shop.setting.view.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.tokopedia.seller.R;
import com.tokopedia.seller.shop.setting.view.model.RecommendationDistrictItemViewModel;
import com.tokopedia.seller.shop.setting.view.model.RecommendationDistrictViewModel;

import java.util.List;

/**
 * Created by sebastianuskh on 3/22/17.
 */

public class LocationCityAdapter extends ArrayAdapter<String> {

    public static final int UNSELECTED_DISTRICT = -1;
    private RecommendationDistrictViewModel datas;
    private int selected = UNSELECTED_DISTRICT;

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
        viewHolder.bindData(datas.getItems().get(position), datas.getStringTyped());
        return convertView;
    }

    @Override
    public int getCount() {
        return datas.getItems().size();
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return datas.getItems().get(position).getDistrictString();
    }

    public void addDistrictModel(RecommendationDistrictViewModel viewModels) {
        datas = viewModels;
        notifyDataSetChanged();
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public RecommendationDistrictItemViewModel getSelected() throws RuntimeException {
        return datas.getItems().get(selected);
    }

    public void clearSelectedDistrict() {
        selected = UNSELECTED_DISTRICT;
    }
}
