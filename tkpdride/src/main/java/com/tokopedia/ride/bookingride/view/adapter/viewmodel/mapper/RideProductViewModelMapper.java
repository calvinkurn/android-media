package com.tokopedia.ride.bookingride.view.adapter.viewmodel.mapper;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.ride.bookingride.domain.model.ProductEstimate;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.RideProductViewModel;
import com.tokopedia.ride.common.ride.domain.model.FareEstimate;
import com.tokopedia.ride.common.ride.domain.model.PriceDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alvarisi on 3/16/17.
 */

public class RideProductViewModelMapper {
    public RideProductViewModelMapper() {
    }

    /*public Visitable transform(Product product) {
        RideProductViewModel rideProductViewModel = null;
        if (product != null) {
            rideProductViewModel = new RideProductViewModel();
            rideProductViewModel.setProductName(product.getDisplayName());
            rideProductViewModel.setProductId(product.getProductId());
            rideProductViewModel.setProductImage(product.getImage());
        }
        return rideProductViewModel;
    }

    public List<Visitable> transform(List<Product> products) {
        List<Visitable> rideProductViewModels = new ArrayList<>();
        Visitable productViewModel = null;
        for (Product product : products) {
            productViewModel = transform(product);
            if (productViewModel != null)
                rideProductViewModels.add(productViewModel);
        }
        return rideProductViewModels;
    }*/

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
            rideProductViewModel.setProductPriceFmt(fareEstimate.getFare().getDisplay());
            rideProductViewModel.setProductPrice(fareEstimate.getFare().getValue());
            rideProductViewModel.setFareId(fareEstimate.getFare().getFareId());
            rideProductViewModel.setCapacity(product.getProduct().getCapacity());
            rideProductViewModel.setTimeEstimate(product.getTimesEstimate().getEstimate() / 60 + " min");
            rideProductViewModel.setBaseFare(getBaseFare(product.getProduct().getPriceDetail()));
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
            baseFare = "Base Fare: " + " " + priceDetail.getCurrencyCode() + " " + priceDetail.getBase();
        }

        return baseFare;
    }


    private String getPricePerDistance(PriceDetail priceDetail) {
        //set base fare
        String productPrice = "--";
        if (priceDetail != null) {
            productPrice = priceDetail.getCurrencyCode() + " " + priceDetail.getCostPerDistance() + "/" + priceDetail.getDistanceUnit();
        }

        return productPrice;
    }
}
