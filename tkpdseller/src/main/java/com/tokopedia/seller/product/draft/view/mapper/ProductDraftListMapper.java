package com.tokopedia.seller.product.draft.view.mapper;

import android.text.TextUtils;

import com.tokopedia.seller.product.draft.view.model.ProductDraftViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductPictureViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;

import java.util.List;

public class ProductDraftListMapper {

    private static final int MAX_COMPLETION_COUNT = 5;
    private static final int MIN_COMPLETION_PERCENT = 5;
    private static final int MAX_COMPLETION_PERCENT = 95;

    public static ProductDraftViewModel mapDomainToView(ProductViewModel domainModel, long draftId) {
        String primaryPhotoUrl = null;
        List<ProductPictureViewModel> imageProductInputDomainModelList = domainModel.getProductPictureViewModelList();
        if (imageProductInputDomainModelList != null && imageProductInputDomainModelList.size() > 0 &&
                imageProductInputDomainModelList.get(0)!= null) {
            ProductPictureViewModel imageProductInputDomainModel = imageProductInputDomainModelList.get(0);
            primaryPhotoUrl = imageProductInputDomainModel.getUriOrPath();
        } else {
            primaryPhotoUrl = null;
        }
        String productName = domainModel.getProductName();

        long departmentId;
        if (domainModel.getProductCategory() != null) {
            departmentId = domainModel.getProductCategory().getCategoryId();
        } else {
            departmentId = 0;
        }
        double productPrice = domainModel.getProductPrice();
        int productWeight = (int) domainModel.getProductWeight();
        long etalaseId;
        if (domainModel.getProductEtalase()!= null) {
            etalaseId =domainModel.getProductEtalase().getEtalaseId();
        } else {
            etalaseId = 0;
        }

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
                draftId,
                primaryPhotoUrl,
                productName,
                completionPercent,
                !TextUtils.isEmpty(domainModel.getProductId()));
    }
}
