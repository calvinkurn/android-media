package com.tokopedia.seller.product.edit.domain.interactor.uploadproduct;

import android.text.TextUtils;

import com.tokopedia.seller.base.domain.interactor.UploadImageUseCase;
import com.tokopedia.seller.base.domain.model.ImageUploadDomainModel;
import com.tokopedia.seller.product.common.constant.ProductNetworkConstant;
import com.tokopedia.seller.product.draft.data.mapper.ProductDraftMapper;
import com.tokopedia.seller.product.edit.data.source.cloud.model.UploadImageModel;
import com.tokopedia.seller.product.edit.domain.listener.NotificationCountListener;
import com.tokopedia.seller.product.edit.view.model.edit.ProductPictureResultUploadedViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductPictureViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class UploadProductImageUseCase extends UseCase<List<ProductPictureViewModel>> {

    private static final String PRODUCT_VIEW_MODEL = "PRODUCT_VIEW_MODEL";
    private static final String NOTIFICATION_COUNT_LISTENER = "NOTIFICATION_COUNT_LISTENER";

    private final UploadImageUseCase<UploadImageModel> uploadImageUseCase;
    private NotificationCountListener notificationCountListener;

    @Inject
    public UploadProductImageUseCase(UploadImageUseCase<UploadImageModel> uploadImageUseCase) {
        this.uploadImageUseCase = uploadImageUseCase;
    }

    @Override
    public Observable<List<ProductPictureViewModel>> createObservable(RequestParams requestParams) {
        ProductViewModel productViewModel = (ProductViewModel) requestParams.getObject(PRODUCT_VIEW_MODEL);
        notificationCountListener = (NotificationCountListener) requestParams.getObject(NOTIFICATION_COUNT_LISTENER);
        return Observable.from(productViewModel.getProductPictureViewModelList())
                .flatMap(new UploadSingleImage(productViewModel.getProductId()))
                .toList();
    }

    private class UploadSingleImage implements Func1<ProductPictureViewModel, Observable<ProductPictureViewModel>> {

        private String productId;

        public UploadSingleImage(String productId) {
            this.productId = productId;
        }

        @Override
        public Observable<ProductPictureViewModel> call(ProductPictureViewModel productPictureViewModel) {
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

    private class MapImageModelToProductInput implements Func1<ImageUploadDomainModel<UploadImageModel>, ProductPictureViewModel> {
        private ProductPictureViewModel productPictureViewModel;

        public MapImageModelToProductInput(ProductPictureViewModel productPictureViewModel) {

            this.productPictureViewModel = productPictureViewModel;
        }

        @Override
        public ProductPictureViewModel call(ImageUploadDomainModel<UploadImageModel> uploadDomainModel) {
            if (uploadDomainModel.getDataResultImageUpload() != null) {
                try {
                    ProductPictureResultUploadedViewModel resultUploadedViewModel = ProductDraftMapper.generatePicObj(uploadDomainModel.getDataResultImageUpload().getResult().getPicObj());
                    productPictureViewModel.setFilePath(resultUploadedViewModel.getFilePath());
                    productPictureViewModel.setFileName(resultUploadedViewModel.getFileName());
                    productPictureViewModel.setX(Long.parseLong(resultUploadedViewModel.getW()));
                    productPictureViewModel.setY(Long.parseLong(resultUploadedViewModel.getH()));
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

    public static RequestParams createParams(ProductViewModel productViewModel, NotificationCountListener notificationCountListener) {
        RequestParams params = RequestParams.create();
        params.putObject(PRODUCT_VIEW_MODEL, productViewModel);
        params.putObject(NOTIFICATION_COUNT_LISTENER, notificationCountListener);
        return params;
    }
}
