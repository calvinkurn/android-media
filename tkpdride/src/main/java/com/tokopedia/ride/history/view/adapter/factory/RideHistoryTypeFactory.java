package com.tokopedia.ride.history.view.adapter.factory;

import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.ride.history.view.viewmodel.RideHistoryViewModel;

/**
 * Created by alvarisi on 4/11/17.
 */

public interface RideHistoryTypeFactory {

    int type(RideHistoryViewModel transactionViewModel);

    AbstractViewHolder createViewHolder(View view, int viewType);
}
