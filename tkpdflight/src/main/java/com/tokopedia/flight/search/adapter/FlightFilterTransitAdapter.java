package com.tokopedia.flight.search.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.BaseListCheckableV2Adapter;
import com.tokopedia.abstraction.base.view.adapter.binder.LoadingDataBinder;
import com.tokopedia.abstraction.base.view.adapter.binder.NoResultDataBinder;
import com.tokopedia.abstraction.base.view.adapter.binder.RetryDataBinder;
import com.tokopedia.abstraction.base.view.adapter.holder.CheckableBaseViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.search.view.model.resultstatistics.TransitStat;

/**
 * Created by User on 10/26/2017.
 */

public class FlightFilterTransitAdapter extends BaseListCheckableV2Adapter<TransitStat> {

    public FlightFilterTransitAdapter(OnBaseListV2AdapterListener<TransitStat> onBaseListV2AdapterListener){
        super(onBaseListV2AdapterListener);
    }

    @Override
    public CheckableBaseViewHolder<TransitStat> onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutView(parent, R.layout.item_flight_transit_filter);
        return new FlightSearchViewHolder(view, this);
    }

    public static class FlightSearchViewHolder extends CheckableBaseViewHolder<TransitStat> implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

        TextView tvDeparture;
        TextView tvArrival;
        TextView tvAirline;
        AppCompatTextView tvPrice;
        AppCompatTextView tvDuration;
        AppCompatCheckBox checkBox;

        public FlightSearchViewHolder(View itemView, BaseListCheckableV2Adapter<TransitStat> baseListCheckableV2Adapter) {
            super(itemView, baseListCheckableV2Adapter);
            tvDeparture = (TextView) itemView.findViewById(R.id.tv_departure);
            tvArrival = (TextView) itemView.findViewById(R.id.tv_arrival);
            tvAirline = (TextView) itemView.findViewById(R.id.tv_airline);
            tvPrice = (AppCompatTextView) itemView.findViewById(R.id.tv_total_price);
            tvDuration = (AppCompatTextView) itemView.findViewById(R.id.tv_duration);
            checkBox = (AppCompatCheckBox) itemView.findViewById(R.id.checkbox);
        }

        @Override
        public void bindObject(TransitStat transitStat, boolean isChecked) {
            super.bindObject(transitStat, isChecked);
            tvDeparture.setText(String.valueOf(transitStat.getMinPrice()));
            tvArrival.setText(transitStat.getMinPrice() + (isChecked? "Checked":"Not Checked"));
            itemView.setOnClickListener(this);
            //tvAirline.setText(flightSearchViewModel.getAirline());
        }

        @Override
        public CheckBox getCheckBox() {
            return checkBox;
        }

        @Override
        public void onClick(View v) {
            toggle();
        }
    }

    @Nullable
    @Override
    protected LoadingDataBinder createLoadingDataBinder() {
        return null;
    }

    @Nullable
    @Override
    protected NoResultDataBinder createEmptyViewBinder() {
        return null;
    }

    @Nullable
    @Override
    protected NoResultDataBinder createEmptyViewSearchBinder() {
        return null;
    }

    @Nullable
    @Override
    protected RetryDataBinder createRetryDataBinder() {
        return null;
    }

}
