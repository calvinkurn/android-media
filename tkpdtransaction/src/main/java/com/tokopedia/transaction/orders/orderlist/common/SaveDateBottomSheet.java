package com.tokopedia.transaction.orders.orderlist.common;

import android.view.View;
import android.widget.TextView;

import com.tokopedia.transaction.R;

import com.tokopedia.design.component.BottomSheets;

public class SaveDateBottomSheet extends BottomSheets implements View.OnClickListener {

    public SaveDateBottomSheet(){

    }
    @Override
    public int getLayoutResourceId() {
        return R.layout.select_filter_date;
    }

    @Override
    public void initView(View view) {
        TextView startDate = view.findViewById(R.id.set_current_date);
        TextView endDate = view.findViewById(R.id.set_end_date);
        TextView saveDate = view.findViewById(R.id.save_date);
        startDate.setOnClickListener(this);
        endDate.setOnClickListener(this);
        saveDate.setOnClickListener(this);
    }


    @Override
    protected String title() {
        return "Filter Tanggal";
    }

    @Override
    public void onClick(View view) {

    }
}
