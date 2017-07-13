package com.tokopedia.ride.bookingride.view.adapter.viewmodel;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.ride.bookingride.view.adapter.factory.PlaceAutoCompleteTypeFactory;

/**
 * Created by alvarisi on 5/3/17.
 */

public class LabelViewModel implements Visitable<PlaceAutoCompleteTypeFactory> {
    @Override
    public int type(PlaceAutoCompleteTypeFactory favoriteTypeFactory) {
        return favoriteTypeFactory.type(this);
    }
}
