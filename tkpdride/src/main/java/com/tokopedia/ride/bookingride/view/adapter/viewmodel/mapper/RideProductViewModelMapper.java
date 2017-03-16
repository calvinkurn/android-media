package com.tokopedia.ride.bookingride.view.adapter.viewmodel.mapper;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.ride.base.domain.model.Product;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.RideProductViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alvarisi on 3/16/17.
 */

public class RideProductViewModelMapper {
    public RideProductViewModelMapper() {
    }

    public Visitable transform(Product product) {
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
    }
}
