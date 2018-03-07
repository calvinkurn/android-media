package com.tokopedia.inbox.attachproduct.domain.util;

import com.tokopedia.core.shopinfo.models.productmodel.Product;
import com.tokopedia.inbox.attachproduct.view.viewmodel.AttachProductItemViewModel;

/**
 * Created by Hendri on 14/02/18.
 */

public class DomainModelToViewModelConverter {
    public static AttachProductItemViewModel convertProductDomainModel(Product product){
        return new AttachProductItemViewModel(product.getProductUrl(),
                product.getProductName(),
                product.getProductId(),
                product.getProductImageFull(),
                product.getProductImage(),
                product.getProductPrice());
    }
}
