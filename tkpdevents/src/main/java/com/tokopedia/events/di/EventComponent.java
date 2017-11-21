package com.tokopedia.events.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.events.di.scope.EventScope;
import com.tokopedia.events.domain.EventRepository;
import com.tokopedia.events.view.activity.EventLocationActivity;
import com.tokopedia.events.view.activity.EventsHomeActivity;

import dagger.Component;

/**
 * Created by ashwanityagi on 03/11/17.
 */

@EventScope
@Component(modules = EventModule.class, dependencies = AppComponent.class)
public interface EventComponent {

    void inject(EventsHomeActivity activity);

    void inject(EventLocationActivity activity);

    ThreadExecutor threadExecutor();

    PostExecutionThread postExecutionThread();

    SessionHandler sessionHandler();

    EventRepository eventRepository();
}
