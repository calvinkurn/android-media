package com.tokopedia.ride.bookingride.view.adapter.viewmodel.mapper;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.ride.bookingride.domain.model.ProductEstimate;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.RideProductViewModel;
import com.tokopedia.ride.common.ride.domain.model.PriceDetail;
import com.tokopedia.ride.common.ride.domain.model.PriceEstimate;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

/**
 * Created by alvarisi on 3/16/17.
 */

public class RideProductViewModelMapper {
    private static final int DEFAULT_TIME_ESTIMATE = 15;

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
            if (isIndonesiaCurrency(priceDetail)) {
                baseFare = "Base Fare: " + getStringIdrFormat(Integer.parseInt(priceDetail.getBase()));
            } else {
                baseFare = formatNumber(priceDetail.getBase(), priceDetail.getCurrencyCode());
            }
        }

        return baseFare;
    }

    private boolean isIndonesiaCurrency(PriceDetail priceDetail) {
        return priceDetail.getCurrencyCode().equalsIgnoreCase("IDR") || priceDetail.getCurrencyCode().equalsIgnoreCase("Rp");
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
            if (isIndonesiaCurrency(priceDetail)) {
                baseFare = getStringIdrFormat(Integer.parseInt(priceDetail.getCancellationFee()));
            } else {
                baseFare = formatNumber(priceDetail.getCancellationFee(), priceDetail.getCurrencyCode());
            }
        }

        return baseFare;
    }


    private String getPricePerDistance(PriceDetail priceDetail) {
        //set base fare
        String productPrice = "--";
        if (priceDetail != null) {
            if (isIndonesiaCurrency(priceDetail)) {
                productPrice = getStringIdrFormat(Integer.parseInt(priceDetail.getCostPerDistance())) + "/" + priceDetail.getDistanceUnit();
            } else {
                productPrice = formatNumber(priceDetail.getCostPerDistance(), priceDetail.getCurrencyCode()) + "/" + priceDetail.getDistanceUnit();
            }
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
                result = format.format(Float.parseFloat(number)).replace(",", ".").replace("IDR", "Rp ");
            } else {
                result = format.format(number);
            }
            return result;
        } catch (Exception ex) {
            return currency + " " + number;
        }
    }

    public List<RideProductViewModel> transformRide(List<ProductEstimate> productEstimateList, List<PriceEstimate> timePriceEstimates) {
        List<RideProductViewModel> rideProductViewModels = new ArrayList<>();
        RideProductViewModel rideProductViewModel = null;
        for (ProductEstimate productEstimate : productEstimateList) {
            rideProductViewModel = transformProductEstimate(productEstimate);
            for (PriceEstimate priceEstimate : timePriceEstimates) {
                if (productEstimate.getProduct().getProductId().equalsIgnoreCase(priceEstimate.getProductId())) {
                    rideProductViewModel = transformWithPriceEstimate(rideProductViewModel, priceEstimate);
                    break;
                }
            }
            if (rideProductViewModel != null)
                rideProductViewModels.add(rideProductViewModel);
        }
        return rideProductViewModels;
    }

    private RideProductViewModel transformProductEstimate(ProductEstimate product) {
        RideProductViewModel rideProductViewModel = null;
        if (product != null) {
            rideProductViewModel = new RideProductViewModel();
            rideProductViewModel.setProductName(product.getProduct().getDisplayName());
            rideProductViewModel.setProductId(product.getProduct().getProductId());
            rideProductViewModel.setProductImage(product.getProduct().getImage());
            rideProductViewModel.setCapacity(product.getProduct().getCapacity());
            if (product.getTimesEstimate() != null) {
                rideProductViewModel.setTimeEstimate(product.getTimesEstimate().getEstimate() / 60 + " min");
            } else {
                rideProductViewModel.setTimeEstimate(DEFAULT_TIME_ESTIMATE + " min");
            }
            rideProductViewModel.setBaseFare(getBaseFare(product.getProduct().getPriceDetail()));
            rideProductViewModel.setCancellationFee(getCancellationCharges(product.getProduct().getPriceDetail()));
            rideProductViewModel.setEnabled(false);
            rideProductViewModel.setProductPriceFmt("--");
        }
        return rideProductViewModel;
    }

    private RideProductViewModel transformWithPriceEstimate(RideProductViewModel product, PriceEstimate priceEstimate) {
        if (product != null && priceEstimate != null) {
            product.setEnabled(true);
            if (priceEstimate.getCurrencyCode().equalsIgnoreCase("IDR") || priceEstimate.getCurrencyCode().equalsIgnoreCase("Rp"))
                product.setProductPriceFmt(getStringIdrFormat(priceEstimate.getLowEstimate()) + " - " + getStringIdrFormat(priceEstimate.getHighEstimate()));
            else
                product.setProductPriceFmt(priceEstimate.getEstimate());
        }
        return product;
    }

    private static String getStringIdrFormat(int value) {
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        kursIndonesia.setMaximumFractionDigits(0);
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp ");
        formatRp.setGroupingSeparator('.');
        formatRp.setMonetaryDecimalSeparator('.');
        formatRp.setDecimalSeparator('.');
        kursIndonesia.setDecimalFormatSymbols(formatRp);

        return kursIndonesia.format(value);
    }
}
