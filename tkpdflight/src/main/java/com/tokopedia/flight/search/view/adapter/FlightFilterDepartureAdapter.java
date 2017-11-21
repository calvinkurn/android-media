package com.tokopedia.flight.search.view.adapter;

import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.BaseListCheckableAdapter;
import com.tokopedia.abstraction.base.view.adapter.holder.CheckableBaseViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.search.view.model.resultstatistics.DepartureStat;

/**
 * Created by User on 10/26/2017.
 */

public class FlightFilterDepartureAdapter extends BaseListCheckableAdapter<DepartureStat> {

    public FlightFilterDepartureAdapter(OnBaseListV2AdapterListener<DepartureStat> onBaseListV2AdapterListener,
                                        OnCheckableAdapterListener<DepartureStat> onCheckableAdapterListener){
        super(onBaseListV2AdapterListener, onCheckableAdapterListener);
    }

    @Override
    public CheckableBaseViewHolder<DepartureStat> onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutView(parent, R.layout.item_flight_general_filter);
        return new FlightSearchViewHolder(view, this);
    }

    public static class FlightSearchViewHolder extends CheckableBaseViewHolder<DepartureStat>
            implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

        TextView tvTitle;
        TextView tvDesc;
        CheckBox checkBox;

        public FlightSearchViewHolder(View itemView, BaseListCheckableAdapter<DepartureStat> baseListCheckableV2Adapter) {
            super(itemView, baseListCheckableV2Adapter);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvDesc = (TextView) itemView.findViewById(R.id.tv_desc);
            checkBox = (AppCompatCheckBox) itemView.findViewById(R.id.checkbox);
        }

        @Override
        public void bindObject(DepartureStat departureStat, boolean isChecked) {
            super.bindObject(departureStat, isChecked);
            tvTitle.setText(departureStat.getDepartureTime().getValueRes());
            tvDesc.setText(getString(R.string.start_from_x, departureStat.getMinPriceString()));
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
