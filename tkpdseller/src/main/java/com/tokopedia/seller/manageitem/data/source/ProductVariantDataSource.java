package com.tokopedia.seller.manageitem.data.source;

import com.tokopedia.network.mapper.DataResponseMapper;
import com.tokopedia.seller.manageitem.data.cloud.ProductVariantCloud;
import com.tokopedia.seller.manageitem.data.cloud.model.product.ProductVariantByCatModel;
import com.tokopedia.seller.manageitem.data.cloud.model.product.ProductVariantByPrdModel;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author hendry on 4/7/17.
 */

public class ProductVariantDataSource {

    private ProductVariantCloud productVariantCloud;

    @Inject
    public ProductVariantDataSource(ProductVariantCloud productVariantCloud) {
        this.productVariantCloud = productVariantCloud;
    }

    public Observable<List<ProductVariantByCatModel>> fetchProductVariantByCat(long categoryId) {
        return productVariantCloud.fetchProductVariantByCat(categoryId)
                .map(new DataResponseMapper<List<ProductVariantByCatModel>>())
                .map(new Func1<List<ProductVariantByCatModel>, List<ProductVariantByCatModel>>() {
                    @Override
                    public List<ProductVariantByCatModel> call(List<ProductVariantByCatModel> productVariantByCatModels) {
                        Collections.sort(productVariantByCatModels, new Comparator<ProductVariantByCatModel>() {
                            @Override
                            public int compare(ProductVariantByCatModel o1, ProductVariantByCatModel o2) {
                                // we want descending. order.
                                return o2.getStatus() - o1.getStatus();
                            }
                        });
                        return productVariantByCatModels;
                    }
                });
    }

    public Observable<ProductVariantByPrdModel> fetchProductVariantByPrd(long productId) {
        return productVariantCloud.fetchProductVariantByPrd(productId)
                .map(new DataResponseMapper<ProductVariantByPrdModel>());
    }
}