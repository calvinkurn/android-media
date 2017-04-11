package com.tokopedia.seller.product.domain.interactor.observable;

import android.text.TextUtils;

import com.tokopedia.seller.product.domain.AddProductRepository;
import com.tokopedia.seller.product.domain.mapper.AddProductValidationToSubmitMapper;
import com.tokopedia.seller.product.domain.model.AddProductPictureDomainModel;
import com.tokopedia.seller.product.domain.model.AddProductPictureInputDomainModel;
import com.tokopedia.seller.product.domain.model.AddProductSubmitInputDomainModel;
import com.tokopedia.seller.product.domain.model.AddProductValidationDomainModel;
import com.tokopedia.seller.product.domain.model.AddProductDomainModel;
import com.tokopedia.seller.product.domain.model.UploadProductInputDomainModel;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/10/17.
 */

public class AddProductObservable
        implements Func1<UploadProductInputDomainModel,
                Observable<AddProductDomainModel>> {
    private final AddProductRepository addProductRepository;

    public AddProductObservable(AddProductRepository addProductRepository) {
        this.addProductRepository = addProductRepository;
    }

    @Override
    public Observable<AddProductDomainModel> call(
            UploadProductInputDomainModel uploadProductInputDomainModel
    ) {
        return Observable.just(uploadProductInputDomainModel)
                .flatMap(new AddProductValidation())
                .flatMap(new ProcessAddProductValidation());
    }

    private class AddProductValidation implements Func1<UploadProductInputDomainModel, Observable<AddProductValidationDomainModel>> {
        @Override
        public Observable<AddProductValidationDomainModel> call(UploadProductInputDomainModel uploadProductInputDomainModel) {
            return addProductRepository
                    .addProductValidation(uploadProductInputDomainModel);
        }
    }

    private class ProcessAddProductValidation implements Func1<AddProductValidationDomainModel, Observable<AddProductDomainModel>> {
        @Override
        public Observable<AddProductDomainModel> call(AddProductValidationDomainModel addProductValidationDomainModel) {
            if (!TextUtils.isEmpty(addProductValidationDomainModel.getPostKey())){
                AddProductPictureInputDomainModel addProductPictureInputModel = new AddProductPictureInputDomainModel();
                return Observable.just(addProductPictureInputModel)
                        .flatMap(new AddProductPicture())
                        .map(new ProcessAddProductPicture())
                        .flatMap(new AddProductSubmit());
            } else {
                return Observable.just(AddProductValidationToSubmitMapper.map(addProductValidationDomainModel));
            }
        }
    }

    private class AddProductPicture implements Func1<AddProductPictureInputDomainModel, Observable<AddProductPictureDomainModel>> {
        @Override
        public Observable<AddProductPictureDomainModel> call(AddProductPictureInputDomainModel addProductPictureInputDomainModel) {
            return addProductRepository
                    .addProductPicture(addProductPictureInputDomainModel);
        }
    }

    private class ProcessAddProductPicture implements Func1<AddProductPictureDomainModel, AddProductSubmitInputDomainModel> {
        @Override
        public AddProductSubmitInputDomainModel call(AddProductPictureDomainModel addProductPictureDomainModel) {
            return null;
        }

    }

    private class AddProductSubmit implements Func1<AddProductSubmitInputDomainModel, Observable<AddProductDomainModel>> {

        @Override
        public Observable<AddProductDomainModel> call(AddProductSubmitInputDomainModel addProductSubmitInputDomainModel) {
            return addProductRepository
                    .addProductSubmit(addProductSubmitInputDomainModel);
        }
    }
}
