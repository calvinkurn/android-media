package com.tokopedia.tkpd.tkpdreputation.shopreview.domain;

import com.tokopedia.tkpd.tkpdreputation.inbox.data.repository.ReputationRepository;
import com.tokopedia.tkpd.tkpdreputation.productreview.data.model.reviewlist.DataResponseReviewShop;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 1/19/18.
 */

public class ShopReviewUseCase extends UseCase<DataResponseReviewShop> {

    public static final String SHOP_DOMAIN = "shop_domain";
    public static final String SHOP_ID = "shop_id";
    public static final String PAGE = "page";
    public static final String PER_PAGE = "per_page";
    public static final String DEFAULT_PER_PAGE = "10";
    private ReputationRepository reputationRepository;

    @Inject
    public ShopReviewUseCase(ReputationRepository reputationRepository) {
        this.reputationRepository = reputationRepository;
    }

    @Override
    public Observable<DataResponseReviewShop> createObservable(RequestParams requestParams) {
        return reputationRepository.getReviewShopList(requestParams.getParamsAllValueInString());
    }

    public RequestParams createRequestParams(String shopDomain, String shopId, String page){
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(SHOP_DOMAIN, shopDomain);
        requestParams.putString(SHOP_ID, shopId);
        requestParams.putString(PAGE, page);
        requestParams.putString(PER_PAGE, DEFAULT_PER_PAGE);
        return requestParams;
    }
}
