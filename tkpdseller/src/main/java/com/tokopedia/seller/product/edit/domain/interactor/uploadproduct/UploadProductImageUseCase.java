package com.tokopedia.seller.product.edit.domain.interactor.uploadproduct;

import android.text.TextUtils;

import com.tokopedia.seller.base.domain.interactor.UploadImageUseCase;
import com.tokopedia.seller.base.domain.model.ImageUploadDomainModel;
import com.tokopedia.seller.product.common.constant.ProductNetworkConstant;
import com.tokopedia.seller.product.draft.data.mapper.ProductDraftMapper;
import com.tokopedia.seller.product.edit.data.source.cloud.model.UploadImageModel;
import com.tokopedia.seller.product.edit.domain.listener.ProductSubmitNotificationListener;
import com.tokopedia.seller.product.edit.domain.mapper.ProductUploadMapper;
import com.tokopedia.seller.product.edit.view.model.edit.BasePictureViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductPictureResultUploadedViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class UploadProductImageUseCase extends UseCase<ProductViewModel> {

    private static final String PRODUCT_VIEW_MODEL = "PRODUCT_VIEW_MODEL";
    private static final String NOTIFICATION_COUNT_LISTENER = "NOTIFICATION_COUNT_LISTENER";

    private final UploadImageUseCase<UploadImageModel> uploadImageUseCase;
    private final ProductUploadMapper productUploadMapper;
    private ProductSubmitNotificationListener notificationCountListener;

    @Inject
    public UploadProductImageUseCase(UploadImageUseCase<UploadImageModel> uploadImageUseCase, ProductUploadMapper productUploadMapper) {
        this.uploadImageUseCase = uploadImageUseCase;
        this.productUploadMapper = productUploadMapper;
    }

    @Override
    public Observable<ProductViewModel> createObservable(RequestParams requestParams) {
        final ProductViewModel productViewModel = (ProductViewModel) requestParams.getObject(PRODUCT_VIEW_MODEL);
        notificationCountListener = (ProductSubmitNotificationListener) requestParams.getObject(NOTIFICATION_COUNT_LISTENER);
        return uploadProductImageList(productViewModel).flatMap(new Func1<List<BasePictureViewModel>, Observable<ProductViewModel>>() {
            @Override
            public Observable<ProductViewModel> call(List<BasePictureViewModel> basePictureViewModels) {
                return uploadProductVariantImageList(productViewModel).map(new Func1<List<BasePictureViewModel>, ProductViewModel>() {
                    @Override
                    public ProductViewModel call(List<BasePictureViewModel> basePictureViewModels) {
                        return productViewModel;
                    }
                });
            }
        });
    }

    private Observable<List<BasePictureViewModel>> uploadProductImageList(ProductViewModel productViewModel) {
        return Observable.from(productViewModel.getProductPictureViewModelList())
                .flatMap(new UploadSingleImage(productViewModel.getProductId()))
                .toList();
    }

    private Observable<List<BasePictureViewModel>> uploadProductVariantImageList(ProductViewModel productViewModel) {
        return Observable.from(productUploadMapper.getVariantPictureViewModelList(productViewModel))
                .flatMap(new UploadSingleImage(productViewModel.getProductId()))
                .toList();
    }

    private class UploadSingleImage implements Func1<BasePictureViewModel, Observable<BasePictureViewModel>> {

        private String productId;

        public UploadSingleImage(String productId) {
            this.productId = productId;
        }

        @Override
        public Observable<BasePictureViewModel> call(BasePictureViewModel productPictureViewModel) {
            if (notificationCountListener != null) {
                notificationCountListener.addProgress();
            }
            if (TextUtils.isEmpty(productPictureViewModel.getId())) {
                return uploadImageUseCase.createObservable(uploadImageUseCase.createRequestParams(
                        ProductNetworkConstant.UPLOAD_PRODUCT_IMAGE_PATH, productPictureViewModel.getFilePath(),
                        ProductNetworkConstant.LOGO_FILENAME_IMAGE_JPG, String.valueOf(productId)))
                        .map(new MapImageModelToProductInput(productPictureViewModel));
            } else {
                return Observable.just(productPictureViewModel);
            }
        }
    }

    private class MapImageModelToProductInput implements Func1<ImageUploadDomainModel<UploadImageModel>, BasePictureViewModel> {
        private BasePictureViewModel productPictureViewModel;

        public MapImageModelToProductInput(BasePictureViewModel productPictureViewModel) {
            this.productPictureViewModel = productPictureViewModel;
        }

        @Override
        public BasePictureViewModel call(ImageUploadDomainModel<UploadImageModel> uploadDomainModel) {
            if (uploadDomainModel.getDataResultImageUpload() != null) {
                try {
                    ProductPictureResultUploadedViewModel resultUploadedViewModel = ProductDraftMapper.generatePicObj(uploadDomainModel.getDataResultImageUpload().getResult().getPicObj());
                    productPictureViewModel.setFilePath(resultUploadedViewModel.getFilePath());
                    productPictureViewModel.setFileName(resultUploadedViewModel.getFileName());
                    productPictureViewModel.setX(Long.parseLong(resultUploadedViewModel.getW()));
                    productPictureViewModel.setY(Long.parseLong(resultUploadedViewModel.getH()));
                    productPictureViewModel.setId(resultUploadedViewModel.getPId());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            return productPictureViewModel;
        }
    }

    public static RequestParams createParams(ProductViewModel productViewModel) {
        return createParams(productViewModel, null);
    }

    public static RequestParams createParams(ProductViewModel productViewModel, ProductSubmitNotificationListener notificationCountListener) {
        RequestParams params = RequestParams.create();
        params.putObject(PRODUCT_VIEW_MODEL, productViewModel);
        params.putObject(NOTIFICATION_COUNT_LISTENER, notificationCountListener);
        return params;
    }
}
