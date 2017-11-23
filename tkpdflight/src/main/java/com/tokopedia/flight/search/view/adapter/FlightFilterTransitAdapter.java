package com.tokopedia.flight.search.view.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.BaseListCheckableAdapter;
import com.tokopedia.abstraction.base.view.adapter.holder.CheckableBaseViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.search.view.model.resultstatistics.TransitStat;

/**
 * Created by User on 10/26/2017.
 */

public class FlightFilterTransitAdapter extends BaseListCheckableAdapter<TransitStat> {

    public FlightFilterTransitAdapter(Context context, OnBaseListV2AdapterListener<TransitStat> onBaseListV2AdapterListener,
                                      OnCheckableAdapterListener<TransitStat> onCheckableAdapterListener) {
        super(context, onBaseListV2AdapterListener, onCheckableAdapterListener);
    }

    @Override
    public CheckableBaseViewHolder<TransitStat> onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutView(parent, R.layout.item_flight_general_filter);
        return new FlightSearchViewHolder(view, this);
    }

    public static class FlightSearchViewHolder extends CheckableBaseViewHolder<TransitStat> implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

        TextView tvTitle;
        TextView tvDesc;
        CheckBox checkBox;

        public FlightSearchViewHolder(View itemView, BaseListCheckableAdapter<TransitStat> baseListCheckableV2Adapter) {
            super(itemView, baseListCheckableV2Adapter);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvDesc = (TextView) itemView.findViewById(R.id.tv_desc);
            checkBox = (AppCompatCheckBox) itemView.findViewById(R.id.checkbox);
        }

        @Override
        public void bindObject(TransitStat transitStat, boolean isChecked) {
            super.bindObject(transitStat, isChecked);
            tvTitle.setText(transitStat.getTransitType().getValueRes());
            tvDesc.setText(getString(R.string.start_from_x, transitStat.getMinPriceString()));
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
