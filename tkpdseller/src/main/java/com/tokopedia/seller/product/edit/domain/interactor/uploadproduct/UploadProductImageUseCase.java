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
import com.tokopedia.seller.shop.common.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func3;
import rx.schedulers.Schedulers;

public class UploadProductImageUseCase extends UseCase<List<BasePictureViewModel>> {

    private static final String PRODUCT_VIEW_MODEL = "PRODUCT_VIEW_MODEL";
    private static final String PRODUCT_ID = "PRODUCT_ID";
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
    public Observable<List<BasePictureViewModel>> createObservable(RequestParams requestParams) {
        final ProductViewModel productViewModel = (ProductViewModel) requestParams.getObject(PRODUCT_VIEW_MODEL);
        final String productId = requestParams.getString(PRODUCT_ID, "");
        notificationCountListener = (ProductSubmitNotificationListener) requestParams.getObject(NOTIFICATION_COUNT_LISTENER);
        return Observable.zip(uploadProductImageList(productViewModel, productId).subscribeOn(Schedulers.io()),
                uploadProductVariantImageList(productViewModel, productId).subscribeOn(Schedulers.io()),
                uploadProductSizeCart(productViewModel, productId).subscribeOn(Schedulers.io()),
                new Func3<List<BasePictureViewModel>, List<BasePictureViewModel>, List<BasePictureViewModel>, List<BasePictureViewModel>>() {
                    @Override
                    public List<BasePictureViewModel> call(List<BasePictureViewModel> basePictureViewModelList, List<BasePictureViewModel> basePictureViewModels2, List<BasePictureViewModel> basePictureViewModels3) {
                        basePictureViewModelList.addAll(basePictureViewModels2);
                        basePictureViewModelList.addAll(basePictureViewModels3);
                        return basePictureViewModelList;
                    }
                });
    }

    private Observable<List<BasePictureViewModel>> uploadProductImageList(ProductViewModel productViewModel, String productId) {
        return Observable.from(productViewModel.getProductPictureViewModelList())
                .flatMap(new UploadSingleImage(productId))
                .toList();
    }

    private Observable<List<BasePictureViewModel>> uploadProductVariantImageList(ProductViewModel productViewModel, String productId) {
        return Observable.from(productUploadMapper.getVariantPictureViewModelList(productViewModel))
                .flatMap(new UploadSingleImage(productId))
                .toList();
    }

    private Observable<List<BasePictureViewModel>> uploadProductSizeCart(ProductViewModel productViewModel, String productId) {
        List<BasePictureViewModel> basePictureViewModelList = new ArrayList<>();
        if (productViewModel.getProductSizeChart() != null) {
            basePictureViewModelList.add(productViewModel.getProductSizeChart());
            return Observable.from(basePictureViewModelList)
                    .flatMap(new UploadSingleImage(productId))
                    .toList();
        } else {
            return Observable.just(basePictureViewModelList);
        }
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
            if (productPictureViewModel.getId()<= 0) {
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
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            return productPictureViewModel;
        }
    }

    public static RequestParams createParams(ProductViewModel productViewModel, String productId, ProductSubmitNotificationListener notificationCountListener) {
        RequestParams params = RequestParams.create();
        params.putObject(PRODUCT_VIEW_MODEL, productViewModel);
        params.putString(PRODUCT_ID, productId);
        params.putObject(NOTIFICATION_COUNT_LISTENER, notificationCountListener);
        return params;
    }
}
