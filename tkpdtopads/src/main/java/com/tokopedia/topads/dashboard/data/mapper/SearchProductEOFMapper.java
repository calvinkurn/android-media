package com.tokopedia.topads.dashboard.data.mapper;


import android.support.annotation.NonNull;

import com.tokopedia.topads.dashboard.data.model.data.Product;
import com.tokopedia.seller.common.data.response.DataResponse;
import com.tokopedia.topads.dashboard.domain.model.ProductDomain;
import com.tokopedia.topads.dashboard.domain.model.ProductListDomain;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author normansyahputa on 3/3/17.
 */

public class SearchProductEOFMapper implements Func1<Response<DataResponse<List<Product>>>, ProductListDomain> {

    @Inject
    public SearchProductEOFMapper() {
    }

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
            productDomain.setDepartmentId(product.getDepartmentId());

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
