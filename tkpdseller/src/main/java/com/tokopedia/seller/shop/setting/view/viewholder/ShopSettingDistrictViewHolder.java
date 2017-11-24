package com.tokopedia.seller.shop.setting.view.viewholder;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.shop.setting.view.adapter.ShopSettingLocationCityAdapter;
import com.tokopedia.seller.shop.setting.view.listener.ShopSettingDistrictViewHolderListener;
import com.tokopedia.seller.shop.setting.view.model.RecommendationDistrictViewModel;

/**
 * Created by sebastianuskh on 3/23/17.
 */

public class ShopSettingDistrictViewHolder {

    private final Context context;
    private final ShopSettingDistrictViewHolderListener listener;
    private final TextInputLayout districtInputLayout;
    private final ShopSettingLocationCityAdapter locationDistrictAdapter;

    public ShopSettingDistrictViewHolder(Context context, View view, ShopSettingDistrictViewHolderListener listener) {
        this.context = context;
        this.listener = listener;
        AutoCompleteTextView locationDistrictTextView = (AutoCompleteTextView) view.findViewById(R.id.edit_text_shop_setting_location_district);
        districtInputLayout = (TextInputLayout) view.findViewById(R.id.input_layout_shop_setting_location_district);
        locationDistrictAdapter = new ShopSettingLocationCityAdapter(context, android.R.layout.simple_dropdown_item_1line);
        locationDistrictTextView.setAdapter(locationDistrictAdapter);
        locationDistrictTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                locationDistrictAdapter.clearSelectedDistrict();
                ShopSettingDistrictViewHolder.this.listener.getRecomendationLocationDistrict(s.toString());
            }
        });
        locationDistrictTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                locationDistrictAdapter.setSelected(position);
            }
        });
    }

    public void renderRecomendationDistrictModel(RecommendationDistrictViewModel viewModels) {
        locationDistrictAdapter.addDistrictModel(viewModels);
    }

    public int getDistrictCode() throws RuntimeException {
        try {
            return locationDistrictAdapter.getSelected().getDistrictId();
        } catch (RuntimeException e) {
            districtInputLayout.setError(context.getString(R.string.shop_setting_city_not_filled));
            throw new RuntimeException("District Kosong");
        }
    }
}
