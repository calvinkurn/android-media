package com.tokopedia.seller.product.edit.view.presenter;

import com.tokopedia.seller.product.edit.view.listener.ProductAddView;
import com.tokopedia.seller.product.edit.view.model.upload.UploadProductInputViewModel;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantByCatModel;

import java.util.List;

/**
 * @author sebastianuskh on 4/26/17.
 */

public interface ProductDraftView extends ProductAddView {

    void onSuccessLoadDraftProduct(UploadProductInputViewModel model);

    void onErrorLoadDraftProduct(Throwable throwable);

}
