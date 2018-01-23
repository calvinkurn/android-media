package com.tokopedia.flight.detail.view.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView;
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration;
import com.tokopedia.flight.R;

/**
 * Created by zulfikarrahman on 12/11/17.
 */

public class FlightDetailRecyclerView extends VerticalRecyclerView {
    public FlightDetailRecyclerView(Context context) {
        super(context);
    }

    public FlightDetailRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FlightDetailRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected ItemDecoration getItemDecoration() {
        return new DividerItemDecoration(getContext(), ContextCompat.getDrawable(getContext(), R.drawable.line_divider_flight_detail));
    }
}
