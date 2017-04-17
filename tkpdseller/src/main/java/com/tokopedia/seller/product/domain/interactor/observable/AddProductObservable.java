package com.tokopedia.seller.product.domain.interactor.observable;

import android.text.TextUtils;

import com.tokopedia.seller.product.domain.UploadProductRepository;
import com.tokopedia.seller.product.domain.mapper.AddProductDomainMapper;
import com.tokopedia.seller.product.domain.model.AddProductPictureDomainModel;
import com.tokopedia.seller.product.domain.model.AddProductPictureInputDomainModel;
import com.tokopedia.seller.product.domain.model.AddProductSubmitInputDomainModel;
import com.tokopedia.seller.product.domain.model.AddProductValidationDomainModel;
import com.tokopedia.seller.product.domain.model.AddProductDomainModel;
import com.tokopedia.seller.product.domain.model.UploadProductInputDomainModel;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/10/17.
 */

public class AddProductObservable
        implements Func1<UploadProductInputDomainModel,
                Observable<AddProductDomainModel>> {
    private final UploadProductRepository uploadProductRepository;

    @Inject
    public AddProductObservable(UploadProductRepository uploadProductRepository) {
        this.uploadProductRepository = uploadProductRepository;
    }

    @Override
    public Observable<AddProductDomainModel> call(
            UploadProductInputDomainModel uploadProductInputDomainModel
    ) {
        return Observable.just(uploadProductInputDomainModel)
                .flatMap(new AddProductValidation())
                .flatMap(new ProcessAddProductValidation(uploadProductInputDomainModel));
    }

    private class AddProductValidation implements Func1<UploadProductInputDomainModel, Observable<AddProductValidationDomainModel>> {
        @Override
        public Observable<AddProductValidationDomainModel> call(UploadProductInputDomainModel uploadProductInputDomainModel) {
            return uploadProductRepository
                    .addProductValidation(uploadProductInputDomainModel);
        }
    }

    private class ProcessAddProductValidation implements Func1<AddProductValidationDomainModel, Observable<AddProductDomainModel>> {
        private final UploadProductInputDomainModel uploadProductInputDomainModel;

        public ProcessAddProductValidation(UploadProductInputDomainModel uploadProductInputDomainModel) {
            this.uploadProductInputDomainModel = uploadProductInputDomainModel;
        }

        @Override
        public Observable<AddProductDomainModel> call(AddProductValidationDomainModel addProductValidationDomainModel) {
            String postKey = addProductValidationDomainModel.getPostKey();
            if (!TextUtils.isEmpty(postKey)){
                AddProductPictureInputDomainModel addProductPictureInputModel = AddProductDomainMapper.mapUploadToPicture(uploadProductInputDomainModel);
                return Observable.just(addProductPictureInputModel)
                        .flatMap(new AddProductPicture())
                        .map(new ProcessAddProductPicture(uploadProductInputDomainModel, postKey))
                        .flatMap(new AddProductSubmit());
            } else {
                return Observable.just(AddProductDomainMapper.mapValidationToSubmit(addProductValidationDomainModel));
            }
        }
    }

    private class AddProductPicture implements Func1<AddProductPictureInputDomainModel, Observable<AddProductPictureDomainModel>> {
        @Override
        public Observable<AddProductPictureDomainModel> call(AddProductPictureInputDomainModel addProductPictureInputDomainModel) {
            return uploadProductRepository
                    .addProductPicture(addProductPictureInputDomainModel);
        }
    }

    private class ProcessAddProductPicture implements Func1<AddProductPictureDomainModel, AddProductSubmitInputDomainModel> {
        private final UploadProductInputDomainModel uploadProductInputDomainModel;
        private final String postKey;

        public ProcessAddProductPicture(UploadProductInputDomainModel uploadProductInputDomainModel, String postKey) {
            this.uploadProductInputDomainModel = uploadProductInputDomainModel;
            this.postKey = postKey;
        }

        @Override
        public AddProductSubmitInputDomainModel call(AddProductPictureDomainModel addProductPictureDomainModel) {
            return AddProductDomainMapper.mapUploadToSubmit(addProductPictureDomainModel, uploadProductInputDomainModel, postKey);
        }

    }

    private class AddProductSubmit implements Func1<AddProductSubmitInputDomainModel, Observable<AddProductDomainModel>> {
        @Override
        public Observable<AddProductDomainModel> call(AddProductSubmitInputDomainModel addProductSubmitInputDomainModel) {
            return uploadProductRepository
                    .addProductSubmit(addProductSubmitInputDomainModel);
        }
    }
}
