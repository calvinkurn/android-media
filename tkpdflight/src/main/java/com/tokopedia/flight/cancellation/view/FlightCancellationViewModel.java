package com.tokopedia.flight.cancellation.view;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.flight.cancellation.view.adapter.FlightCancellationTypeFactory;

/**
 * @author by furqan on 21/03/18.
 */

public class FlightCancellationViewModel implements Visitable<FlightCancellationTypeFactory> {

    @Override
    public int type(FlightCancellationTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

}
