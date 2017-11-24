package com.tokopedia.events.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.events.domain.GetEventDetailsRequestUseCase;
import com.tokopedia.events.view.contractor.EventsDetailsContract;

/**
 * Created by ashwanityagi on 23/11/17.
 */

public class EventsDetailsPresenter extends BaseDaggerPresenter<EventsDetailsContract.View> implements EventsDetailsContract.Presenter{

    GetEventDetailsRequestUseCase getEventDetailsRequestUseCase;

    @Override
    public void initialize() {

    }

    @Override
    public void onDestroy() {

    }
}
