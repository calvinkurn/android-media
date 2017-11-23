package com.tokopedia.seller.product.edit.view.mapper;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.base.utils.StringUtils;
import com.tokopedia.seller.product.edit.constant.InvenageSwitchTypeDef;
import com.tokopedia.seller.product.edit.view.model.upload.UploadProductInputViewModel;
import com.tokopedia.seller.product.variant.constant.ProductVariantConstant;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.ProductVariantDataSubmit;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.ProductVariantUnitSubmit;
import com.tokopedia.seller.product.variant.data.source.ProductVariantDataSource;
import com.tokopedia.seller.product.variant.util.ProductVariantUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 5/10/17.
 */

public class AnalyticsMapper {

    public static String mapViewToAnalytic(UploadProductInputViewModel viewModel, int freeReturnActive, boolean isShare) {
        List<String> listOfFields = new ArrayList<>();
        if(viewModel.getProductPhotos().getPhotos().size() > 0){
            listOfFields.add(AppEventTracking.AddProduct.FIELDS_OPTIONAL_PICTURE);
        }
        if(viewModel.getProductWholesaleList()!= null && viewModel.getProductWholesaleList().size() >0 ){
            listOfFields.add(AppEventTracking.AddProduct.FIELDS_OPTIONAL_WHOLESALE);
        }
        if(viewModel.getProductInvenageSwitch() == InvenageSwitchTypeDef.TYPE_ACTIVE){
            listOfFields.add(AppEventTracking.AddProduct.FIELDS_OPTIONAL_STOCK_MANAGEMENT);
        }
        if(viewModel.getProductReturnable() == freeReturnActive){
            listOfFields.add(AppEventTracking.AddProduct.FIELDS_OPTIONAL_FREE_RETURN);
        }
        if(StringUtils.isNotBlank(viewModel.getProductDescription())){
            listOfFields.add(AppEventTracking.AddProduct.FIELDS_OPTIONAL_DESCRIPTION);
        }
        if(viewModel.getProductVideos() != null && viewModel.getProductVideos().size() > 0){
            listOfFields.add(AppEventTracking.AddProduct.FIELDS_OPTIONAL_PRODUCT_VIDEO);
        }
        if(viewModel.getPoProcessValue() > 0){
            listOfFields.add(AppEventTracking.AddProduct.FIELDS_OPTIONAL_PREORDER);
        }
        if(isShare){
            listOfFields.add(AppEventTracking.AddProduct.FIELDS_OPTIONAL_SHARE);
        }
        if (viewModel.getSwitchVariant() == ProductVariantConstant.SWITCH_VARIANT_EXIST) {
            boolean hasCustomVariantLv1 = false;
            boolean hasCustomVariantLv2 = false;
            List<ProductVariantUnitSubmit> productVariantUnitSubmitList = viewModel.getProductVariantDataSubmit().getProductVariantUnitSubmitList();
            if (productVariantUnitSubmitList.size() >= 1) {
                hasCustomVariantLv1 = ProductVariantUtils.hasCustomVariant(productVariantUnitSubmitList, ProductVariantConstant.VARIANT_LEVEL_ONE_VALUE);
                if (!hasCustomVariantLv1) {
                    listOfFields.add(AppEventTracking.AddProduct.FIELDS_OPTIONAL_VARIANT_LEVEL1);
                }
            }
            if (productVariantUnitSubmitList.size() >= 2) {
                hasCustomVariantLv2 = ProductVariantUtils.hasCustomVariant(productVariantUnitSubmitList, ProductVariantConstant.VARIANT_LEVEL_TWO_VALUE);
                if (!hasCustomVariantLv2) {
                    listOfFields.add(AppEventTracking.AddProduct.FIELDS_OPTIONAL_VARIANT_LEVEL2);
                }
            }
            if (hasCustomVariantLv1) {
                listOfFields.add(AppEventTracking.AddProduct.FIELDS_OPTIONAL_VARIANT_LEVEL1_CUSTOM);
            }
            if (hasCustomVariantLv2) {
                listOfFields.add(AppEventTracking.AddProduct.FIELDS_OPTIONAL_VARIANT_LEVEL2_CUSTOM);
            }
        }

        return StringUtils.convertListToStringDelimiter(listOfFields, ",");
    }
}
