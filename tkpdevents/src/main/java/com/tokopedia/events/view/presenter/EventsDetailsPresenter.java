package com.tokopedia.events.view.presenter;

import android.content.Intent;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.events.data.entity.response.seatlayoutresponse.SeatLayoutResponse;
import com.tokopedia.events.domain.GetEventDetailsRequestUseCase;
import com.tokopedia.events.domain.GetSeatLayoutUseCase;
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
import rx.functions.Func1;

/**
 * Created by ashwanityagi on 23/11/17.
 */

public class EventsDetailsPresenter extends BaseDaggerPresenter<EventsDetailsContract.EventDetailsView> implements EventsDetailsContract.Presenter {

    private GetEventDetailsRequestUseCase getEventDetailsRequestUseCase;
    private GetSeatLayoutUseCase getSeatLayoutUseCase;
    private EventsDetailsViewModel eventsDetailsViewModel;
    private Subscriber<SeatLayoutResponse> seatLayoutResponseSubscriber;
    private Subscriber<EventDetailsDomain> eventDetailsDomainSubscriber;
    public static String EXTRA_EVENT_VIEWMODEL = "extraeventviewmodel";
    //public static String EXTRA_SEATING_URL = "extraseatingurl";
    public static String EXTRA_SEATING_PARAMETER = "hasSeatLayout";

    private int hasSeatLayout;

    @Inject
    public EventsDetailsPresenter(GetEventDetailsRequestUseCase eventDetailsRequestUseCase,
                                  GetSeatLayoutUseCase seatLayoutUseCase) {
        this.getEventDetailsRequestUseCase = eventDetailsRequestUseCase;
        this.getSeatLayoutUseCase = seatLayoutUseCase;
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
        getView().showProgressBar();
        getEventDetailsRequestUseCase.getExecuteObservableAsync(getView().getParams()).map(new Func1<EventDetailsDomain, EventsDetailsViewModel>() {
            @Override
            public EventsDetailsViewModel call(EventDetailsDomain eventDetailsDomain) {
                return convertIntoEventDetailsViewModel(eventDetailsDomain);
            }
        }).subscribe(new Subscriber<EventsDetailsViewModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                CommonUtils.dumper("enter error");
                throwable.printStackTrace();
                getView().hideProgressBar();
                NetworkErrorHelper.showEmptyState(getView().getActivity(),
                        getView().getRootView(), new NetworkErrorHelper.RetryClickedListener() {
                            @Override
                            public void onRetryClicked() {
                                getEventDetails();
                            }
                        });
            }

            @Override
            public void onNext(EventsDetailsViewModel detailsViewModel) {
                getView().renderFromCloud(detailsViewModel);   //chained using map
                if (eventsDetailsViewModel.getSeatMapImage() != null && !eventsDetailsViewModel.getSeatMapImage().isEmpty())
                    getView().renderSeatmap(eventsDetailsViewModel.getSeatMapImage());
                hasSeatLayout = eventsDetailsViewModel.getHasSeatLayout();
                getView().hideProgressBar();
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

    public void bookBtnClick() {
        getView().showProgressBar();
        Intent bookTicketIntent = new Intent(getView().getActivity(), EventBookTicketActivity.class);
        bookTicketIntent.putExtra(EXTRA_EVENT_VIEWMODEL, eventsDetailsViewModel);
        bookTicketIntent.putExtra(EXTRA_SEATING_PARAMETER, hasSeatLayout);
        getView().navigateToActivityRequest(bookTicketIntent, 100);
    }

}
