package com.tokopedia.topads.keyword.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.shop.common.domain.repository.ShopInfoRepository;
import com.tokopedia.topads.keyword.constant.KeywordTypeDef;
import com.tokopedia.topads.keyword.domain.TopAdsKeywordRepository;
import com.tokopedia.topads.keyword.domain.model.keywordadd.AddKeywordDomainModel;
import com.tokopedia.topads.keyword.domain.model.keywordadd.AddKeywordDomainModelDatum;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by normansyahputa on 5/18/17.
 */

public class KeywordAddUseCase extends UseCase<AddKeywordDomainModel> {
    public static final String GROUP_ID = "grp_id";
    public static final String KEYWORD_TYPE = "key_typ";
    public static final String KEYWORD_LIST = "key_list";
    private TopAdsKeywordRepository topAdsKeywordRepository;
    private ShopInfoRepository shopInfoRepository;

    @Inject
    public KeywordAddUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            TopAdsKeywordRepository topAdsKeywordRepository,
            ShopInfoRepository shopInfoRepository
    ) {
        super(threadExecutor, postExecutionThread);
        this.topAdsKeywordRepository = topAdsKeywordRepository;
        this.shopInfoRepository = shopInfoRepository;
    }

    @Override
    public Observable<AddKeywordDomainModel> createObservable(RequestParams requestParams) {
        final String groupId = requestParams.getString(GROUP_ID, "");
        final int keywordType = requestParams.getInt(KEYWORD_TYPE, KeywordTypeDef.KEYWORD_TYPE_EXACT);
        final ArrayList<String> keywordList = (ArrayList<String>) requestParams.getObject(KEYWORD_LIST);

        String shopId = shopInfoRepository.getShopId();
        List<AddKeywordDomainModelDatum> addKeywordDomainModelDatumList = new ArrayList<>();
        int size = keywordList.size();
        for (int i = 0; i < size; i++) {
            AddKeywordDomainModelDatum addKeywordDomainModelDatum = new AddKeywordDomainModelDatum(
                    keywordList.get(i),
                    keywordType,
                    groupId,
                    shopId
            );
            addKeywordDomainModelDatumList.add(addKeywordDomainModelDatum);
        }
        AddKeywordDomainModel addKeywordDomainModel = new AddKeywordDomainModel(addKeywordDomainModelDatumList);
        return topAdsKeywordRepository.addKeyword(addKeywordDomainModel);
    }

    public static RequestParams createRequestParam(String groupId,
                                                   @KeywordTypeDef int keywordType,
                                                   ArrayList<String> keywordList) {
        RequestParams params = RequestParams.create();
        params.putString(GROUP_ID, groupId);
        params.putInt(KEYWORD_TYPE, keywordType);
        params.putObject(KEYWORD_LIST, keywordList);
        return params;
    }
}
