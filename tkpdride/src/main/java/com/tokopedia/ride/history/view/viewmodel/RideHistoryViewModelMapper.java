package com.tokopedia.ride.history.view.viewmodel;

import com.tokopedia.ride.common.configuration.PaymentMode;
import com.tokopedia.ride.common.ride.domain.model.LocationLatLng;
import com.tokopedia.ride.common.ride.utils.RideUtils;
import com.tokopedia.ride.history.domain.model.RideHistory;

/**
 * Created by alvarisi on 7/21/17.
 */

public class RideHistoryViewModelMapper {
    private String googleKey;

    public RideHistoryViewModelMapper(String googleKey) {
        this.googleKey = googleKey;
    }

    public RideHistoryViewModel transform(String mapSize, RideHistory rideHistory) {
        RideHistoryViewModel viewModel = new RideHistoryViewModel();
        viewModel.setDriverCarDisplay(String.format("%s %s %s",
                rideHistory.getVehicle().getMake(),
                rideHistory.getVehicle().getVehicleModel(),
                rideHistory.getVehicle().getLicensePlate())
        );
        viewModel.setStatus(rideHistory.getStatus());
        viewModel.setTotalFare(RideUtils.formatStringToPriceString(rideHistory.getPayment().getTotalAmount(), rideHistory.getPayment().getCurrency()));
        viewModel.setTokoCashCharged(RideUtils.formatStringToPriceString(rideHistory.getPayment().getPaidAmount(), rideHistory.getPayment().getCurrency()));
        viewModel.setPendingAmountDisplayFormat(RideUtils.formaNumberToPriceString(rideHistory.getPayment().getPendingAmount(), rideHistory.getPayment().getCurrency()));
        viewModel.setPendingAmount(rideHistory.getPayment().getPendingAmount());
        viewModel.setPaymentMethod(transformPaymentMethod(rideHistory.getPayment().getPaymentMethod()));
        viewModel.setCashback(rideHistory.getCashbackAmount());
        viewModel.setDiscount(rideHistory.getDiscountAmount());
        viewModel.setCashbackDisplayFormat(
                RideUtils.formatStringToPriceString(
                        String.valueOf(Math.round(rideHistory.getCashbackAmount())), rideHistory.getPayment().getCurrency()
                )
        );
        viewModel.setDiscountDisplayFormat(
                RideUtils.formatStringToPriceString(
                        String.valueOf(Math.round(rideHistory.getDiscountAmount())), rideHistory.getPayment().getCurrency()
                )
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
        StringBuffer urlBuffer = new StringBuffer("https://maps.googleapis.com/maps/api/staticmap?size=").append(mapSize);
        urlBuffer.append("&markers=color:green|label:S|").append(startlatitude + "," + startLongitude)
                .append("&zoom=13")
                .append("&key=").append(googleKey);


        if (endLatitude != 0 && endLongitude != 0) {
            urlBuffer.append("&markers=color:red|label:D|").append(endLatitude + "," + endLongitude);
        }

        return urlBuffer.toString();
    }

    private String transformPaymentMethod(String paymentMethod) {
        if (paymentMethod != null && paymentMethod.equalsIgnoreCase(PaymentMode.CC)) {
            return PaymentMode.CC_DISPLAY_NAME;
        } else if (paymentMethod != null && paymentMethod.equalsIgnoreCase(PaymentMode.WALLET)) {
            return PaymentMode.WALLET_DISPLAY_NAME;
        }

        return PaymentMode.DEFAULT_DISPLAY_NAME;
    }
}
