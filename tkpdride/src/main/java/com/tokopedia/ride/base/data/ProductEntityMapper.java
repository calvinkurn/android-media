package com.tokopedia.ride.base.data;

import com.tokopedia.ride.base.data.entity.ProductEntity;
import com.tokopedia.ride.base.domain.model.Product;

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
            product.setUpfrontFareEnabled(entity.isUpfrontFareEnabled());
        }
        return product;
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
