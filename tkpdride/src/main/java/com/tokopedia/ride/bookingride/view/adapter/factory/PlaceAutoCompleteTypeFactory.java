package com.tokopedia.ride.bookingride.view.adapter.factory;

import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.LabelViewModel;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.PlaceAutoCompeleteViewModel;

/**
 * Created by alvarisi on 3/15/17.
 */

public interface PlaceAutoCompleteTypeFactory {

    int type(PlaceAutoCompeleteViewModel placeAutoCompeleteViewModel);

    AbstractViewHolder createViewHolder(View view, int viewType);

    int type(LabelViewModel labelViewHolder);
}
