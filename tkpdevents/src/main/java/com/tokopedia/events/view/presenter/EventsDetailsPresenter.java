package com.tokopedia.events.view.presenter;

import android.content.Intent;
import android.util.Log;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.base.domain.RequestParams;
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

/**
 * Created by ashwanityagi on 23/11/17.
 */

public class EventsDetailsPresenter extends BaseDaggerPresenter<EventsDetailsContract.EventDetailsView> implements EventsDetailsContract.Presenter {

    GetEventDetailsRequestUseCase getEventDetailsRequestUseCase;
    GetSeatLayoutUseCase getSeatLayoutUseCase;
    EventsDetailsViewModel eventsDetailsViewModel;
    Subscriber<SeatLayoutResponse> seatLayoutResponseSubscriber;
    Subscriber<EventDetailsDomain> eventDetailsDomainSubscriber;
    public static String EXTRA_EVENT_VIEWMODEL = "extraeventviewmodel";
    public static String EXTRA_SEATING_URL = "extraseatingurl";
    public static String EXTRA_SEATING_PARAMETER = "hasSeatLayout";

    int hasSeatLayout;

    @Inject
    public EventsDetailsPresenter(GetEventDetailsRequestUseCase eventDetailsRequestUseCase,
                                  GetSeatLayoutUseCase seatLayoutUseCase) {
        this.getEventDetailsRequestUseCase = eventDetailsRequestUseCase;
        this.getSeatLayoutUseCase = seatLayoutUseCase;
    }

    @Override
    public void initialize() {
//        seatLayoutResponseSubscriber = new Subscriber<SeatLayoutResponse>() {
//            @Override
//            public void onCompleted() {
//                CommonUtils.dumper("enter onCompleted seatlayout usecase");
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                CommonUtils.dumper("enter error in seatlayout usecase");
//                e.printStackTrace();
//                getView().hideProgressBar();
//                NetworkErrorHelper.showEmptyState(getView().getActivity(),
//                        getView().getRootView(), new NetworkErrorHelper.RetryClickedListener() {
//                            @Override
//                            public void onRetryClicked() {
//                                getSeatLayout();
//                            }
//                        });
//            }
//
//            @Override
//            public void onNext(SeatLayoutResponse seatLayoutResponse) {
//                getView().hideProgressBar();
//                try {
//                    seatingURL = seatLayoutResponse.getUrl();
//                    //getView().renderSeatLayout(seatingURL);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        };

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
        getEventDetailsRequestUseCase.execute(getView().getParams(), new Subscriber<EventDetailsDomain>() {
            @Override
            public void onCompleted() {
                CommonUtils.dumper("enter onCompleted");

            }

            @Override
            public void onError(Throwable e) {
                CommonUtils.dumper("enter error");
                e.printStackTrace();
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
            public void onNext(EventDetailsDomain eventDetailEntities) {
                getView().renderFromCloud(convertIntoEventDetailsViewModel(eventDetailEntities));   //TODO:should be chained using concatMap
                if (eventsDetailsViewModel.getSeatMapImage() != null && !eventsDetailsViewModel.getSeatMapImage().isEmpty())
                    getView().renderSeatmap(eventsDetailsViewModel.getSeatMapImage());
                hasSeatLayout = eventsDetailsViewModel.getHasSeatLayout();
                getView().hideProgressBar();
                CommonUtils.dumper("enter onNext");
            }
        });

    }

//    private void getSeatLayout() {
//        getView().showProgressBar();
//        getSeatLayoutUseCase.execute(RequestParams.EMPTY, new Subscriber<SeatLayoutResponse>() {
//            @Override
//            public void onCompleted() {
//                CommonUtils.dumper("enter onCompleted seatlayout usecase");
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                CommonUtils.dumper("enter error in seatlayout usecase");
//                e.printStackTrace();
//                getView().hideProgressBar();
//                NetworkErrorHelper.showEmptyState(getView().getActivity(),
//                        getView().getRootView(), new NetworkErrorHelper.RetryClickedListener() {
//                            @Override
//                            public void onRetryClicked() {
//                                getSeatLayout();
//                            }
//                        });
//            }
//
//            @Override
//            public void onNext(SeatLayoutResponse seatLayoutResponse) {
//                getView().hideProgressBar();
//                try {
//                    seatingURL = seatLayoutResponse.getUrl();
//                    getView().renderSeatLayout(seatingURL);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }

    private EventsDetailsViewModel convertIntoEventDetailsViewModel(EventDetailsDomain eventDetailsDomain) {
        eventsDetailsViewModel = new EventsDetailsViewModel();
        if (eventDetailsDomain != null) {
            EventDetailsViewModelMapper.mapDomainToViewModel(eventDetailsDomain, eventsDetailsViewModel);
        }
//        try {
//            if (eventsDetailsViewModel.getHasSeatLayout() == 1) {
//                getSeatLayoutUseCase.setUrl(eventsDetailsViewModel.getSchedulesViewModels().get(0).getPackages().get(0));
//                getSeatLayout();
//            }
//        } catch (Exception e) {
//            Log.d("EventDetailsPresenter", "Catch in seatlayout usecase");
//            e.printStackTrace();
//        }

        return eventsDetailsViewModel;
    }

    public String convertEpochToString(int time) {
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta"));
        Long epochTime = time * 1000L;
        Date date = new Date(epochTime);
        String dateString = sdf.format(date);
        return dateString;
    }

    public void bookBtnClick() {
        getView().showProgressBar();
        Intent bookTicketIntent = new Intent(getView().getActivity(), EventBookTicketActivity.class);
        bookTicketIntent.putExtra(EXTRA_EVENT_VIEWMODEL, eventsDetailsViewModel);
        bookTicketIntent.putExtra(EXTRA_SEATING_PARAMETER, hasSeatLayout);
        getView().navigateToActivityRequest(bookTicketIntent, 100);
    }

}
