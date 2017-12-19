package com.tokopedia.events.view.presenter;

import android.content.Intent;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.events.domain.GetEventDetailsRequestUseCase;
import com.tokopedia.events.domain.model.EventDetailsDomain;
import com.tokopedia.events.view.activity.EventBookTicketActivity;
import com.tokopedia.events.view.contractor.EventsDetailsContract;
import com.tokopedia.events.view.mapper.EventDetailsViewModelMapper;
import com.tokopedia.events.view.viewmodel.CategoryItemsViewModel;
import com.tokopedia.events.view.viewmodel.EventsDetailsViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by ashwanityagi on 23/11/17.
 */

public class EventsDetailsPresenter extends BaseDaggerPresenter<EventsDetailsContract.EventDetailsView> implements EventsDetailsContract.Presenter {

    GetEventDetailsRequestUseCase getEventDetailsRequestUseCase;
    EventsDetailsViewModel eventsDetailsViewModel;
    public static String EXTRA_EVENT_VIEWMODEL= "extraeventviewmodel";

    @Inject
    public EventsDetailsPresenter(GetEventDetailsRequestUseCase eventDetailsRequestUseCase) {
        this.getEventDetailsRequestUseCase = eventDetailsRequestUseCase;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void onDestroy() {

    }


    @Override
    public void attachView(EventsDetailsContract.EventDetailsView view) {
        super.attachView(view);
        CategoryItemsViewModel dataFromHome = (CategoryItemsViewModel)
                getView().getActivity().getIntent().getParcelableExtra("homedata");
        getView().renderFromHome(dataFromHome);
        String url = dataFromHome.getUrl();
        getEventDetailsRequestUseCase.setUrl(url);


    }

    @Override
    public void getEventDetails() {
        getEventDetailsRequestUseCase.execute(getView().getParams(), new Subscriber<EventDetailsDomain>() {
            @Override
            public void onCompleted() {
                CommonUtils.dumper("enter onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                CommonUtils.dumper("enter error");
                e.printStackTrace();
            }

            @Override
            public void onNext(EventDetailsDomain eventDetailEntities) {
                getView().renderFromCloud(convertIntoEventDetailsViewModel(eventDetailEntities));   //TODO:should be chained using concatMap
                CommonUtils.dumper("enter onNext");
            }
        });

    }

    private EventsDetailsViewModel convertIntoEventDetailsViewModel(EventDetailsDomain eventDetailsDomain) {
        eventsDetailsViewModel = new EventsDetailsViewModel();
        if (eventDetailsDomain != null) {
            EventDetailsViewModelMapper.mapDomainToViewModel(eventDetailsDomain, eventsDetailsViewModel);
        }

        return eventsDetailsViewModel;
    }

    public String convertEpochToString(int time) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta"));
        Long epochTime = time * 1000L;
        Date date = new Date(epochTime);
        String dateString = sdf.format(date);
        return dateString;
    }

    public void bookBtnClick(){
        Intent bookTicketIntent = new Intent(getView().getActivity(), EventBookTicketActivity.class);
        bookTicketIntent.putExtra(EXTRA_EVENT_VIEWMODEL,eventsDetailsViewModel);
        getView().navigateToActivityRequest(bookTicketIntent,100);
    }


}
