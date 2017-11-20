package com.tokopedia.flight.search.view.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
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
import com.tokopedia.flight.search.view.model.filter.RefundableEnum;
import com.tokopedia.flight.search.view.model.resultstatistics.RefundableStat;

/**
 * Created by User on 10/26/2017.
 */

public class FlightFilterRefundableAdapter extends BaseListCheckableV2Adapter<RefundableStat> {

    public FlightFilterRefundableAdapter(OnBaseListV2AdapterListener<RefundableStat> onBaseListV2AdapterListener,
                                         OnCheckableAdapterListener<RefundableStat> onCheckableAdapterListener){
        super(onBaseListV2AdapterListener, onCheckableAdapterListener);
    }

    @Override
    public CheckableBaseViewHolder<RefundableStat> onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutView(parent, R.layout.item_flight_general_filter);
        return new FlightSearchViewHolder(view, this);
    }

    public static class FlightSearchViewHolder extends CheckableBaseViewHolder<RefundableStat> implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

        TextView tvTitle;
        TextView tvDesc;
        CheckBox checkBox;

        public FlightSearchViewHolder(View itemView, BaseListCheckableV2Adapter<RefundableStat> baseListCheckableV2Adapter) {
            super(itemView, baseListCheckableV2Adapter);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvDesc = (TextView) itemView.findViewById(R.id.tv_desc);
            checkBox = (AppCompatCheckBox) itemView.findViewById(R.id.checkbox);
        }

        @Override
        public void bindObject(RefundableStat refundableStat, boolean isChecked) {
            super.bindObject(refundableStat, isChecked);
            tvTitle.setText(refundableStat.getRefundableEnum().getValueRes());
            tvDesc.setText(getString(R.string.start_from_x, refundableStat.getMinPriceString()));
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
