package com.tokopedia.seller.product.picker.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.common.cashback.DataCashbackModel;
import com.tokopedia.seller.product.manage.constant.CatalogProductOption;
import com.tokopedia.seller.product.manage.constant.ConditionProductOption;
import com.tokopedia.seller.product.manage.constant.EtalaseProductOption;
import com.tokopedia.seller.product.manage.constant.PictureStatusProductOption;
import com.tokopedia.seller.product.manage.constant.SortProductOption;
import com.tokopedia.seller.product.picker.common.ProductListPickerConstant;
import com.tokopedia.seller.product.picker.data.model.ProductListSellerModel;
import com.tokopedia.seller.product.picker.domain.GetProductListSellingRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 9/4/17.
 */

public class GetProductListSellingUseCase extends UseCase<ProductListSellerModel> {
    private final GetProductListSellingRepository getProductListSellingRepository;
    private final SellerModuleRouter sellerModuleRouter;

    @Inject
    public GetProductListSellingUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                        GetProductListSellingRepository getProductListSellingRepository,
                                        SellerModuleRouter sellerModuleRouter) {
        super(threadExecutor, postExecutionThread);
        this.getProductListSellingRepository = getProductListSellingRepository;
        this.sellerModuleRouter = sellerModuleRouter;
    }

    @Override
    public Observable<ProductListSellerModel> createObservable(RequestParams requestParams) {
        return getProductListSellingRepository.getProductListSeller(requestParams.getParamsAllValueInString())
                .flatMap(new Func1<ProductListSellerModel, Observable<ProductListSellerModel>>() {
                    @Override
                    public Observable<ProductListSellerModel> call(final ProductListSellerModel productListSellerModel) {
                        List<String> productIds = new ArrayList<String>();
                        for (ProductListSellerModel.Product data : productListSellerModel.getData().getList()) {
                            productIds.add(data.getProductId());
                        }
                        return sellerModuleRouter.getCashbackList(productIds)
                                .flatMap(new Func1<List<DataCashbackModel>, Observable<ProductListSellerModel>>() {
                                    @Override
                                    public Observable<ProductListSellerModel> call(List<DataCashbackModel> dataCashbackModels) {
                                        for(DataCashbackModel dataCashbackModel : dataCashbackModels){
                                            String productId = String.valueOf(dataCashbackModel.getProductId());
                                            for(ProductListSellerModel.Product product : productListSellerModel.getData().getList()){
                                                if(productId.equals(product.getProductId())){
                                                    product.setProductCashback(dataCashbackModel.getCashback());
                                                    product.setProductCashbackAmount(dataCashbackModel.getCashbackAmount());
                                                }
                                            }
                                        }
                                        return Observable.just(productListSellerModel);
                                    }
                                });
                    }
                });
    }

    public static RequestParams createRequestParams(int page, String keywordFilter) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(ProductListPickerConstant.QUERY_PER_PAGE, ProductListPickerConstant.REQUEST_PER_PAGE);
        requestParams.putString(ProductListPickerConstant.QUERY_PAGE, String.valueOf(page));
        requestParams.putString(ProductListPickerConstant.QUERY_SORT, ProductListPickerConstant.DEFAULT_SORT);
        requestParams.putString(ProductListPickerConstant.QUERY_KEYWORD, keywordFilter);
        return requestParams;
    }

    public static RequestParams createRequestParamsManageProduct(int page,
                                                                 String keywordFilter,
                                                                 @CatalogProductOption String catalogId,
                                                                 @ConditionProductOption String condition,
                                                                 String departmentId,
                                                                 @EtalaseProductOption String etalaseId,
                                                                 @PictureStatusProductOption String pictureStatus,
                                                                 @SortProductOption String sort) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(ProductListPickerConstant.QUERY_PER_PAGE, ProductListPickerConstant.REQUEST_PER_PAGE_MANAGE_PRODUCT);
        requestParams.putString(ProductListPickerConstant.QUERY_PAGE, String.valueOf(page));
        requestParams.putString(ProductListPickerConstant.QUERY_SORT, String.valueOf(sort));
        requestParams.putString(ProductListPickerConstant.QUERY_KEYWORD, keywordFilter);
        if (!catalogId.equals(CatalogProductOption.NOT_USED)) {
            requestParams.putString(ProductListPickerConstant.QUERY_CATALOG, catalogId);
        }
        if (!condition.equals(ConditionProductOption.NOT_USED)) {
            requestParams.putString(ProductListPickerConstant.QUERY_CONDITION, condition);
        }
        if (departmentId != null && !departmentId.isEmpty()) {
            requestParams.putString(ProductListPickerConstant.QUERY_DEPARTMENT_ID, departmentId);
        }
        requestParams.putString(ProductListPickerConstant.QUERY_ETALASE_ID, etalaseId);
        if (!pictureStatus.equals(PictureStatusProductOption.NOT_USED)) {
            requestParams.putString(ProductListPickerConstant.QUERY_PICTURE_STATUS, pictureStatus);
        }
        return requestParams;
    }
}
