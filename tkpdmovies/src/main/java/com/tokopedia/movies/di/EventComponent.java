package com.tokopedia.movies.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.movies.di.scope.EventScope;
import com.tokopedia.movies.view.activity.EventBookTicketActivity;
import com.tokopedia.movies.view.activity.EventDetailsActivity;
import com.tokopedia.movies.view.activity.EventLocationActivity;
import com.tokopedia.movies.view.activity.ReviewTicketActivity;
import com.tokopedia.movies.view.activity.SeatSelectionActivity;
import com.tokopedia.movies.view.fragment.FragmentAddTickets;

import dagger.Component;


@EventScope
@Component(modules = EventModule.class, dependencies = AppComponent.class)
public interface EventComponent {

    void inject(com.tokopedia.movies.view.activity.MoviesHomeActivity activity);

    void inject(EventLocationActivity activity);

    void inject(EventDetailsActivity activity);

    void inject(EventBookTicketActivity activity);

    void inject(ReviewTicketActivity activity);

    void inject(FragmentAddTickets fragment);

    void inject(SeatSelectionActivity activity);

    //ThreadExecutor threadExecutor();

    //PostExecutionThread postExecutionThread();

    SessionHandler sessionHandler();

    //EventRepository eventRepository();

}
