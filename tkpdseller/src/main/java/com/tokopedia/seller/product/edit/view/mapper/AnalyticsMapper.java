package com.tokopedia.seller.product.edit.view.mapper;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.base.utils.StringUtils;
import com.tokopedia.seller.product.constant.InvenageSwitchTypeDef;
import com.tokopedia.seller.product.edit.view.model.upload.UploadProductInputViewModel;

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

        return StringUtils.convertListToStringDelimiter(listOfFields, ",");
    }
}
