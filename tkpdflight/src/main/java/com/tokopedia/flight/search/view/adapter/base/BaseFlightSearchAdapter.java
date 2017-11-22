package com.tokopedia.flight.search.view.adapter.base;

import android.content.Context;
import android.support.annotation.Nullable;

import com.tokopedia.abstraction.base.view.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.binder.EmptyDataBinder;
import com.tokopedia.abstraction.base.view.adapter.binder.NoResultDataBinder;
import com.tokopedia.abstraction.base.view.adapter.type.ItemType;
import com.tokopedia.flight.R;

import java.util.List;

/**
 * Created by User on 11/22/2017.
 */

public abstract class BaseFlightSearchAdapter<T extends ItemType> extends BaseListAdapter<T> {

    public BaseFlightSearchAdapter(Context context, OnBaseListV2AdapterListener<T> onBaseListV2AdapterListener) {
        super(context, onBaseListV2AdapterListener);
    }

    public BaseFlightSearchAdapter(Context context, @Nullable List<T> data, int rowPerPage, OnBaseListV2AdapterListener<T> onBaseListV2AdapterListener) {
        super(context, data, rowPerPage, onBaseListV2AdapterListener);
    }

    @Nullable
    @Override
    protected NoResultDataBinder createEmptyViewBinder() {
        EmptyDataBinder emptyDataBinder = new EmptyDataBinder(this, R.drawable.ic_flight_empty_state);
        emptyDataBinder.setEmptyContentText(context.getString(R.string.flight_there_is_no_flight_available));
        emptyDataBinder.setEmptyButtonItemText(context.getString(R.string.change_date));
        return emptyDataBinder;
    }
}
