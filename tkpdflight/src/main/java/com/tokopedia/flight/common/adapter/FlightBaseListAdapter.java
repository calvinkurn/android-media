package com.tokopedia.flight.common.adapter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.tokopedia.abstraction.base.view.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.binder.LoadingDataBinder;
import com.tokopedia.abstraction.base.view.adapter.binder.NoResultDataBinder;
import com.tokopedia.abstraction.base.view.adapter.binder.RetryDataBinder;
import com.tokopedia.abstraction.base.view.adapter.type.ItemType;

import java.util.List;

/**
 * Created by User on 11/15/2017.
 */

public abstract class FlightBaseListAdapter<T extends ItemType> extends BaseListAdapter<T> {
    public FlightBaseListAdapter(Context context, OnBaseListV2AdapterListener<T> onBaseListV2AdapterListener) {
        super(context, onBaseListV2AdapterListener);
    }

    public FlightBaseListAdapter(Context context, @Nullable List<T> data, int rowPerPage, OnBaseListV2AdapterListener<T> onBaseListV2AdapterListener) {
        super(context, data, rowPerPage, onBaseListV2AdapterListener);
    }

    @Nullable
    @Override
    protected LoadingDataBinder createLoadingDataBinder() {
        return super.createLoadingDataBinder();
    }

    @Nullable
    @Override
    protected NoResultDataBinder createEmptyViewBinder() {
        return super.createEmptyViewBinder();
    }

    @Nullable
    @Override
    protected NoResultDataBinder createEmptyViewSearchBinder() {
        return super.createEmptyViewSearchBinder();
    }

    @Nullable
    @Override
    protected RetryDataBinder createRetryDataBinder() {
        return super.createRetryDataBinder();
    }

}
