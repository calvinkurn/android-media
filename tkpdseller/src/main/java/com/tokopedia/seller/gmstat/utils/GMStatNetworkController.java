package com.tokopedia.seller.gmstat.utils;

import android.content.res.AssetManager;
import android.util.Log;

import com.google.gson.Gson;
import com.tokopedia.core.discovery.dynamicfilter.facade.HadesNetwork;
import com.tokopedia.core.discovery.dynamicfilter.facade.models.HadesV1Model;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetBuyerGraph;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetKeyword;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetPopularProduct;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetProductGraph;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetShopCategory;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetTransactionGraph;
import com.tokopedia.seller.goldmerchant.statistic.domain.GMStatRepository;
import com.tokopedia.seller.goldmerchant.statistic.domain.KeywordModel;
import com.tokopedia.seller.goldmerchant.statistic.domain.OldGMStatRepository;
import com.tokopedia.seller.goldmerchant.statistic.utils.GMStatisticUtil;
import com.tokopedia.seller.goldmerchant.statistic.view.model.GMTransactionGraphMergeModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
 * <p>https://phab.tokopedia.com/w/api/krabs/shopstatistic/</> for more detail
 */

public class GMStatNetworkController {

    public static final int MAXIMUM_CATEGORY = 3;
    private static final String TAG = "GMStatNetworkController";
    private Gson gson;
    private GMStatRepository repository;

    public GMStatNetworkController(Gson gson, GMStatRepository repository) {
        this.gson = gson;
        this.repository = repository;
    }

    private static GetKeyword from(GetKeyword getKeyword) {
        GetKeyword res =
                new GetKeyword();
        List<GetKeyword.SearchKeyword> datas =
                new ArrayList<>();
        for (GetKeyword.SearchKeyword searchKeyword : getKeyword.getSearchKeyword()) {
            GetKeyword.SearchKeyword searchKeyword1
                    = new GetKeyword.SearchKeyword();
            searchKeyword1.setFrequency(searchKeyword.getFrequency());
            searchKeyword1.setKeyword(searchKeyword.getKeyword());

            datas.add(searchKeyword1);
        }

        res.setSearchKeyword(datas);

        return res;


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

    protected void showTransactionEmptyView(OldGMStatRepository oldGMStatRepository, AssetManager assetManager) {
        GetTransactionGraph body2 = gson.fromJson(readJson("get_transaction_graph_empty_state.json", assetManager), GetTransactionGraph.class);
        oldGMStatRepository.onSuccessTransactionGraph(repository.getTransactionGraph(body2));
    }

    public Observable<KeywordModel> getKeywordModelObservable(final long shopId) {
        return getShopCategory2(shopId)
                .flatMap(new Func1<GetShopCategory, Observable<KeywordModel>>() {
                    @Override
                    public Observable<KeywordModel> call(GetShopCategory getShopCategory) {
                        KeywordModel keywordModel =
                                new KeywordModel();
                        keywordModel.setShopCategory(getShopCategory);

                        if (keywordModel.getShopCategory() == null || keywordModel.getShopCategory().getShopCategory() == null
                                || keywordModel.getShopCategory().getShopCategory().isEmpty()) {
                            keywordModel.setResponseList(new ArrayList<Response<GetKeyword>>());
                            keywordModel.setHadesv1Models(new ArrayList<HadesV1Model>());
                            return Observable.just(keywordModel);
                        }

                        List<Integer> shopCategory = GMStatisticUtil.subList(getShopCategory.getShopCategory(), MAXIMUM_CATEGORY);
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
                                keywordModel.setKeywords(new ArrayList<GetKeyword>());
                                keywordModel.setHadesv1Models(new ArrayList<HadesV1Model>());
                                List<Integer> indexToRemoved = new ArrayList<>();
                                List<HadesV1Model> hadesV1Models = new ArrayList<>();
                                if (responses != null && responses2 != null
                                        && responses.size() == responses2.size()) {
                                    int size = responses.size();
                                    for (int i = 0; i < size; i++) {
                                        GetKeyword h1 = responses.get(i);
                                        Response<HadesV1Model> h2 = responses2.get(i);
                                        if (h2.isSuccessful() && h2.errorBody() == null) {
                                            keywordModel.getKeywords().add(from(h1));
                                            keywordModel.getHadesv1Models().add(h2.body());
                                            hadesV1Models.add(h2.body());
                                            indexToRemoved.add(i);
                                        }
                                    }
                                } else {
                                    keywordModel.setKeywords(new ArrayList<GetKeyword>());
                                    for (GetKeyword response : responses) {
                                        keywordModel.getKeywords().add(from(response));
                                    }

                                    hadesV1Models = new ArrayList<>();
                                    for (Response<HadesV1Model> hadesV1ModelResponse : responses2) {
                                        hadesV1Models.add(hadesV1ModelResponse.body());
                                    }

                                    keywordModel.setHadesv1Models(hadesV1Models);
                                    return keywordModel;
                                }


                                return keywordModel;
                            }
                        });
                    }
                });
    }

    public void fetchData(long id, long sDate, long shopId, CompositeSubscription compositeSubscription, final OldGMStatRepository oldGMStatRepository) {
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
                                        Log.e(TAG, "fetchData : " + e.getMessage());

                                        oldGMStatRepository.onError(e);
                                    }

                                    @Override
                                    public void onNext(List<Object> responses) {
                                        Log.d(TAG, "fetchData : " + responses.size());

                                        GetProductGraph getProductGraph = (GetProductGraph) responses.get(0);
                                        oldGMStatRepository.onSuccessProductnGraph(getProductGraph);

                                        GMTransactionGraphMergeModel gmTransactionGraphMergeModel = (GMTransactionGraphMergeModel) responses.get(1);
                                        oldGMStatRepository.onSuccessTransactionGraph(gmTransactionGraphMergeModel);

                                        GetPopularProduct getPopularProduct = (GetPopularProduct) responses.get(2);
                                        oldGMStatRepository.onSuccessPopularProduct(getPopularProduct);

                                        GetBuyerGraph getBuyerGraph = (GetBuyerGraph) responses.get(3);
                                        oldGMStatRepository.onSuccessBuyerGraph(getBuyerGraph);

                                        KeywordModel keywordModel = (KeywordModel) responses.get(4);
                                        processKeywordModel(keywordModel, oldGMStatRepository);
                                    }
                                }
                        )
        );
    }

    protected void showProductGraphEmpty(OldGMStatRepository oldGMStatRepository, AssetManager assetManager) {
        GetProductGraph body = gson.fromJson(readJson("get_product_graph_empty_state.json", assetManager), GetProductGraph.class);
        oldGMStatRepository.onSuccessProductnGraph(body);
    }

    private void processKeywordModel(KeywordModel keywordModel, OldGMStatRepository oldGMStatRepository) {
        GetShopCategory getShopCategory = keywordModel.getShopCategory();
        oldGMStatRepository.onSuccessGetShopCategory(getShopCategory);

        if (getShopCategory == null || getShopCategory.getShopCategory() == null || getShopCategory.getShopCategory().isEmpty())
            return;

        List<GetKeyword> getKeywords = keywordModel.getKeywords();
        oldGMStatRepository.onSuccessGetKeyword(getKeywords);

        oldGMStatRepository.onSuccessGetCategory(keywordModel.getHadesv1Models());
    }

    public void fetchDataEmptyState(final OldGMStatRepository oldGMStatRepository, AssetManager assetManager) {

        showProductGraphEmpty(oldGMStatRepository, assetManager);

        showTransactionEmptyView(oldGMStatRepository, assetManager);

        GetPopularProduct body3 = gson.fromJson(readJson("popular_product_empty_state.json", assetManager), GetPopularProduct.class);
        oldGMStatRepository.onSuccessPopularProduct(body3);

        GetBuyerGraph body4 = gson.fromJson(readJson("buyer_graph_empty_state.json", assetManager), GetBuyerGraph.class);
        oldGMStatRepository.onSuccessBuyerGraph(body4);

        KeywordModel keywordModel = new KeywordModel();
        keywordModel.setShopCategory(gson.fromJson(readJson("shop_category_empty.json", assetManager), GetShopCategory.class));

        if (keywordModel.getShopCategory() == null || keywordModel.getShopCategory().getShopCategory() == null || keywordModel.getShopCategory().getShopCategory().isEmpty()) {
            oldGMStatRepository.onSuccessGetShopCategory(keywordModel.getShopCategory());
            return;
        }


        List<GetKeyword> getKeywords = new ArrayList<>();
        getKeywords.add(gson.fromJson(readJson("search_keyword.json", assetManager), GetKeyword.class));
        keywordModel.setKeywords(getKeywords);
        oldGMStatRepository.onSuccessGetKeyword(getKeywords);
    }

    protected String readJson(String fileName, AssetManager assetManager) {
        BufferedReader reader = null;
        String total = "";
        try {
            reader = new BufferedReader(
                    new InputStreamReader(assetManager.open(fileName), "UTF-8"));

            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                //process line
                total += mLine;
            }
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
            Log.i("MNORMANSYAH", total);
        }
        return total;
    }
}
