package com.tokopedia.flight.search.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.holder.BaseCheckableViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.flight.R;
import com.tokopedia.flight.search.view.model.resultstatistics.AirlineStat;

/**
 * Created by alvarisi on 12/21/17.
 */

public class FlightFilterAirlineViewHolder extends BaseCheckableViewHolder<AirlineStat> implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_flight_airline_filter;

    ImageView ivLogo;
    TextView tvTitle;
    TextView tvDesc;
    CheckBox checkBox;

    public FlightFilterAirlineViewHolder(View itemView, CheckableInteractionListener checkableInteractionListener) {
        super(itemView, checkableInteractionListener);
        ivLogo = (ImageView) itemView.findViewById(R.id.iv_logo);
        tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        tvDesc = (TextView) itemView.findViewById(R.id.tv_desc);
        checkBox = (AppCompatCheckBox) itemView.findViewById(R.id.checkbox);
    }

    @Override
    public void bind(AirlineStat airlineStat) {
        super.bind(airlineStat);
        ImageHandler.loadImageWithPlaceholder(ivLogo, airlineStat.getAirlineDB().getLogo(), R.drawable.ic_airline_default);
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
