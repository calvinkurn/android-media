package com.tokopedia.seller.product.picker.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.common.cashback.DataCashbackModel;
import com.tokopedia.product.manage.list.constant.CatalogProductOption;
import com.tokopedia.product.manage.list.constant.ConditionProductOption;
import com.tokopedia.product.manage.list.constant.PictureStatusProductOption;
import com.tokopedia.product.manage.list.constant.ProductManageConstant;
import com.tokopedia.product.manage.list.constant.SortProductOption;
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
    public static final String VALUE_REQUEST_PRODUCT_VARIANT = "1";
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
        requestParams.putString(ProductListPickerConstant.REQUEST_PARAM_PRODUCT_VARIANT, VALUE_REQUEST_PRODUCT_VARIANT);
        return requestParams;
    }

    public static RequestParams createRequestParamsManageProduct(int page,
                                                                 String keywordFilter,
                                                                 @CatalogProductOption String catalogId,
                                                                 @ConditionProductOption String condition,
                                                                 String departmentId,
                                                                 int etalaseId,
                                                                 @PictureStatusProductOption String pictureStatus,
                                                                 @SortProductOption String sort) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(ProductListPickerConstant.QUERY_PER_PAGE, ProductListPickerConstant.REQUEST_PER_PAGE_MANAGE_PRODUCT);
        requestParams.putString(ProductListPickerConstant.QUERY_PAGE, String.valueOf(page));
        requestParams.putString(ProductListPickerConstant.QUERY_SORT, String.valueOf(sort));
        requestParams.putString(ProductListPickerConstant.QUERY_KEYWORD, keywordFilter);
        if (!catalogId.equals(CatalogProductOption.WITH_AND_WITHOUT)) {
            requestParams.putString(ProductListPickerConstant.QUERY_CATALOG, catalogId);
        }
        if (!condition.equals(ConditionProductOption.ALL_CONDITION)) {
            requestParams.putString(ProductListPickerConstant.QUERY_CONDITION, condition);
        }
        if (departmentId != null && !departmentId.isEmpty() && !departmentId.equals(String.valueOf(ProductManageConstant.FILTER_ALL_CATEGORY))) {
            requestParams.putString(ProductListPickerConstant.QUERY_DEPARTMENT_ID, departmentId);
        }
        requestParams.putString(ProductListPickerConstant.QUERY_ETALASE_ID, generateEtalaseIdFilter(etalaseId) );
        if (!pictureStatus.equals(PictureStatusProductOption.WITH_AND_WITHOUT)) {
            requestParams.putString(ProductListPickerConstant.QUERY_PICTURE_STATUS, pictureStatus);
        }
        requestParams.putString(ProductListPickerConstant.REQUEST_PARAM_PRODUCT_VARIANT, VALUE_REQUEST_PRODUCT_VARIANT);
        return requestParams;
    }


    private static String generateEtalaseIdFilter(int etalaseId) {
        switch (etalaseId){
            case ProductManageConstant.FILTER_ALL_PRODUK:
                return ProductManageConstant.FILTER_ALL_PRODUK_VALUE;
            case ProductManageConstant.FILTER_SOLD_PRODUK:
                return ProductManageConstant.FILTER_SOLD_PRODUK_VALUE;
            case ProductManageConstant.FILTER_EMPTY_STOK:
                return ProductManageConstant.FILTER_EMPTY_STOK_VALUE;
            case ProductManageConstant.FILTER_PENDING:
                return ProductManageConstant.FILTER_PENDING_VALUE;
            case ProductManageConstant.FILTER_FREE_RETURNS:
                return ProductManageConstant.FILTER_FREE_RETURNS_VALUE;
            case ProductManageConstant.FILTER_PREORDER:
                return ProductManageConstant.FILTER_PREORDER_VALUE;
            case ProductManageConstant.FILTER_ALL_SHOWCASE:
                return ProductManageConstant.FILTER_ALL_SHOWCASE_VALUE;
            default:
                return String.valueOf(etalaseId);
        }
    }
}
