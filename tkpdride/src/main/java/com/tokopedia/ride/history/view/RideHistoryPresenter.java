package com.tokopedia.ride.history.view;

import android.support.annotation.NonNull;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.ride.common.ride.domain.model.LocationLatLng;
import com.tokopedia.ride.common.ride.domain.model.RideHistoryWrapper;
import com.tokopedia.ride.history.domain.GetHistoriesWithPaginationUseCase;
import com.tokopedia.ride.history.domain.GetRideHistoriesUseCase;
import com.tokopedia.ride.history.domain.model.RideHistory;
import com.tokopedia.ride.history.view.viewmodel.RideHistoryViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by alvarisi on 4/19/17.
 */

public class RideHistoryPresenter extends BaseDaggerPresenter<RideHistoryContract.View>
        implements RideHistoryContract.Presenter {
    private GetRideHistoriesUseCase getRideHistoriesUseCase;
    private GetHistoriesWithPaginationUseCase getHistoriesWithPaginationUseCase;

    @Inject
    public RideHistoryPresenter(GetRideHistoriesUseCase getRideHistoriesUseCase,
                                GetHistoriesWithPaginationUseCase getHistoriesWithPaginationUseCase) {
        this.getRideHistoriesUseCase = getRideHistoriesUseCase;
        this.getHistoriesWithPaginationUseCase = getHistoriesWithPaginationUseCase;
    }

    @Override
    public void initialize() {
//        actionGetHistoriesData();
        actionGetHistoriesWithPaginationData();
    }

    private void actionGetHistoriesWithPaginationData() {
        getView().disableRefreshLayout();
        getView().showMainLoading();
        getView().hideMainLayout();
        getHistoriesWithPaginationUseCase.execute(RequestParams.EMPTY, new Subscriber<RideHistoryWrapper>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (isViewAttached()) {
                    getView().hideMainLoading();
                    getView().enableRefreshLayout();
                    getView().setRefreshLayoutToFalse();
                    getView().showRetryLayout();
                }
            }

            @Override
            public void onNext(RideHistoryWrapper rideHistoryWrapper) {
                if (isViewAttached()) {
                    getView().showMainLayout();
                    getView().hideMainLoading();
                    ArrayList<Visitable> histories = new ArrayList<>();
                    String mapSize = getView().getMapImageSize();
                    for (RideHistory rideHistory : rideHistoryWrapper.getHistories()) {
                        RideHistoryViewModel viewModel = getRideHistoryViewModel(mapSize, rideHistory);
                        histories.add(viewModel);
                    }
                    getView().enableRefreshLayout();
                    getView().setRefreshLayoutToFalse();
                    getView().setPaging(rideHistoryWrapper.getPaging());
                    if (histories.size() > 0) {
                        getView().renderHistoryLists(histories);
                    } else {
                        getView().showEmptyResultLayout();
                    }
                }
            }
        });
    }

    private void actionGetHistoriesData() {
        getView().disableRefreshLayout();
        getView().showMainLoading();
        getView().hideMainLayout();
        getRideHistoriesUseCase.execute(getView().getHistoriesParam(), new Subscriber<List<RideHistory>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (isViewAttached()) {
                    getView().hideMainLoading();
                    getView().enableRefreshLayout();
                    getView().setRefreshLayoutToFalse();
                    getView().showRetryLayout();
                }
            }

            @Override
            public void onNext(List<RideHistory> rideHistories) {
                if (isViewAttached()) {
                    getView().showMainLayout();
                    getView().hideMainLoading();
                    ArrayList<Visitable> histories = new ArrayList<>();
                    String mapSize = getView().getMapImageSize();
                    for (RideHistory rideHistory : rideHistories) {
                        RideHistoryViewModel viewModel = getRideHistoryViewModel(mapSize, rideHistory);

                        histories.add(viewModel);
                    }
                    getView().enableRefreshLayout();
                    getView().setRefreshLayoutToFalse();
                    if (histories.size() > 0) {
                        getView().renderHistoryLists(histories);
                        //actionGetOverviewPolyline(histories);
                    } else {
                        getView().showEmptyResultLayout();
                    }
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
        //get screen width
        getView().getMapImageSize();


        StringBuffer urlBuffer = new StringBuffer("https://maps.googleapis.com/maps/api/staticmap?size=").append(mapSize);
        urlBuffer.append("&markers=color:green|label:S|").append(startlatitude + "," + startLongitude)
                //.append("&zoom=13")
                .append("&key=").append(getView().getMapKey());


        if (endLatitude != 0 && endLongitude != 0) {
            urlBuffer.append("&markers=color:red|label:D|").append(endLatitude + "," + endLongitude);
        }

        return urlBuffer.toString();
    }

    @Override
    public void actionRefreshHistoriesData() {
        getView().disableRefreshLayout();
        actionGetHistoriesWithPaginationData();
    }

    @Override
    public void actionLoadMore() {
        getView().disableRefreshLayout();
        getView().showLoadMoreLoading();
        getHistoriesWithPaginationUseCase.execute(getView().getHistoriesLoadMoreParam(), new Subscriber<RideHistoryWrapper>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (isViewAttached()) {
                    getView().hideLoadMoreLoading();
                    getView().enableRefreshLayout();
                    getView().setRefreshLayoutToFalse();
                    getView().showRetryLoadMoreLayout();
                }
            }

            @Override
            public void onNext(RideHistoryWrapper rideHistoryWrapper) {
                if (isViewAttached()) {
                    getView().hideLoadMoreLoading();
                    ArrayList<Visitable> histories = new ArrayList<>();
                    String mapSize = getView().getMapImageSize();
                    for (RideHistory rideHistory : rideHistoryWrapper.getHistories()) {
                        RideHistoryViewModel viewModel = getRideHistoryViewModel(mapSize, rideHistory);
                        histories.add(viewModel);
                    }
                    getView().enableRefreshLayout();
                    getView().setRefreshLayoutToFalse();
                    getView().setPaging(rideHistoryWrapper.getPaging());
                    if (histories.size() > 0) {
                        getView().renderHistoryLoadMoreLists(histories);
                    }
                }
            }
        });
    }
}
