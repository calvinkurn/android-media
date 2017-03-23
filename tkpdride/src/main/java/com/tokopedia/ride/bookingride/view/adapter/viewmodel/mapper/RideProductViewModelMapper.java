package com.tokopedia.ride.bookingride.view.adapter.viewmodel.mapper;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.ride.bookingride.domain.model.ProductEstimate;
import com.tokopedia.ride.common.ride.domain.model.FareEstimate;
import com.tokopedia.ride.common.ride.domain.model.Product;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.RideProductViewModel;

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
        if (product != null) {
            rideProductViewModel = new RideProductViewModel();
            rideProductViewModel.setProductName(product.getProduct().getDisplayName());
            rideProductViewModel.setProductId(product.getProduct().getProductId());
            rideProductViewModel.setProductImage(product.getProduct().getImage());
            rideProductViewModel.setTimeEstimate(String.valueOf(product.getTimesEstimate().getEstimate()));
            rideProductViewModel.setCapacity(product.getProduct().getCapacity());
        }
        return rideProductViewModel;
    }

    public Visitable transform(ProductEstimate product, FareEstimate fareEstimate){
        RideProductViewModel rideProductViewModel = null;
        if (product != null) {
            rideProductViewModel = new RideProductViewModel();
            rideProductViewModel.setProductName(product.getProduct().getDisplayName());
            rideProductViewModel.setProductId(product.getProduct().getProductId());
            rideProductViewModel.setProductImage(product.getProduct().getImage());
            rideProductViewModel.setProductPrice(fareEstimate.getFare().getDisplay());
            rideProductViewModel.setBaseFare(fareEstimate.getFare().getDisplay());
            rideProductViewModel.setTimeEstimate(String.valueOf(product.getTimesEstimate().getEstimate()));
            rideProductViewModel.setFareId(fareEstimate.getFare().getFareId());
            rideProductViewModel.setCapacity(product.getProduct().getCapacity());
        }
        return rideProductViewModel;

    }
}
