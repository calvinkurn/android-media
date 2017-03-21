package com.tokopedia.ride.common.ride.data;

import com.tokopedia.ride.common.ride.data.entity.PriceDetailEntity;
import com.tokopedia.ride.common.ride.data.entity.ProductEntity;
import com.tokopedia.ride.common.ride.domain.model.PriceDetail;
import com.tokopedia.ride.common.ride.domain.model.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alvarisi on 3/14/17.
 */

public class ProductEntityMapper {

    public ProductEntityMapper() {
    }

    public Product transform(ProductEntity entity){
        Product product = null;
        if (entity != null){
            product = new Product();
            product.setProductId(entity.getProductId());
            product.setCapacity(entity.getCapacity());
            product.setCashEnabled(entity.isCashEnabled());
            product.setDescription(entity.getDescription());
            product.setDisplayName(entity.getDisplayName());
            product.setImage(entity.getImage());
            product.setProductGroup(entity.getProductGroup());
            product.setShared(entity.isShared());
            product.setShortDescription(entity.getShortDescription());
            product.setPriceDetail(transform(entity.getPriceDetailEntity()));
        }
        return product;
    }

    private PriceDetail transform(PriceDetailEntity entity){
        PriceDetail priceDetail = null;
        if (entity != null){
            priceDetail = new PriceDetail();
            priceDetail.setBase(entity.getBase());
            priceDetail.setCancellationFee(entity.getCancellationFee());
            priceDetail.setCostPerDistance(entity.getCostPerDistance());
            priceDetail.setCostPerMinute(entity.getCostPerMinute());
            priceDetail.setCostPerDistance(entity.getCostPerDistance());
            priceDetail.setCurrencyCode(entity.getCurrencyCode());
            priceDetail.setDistanceUnit(entity.getDistanceUnit());
            priceDetail.setMinimum(entity.getMinimum());
            priceDetail.setServiceFees(entity.getServiceFees());
        }
        return priceDetail;
    }

    public List<Product> transform(List<ProductEntity> entities){
        List<Product> products = new ArrayList<>();
        Product product;
        for (ProductEntity entity : entities){
            product = transform(entity);
            if (product != null)
                products.add(product);
        }
        return products;
    }
}
