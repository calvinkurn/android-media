package com.tokopedia.seller.shop.setting.view.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import com.tokopedia.seller.shop.setting.view.model.RecommendationDistrictViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sebastianuskh on 3/22/17.
 */

public class LocationCityAdapter extends ArrayAdapter<RecommendationDistrictViewModel>{

    public LocationCityAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }


}
