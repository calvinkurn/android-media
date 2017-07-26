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
                    RideHistoryViewModel viewModel = getRideHistoryViewModel(getView().getMapSize(), rideHistory);
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

    @NonNull
    private RideHistoryViewModel getRideHistoryViewModel(String mapSize, RideHistory rideHistory) {
        RideHistoryViewModel viewModel = new RideHistoryViewModel();
        viewModel.setDriverCarDisplay(String.format("%s %s %s",
                rideHistory.getVehicle().getMake(),
                rideHistory.getVehicle().getVehicleModel(),
                rideHistory.getVehicle().getLicensePlate())
        );
        viewModel.setStatus(rideHistory.getStatus());
//        viewModel.setFare(String.format("%s %s",
//                rideHistory.getPayment().getCurrency(),
//                rideHistory.getPayment().getTotalAmount())
//        );
//        viewModel.setTotalFare(String.format("%s %s",
//                rideHistory.getPayment().getCurrency(),
//                rideHistory.getPayment().getTotalAmount())
//        );

        viewModel.setFare(RideHistoryViewModel.formatStringToPriceString(rideHistory.getPayment().getTotalAmount(), rideHistory.getPayment().getCurrency()));
        viewModel.setTotalFare(RideHistoryViewModel.formatStringToPriceString(rideHistory.getPayment().getTotalAmount(), rideHistory.getPayment().getCurrency()));
        viewModel.setCashback(rideHistory.getCashbackAmount());
        viewModel.setDiscount(rideHistory.getDiscountAmount());
        viewModel.setCashbackDisplayFormat(RideHistoryViewModel.formaNumberToPriceString(rideHistory.getCashbackAmount(), rideHistory.getPayment().getCurrency()));
        viewModel.setDiscountDisplayFormat(RideHistoryViewModel.formaNumberToPriceString(rideHistory.getDiscountAmount(), rideHistory.getPayment().getCurrency()));

        viewModel.setRequestTime(rideHistory.getRequestTime());
        viewModel.setRequestId(rideHistory.getRequestId());
        viewModel.setDriverName(rideHistory.getDriver() == null ? "" : rideHistory.getDriver().getName());
        viewModel.setDriverPictureUrl(rideHistory.getDriver() == null ? "" : rideHistory.getDriver().getPictureUrl());
        viewModel.setDisplayStatus(RideHistoryViewModel.transformToDisplayStatus(rideHistory.getStatus()));

        if (rideHistory.getVehicle() != null) {
            viewModel.setLicensePlateNumber(rideHistory.getVehicle().getLicensePlate());
        }

        LocationLatLng pickupObject = rideHistory.getPickup();
        if (pickupObject != null) {
            viewModel.setStartLatitude(pickupObject.getLatitude());
            viewModel.setStartLongitude(pickupObject.getLongitude());
            viewModel.setStartAddress(pickupObject.getAddress());
        }

        LocationLatLng destObject = rideHistory.getDestination();
        if (destObject != null) {
            viewModel.setEndLatitude(destObject.getLatitude());
            viewModel.setEndLongitude(destObject.getLongitude());
            viewModel.setEndAddress(destObject.getAddress());
        }

        viewModel.setMapImage(getMapImageUrl(viewModel.getStartLatitude(), viewModel.getStartLongitude(), viewModel.getEndLatitude(), viewModel.getEndLongitude(), mapSize));
        viewModel.setRating(rideHistory.getRating());
        viewModel.setHelpUrl(rideHistory.getHelpUrl());
        return viewModel;
    }

    private String getMapImageUrl(double startlatitude, double startLongitude, double endLatitude, double endLongitude, String mapSize) {


        StringBuffer urlBuffer = new StringBuffer("https://maps.googleapis.com/maps/api/staticmap?size=").append(mapSize);
        urlBuffer.append("&markers=color:green|label:S|").append(startlatitude + "," + startLongitude)
                .append("&zoom=13")
                .append("&key=").append(getView().getMapKey());


        if (endLatitude != 0 && endLongitude != 0) {
            urlBuffer.append("&markers=color:red|label:D|").append(endLatitude + "," + endLongitude);
        }

        return urlBuffer.toString();

        //String mapImageUrl = "https://maps.googleapis.com/maps/api/staticmap?size=500x140&markers=color:green|label:S|" + pickupLatLonString + "&zoom=13&key=" + getView().getMapKey();
        //return mapImageUrl;
    }
}
