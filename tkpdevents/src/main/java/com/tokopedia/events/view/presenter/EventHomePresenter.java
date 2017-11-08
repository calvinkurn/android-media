package com.tokopedia.events.view.presenter;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.events.domain.GetEventsListRequestUseCase;
import com.tokopedia.events.domain.model.Event;
import com.tokopedia.events.view.contractor.EventsContract;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by ashwanityagi on 06/11/17.
 */

public class EventHomePresenter extends BaseDaggerPresenter<EventsContract.View> implements EventsContract.Presenter  {

    public GetEventsListRequestUseCase getEventsListRequestUsecase;

    @Inject
    public EventHomePresenter(GetEventsListRequestUseCase getEventsListRequestUsecase) {
        this.getEventsListRequestUsecase = getEventsListRequestUsecase;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void onDestroy() {

    }

    public void getEventsList(){

        getEventsListRequestUsecase.execute(getView().getParams(), new Subscriber<Event>() {
            @Override
            public void onCompleted() {
                CommonUtils.dumper("enter onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                CommonUtils.dumper("enter error");
            }

            @Override
            public void onNext(Event event) {
                CommonUtils.dumper("enter onNext");
            }
        });
    }
}
