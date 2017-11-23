package com.tokopedia.flight.airport.view.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;

import com.tokopedia.abstraction.base.view.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.holder.BaseViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by zulfikarrahman on 10/24/17.
 */

public class FlightAirportAdapter extends BaseListAdapter<FlightAirportDB>
        implements FlightAirportViewHolder.FilterTextListener{

    private String filterText;

    public FlightAirportAdapter(Context context, OnBaseListV2AdapterListener<FlightAirportDB> onBaseListV2AdapterListener) {
        super(context, onBaseListV2AdapterListener);
    }

    @Override
    public BaseViewHolder<FlightAirportDB> onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutView(parent, R.layout.item_flight_airport);
        return new FlightAirportViewHolder(view, this);
    }

    public void setFilterText(String filterText) {
        this.filterText = filterText;
    }

    public String getFilterText() {
        return filterText;
    }
}
