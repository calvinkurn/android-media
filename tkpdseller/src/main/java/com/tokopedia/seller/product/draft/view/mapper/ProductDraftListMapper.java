package com.tokopedia.seller.product.draft.view.mapper;

import android.text.TextUtils;

import com.tokopedia.seller.product.draft.view.model.ProductDraftViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductPictureViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;

import java.util.List;

/**
 * Created by User on 6/21/2017.
 */

public class ProductDraftListMapper {

    public static final int MAX_COMPLETION_COUNT = 5;
    public static final int MIN_COMPLETION_PERCENT = 5;
    public static final int MAX_COMPLETION_PERCENT = 95;

    public static ProductDraftViewModel mapDomainToView(ProductViewModel domainModel) {
        String primaryPhotoUrl = null;
        List<ProductPictureViewModel> imageProductInputDomainModelList = domainModel.getProductPictureViewModelList();
        if (imageProductInputDomainModelList != null && imageProductInputDomainModelList.size() > 0) {
            ProductPictureViewModel imageProductInputDomainModel = imageProductInputDomainModelList.get(0);
            primaryPhotoUrl = imageProductInputDomainModel.getUrlOriginal();
        } else {
            primaryPhotoUrl = null;
        }
        String productName = domainModel.getProductName();
        long departmentId = domainModel.getProductCategory().getCategoryId();
        double productPrice = domainModel.getProductPrice();
        int productWeight = (int) domainModel.getProductWeight();
        long etalaseId = domainModel.getProductEtalase().getEtalaseId();

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
        if (completionCount == MAX_COMPLETION_COUNT) {
            completionPercent = MAX_COMPLETION_PERCENT;
        } else {
            completionPercent = completionCount * 100 / MAX_COMPLETION_COUNT;
        }
        if (completionPercent == 0) {
            completionPercent = MIN_COMPLETION_PERCENT;
        }

        return new ProductDraftViewModel(
                domainModel.getDraftId(),
                primaryPhotoUrl,
                productName,
                completionPercent,
                domainModel.getProductId() > 0);
    }
}
