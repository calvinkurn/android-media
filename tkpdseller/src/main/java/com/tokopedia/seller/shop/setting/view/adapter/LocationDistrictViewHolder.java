package com.tokopedia.seller.shop.setting.view.adapter;

import android.view.View;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.shop.setting.view.model.RecommendationDistrictViewModel;

/**
 * Created by sebastianuskh on 3/22/17.
 */

class LocationDistrictViewHolder {
    private final TextView districtStringTextView;

    public LocationDistrictViewHolder(View convertView) {
        districtStringTextView = (TextView) convertView.findViewById(R.id.textivew_district_string);
    }

    public void bindData(RecommendationDistrictViewModel recommendationDistrictViewModel) {
        districtStringTextView.setText(recommendationDistrictViewModel.getDistrictString());
    }
}
