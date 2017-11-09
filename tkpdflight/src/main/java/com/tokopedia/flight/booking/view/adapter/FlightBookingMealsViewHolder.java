package com.tokopedia.flight.booking.view.adapter;

import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.holder.BaseMultipleCheckViewHolder;
import com.tokopedia.abstraction.base.view.adapter.holder.BaseViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingMealViewModel;

/**
 * Created by zulfikarrahman on 11/8/17.
 */

public class FlightBookingMealsViewHolder extends BaseMultipleCheckViewHolder<FlightBookingMealViewModel> {

    private TextView mealName;
    private TextView mealPrice;
    private AppCompatCheckBox checkBox;

    public FlightBookingMealsViewHolder(View layoutView) {
        super(layoutView);
        mealName = (TextView) layoutView.findViewById(R.id.meal_name);
        mealPrice = (TextView) layoutView.findViewById(R.id.meal_price);
        checkBox = (AppCompatCheckBox) layoutView.findViewById(R.id.checkbox);
    }

    @Override
    public void bindObject(FlightBookingMealViewModel flightBookingMealViewModel) {
        mealName.setText(flightBookingMealViewModel.getTitle());
        mealPrice.setText(flightBookingMealViewModel.getPrice());
    }

    @Override
    public void bindObject(final FlightBookingMealViewModel flightBookingMealViewModel, boolean checked) {
        bindObject(flightBookingMealViewModel);
        setChecked(checked);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkedCallback != null) {
                    checkedCallback.onItemChecked(flightBookingMealViewModel, checkBox.isChecked());
                }
            }
        });
    }

    @Override
    public boolean isChecked() {
        return checkBox.isChecked();
    }

    @Override
    public void setChecked(boolean checked) {
        checkBox.setChecked(checked);
    }
}
