package com.tokopedia.flight.booking.view.adapter.viewholder;

import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.BaseListCheckableAdapter;
import com.tokopedia.abstraction.base.view.adapter.holder.CheckableBaseViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityViewModel;

/**
 * Created by zulfikarrahman on 11/8/17.
 */

public class FlightBookingMealsViewHolder extends CheckableBaseViewHolder<FlightBookingAmenityViewModel> implements View.OnClickListener {

    private TextView mealName;
    private TextView mealPrice;
    private AppCompatCheckBox checkBox;

    public FlightBookingMealsViewHolder(View itemView,
                                        BaseListCheckableAdapter<FlightBookingAmenityViewModel> baseListCheckableV2Adapter) {
        super(itemView, baseListCheckableV2Adapter);
        mealName = (TextView) itemView.findViewById(R.id.meal_name);
        mealPrice = (TextView) itemView.findViewById(R.id.meal_price);
        checkBox = (AppCompatCheckBox) itemView.findViewById(R.id.checkbox);
    }

    @Override
    public CompoundButton getCheckable() {
        return checkBox;
    }

    @Override
    public void bindObject(FlightBookingAmenityViewModel flightBookingAmenityViewModel, boolean isChecked) {
        super.bindObject(flightBookingAmenityViewModel, isChecked);
        mealName.setText(flightBookingAmenityViewModel.getTitle());
        mealPrice.setText(flightBookingAmenityViewModel.getPrice());
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        toggle();
    }

}
