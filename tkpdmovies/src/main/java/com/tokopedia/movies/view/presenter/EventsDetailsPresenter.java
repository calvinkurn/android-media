package com.tokopedia.movies.view.presenter;

import android.content.Intent;
import android.util.Log;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.movies.data.entity.response.seatlayoutresponse.SeatLayoutResponse;
import com.tokopedia.movies.domain.GetEventDetailsRequestUseCase;
import com.tokopedia.movies.domain.GetSeatLayoutUseCase;
import com.tokopedia.movies.domain.model.EventDetailsDomain;
import com.tokopedia.movies.view.activity.EventBookTicketActivity;
import com.tokopedia.movies.view.contractor.EventsDetailsContract;
import com.tokopedia.movies.view.mapper.EventDetailsViewModelMapper;
import com.tokopedia.movies.view.viewmodel.CategoryItemsViewModel;
import com.tokopedia.movies.view.viewmodel.EventsDetailsViewModel;

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
    EventsDetailsViewModel moviesDetailsViewModel;
    public static String EXTRA_EVENT_VIEWMODEL = "extraeventviewmodel";

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
        getEventDetailsRequestUseCase.execute(getView().getParams(), new Subscriber<EventDetailsDomain>() {
            @Override
            public void onCompleted() {
                CommonUtils.dumper("enter onCompleted");
//                getView().hideProgressBar();
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
        moviesDetailsViewModel = new EventsDetailsViewModel();
        if (eventDetailsDomain != null) {
            EventDetailsViewModelMapper.mapDomainToViewModel(eventDetailsDomain, moviesDetailsViewModel);
            getView().hideProgressBar();
        }
//        try {
//            if (moviesDetailsViewModel.getHasSeatLayout() == 1) {
//                getSeatLayoutUseCase.setUrl(moviesDetailsViewModel.getSchedulesViewModels().get(0).getPackages().get(0));
//                getSeatLayoutUseCase.execute(RequestParams.EMPTY, new Subscriber<SeatLayoutResponse>() {
//                    @Override
//                    public void onCompleted() {
//                        CommonUtils.dumper("enter onCompleted seatlayout usecase");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        CommonUtils.dumper("enter error in seatlayout usecase");
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    public void onNext(SeatLayoutResponse seatLayoutResponse) {
//                        getView().renderSeatLayout(seatLayoutResponse.getUrl());
//                        getView().hideProgressBar();
//                    }
//                });
//            }
//        } catch (Exception e) {
//            Log.d("EventDetailsPresenter","Catch in seatlayout usecase");
//            e.printStackTrace();
//        }

        return moviesDetailsViewModel;
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
        bookTicketIntent.putExtra(EXTRA_EVENT_VIEWMODEL, moviesDetailsViewModel);
        getView().navigateToActivityRequest(bookTicketIntent, 100);
    }

}
