package com.tokopedia.flight.search.view.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.BaseListCheckableV2Adapter;
import com.tokopedia.abstraction.base.view.adapter.binder.LoadingDataBinder;
import com.tokopedia.abstraction.base.view.adapter.binder.NoResultDataBinder;
import com.tokopedia.abstraction.base.view.adapter.binder.RetryDataBinder;
import com.tokopedia.abstraction.base.view.adapter.holder.CheckableBaseViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.detail.util.FlightAirlineIconUtil;
import com.tokopedia.flight.search.view.model.resultstatistics.AirlineStat;

/**
 * Created by User on 10/26/2017.
 */

public class FlightFilterAirlineAdapter extends BaseListCheckableV2Adapter<AirlineStat> {

    public FlightFilterAirlineAdapter(OnBaseListV2AdapterListener<AirlineStat> onBaseListV2AdapterListener,
                                      OnCheckableAdapterListener<AirlineStat> onCheckableAdapterListener){
        super(onBaseListV2AdapterListener, onCheckableAdapterListener);
    }

    @Override
    public CheckableBaseViewHolder<AirlineStat> onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutView(parent, R.layout.item_flight_airline_filter);
        return new FlightSearchViewHolder(view, this);
    }

    public static class FlightSearchViewHolder extends CheckableBaseViewHolder<AirlineStat> implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

        ImageView ivLogo;
        TextView tvTitle;
        TextView tvDesc;
        CheckBox checkBox;

        public FlightSearchViewHolder(View itemView, BaseListCheckableV2Adapter<AirlineStat> baseListCheckableV2Adapter) {
            super(itemView, baseListCheckableV2Adapter);
            ivLogo = (ImageView) itemView.findViewById(R.id.iv_logo);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvDesc = (TextView) itemView.findViewById(R.id.tv_desc);
            checkBox = (AppCompatCheckBox) itemView.findViewById(R.id.checkbox);
        }

        @Override
        public void bindObject(AirlineStat airlineStat, boolean isChecked) {
            super.bindObject(airlineStat, isChecked);
            ivLogo.setImageResource(FlightAirlineIconUtil.getImageResource(airlineStat.getAirlineDB().getId()));
            tvTitle.setText(airlineStat.getAirlineDB().getName());
            tvDesc.setText(getString(R.string.start_from_x, airlineStat.getMinPriceString()));
            itemView.setOnClickListener(this);
        }

        @Override
        public CompoundButton getCheckable() {
            return checkBox;
        }

        @Override
        public void onClick(View v) {
            toggle();
        }
    }

}
