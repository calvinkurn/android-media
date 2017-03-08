package com.tokopedia.seller.topads.data.mapper;


import android.support.annotation.NonNull;

import com.tokopedia.seller.topads.data.model.data.Product;
import com.tokopedia.seller.topads.data.model.response.DataResponse;
import com.tokopedia.seller.topads.domain.model.ProductDomain;
import com.tokopedia.seller.topads.domain.model.ProductListDomain;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author normansyahputa on 3/3/17.
 */

public class SearchProductEOFMapper implements Func1<Response<DataResponse<List<Product>>>, ProductListDomain> {
    @NonNull
    public static List<ProductDomain> getProductDomains(List<Product> data) {
        List<ProductDomain> productDomains = new ArrayList<>();

        for (Product product : data) {
            ProductDomain productDomain = new ProductDomain();
            productDomain.setAdId(product.getAdId());
            productDomain.setId(product.getId());
            productDomain.setImageUrl(product.getImageUrl());
            productDomain.setName(product.getName());
            productDomain.setPromoted(product.isPromoted());
            productDomain.setGroupName(product.getGroupName());

            productDomains.add(productDomain);
        }
        return productDomains;
    }

    @Override
    public ProductListDomain call(Response<DataResponse<List<Product>>> response) {

        List<Product> data = response.body().getData();

        ProductListDomain productListDomain = new ProductListDomain();

        List<ProductDomain> productDomains = getProductDomains(data);

        productListDomain.setProductDomains(productDomains);
        productListDomain.setEof(response.body().isEof());

        return productListDomain;
    }
}
