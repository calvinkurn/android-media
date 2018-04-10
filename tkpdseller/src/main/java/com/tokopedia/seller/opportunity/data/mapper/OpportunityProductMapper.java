package com.tokopedia.seller.opportunity.data.mapper;

import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.product.model.productdetail.ProductImage;
import com.tokopedia.core.product.model.productdetail.ProductInfo;
import com.tokopedia.design.utils.CurrencyFormatHelper;
import com.tokopedia.seller.opportunity.domain.entity.OpportunityDetail;
import com.tokopedia.seller.opportunity.domain.entity.OpportunityProduct;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func2;

/**
 * Created by nakama on 10/04/18.
 */

public class OpportunityProductMapper implements Func2<ProductDetailData, OpportunityDetail, ProductDetailData> {
    @Override
    public ProductDetailData call(ProductDetailData productDetailData, OpportunityDetail opportunityDetail) {
        OpportunityProduct opportunityProduct = opportunityDetail.getDetail();
        ProductInfo productDetailInfo = productDetailData.getInfo();

        productDetailInfo.setProductName(opportunityProduct.getProductName());
        productDetailInfo.setProductUrl(opportunityProduct.getProductUrl());
        productDetailInfo.setProductDescription(opportunityProduct.getProductDescription());
        productDetailInfo.setProductWeight(opportunityProduct.getProductWeight());
        productDetailInfo.setProductPrice(CurrencyFormatHelper
                .ConvertToRupiah(opportunityProduct.getProductPrice()).replaceAll(",","."));

        productDetailData.setInfo(productDetailInfo);

        List<ProductImage> images = new ArrayList<>();
        for(String imageUrl : opportunityProduct.getProductPictures()){
            ProductImage image = new ProductImage();
            image.setImageSrc(imageUrl);
            images.add(image);
        }

        productDetailData.setProductImages(images);

        return productDetailData;
    }
}
