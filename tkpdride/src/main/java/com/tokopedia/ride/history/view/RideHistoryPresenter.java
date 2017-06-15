package com.tokopedia.ride.history.view;

import android.support.annotation.NonNull;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.ride.bookingride.domain.GetOverviewPolylineUseCase;
import com.tokopedia.ride.common.ride.domain.model.LocationLatLng;
import com.tokopedia.ride.history.domain.GetRideHistoriesUseCase;
import com.tokopedia.ride.history.domain.model.RideHistory;
import com.tokopedia.ride.history.view.viewmodel.RideHistoryViewModel;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by alvarisi on 4/19/17.
 */

public class RideHistoryPresenter extends BaseDaggerPresenter<RideHistoryContract.View>
        implements RideHistoryContract.Presenter {
    private GetRideHistoriesUseCase mGetRideHistoriesUseCase;
    private GetOverviewPolylineUseCase mGetOverviewPolylineUseCase;

    public RideHistoryPresenter(GetRideHistoriesUseCase getRideHistoriesUseCase,
                                GetOverviewPolylineUseCase getOverviewPolylineUseCase) {
        mGetRideHistoriesUseCase = getRideHistoriesUseCase;
        mGetOverviewPolylineUseCase = getOverviewPolylineUseCase;
    }

    @Override
    public void initialize() {
        actionGetHistoriesData();
    }

    private void actionGetHistoriesData() {
        getView().disableRefreshLayout();
        getView().showMainLoading();
        getView().hideMainLayout();
        mGetRideHistoriesUseCase.execute(getView().getHistoriesParam(), new Subscriber<List<RideHistory>>() {
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
        viewModel.setFare(String.format("%s %s",
                rideHistory.getPayment().getCurrency(),
                rideHistory.getPayment().getTotalAmount())
        );
        viewModel.setTotalFare(String.format("%s %s",
                rideHistory.getPayment().getCurrency(),
                rideHistory.getPayment().getTotalAmount())
        );
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
                .append("&zoom=13")
                .append("&key=").append(getView().getMapKey());


        if (endLatitude != 0 && endLongitude != 0) {
            urlBuffer.append("&markers=color:red|label:D|").append(endLatitude + "," + endLongitude);
        }

        return urlBuffer.toString();

        //String mapImageUrl = "https://maps.googleapis.com/maps/api/staticmap?size=500x140&markers=color:green|label:S|" + pickupLatLonString + "&zoom=13&key=" + getView().getMapKey();
        //return mapImageUrl;
    }

    /*private void actionGetOverviewPolyline(ArrayList<Visitable> histories) {
        for (int position = 0; position < histories.size(); position++) {
            final RideHistoryViewModel viewModel = (RideHistoryViewModel) histories.get(position);
            RequestParams requestParams = RequestParams.create();
            requestParams.putString("origin", String.format("%s,%s",
                    viewModel.getStartLatitude(),
                    viewModel.getStartLongitude()
            ));
            requestParams.putString("destination", String.format("%s,%s",
                    viewModel.getEndLatitude(),
                    viewModel.getEndLongitude()
            ));
            requestParams.putString("sensor", "false");
            final int finalPosition = position;
            mGetOverviewPolylineUseCase.execute(requestParams, new Subscriber<List<String>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(List<String> strings) {
                    if (isViewAttached() && !isUnsubscribed()) {
                        List<List<LatLng>> routes = new ArrayList<>();
                        for (String route : strings) {
                            routes.add(PolyUtil.decode(route));
                        }
                        if (routes.size() > 0) {
                            viewModel.setLatLngs(routes.get(0));
                            getView().renderUpdatedHistoryRow(finalPosition, viewModel);
                        }
                    }
                }
            });
        }
    }*/

    @Override
    public void actionRefreshHistoriesData() {
        getView().disableRefreshLayout();
        actionGetHistoriesData();
    }
}
