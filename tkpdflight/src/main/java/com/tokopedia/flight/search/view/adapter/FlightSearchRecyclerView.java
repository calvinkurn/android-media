package com.tokopedia.flight.search.view.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView;
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration;
import com.tokopedia.flight.R;

/**
 * Created by zulfikarrahman on 11/14/17.
 */

public class FlightSearchRecyclerView extends VerticalRecyclerView {
    public FlightSearchRecyclerView(Context context) {
        super(context);
    }

    public FlightSearchRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FlightSearchRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected ItemDecoration getItemDecoration() {
        return new DividerItemDecoration(getContext(), ContextCompat.getDrawable(getContext(), R.drawable.line_divider_flight_search));
    }
}
