package com.tokopedia.seller.gmstat.utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.tokopedia.core.discovery.dynamicfilter.facade.HadesNetwork;
import com.tokopedia.core.discovery.dynamicfilter.facade.models.HadesV1Model;
import com.tokopedia.seller.gmstat.apis.GMStatApi;
import com.tokopedia.seller.gmstat.models.GetShopCategory;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetBuyerGraph;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetKeyword;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetPopularProduct;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetProductGraph;
import com.tokopedia.seller.goldmerchant.statistic.domain.GMStatRepository;
import com.tokopedia.seller.goldmerchant.statistic.view.model.GMTransactionGraphMergeModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func3;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author normansyahputa on 7/19/17.
 */

public class GMStatNetworkController2 extends GMStatNetworkController {

    private static final String TAG = "GMStatNetworkController";

    private GMStatRepository repository;

    public GMStatNetworkController2(Context context, Gson gson, GMStatApi gmStatApi) {
        super(context, gson, gmStatApi);
    }

    public void setRepository(GMStatRepository repository) {
        this.repository = repository;
    }

    public Observable<GetProductGraph> getProductGraph2(long shopId, long sDate, long eDate) {
        return repository.getProductGraph(sDate, eDate);
    }

    public Observable<GetProductGraph> getProductGraph2(long shopId) {
        return getProductGraph2(shopId, -1, -1);
    }

    public Observable<GMTransactionGraphMergeModel> getTransactionGraph2(
            long shopId, long sDate, long eDate
    ) {
        return repository.getTransactionGraph(sDate, eDate);
    }

    public Observable<GMTransactionGraphMergeModel> getTransactionGraph2(
            long shopId
    ) {
        return getTransactionGraph2(shopId, -1, -1);
    }

    public Observable<GetPopularProduct> getPopularProduct2(long shopId) {

        Calendar dayOne = Calendar.getInstance();
        dayOne.add(Calendar.DATE, -30);

        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);

        return repository.getPopularProduct(dayOne.getTimeInMillis(), yesterday.getTimeInMillis());
    }

    public Observable<GetBuyerGraph> getBuyerData2(long shopId, long sDate, long eDate) {
        return repository.getBuyerGraph(sDate, eDate);
    }

    public Observable<GetBuyerGraph> getBuyerData2(long shopId) {
        return getBuyerData2(shopId, -1, -1);
    }

    public Observable<GetShopCategory> getShopCategory2(long shopId) {
        return repository.getShopCategory(-1, -1);
    }

    public Observable<GetKeyword> getKeyword2(long shopId, long catId) {
        return repository.getKeywordModel(Long.toString(catId));
    }

    public Observable<KeywordModel> getKeywordModelObservable(final long shopId) {
        return getShopCategory2(shopId)
                .flatMap(new Func1<GetShopCategory, Observable<KeywordModel>>() {
                    @Override
                    public Observable<KeywordModel> call(GetShopCategory getShopCategory) {
                        KeywordModel keywordModel =
                                new KeywordModel2();
                        keywordModel.getShopCategory = getShopCategory;

                        if (keywordModel.getShopCategory == null || keywordModel.getShopCategory.getShopCategory() == null
                                || keywordModel.getShopCategory.getShopCategory().isEmpty()) {
                            keywordModel.getResponseList = new ArrayList<>();
                            keywordModel.hadesv1Models = new ArrayList<>();
                            return Observable.just(keywordModel);
                        }

                        List<Integer> shopCategory = subList(getShopCategory.getShopCategory(), MAXIMUM_CATEGORY);
                        Observable<List<Response<HadesV1Model>>> getCategories = Observable.from(shopCategory).flatMap(
                                new Func1<Integer, Observable<Response<HadesV1Model>>>() {
                                    @Override
                                    public Observable<Response<HadesV1Model>> call(Integer integer) {
                                        Observable<Response<HadesV1Model>> hades
                                                = HadesNetwork.fetchDepartment(integer, -1, HadesNetwork.TREE);
                                        return Observable.just(hades.toBlocking().first());
                                    }
                                }
                        ).toList();

                        Observable<List<GetKeyword>> getKeywords
                                = Observable.from(shopCategory)
                                .flatMap(new Func1<Integer, Observable<GetKeyword>>() {
                                    @Override
                                    public Observable<GetKeyword> call(Integer catId) {
                                        return getKeyword2(shopId, catId);
                                    }
                                })
                                .toList();


                        return Observable.zip(getKeywords, getCategories, Observable.just(keywordModel), new Func3<List<GetKeyword>, List<Response<HadesV1Model>>, KeywordModel, KeywordModel>() {
                            @Override
                            public KeywordModel call(List<GetKeyword> responses, List<Response<HadesV1Model>> responses2, KeywordModel keywordModel) {
                                keywordModel.getKeywords = new ArrayList<>();
                                keywordModel.hadesv1Models = new ArrayList<>();
                                List<Integer> indexToRemoved = new ArrayList<>();
                                List<HadesV1Model> hadesV1Models = new ArrayList<>();
                                if (responses != null && responses2 != null
                                        && responses.size() == responses2.size()) {
                                    int size = responses.size();
                                    for (int i = 0; i < size; i++) {
                                        GetKeyword h1 = responses.get(i);
                                        Response<HadesV1Model> h2 = responses2.get(i);
                                        if (h2.isSuccessful() && h2.errorBody() == null) {
                                            if (keywordModel instanceof KeywordModel2) {
                                                ((KeywordModel2) keywordModel).getKeywords2.add(h1);
                                                keywordModel.hadesv1Models.add(h2.body());
                                                hadesV1Models.add(h2.body());
                                                indexToRemoved.add(i);
                                            }
                                        }
                                    }
                                } else {
                                    if (keywordModel instanceof KeywordModel2) {
                                        ((KeywordModel2) keywordModel).getKeywords2 = new ArrayList<>();
                                        for (GetKeyword response : responses) {
                                            ((KeywordModel2) keywordModel).getKeywords2.add(response);
                                        }
                                    }

                                    hadesV1Models = new ArrayList<>();
                                    for (Response<HadesV1Model> hadesV1ModelResponse : responses2) {
                                        hadesV1Models.add(hadesV1ModelResponse.body());
                                    }

                                    keywordModel.hadesv1Models = hadesV1Models;
                                    return keywordModel;
                                }

                                return keywordModel;
                            }
                        });
                    }
                });
    }

    public void testApi(long shopId, CompositeSubscription compositeSubscription) {
        compositeSubscription.add(
                Observable.concat(
                        getProductGraph2(shopId),
                        getTransactionGraph2(shopId),
                        getPopularProduct2(shopId),
                        getBuyerData2(shopId),
                        getKeywordModelObservable(shopId),
                        repository.getBuyerTable(-1, -1),
                        repository.getProductTable(-1, -1)
                ).toList()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(
                                new Subscriber<List<Object>>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Log.e(TAG, "testApi : " + e.getMessage());
                                    }

                                    @Override
                                    public void onNext(List<Object> objects) {
                                        Log.d(TAG, "testApi : " + objects.size());
                                    }
                                }
                        )
        );
    }

    public static class KeywordModel2 extends KeywordModel {
        public List<GetKeyword> getKeywords2;
    }
}
