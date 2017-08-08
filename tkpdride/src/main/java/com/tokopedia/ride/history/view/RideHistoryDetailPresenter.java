package com.tokopedia.ride.history.view;

import android.support.annotation.NonNull;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.ride.R;
import com.tokopedia.ride.common.configuration.RideStatus;
import com.tokopedia.ride.common.ride.domain.model.LocationLatLng;
import com.tokopedia.ride.completetrip.domain.GiveDriverRatingUseCase;
import com.tokopedia.ride.history.domain.GetSingleRideHistoryUseCase;
import com.tokopedia.ride.history.domain.model.RideHistory;
import com.tokopedia.ride.history.view.viewmodel.RideHistoryViewModel;
import com.tokopedia.ride.history.view.viewmodel.RideHistoryViewModelMapper;

import java.net.UnknownHostException;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by alvarisi on 4/20/17.
 */

public class RideHistoryDetailPresenter extends BaseDaggerPresenter<RideHistoryDetailContract.View> implements RideHistoryDetailContract.Presenter {
    private GetSingleRideHistoryUseCase getSingleRideHistoryUseCase;
    private GiveDriverRatingUseCase giveDriverRatingUseCase;

    @Inject
    public RideHistoryDetailPresenter(GetSingleRideHistoryUseCase getSingleRideHistoryUseCase,
                                      GiveDriverRatingUseCase giveDriverRatingUseCase) {
        this.getSingleRideHistoryUseCase = getSingleRideHistoryUseCase;
        this.giveDriverRatingUseCase = giveDriverRatingUseCase;
    }

    @Override
    public void initialize() {
        if (getView().getRideHistory() != null) {
            getView().showHistoryDetailLayout();
            getView().renderHistory(getView().getRideHistory());
            if (!getView().isRatingAvailable()) {
                if (getView().getRideHistory().getStatus().equalsIgnoreCase(RideStatus.COMPLETED)) {
                    getView().showRatingLayout();
                }
            } else {
                getView().renderSuccessfullGiveRating(Integer.parseInt(getView().getRideHistory().getRating().getStar()));
                getView().hideRatingLayout();
            }
        } else {
            actionGetSingleHistory();
        }
    }

    private void actionGetSingleHistory() {
        getView().hideMainLayout();
        getView().showLoading();
        getSingleRideHistoryUseCase.execute(getView().getSingleHistoryParam(), new Subscriber<RideHistory>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().hideLoading();
                    getView().showErrorLayout();
                }
            }

            @Override
            public void onNext(RideHistory rideHistory) {
                if (isViewAttached()) {
                    getView().showMainLayout();
                    getView().hideLoading();
                    getView().showHistoryDetailLayout();
                    RideHistoryViewModelMapper mapper = new RideHistoryViewModelMapper(getView().getMapKey());
                    RideHistoryViewModel viewModel = mapper.transform(getView().getMapSize(), rideHistory);
                    getView().setHistoryViewModelData(viewModel);
                    getView().renderHistory(viewModel);
                    if (!getView().isRatingAvailable()) {
                        if (getView().getRideHistory().getStatus().equalsIgnoreCase(RideStatus.COMPLETED)) {
                            getView().showRatingLayout();
                        }
                    } else {
                        getView().renderSuccessfullGiveRating(Integer.parseInt(viewModel.getRating().getStar()));
                        getView().hideRatingLayout();
                    }
                }
            }
        });
    }

    @Override
    public void actionSendRating() {
        getView().showProgressDialog();
        giveDriverRatingUseCase.execute(getView().getRatingParam(), new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (isViewAttached()) {
                    getView().hideProgressLoading();

                    if (e instanceof UnknownHostException) {
                        getView().showRatingNetworkError(getView().getActivity().getString(R.string.error_internet_not_connected));
                    } else {
                        getView().showRatingNetworkError(e.getMessage());
                    }
                }
            }

            @Override
            public void onNext(String s) {
                if (isViewAttached()) {
                    getView().renderSuccessfullGiveRating(getView().getRateStars());
                    getView().hideRatingLayout();
                    getView().hideProgressLoading();
                    RideHistoryViewModel viewModel = getView().getRideHistory();
                    viewModel.getRating().setStar(String.valueOf(getView().getRateStars()));
                    getView().setHistoryViewModelData(viewModel);
                }
            }
        });
    }
}
