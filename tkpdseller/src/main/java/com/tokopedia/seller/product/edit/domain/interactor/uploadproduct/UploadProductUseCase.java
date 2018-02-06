package com.tokopedia.seller.product.edit.domain.interactor.uploadproduct;

import android.text.TextUtils;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.myproduct.utils.FileUtils;
import com.tokopedia.seller.base.domain.interactor.UploadImageUseCase;
import com.tokopedia.seller.product.edit.data.source.cloud.model.UploadImageModel;
import com.tokopedia.seller.product.edit.domain.GenerateHostRepository;
import com.tokopedia.seller.product.edit.domain.ImageProductUploadRepository;
import com.tokopedia.seller.product.draft.domain.model.ProductDraftRepository;
import com.tokopedia.seller.product.edit.domain.UploadProductRepository;
import com.tokopedia.seller.product.edit.domain.listener.AddProductNotificationListener;
import com.tokopedia.seller.product.edit.domain.model.AddProductDomainModel;
import com.tokopedia.seller.product.edit.domain.model.ImageProductInputDomainModel;
import com.tokopedia.seller.product.edit.domain.model.ProductPhotoListDomainModel;
import com.tokopedia.seller.product.edit.domain.model.UploadProductInputDomainModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductPictureViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;

/**
 * @author sebastianuskh on 4/10/17.
 */

public class UploadProductUseCase extends UseCase<AddProductDomainModel> {

    public static final String UPLOAD_PRODUCT_ID = "UPLOAD_PRODUCT_ID";
    public static final int UNSELECTED_PRODUCT_ID = -1;

    private final ProductDraftRepository productDraftRepository;
    private final UploadProductRepository uploadProductRepository;

    private AddProductNotificationListener listener;
    private ProductViewModel productViewModel;
    private UploadImageUseCase<UploadImageModel> uploadImageUseCase;

    @Inject
    public UploadProductUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            ProductDraftRepository productDraftRepository,
            UploadProductRepository uploadProductRepository,
            UploadImageUseCase<UploadImageModel> uploadImageUseCase) {
        super(threadExecutor, postExecutionThread);
        this.productDraftRepository = productDraftRepository;
        this.uploadProductRepository = uploadProductRepository;
        this.uploadImageUseCase = uploadImageUseCase;
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
        productViewModel = productDraftRepository.getDraft(productId).toBlocking().first();
        if (productViewModel == null) {
            throw new RuntimeException("Draft is already deleted");
        }

        return Observable.just(productViewModel)
                .flatMap(new UploadProduct(productId, listener, uploadProductRepository,
                        uploadImageUseCase))
                .doOnNext(new DeleteProductDraft(productId, productDraftRepository))
                .doOnNext(new DeleteImageCacheDraftFile());
    }

    private class DeleteImageCacheDraftFile implements Action1<AddProductDomainModel> {
        @Override
        public void call(AddProductDomainModel addProductDomainModel) {
            List<ProductPictureViewModel> productPictureViewModels = productViewModel.getProductPicture();
            if (productPictureViewModels == null || productPictureViewModels.size() == 0) {
                return;
            }
            ArrayList<String> pathToDelete = new ArrayList<>();
            for (int i = 0, sizei = productPictureViewModels.size(); i<sizei; i++) {
                ProductPictureViewModel productPictureViewModel = productPictureViewModels.get(i);
                if (productPictureViewModel == null) {
                    continue;
                }
                String imagePath = productPictureViewModel.getFilePath();
                if (!TextUtils.isEmpty(imagePath)) {
                    pathToDelete.add(imagePath);
                }
            }
            if (pathToDelete.size() > 0) {
                FileUtils.deleteAllCacheTkpdFiles(pathToDelete);
            }
        }
    }

    private static class DeleteProductDraft implements Action1<AddProductDomainModel> {
        private final long productId;
        private final ProductDraftRepository productDraftRepository;

        public DeleteProductDraft(long productId, ProductDraftRepository productDraftRepository) {
            this.productId = productId;
            this.productDraftRepository = productDraftRepository;
        }

        @Override
        public void call(AddProductDomainModel addProductDomainModel) {
            productDraftRepository.deleteDraft(productId);
        }
    }

}
