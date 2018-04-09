package com.tokopedia.flight.cancellation.di;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.flight.cancellation.view.fragment.FlightCancellationFragment;
import com.tokopedia.flight.cancellation.view.fragment.FlightCancellationReasonAndProofFragment;
import com.tokopedia.flight.cancellation.view.fragment.FlightCancellationRefundDetailFragment;
import com.tokopedia.flight.cancellation.view.fragment.FlightReviewCancellationFragment;
import com.tokopedia.flight.common.di.component.FlightComponent;

import dagger.Component;

/**
 * @author by furqan on 21/03/18.
 */

@FlightCancellationScope
@Component(modules = FlightCancellationModule.class, dependencies = FlightComponent.class)
public interface FlightCancellationComponent {

    UserSession userSession();

    void inject(FlightCancellationFragment flightCancellationFragment);

    void inject(FlightCancellationReasonAndProofFragment flightCancellationReasonAndProofFragment);

    void inject(FlightReviewCancellationFragment flightReviewCancellationFragment);

    void inject(FlightCancellationRefundDetailFragment flightCancellationRefundDetailFragment);
}
