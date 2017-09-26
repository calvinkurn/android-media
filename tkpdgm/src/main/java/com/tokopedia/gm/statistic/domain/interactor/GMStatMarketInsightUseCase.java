package com.tokopedia.gm.statistic.domain.interactor;

import android.text.TextUtils;

import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetKeyword;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetShopCategory;
import com.tokopedia.gm.statistic.domain.KeywordModel;
import com.tokopedia.seller.common.williamchart.util.GMStatisticUtil;
import com.tokopedia.core.common.category.domain.interactor.GetProductCategoryNameUseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func3;

/**
 * Created by normansyahputa on 8/1/17.
 */

public class GMStatMarketInsightUseCase extends UseCase<KeywordModel> {
    public static final int MAXIMUM_CATEGORY = 1;
    private GMStatGetShopCategoryUseCase categoryUseCase;
    private GetProductCategoryNameUseCase categoryNameUseCase;
    private GMStatGetKeywordUseCase keywordUseCase;

    @Inject
    public GMStatMarketInsightUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            GMStatGetShopCategoryUseCase categoryUseCase,
            GetProductCategoryNameUseCase categoryNameUseCase,
            GMStatGetKeywordUseCase keywordUseCase) {
        super(threadExecutor, postExecutionThread);
        this.categoryUseCase = categoryUseCase;
        this.categoryNameUseCase = categoryNameUseCase;
        this.keywordUseCase = keywordUseCase;
    }

    @Override
    public Observable<KeywordModel> createObservable(RequestParams requestParams) {
        return categoryUseCase.createObservable(requestParams).flatMap(new Func1<GetShopCategory, Observable<KeywordModel>>() {
            @Override
            public Observable<KeywordModel> call(GetShopCategory getShopCategory) {
                return processShopCategory(getShopCategory);
            }
        });
    }

    private Observable<KeywordModel> processShopCategory(GetShopCategory getShopCategory) {
        KeywordModel keywordModel = new KeywordModel();
        keywordModel.setShopCategory(getShopCategory);

        if (keywordModel.getShopCategory() == null || keywordModel.getShopCategory().getCategoryIdList() == null
                || keywordModel.getShopCategory().getCategoryIdList().isEmpty()) {
            keywordModel.setResponseList(new ArrayList<Response<GetKeyword>>());
            keywordModel.setCategoryNames(new ArrayList<String>());
            return Observable.just(keywordModel);
        }

        List<Integer> shopCategory = GMStatisticUtil.subList(getShopCategory.getCategoryIdList(), MAXIMUM_CATEGORY);
        Observable<List<String>> getCategories = Observable.from(shopCategory).flatMap(
                new Func1<Integer, Observable<String>>() {
                    @Override
                    public Observable<String> call(Integer integer) {
                        return categoryNameUseCase.createObservable(
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

    private Observable<GetKeyword> getKeyword(Integer catId) {
        return keywordUseCase.createObservable(GMStatGetKeywordUseCase.createRequestParam(Integer.toString(catId)));
    }

    private GetKeyword from(GetKeyword getKeyword) {
        GetKeyword res = new GetKeyword();
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
}
