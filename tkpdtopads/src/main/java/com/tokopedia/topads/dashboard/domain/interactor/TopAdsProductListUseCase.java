package com.tokopedia.topads.dashboard.domain.interactor;

import android.text.TextUtils;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.topads.dashboard.domain.TopAdsSearchProductRepository;
import com.tokopedia.topads.dashboard.domain.model.ProductListDomain;

import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author normansyahputa on 2/20/17.
 */
public class TopAdsProductListUseCase extends UseCase<ProductListDomain> {
    public static final String PRODUCT_ID = "prd_id";
    public static final String QUERY = "query";
    public static final String PAGE = "pg";
    public static final String ETALASE_ID = "eta_id";
    public static final String IS_PROMOTED = "is_prom";

    public static final int PAGE_ROW = 12;

    private TopAdsSearchProductRepository topAdsSearchProductRepository;

    @Inject
    public TopAdsProductListUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            TopAdsSearchProductRepository topAdsSearchProductRepository) {
        super(threadExecutor, postExecutionThread);
        this.topAdsSearchProductRepository = topAdsSearchProductRepository;
    }

    @Override
    public Observable<ProductListDomain> createObservable(RequestParams requestParams) {
        Map<String, String> params = new TKPDMapParam<>();
        String query = requestParams.getString(QUERY, "");
        if (!TextUtils.isEmpty(query)){
            params.put(TopAdsNetworkConstant.PARAM_KEYWORD, query);
        }
        params.put(TopAdsNetworkConstant.PARAM_ROWS, Integer.toString(PAGE_ROW));
        params.put(TopAdsNetworkConstant.PARAM_START, Integer.toString(PAGE_ROW * requestParams.getInt(PAGE,0)));
        String etalaseId = requestParams.getString(ETALASE_ID,"");
        if (!TextUtils.isEmpty(etalaseId)) {
            params.put(TopAdsNetworkConstant.PARAM_ETALASE, etalaseId);
        }
        int filterStatus = requestParams.getInt(IS_PROMOTED,-1);
        if (filterStatus >= 0) {
            params.put(TopAdsNetworkConstant.PARAM_IS_PROMOTED, String.valueOf(filterStatus));
        }
        String productId = requestParams.getString(PRODUCT_ID, "");
        if (!TextUtils.isEmpty(productId)) {
            params.put(TopAdsNetworkConstant.PARAM_ITEM_ID, productId);
        }
        return topAdsSearchProductRepository.searchProduct(params);
    }

    public Observable<ProductListDomain> createObservable(Map<String, String> param) {
        return topAdsSearchProductRepository.searchProduct(param);
    }

    public void execute(Map<String, String> param, Subscriber<ProductListDomain> subscriber) {
        this.subscription = createObservable(param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber);
    }

    public static RequestParams createRequestParamsByProductId(String productId) {
        RequestParams params = RequestParams.create();
        params.putString(PRODUCT_ID, productId);
        return params;
    }

    public static RequestParams createRequestParams(String query,
                                                    int page,
                                                    String filteredEtalaseId,
                                                    int filteredIsPromoted) {
        RequestParams params = RequestParams.create();
        params.putString(QUERY, query);
        params.putInt(PAGE, page);
        params.putString(ETALASE_ID, filteredEtalaseId);
        params.putInt(IS_PROMOTED, filteredIsPromoted);
        return params;
    }
}