package com.tokopedia.seller.product.edit.domain.interactor.uploadproduct;

import com.tokopedia.seller.base.domain.interactor.UploadImageUseCase;
import com.tokopedia.seller.base.domain.model.ImageUploadDomainModel;
import com.tokopedia.seller.product.common.constant.ProductNetworkConstant;
import com.tokopedia.seller.product.draft.data.mapper.ProductDraftMapper;
import com.tokopedia.seller.product.draft.domain.model.ProductDraftRepository;
import com.tokopedia.seller.product.edit.data.source.cloud.model.UploadImageModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductPictureResultUploadedViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductPictureViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;

import java.io.UnsupportedEncodingException;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 5/8/17.
 */

public class AddProductImage implements Func1<ProductViewModel, Observable<List<ProductPictureViewModel>>> {

    private final UploadImageUseCase<UploadImageModel> uploadImageUseCase;

    public AddProductImage(UploadImageUseCase<UploadImageModel> uploadImageUseCase) {
        this.uploadImageUseCase = uploadImageUseCase;
    }

    @Override
    public Observable<List<ProductPictureViewModel>> call(ProductViewModel productViewModel) {
        return Observable.from(productViewModel.getProductPicture())
                .flatMap(new UploadSingleImage(productViewModel.getProductId()))
                .toList();
    }

    private class UploadSingleImage implements Func1<ProductPictureViewModel, Observable<ProductPictureViewModel>> {

        private long productId;

        public UploadSingleImage(long productId) {
            this.productId = productId;
        }

        @Override
        public Observable<ProductPictureViewModel> call(ProductPictureViewModel productPictureViewModel) {
            if (productPictureViewModel.getId() <= 0) {
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
            if(uploadDomainModel.getDataResultImageUpload() != null){
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
}
