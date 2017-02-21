package com.tokopedia.seller.topads.data.mapper;

import com.tokopedia.seller.topads.domain.model.ProductDomain;
import com.tokopedia.seller.topads.domain.model.data.Product;
import com.tokopedia.seller.topads.domain.model.response.DataResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author normansyahputa on 2/17/17.
 */
public class SearchProductMapper implements Func1<Response<DataResponse<List<Product>>>, List<ProductDomain>> {
    @Override
    public List<ProductDomain> call(Response<DataResponse<List<Product>>> response) {

        List<ProductDomain> productDomains = new ArrayList<>();

        for (Product product : response.body().getData()) {
            ProductDomain productDomain = new ProductDomain();
            productDomain.setAdId(product.getAdId());
            productDomain.setId(product.getAdId());
            productDomain.setImageUrl(product.getImageUrl());
            productDomain.setName(product.getName());
            productDomain.setPromoted(product.isPromoted());
            productDomain.setGroupName(product.getGroupName());

            productDomains.add(productDomain);
        }

        return productDomains;
    }
}
