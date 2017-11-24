package com.tokopedia.flight.dashboard.view.adapter.viewholder;

import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.holder.BaseViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightClassViewModel;

/**
 * Created by alvarisi on 10/30/17.
 */

public class FlightClassViewHolder extends BaseViewHolder<FlightClassViewModel> {
    public static final int LAYOUT = R.layout.item_flight_class;
    private AppCompatTextView titleTextView;

    public FlightClassViewHolder(View itemView) {
        super(itemView);
        titleTextView = (AppCompatTextView) itemView.findViewById(R.id.tv_title);
    }

    @Override
    public void bindObject(FlightClassViewModel flightClassViewModel) {
        titleTextView.setText(String.valueOf(flightClassViewModel.getTitle()));
    }
}
