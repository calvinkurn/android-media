package com.tokopedia.seller.product.edit.domain.interactor.uploadproduct;

import com.tokopedia.seller.product.edit.domain.ImageProductUploadRepository;
import com.tokopedia.seller.product.edit.domain.model.AddProductPictureDomainModel;
import com.tokopedia.seller.product.edit.domain.model.UploadProductInputDomainModel;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 5/8/17.
 */

public class AddProductPicture implements Func1<UploadProductInputDomainModel, Observable<AddProductPictureDomainModel>> {
    private final ImageProductUploadRepository imageProductUploadRepository;

    public AddProductPicture(ImageProductUploadRepository imageProductUploadRepository) {
        this.imageProductUploadRepository = imageProductUploadRepository;
    }

    @Override
    public Observable<AddProductPictureDomainModel> call(UploadProductInputDomainModel domainModel) {
        return imageProductUploadRepository
                .addProductPicture(domainModel);
    }

}