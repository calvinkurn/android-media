package com.tokopedia.ride.history.view;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.ride.bookingride.domain.GetOverviewPolylineUseCase;
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
        mGetRideHistoriesUseCase.execute(getView().getHistoriesParam(), new Subscriber<List<RideHistory>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (isViewAttached()) {
                    getView().showFailedGetHistoriesMessage();
                    getView().enableRefreshLayout();
                    getView().setRefreshLayoutToFalse();
                    getView().showRetryLayout();
                }
            }

            @Override
            public void onNext(List<RideHistory> rideHistories) {
                if (isViewAttached()) {
                    ArrayList<Visitable> histories = new ArrayList<>();
                    for (RideHistory rideHistory : rideHistories) {
                        RideHistoryViewModel viewModel = new RideHistoryViewModel();
                        viewModel.setDriverCarDisplay(String.format("%s %s %s",
                                rideHistory.getVehicle().getMake(),
                                rideHistory.getVehicle().getVehicleModel(),
                                rideHistory.getVehicle().getLicensePlate())
                        );
                        viewModel.setStatus(rideHistory.getStatus());
                        viewModel.setFare(String.format("%s %s",
                                rideHistory.getPayment().getCurrency(),
                                rideHistory.getPayment().getValue())
                        );
                        viewModel.setRequestTime(rideHistory.getRequestTime());
                        viewModel.setStartLatitude(rideHistory.getPickup().getLatitude());
                        viewModel.setStartLongitude(rideHistory.getPickup().getLongitude());
                        viewModel.setEndLatitude(rideHistory.getDestination().getLatitude());
                        viewModel.setEndLongitude(rideHistory.getDestination().getLongitude());
                        viewModel.setRequestId(rideHistory.getRequestId());
                        viewModel.setDriverName(rideHistory.getDriver() == null ? "" : rideHistory.getDriver().getName());
                        viewModel.setDriverPictureUrl(rideHistory.getDriver() == null ? "" : rideHistory.getDriver().getPictureUrl());
                        viewModel.setMapImage(getMapImageUrl(rideHistory.getPickup().getLatitude(), rideHistory.getPickup().getLongitude()));
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

    private String getMapImageUrl(double startlatitude, double startLongitude) {
        String pickupLatLonString = startlatitude + "," + startLongitude;
        String mapImageUrl = "https://maps.googleapis.com/maps/api/staticmap?size=500x140&markers=color:green|label:S|" + pickupLatLonString + "&zoom=13&key=" + getView().getMapKey();
        return mapImageUrl;
    }

    private void actionGetOverviewPolyline(ArrayList<Visitable> histories) {
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
    }

    @Override
    public void actionRefreshHistoriesData() {
        getView().disableRefreshLayout();
        actionGetHistoriesData();
    }
}
