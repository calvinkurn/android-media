package com.tokopedia.seller.topads.data.mapper;


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
    @Override
    public ProductListDomain call(Response<DataResponse<List<Product>>> response) {

        ProductListDomain productListDomain = new ProductListDomain();

        List<ProductDomain> productDomains = new ArrayList<>();

        for (Product product : response.body().getData()) {
            ProductDomain productDomain = new ProductDomain();
            productDomain.setAdId(product.getAdId());
            productDomain.setId(product.getId());
            productDomain.setImageUrl(product.getImageUrl());
            productDomain.setName(product.getName());
            productDomain.setPromoted(product.isPromoted());
            productDomain.setGroupName(product.getGroupName());

            productDomains.add(productDomain);
        }

        productListDomain.setProductDomains(productDomains);
        productListDomain.setEof(response.body().isEof());

        return productListDomain;
    }
}
