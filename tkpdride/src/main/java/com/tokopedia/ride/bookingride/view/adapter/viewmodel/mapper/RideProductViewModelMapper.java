package com.tokopedia.ride.bookingride.view.adapter.viewmodel.mapper;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.ride.bookingride.domain.model.ProductEstimate;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.RideProductViewModel;
import com.tokopedia.ride.common.ride.domain.model.FareEstimate;
import com.tokopedia.ride.common.ride.domain.model.PriceDetail;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

/**
 * Created by alvarisi on 3/16/17.
 */

public class RideProductViewModelMapper {
    public RideProductViewModelMapper() {
    }

    public List<Visitable> transform(List<ProductEstimate> products) {
        List<Visitable> rideProductViewModels = new ArrayList<>();
        Visitable productViewModel = null;
        for (ProductEstimate product : products) {
            productViewModel = transform(product);
            if (productViewModel != null)
                rideProductViewModels.add(productViewModel);
        }
        return rideProductViewModels;
    }

    private Visitable transform(ProductEstimate product) {
        RideProductViewModel rideProductViewModel = null;
        if (product != null && product.getTimesEstimate() != null) {
            rideProductViewModel = new RideProductViewModel();
            rideProductViewModel.setProductName(product.getProduct().getDisplayName());
            rideProductViewModel.setProductId(product.getProduct().getProductId());
            rideProductViewModel.setProductImage(product.getProduct().getImage());
            rideProductViewModel.setCapacity(product.getProduct().getCapacity());
            rideProductViewModel.setBaseFare(getBaseFare(product.getProduct().getPriceDetail()));
            rideProductViewModel.setCancellationFee(getCancellationCharges(product.getProduct().getPriceDetail()));
            rideProductViewModel.setProductPriceFmt(getPricePerDistance(product.getProduct().getPriceDetail()));
            rideProductViewModel.setTimeEstimate(product.getTimesEstimate().getEstimate() / 60 + " min");

        }
        return rideProductViewModel;
    }

    public Visitable transform(ProductEstimate product, FareEstimate fareEstimate) {
        RideProductViewModel rideProductViewModel = null;
        if (product != null && product.getTimesEstimate() != null) {
            rideProductViewModel = new RideProductViewModel();
            rideProductViewModel.setProductName(product.getProduct().getDisplayName());
            rideProductViewModel.setProductId(product.getProduct().getProductId());
            rideProductViewModel.setProductImage(product.getProduct().getImage());
            rideProductViewModel.setCapacity(product.getProduct().getCapacity());
            rideProductViewModel.setTimeEstimate(product.getTimesEstimate().getEstimate() / 60 + " min");
            rideProductViewModel.setBaseFare(getBaseFare(product.getProduct().getPriceDetail()));
            rideProductViewModel.setCancellationFee(getCancellationCharges(product.getProduct().getPriceDetail()));
            rideProductViewModel.setEnabled(true);

            if (fareEstimate.getFare() != null) {
                rideProductViewModel.setProductPriceFmt(fareEstimate.getFare().getDisplay());
                rideProductViewModel.setProductPrice(fareEstimate.getFare().getValue());
                rideProductViewModel.setFareId(fareEstimate.getFare().getFareId());
                rideProductViewModel.setPromoCode(fareEstimate.getCode());
                rideProductViewModel.setPromoMessage(fareEstimate.getMessageSuccess());
            }

            if (fareEstimate.getEstimate() != null) {
                rideProductViewModel.setSurgePrice(true);
                rideProductViewModel.setSurgeMultiplier(fareEstimate.getEstimate().getSurgeMultiplier());
                rideProductViewModel.setSurgeConfirmationHref(fareEstimate.getEstimate().getSurgeConfirmationHref());
            }
        }
        return rideProductViewModel;
    }

    public RideProductViewModel transformRide(ProductEstimate product, FareEstimate fareEstimate) {
        RideProductViewModel rideProductViewModel = null;
        if (product != null && product.getTimesEstimate() != null) {
            rideProductViewModel = new RideProductViewModel();
            rideProductViewModel.setProductName(product.getProduct().getDisplayName());
            rideProductViewModel.setProductId(product.getProduct().getProductId());
            rideProductViewModel.setProductImage(product.getProduct().getImage());
            rideProductViewModel.setCapacity(product.getProduct().getCapacity());
            rideProductViewModel.setTimeEstimate(product.getTimesEstimate().getEstimate() / 60 + " min");
            rideProductViewModel.setBaseFare(getBaseFare(product.getProduct().getPriceDetail()));
            rideProductViewModel.setCancellationFee(getCancellationCharges(product.getProduct().getPriceDetail()));
            rideProductViewModel.setEnabled(true);

            if (fareEstimate.getFare() != null) {
                rideProductViewModel.setProductPriceFmt(fareEstimate.getFare().getDisplay());
                rideProductViewModel.setProductPrice(fareEstimate.getFare().getValue());
                rideProductViewModel.setFareId(fareEstimate.getFare().getFareId());
                rideProductViewModel.setPromoCode(fareEstimate.getCode());
                rideProductViewModel.setPromoMessage(fareEstimate.getMessageSuccess());
            }

            if (fareEstimate.getEstimate() != null) {
                rideProductViewModel.setSurgePrice(true);
                rideProductViewModel.setSurgeMultiplier(fareEstimate.getEstimate().getSurgeMultiplier());
                rideProductViewModel.setSurgeConfirmationHref(fareEstimate.getEstimate().getSurgeConfirmationHref());
            }
        }
        return rideProductViewModel;
    }

    /**
     * This is the helper function to get base fare
     *
     * @param priceDetail
     * @return
     */
    private String getBaseFare(PriceDetail priceDetail) {
        //set base fare
        String baseFare = "--";
        if (priceDetail != null) {
            baseFare = "Base Fare: " + " " + formatNumber(priceDetail.getBase(), priceDetail.getCurrencyCode());
        }

        return baseFare;
    }

    /**
     * This is the helper function to get cancellationCharges
     *
     * @param priceDetail
     * @return
     */
    private String getCancellationCharges(PriceDetail priceDetail) {
        //set base fare
        String baseFare = "--";
        if (priceDetail != null) {
            baseFare = priceDetail.getCurrencyCode() + " " + priceDetail.getCancellationFee();
        }

        return baseFare;
    }


    private String getPricePerDistance(PriceDetail priceDetail) {
        //set base fare
        String productPrice = "--";
        if (priceDetail != null) {
            productPrice = formatNumber(priceDetail.getCostPerDistance(), priceDetail.getCurrencyCode()) + "/" + priceDetail.getDistanceUnit();
        }

        return productPrice;
    }

    private String formatNumber(String number, String currency) {
        try {
            if (currency.equalsIgnoreCase("RP")) {
                currency = "IDR";
            }

            NumberFormat format = NumberFormat.getCurrencyInstance(Locale.getDefault());
            format.setCurrency(Currency.getInstance(currency));
            String result = "";
            if (currency.equalsIgnoreCase("IDR") || currency.equalsIgnoreCase("RP")) {
                format.setMaximumFractionDigits(0);
                result = format.format(Float.parseFloat(number)).replace(",", ".").replace("IDR", "Rp");
            } else {
                result = format.format(number);
            }
            return result;
        } catch (Exception ex) {
            return currency + " " + number;
        }
    }
}
