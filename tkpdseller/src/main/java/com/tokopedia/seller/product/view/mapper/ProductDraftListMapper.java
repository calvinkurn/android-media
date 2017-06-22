package com.tokopedia.seller.product.view.mapper;

import android.text.TextUtils;

import com.tokopedia.core.base.utils.StringUtils;
import com.tokopedia.seller.product.domain.model.ImageProductInputDomainModel;
import com.tokopedia.seller.product.domain.model.ProductPhotoListDomainModel;
import com.tokopedia.seller.product.domain.model.UploadProductInputDomainModel;
import com.tokopedia.seller.product.view.model.ProductDraftViewModel;
import com.tokopedia.seller.product.view.model.upload.intdef.ProductStatus;

/**
 * Created by User on 6/21/2017.
 */

public class ProductDraftListMapper {

    public static final int MAX_COMPLETION_COUNT = 5;
    public static final int MAX_COMPLETION_PERCENT = 95;

    public static ProductDraftViewModel mapDomainToView(UploadProductInputDomainModel domainModel) {
        long draftId = domainModel.getId();
        String primaryPhotoUrl = null;
        ProductPhotoListDomainModel productPhotos = domainModel.getProductPhotos();
        if (productPhotos!= null && productPhotos.getProductDefaultPicture() >= 0){
            ImageProductInputDomainModel imageProductInputDomainModel = domainModel.getProductPhotos().getPhotos().get(productPhotos.getProductDefaultPicture());
            String imageUrl = imageProductInputDomainModel.getUrl();
            String imagePath = imageProductInputDomainModel.getImagePath();

            if(StringUtils.isBlank(imageUrl)){
                if (StringUtils.isBlank(imagePath)) {
                    primaryPhotoUrl = null;
                } else {
                    primaryPhotoUrl = imagePath;
                }
            } else {
                primaryPhotoUrl = imageUrl;
            }
        }
        String productName = domainModel.getProductName();
        long departmentId = domainModel.getProductDepartmentId();
        double productPrice = domainModel.getProductPrice();
        int productWeight = domainModel.getProductWeight();
        long etalaseId = domainModel.getProductEtalaseId();

        int completionCount = 0;
        int completionPercent;
        if (!TextUtils.isEmpty(productName)) {
            completionCount++;
        }
        if (departmentId > 0) {
            completionCount++;
        }
        if (productPrice > 0) {
            completionCount++;
        }
        if (productWeight > 0) {
            completionCount++;
        }
        if (etalaseId > 0) {
            completionCount++;
        }
        if (completionCount == MAX_COMPLETION_COUNT){
            completionPercent = MAX_COMPLETION_PERCENT;
        } else {
            completionPercent = completionCount * 100 / MAX_COMPLETION_COUNT;
        }

        return new ProductDraftViewModel(
                draftId,
                primaryPhotoUrl,
                productName,
                completionPercent,
                domainModel.getProductStatus() == ProductStatus.EDIT);
    }
}
