package com.tokopedia.seller.gmstat.utils;

import android.content.res.AssetManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.tokopedia.core.database.model.CategoryDB;
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
import com.tokopedia.seller.product.domain.interactor.categorypicker.GetProductCategoryNameUseCase;

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

    public static final int MAXIMUM_CATEGORY = 1;
    public static final int INVALID_PARAM = -1;
    private static final String TAG = "GMStatNetworkController";
    private Gson gson;
    private GMStatRepository repository;
    private GetProductCategoryNameUseCase getProductCategoryNameUseCase;

    public GMStatNetworkController(Gson gson, GMStatRepository repository, GetProductCategoryNameUseCase getProductCategoryNameUseCase) {
        this.gson = gson;
        this.repository = repository;
        this.getProductCategoryNameUseCase = getProductCategoryNameUseCase;
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

    public Observable<GetProductGraph> getProductGraph(long sDate, long eDate) {
        return repository.getProductGraph(sDate, eDate);
    }

    public Observable<GMTransactionGraphMergeModel> getTransactionGraph(
            long sDate, long eDate
    ) {
        return repository.getTransactionGraph(sDate, eDate);
    }

    public Observable<GetPopularProduct> getPopularProduct() {

        Calendar dayOne = Calendar.getInstance();
        dayOne.add(Calendar.DATE, -30);

        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);

        return repository.getPopularProduct(dayOne.getTimeInMillis(), yesterday.getTimeInMillis());
    }

    public Observable<GetBuyerGraph> getBuyerData(long sDate, long eDate) {
        return repository.getBuyerGraph(sDate, eDate);
    }

    public Observable<GetShopCategory> getShopCategory() {
        return repository.getShopCategory(INVALID_PARAM, INVALID_PARAM);
    }

    public Observable<GetKeyword> getKeyword(long catId) {
        return repository.getKeywordModel(Long.toString(catId));
    }

    protected void showTransactionEmptyView(OldGMStatRepository oldGMStatRepository, AssetManager assetManager) {
        GetTransactionGraph body2 = gson.fromJson(readJson("get_transaction_graph_empty_state.json", assetManager), GetTransactionGraph.class);
        oldGMStatRepository.onSuccessTransactionGraph(repository.getTransactionGraph(body2));
    }

    public Observable<KeywordModel> getMarketInsight() {
        return getShopCategory()
                .flatMap(new Func1<GetShopCategory, Observable<KeywordModel>>() {
                    @Override
                    public Observable<KeywordModel> call(GetShopCategory getShopCategory) {
                        KeywordModel keywordModel = new KeywordModel();
                        keywordModel.setShopCategory(getShopCategory);

                        if (keywordModel.getShopCategory() == null || keywordModel.getShopCategory().getShopCategory() == null
                                || keywordModel.getShopCategory().getShopCategory().isEmpty()) {
                            keywordModel.setResponseList(new ArrayList<Response<GetKeyword>>());
                            keywordModel.setCategoryNames(new ArrayList<String>());
                            return Observable.just(keywordModel);
                        }

                        List<Integer> shopCategory = GMStatisticUtil.subList(getShopCategory.getShopCategory(), MAXIMUM_CATEGORY);
                        Observable<List<String>> getCategories = Observable.from(shopCategory).flatMap(
                                new Func1<Integer, Observable<String>>() {
                                    @Override
                                    public Observable<String> call(Integer integer) {
                                        return getProductCategoryNameUseCase.createObservable(
                                                GetProductCategoryNameUseCase.createRequestParam(integer));
                                    }
                                }
                        ).toList();

                        Observable<List<GetKeyword>> getKeywords
                                = Observable.from(shopCategory)
                                .flatMap(new Func1<Integer, Observable<GetKeyword>>() {
                                    @Override
                                    public Observable<GetKeyword> call(Integer catId) {
                                        return getKeyword(catId);
                                    }
                                })
                                .toList();


                        return Observable.zip(getKeywords, getCategories, Observable.just(keywordModel), new Func3<List<GetKeyword>, List<String>, KeywordModel, KeywordModel>() {
                            @Override
                            public KeywordModel call(List<GetKeyword> responses, List<String> categoryNameList, KeywordModel keywordModel) {
                                keywordModel.setKeywords(new ArrayList<GetKeyword>());
                                keywordModel.setCategoryNames(new ArrayList<String>());
                                if (responses != null && categoryNameList != null
                                        && responses.size() == categoryNameList.size()) {
                                    int size = responses.size();
                                    for (int i = 0; i < size; i++) {
                                        GetKeyword h1 = responses.get(i);
                                        String h2 = categoryNameList.get(i);
                                        if (!TextUtils.isEmpty(h2)) {
                                            keywordModel.getKeywords().add(from(h1));
                                            keywordModel.getCategoryName().add(h2);
                                        }
                                    }
                                } else {
                                    keywordModel.setKeywords(new ArrayList<GetKeyword>());
                                    for (GetKeyword response : responses) {
                                        keywordModel.getKeywords().add(from(response));
                                    }
                                    keywordModel.setCategoryNames(categoryNameList);
                                    return keywordModel;
                                }


                                return keywordModel;
                            }
                        });
                    }
                });
    }

    public void fetchData(long startDate, long endDate, CompositeSubscription compositeSubscription, final OldGMStatRepository oldGMStatRepository) {
        compositeSubscription.add(
                Observable.concat(
                        getProductGraph(startDate, endDate),
                        getTransactionGraph(startDate, endDate),
                        getPopularProduct(),
                        getBuyerData(startDate, endDate),
                        getMarketInsight()
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
                                        oldGMStatRepository.onSuccessProductGraph(getProductGraph);

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
        oldGMStatRepository.onSuccessProductGraph(body);
    }

    private void processKeywordModel(KeywordModel keywordModel, OldGMStatRepository oldGMStatRepository) {
        GetShopCategory getShopCategory = keywordModel.getShopCategory();
        oldGMStatRepository.onSuccessGetShopCategory(getShopCategory);

        if (getShopCategory == null || getShopCategory.getShopCategory() == null || getShopCategory.getShopCategory().isEmpty())
            return;

        List<GetKeyword> getKeywords = keywordModel.getKeywords();
        oldGMStatRepository.onSuccessGetKeyword(getKeywords);

        oldGMStatRepository.onSuccessGetCategory(keywordModel.getCategoryName());
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

    private Response<HadesV1Model> from(CategoryDB categoryDB) {
        HadesV1Model hadesV1Model = new HadesV1Model();
        HadesV1Model.Data data = new HadesV1Model.Data();
        List<HadesV1Model.Category> categories = new ArrayList<>();
        categories.add(fromItem(categoryDB));
        data.setCategories(categories);
        hadesV1Model.setData(data);
        return from(hadesV1Model);
    }

    private HadesV1Model.Category fromItem(CategoryDB categoryDB) {
        HadesV1Model.Category category = new HadesV1Model.Category();
        category.setId(Long.toString(categoryDB.getId()));
        category.setName(categoryDB.getNameCategory());
        return category;
    }

    private Response<HadesV1Model> from(HadesV1Model hadesV1Model) {
        return Response.success(hadesV1Model);
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
