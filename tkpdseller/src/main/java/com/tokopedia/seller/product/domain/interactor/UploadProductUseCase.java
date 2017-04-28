package com.tokopedia.seller.product.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.base.utils.StringUtils;
import com.tokopedia.seller.product.data.exception.UploadProductException;
import com.tokopedia.seller.product.domain.GenerateHostRepository;
import com.tokopedia.seller.product.domain.ImageProductUploadRepository;
import com.tokopedia.seller.product.domain.ProductDraftRepository;
import com.tokopedia.seller.product.domain.UploadProductRepository;
import com.tokopedia.seller.product.domain.listener.AddProductNotificationListener;
import com.tokopedia.seller.product.domain.mapper.AddProductDomainMapper;
import com.tokopedia.seller.product.domain.model.AddProductDomainModel;
import com.tokopedia.seller.product.domain.model.AddProductPictureDomainModel;
import com.tokopedia.seller.product.domain.model.AddProductSubmitInputDomainModel;
import com.tokopedia.seller.product.domain.model.AddProductValidationDomainModel;
import com.tokopedia.seller.product.domain.model.EditImageProductDomainModel;
import com.tokopedia.seller.product.domain.model.GenerateHostDomainModel;
import com.tokopedia.seller.product.domain.model.ImageProcessDomainModel;
import com.tokopedia.seller.product.domain.model.ImageProductInputDomainModel;
import com.tokopedia.seller.product.domain.model.ProductPhotoListDomainModel;
import com.tokopedia.seller.product.domain.model.UploadProductInputDomainModel;
import com.tokopedia.seller.product.view.model.upload.intdef.ProductStatus;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/10/17.
 */

public class UploadProductUseCase extends UseCase<AddProductDomainModel> {

    public static final String UPLOAD_PRODUCT_ID = "UPLOAD_PRODUCT_ID";
    public static final int UNSELECTED_PRODUCT_ID = -1;

    private final ProductDraftRepository productDraftRepository;
    private final ImageProductUploadRepository imageProductUploadRepository;
    private final GenerateHostRepository generateHostRepository;
    private final UploadProductRepository uploadProductRepository;

    private AddProductNotificationListener listener;
    private int stepNotification;

    @Inject
    public UploadProductUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            ProductDraftRepository productDraftRepository,
            GenerateHostRepository generateHostRepository,
            ImageProductUploadRepository imageProductUploadRepository,
            UploadProductRepository uploadProductRepository) {
        super(threadExecutor, postExecutionThread);
        this.productDraftRepository = productDraftRepository;
        this.generateHostRepository = generateHostRepository;
        this.imageProductUploadRepository = imageProductUploadRepository;
        this.uploadProductRepository = uploadProductRepository;
    }

    public void setListener(AddProductNotificationListener listener) {
        this.listener = listener;
    }

    public static RequestParams generateUploadProductParam(long productId) {
        RequestParams params = RequestParams.create();
        params.putLong(UPLOAD_PRODUCT_ID, productId);
        return params;
    }

    @Override
    public Observable<AddProductDomainModel> createObservable(RequestParams requestParams) {
        long productId = requestParams.getLong(UPLOAD_PRODUCT_ID, UNSELECTED_PRODUCT_ID);
        if (productId == UNSELECTED_PRODUCT_ID) {
            throw new RuntimeException("Input model is missing");
        }
        return Observable.just(productId)
                .flatMap(new GetProductModelObservable())
                .flatMap(new UploadProduct(productId));
    }

    private class GetProductModelObservable implements Func1<Long, Observable<UploadProductInputDomainModel>> {

        @Override
        public Observable<UploadProductInputDomainModel> call(Long productId) {
            return productDraftRepository.getDraft(productId);
        }

    }

    private class UploadProduct implements Func1<UploadProductInputDomainModel, Observable<AddProductDomainModel>> {
        private long productId;

        public UploadProduct(long productId) {
            this.productId = productId;
        }

        @Override
        public Observable<AddProductDomainModel> call(final UploadProductInputDomainModel domainModel) {
            createNotification(domainModel.getProductName());
            return Observable.just(domainModel)
                    .flatMap(new GetGeneratedHost())
                    .doOnNext(new UpdateNotification())
                    .map(new PrepareUploadImage(domainModel))
                    .flatMap(new ProceedUploadProduct())
                    .onErrorResumeNext(new AddProductStatusToError(domainModel.getProductStatus()));
        }

        private class AddProductStatusToError implements Func1<Throwable, Observable<? extends AddProductDomainModel>> {
            @ProductStatus
            private int productStatus;

            public AddProductStatusToError(@ProductStatus int productStatus) {
                this.productStatus = productStatus;
            }

            @Override
            public Observable<? extends AddProductDomainModel> call(Throwable throwable) {
                throw new UploadProductException(String.valueOf(productId), productStatus, throwable);
            }
        }
    }

    private class UpdateNotification implements Action1<Object> {
        @Override
        public void call(Object o) {
            if (listener != null) {
                stepNotification ++;
                listener.notificationUpdate(stepNotification);
            }
        }
    }

    private void createNotification(String productName) {
        if (listener != null) {
            stepNotification = 0;
            listener.createNotification(productName);
        }
    }

    private class GetGeneratedHost implements Func1<UploadProductInputDomainModel, Observable<GenerateHostDomainModel>> {
        @Override
        public Observable<GenerateHostDomainModel> call(UploadProductInputDomainModel domainModel) {
            return generateHostRepository.generateHost();
        }
    }

    private class PrepareUploadImage implements Func1<GenerateHostDomainModel, UploadProductInputDomainModel> {

        private final UploadProductInputDomainModel domainModel;

        public PrepareUploadImage(UploadProductInputDomainModel domainModel) {
            this.domainModel = domainModel;
        }

        @Override
        public UploadProductInputDomainModel call(GenerateHostDomainModel generateHost) {
            domainModel.setServerId(generateHost.getServerId());
            domainModel.setHostUrl(generateHost.getUrl());
            return domainModel;
        }
    }

    private class ProceedUploadProduct implements Func1<UploadProductInputDomainModel, Observable<AddProductDomainModel>> {
        @Override
        public Observable<AddProductDomainModel> call(UploadProductInputDomainModel domainModel) {
            if (domainModel.getProductStatus() == ProductStatus.ADD){
                return Observable.just(domainModel)
                        .flatMap(new AddProductImage())
                        .doOnNext(new UpdateNotification())
                        .map(new PrepareAddProductValidation(domainModel))
                        .flatMap(new AddProductValidation())
                        .doOnNext(new UpdateNotification())
                        .flatMap(new ProcessAddProductValidation(domainModel))
                        .doOnNext(new UpdateNotification());
            } else if (domainModel.getProductStatus() == ProductStatus.EDIT){
                return Observable.just(domainModel)
                        .flatMap(new EditProductImage())
                        .doOnNext(new UpdateNotification())
                        .map(new PrepareEditProduct(domainModel))
                        .flatMap(new EditProduct())
                        .map(new ToUploadProductModel(domainModel));
            } else {
                throw new RuntimeException("No product status available");
            }
        }

        private class ToUploadProductModel implements Func1<Boolean, AddProductDomainModel> {
            private final UploadProductInputDomainModel domainModel;

            public ToUploadProductModel(UploadProductInputDomainModel domainModel) {
                this.domainModel = domainModel;
            }

            @Override
            public AddProductDomainModel call(Boolean aBoolean) {
                AddProductDomainModel uploadProductDomainModel = new AddProductDomainModel();
                uploadProductDomainModel.setProductName(domainModel.getProductName());
                uploadProductDomainModel.setProductDesc(domainModel.getProductDescription());
                return uploadProductDomainModel;
            }
        }
    }

    private class AddProductImage implements Func1<UploadProductInputDomainModel, Observable<List<ImageProductInputDomainModel>>> {

        @Override
        public Observable<List<ImageProductInputDomainModel>> call(UploadProductInputDomainModel domainModel) {
            return Observable.from(domainModel.getProductPhotos().getPhotos())
                    .flatMap(new UploadSingleImage(domainModel.getServerId(), domainModel.getHostUrl()))
                    .toList();
        }
        private class UploadSingleImage implements Func1<ImageProductInputDomainModel, Observable<ImageProductInputDomainModel>> {

            private final int serverId;

            private final String hostUrl;

            public UploadSingleImage(int serverId, String hostUrl) {
                this.serverId = serverId;
                this.hostUrl = hostUrl;
            }

            @Override
            public Observable<ImageProductInputDomainModel> call(ImageProductInputDomainModel imageProductInputDomainModel) {
                if(StringUtils.isNotBlank(imageProductInputDomainModel.getImagePath())){
                    return imageProductUploadRepository.uploadImageProduct(
                            hostUrl,
                            serverId,
                            imageProductInputDomainModel.getImagePath(),
                            0)
                            .map(new MapImageModelToProductInput(imageProductInputDomainModel));
                }else{
                    return Observable.just(imageProductInputDomainModel);
                }
            }

            private class MapImageModelToProductInput implements Func1<ImageProcessDomainModel, ImageProductInputDomainModel> {

                private final ImageProductInputDomainModel inputDomainModel;

                public MapImageModelToProductInput(ImageProductInputDomainModel inputDomainModel) {
                    this.inputDomainModel = inputDomainModel;
                }

                @Override
                public ImageProductInputDomainModel call(ImageProcessDomainModel uploadImageDomainModel) {
                    inputDomainModel.setUrl(uploadImageDomainModel.getUrl());
                    return inputDomainModel;
                }

            }
        }

    }

    private class PrepareAddProductValidation implements Func1<List<ImageProductInputDomainModel>, UploadProductInputDomainModel> {

        private final UploadProductInputDomainModel domainModel;

        public PrepareAddProductValidation(UploadProductInputDomainModel domainModel) {
            this.domainModel = domainModel;
        }
        @Override
        public UploadProductInputDomainModel call(List<ImageProductInputDomainModel> imageProductInputDomainModels) {
            ProductPhotoListDomainModel productPhotos = domainModel.getProductPhotos();
            productPhotos.setPhotos(imageProductInputDomainModels);
            return domainModel;
        }

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
            if (StringUtils.isNotBlank(postKey)) {
                uploadProductInputDomainModel.setPostKey(postKey);
                return Observable.just(uploadProductInputDomainModel)
                        .flatMap(new AddProductPicture())
                        .map(new ProcessAddProductPicture(uploadProductInputDomainModel, postKey))
                        .flatMap(new AddProductSubmit());
            } else {
                return Observable.just(AddProductDomainMapper.mapValidationToSubmit(addProductValidationDomainModel));
            }
        }

    }

    private class AddProductPicture implements Func1<UploadProductInputDomainModel, Observable<AddProductPictureDomainModel>> {
        @Override
        public Observable<AddProductPictureDomainModel> call(UploadProductInputDomainModel domainModel) {
            return imageProductUploadRepository
                    .addProductPicture(domainModel);
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

    private class EditProductImage implements Func1<UploadProductInputDomainModel, Observable<List<ImageProductInputDomainModel>>> {
        private String hostUrl;
        private String productId;
        private int serverId;

        @Override
        public Observable<List<ImageProductInputDomainModel>> call(UploadProductInputDomainModel domainModel) {
            hostUrl = domainModel.getHostUrl();
            serverId = domainModel.getServerId();
            productId = domainModel.getProductId();
            return Observable.from(domainModel.getProductPhotos().getPhotos())
                    .flatMap(new EditProductSigleImage())
                    .filter(new Func1<ImageProductInputDomainModel, Boolean>() {
                        @Override
                        public Boolean call(ImageProductInputDomainModel imageProductInputDomainModel) {
                            return imageProductInputDomainModel != null && StringUtils.isNotBlank(imageProductInputDomainModel.getPicId());
                        }
                    })
                    .toList();
        }

        private class EditProductSigleImage implements Func1<ImageProductInputDomainModel, Observable<ImageProductInputDomainModel>> {
            @Override
            public Observable<ImageProductInputDomainModel> call(ImageProductInputDomainModel domainModel) {
                if (StringUtils.isNotBlank(domainModel.getImagePath())){
                    return Observable
                            .just(domainModel)
                            .flatMap(new UploadNewImage());
                } else if (StringUtils.isBlank(domainModel.getUrl())){
                    return Observable
                            .just(domainModel)
                            .flatMap(new DeleteImage());
                }
                return Observable.just(domainModel);
            }

            private class UploadNewImage implements Func1<ImageProductInputDomainModel, Observable<ImageProductInputDomainModel>> {

                @Override
                public Observable<ImageProductInputDomainModel> call(ImageProductInputDomainModel domainModel) {
                    return imageProductUploadRepository.uploadImageProduct(
                            hostUrl,
                            serverId,
                            domainModel.getImagePath(),
                            Integer.parseInt(productId))
                            .flatMap(new EditProductPicture())
                            .map(new CreateImageModel(domainModel));

                }

                private class EditProductPicture implements Func1<ImageProcessDomainModel, Observable<EditImageProductDomainModel>> {
                    @Override
                    public Observable<EditImageProductDomainModel> call(ImageProcessDomainModel imageProcessDomainModel) {
                        return uploadProductRepository.editImageProduct(imageProcessDomainModel.getPicObj());
                    }
                }

                private class CreateImageModel implements Func1<EditImageProductDomainModel, ImageProductInputDomainModel> {
                    private final ImageProductInputDomainModel domainModel;

                    public CreateImageModel(ImageProductInputDomainModel domainModel) {
                        this.domainModel = domainModel;
                    }

                    @Override
                    public ImageProductInputDomainModel call(EditImageProductDomainModel domainModel) {
                        this.domainModel.setPicId(domainModel.getPicId());
                        return this.domainModel;
                    }
                }
            }

            private class DeleteImage implements Func1<ImageProductInputDomainModel, Observable<ImageProductInputDomainModel>> {
                @Override
                public Observable<ImageProductInputDomainModel> call(ImageProductInputDomainModel domainModel) {
                    return uploadProductRepository.deleteProductPicture(domainModel.getPicId(), productId);
                }
            }
        }
    }

    private class PrepareEditProduct implements Func1<List<ImageProductInputDomainModel>, UploadProductInputDomainModel> {
        private final UploadProductInputDomainModel domainModel;

        public PrepareEditProduct(UploadProductInputDomainModel domainModel) {
            this.domainModel = domainModel;
        }

        @Override
        public UploadProductInputDomainModel call(List<ImageProductInputDomainModel> imageProductInputDomainModels) {
            ProductPhotoListDomainModel productPhotos = domainModel.getProductPhotos();
            productPhotos.setPhotos(imageProductInputDomainModels);
            domainModel.setProductPhotos(productPhotos);
            return domainModel;
        }
    }

    private class EditProduct implements Func1<UploadProductInputDomainModel, Observable<Boolean>> {
        @Override
        public Observable<Boolean> call(UploadProductInputDomainModel editProductInputServiceModel) {
            return uploadProductRepository.editProduct(editProductInputServiceModel);
        }
    }
}
