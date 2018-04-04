package com.tokopedia.events.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.events.di.scope.EventScope;
import com.tokopedia.events.view.activity.EventBookTicketActivity;
import com.tokopedia.events.view.activity.EventDetailsActivity;
import com.tokopedia.events.view.activity.EventLocationActivity;
import com.tokopedia.events.view.activity.EventSearchActivity;
import com.tokopedia.events.view.activity.EventsHomeActivity;
import com.tokopedia.events.view.activity.ReviewTicketActivity;
import com.tokopedia.events.view.activity.SeatSelectionActivity;
import com.tokopedia.events.view.fragment.FragmentAddTickets;

import dagger.Component;

/**
 * Created by ashwanityagi on 03/11/17.
 */

@EventScope
@Component(modules = EventModule.class, dependencies = AppComponent.class)
public interface EventComponent {

    void inject(EventsHomeActivity activity);

    void inject(EventLocationActivity activity);

    void inject(EventDetailsActivity activity);

    void inject(EventBookTicketActivity activity);

    void inject(ReviewTicketActivity activity);

    void inject(FragmentAddTickets fragment);

    void inject(EventSearchActivity activity);

    void inject(SeatSelectionActivity activity);
}
