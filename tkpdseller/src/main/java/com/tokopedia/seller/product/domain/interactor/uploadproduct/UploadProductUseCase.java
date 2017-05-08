package com.tokopedia.seller.product.domain.interactor.uploadproduct;

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
import java.util.concurrent.TimeUnit;

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
                .flatMap(new UploadProduct(productId, listener, generateHostRepository, uploadProductRepository, imageProductUploadRepository));
    }

    private class GetProductModelObservable implements Func1<Long, Observable<UploadProductInputDomainModel>> {

        @Override
        public Observable<UploadProductInputDomainModel> call(Long productId) {
            return productDraftRepository.getDraft(productId);
        }

    }


}
