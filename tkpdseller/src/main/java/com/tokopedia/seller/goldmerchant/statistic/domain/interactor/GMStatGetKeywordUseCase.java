package com.tokopedia.seller.goldmerchant.statistic.domain.interactor;

import com.tokopedia.core.base.domain.CompositeUseCase;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.gmstat.models.GetKeyword;
import com.tokopedia.seller.goldmerchant.statistic.data.repository.GMStatRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by normansyahputa on 5/18/17.
 */

public class GMStatGetKeywordUseCase extends CompositeUseCase<GetKeyword> {
    public static final String CATEGORY_ID = "cat_id";
    private GMStatRepository gmStatRepository;

    @Inject
    public GMStatGetKeywordUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            GMStatRepository gmStatRepository
    ) {
        super(threadExecutor, postExecutionThread);
        this.gmStatRepository = gmStatRepository;
    }

    @Override
    public Observable<GetKeyword> createObservable(RequestParams requestParams) {
        String categoryId = requestParams.getString(CATEGORY_ID,"");
        return gmStatRepository.getKeywordModel(categoryId);
    }

    public static RequestParams createRequestParam(String categoryId) {
        RequestParams params = RequestParams.create();
        params.putString(CATEGORY_ID, categoryId);
        return params;
    }


}
