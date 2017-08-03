package com.tokopedia.seller.topads.dashboard.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.topads.dashboard.data.model.request.AdCreateGroupRequest;
import com.tokopedia.seller.topads.dashboard.data.model.request.CreateGroupRequest;
import com.tokopedia.seller.topads.dashboard.data.model.response.DataResponseCreateGroup;
import com.tokopedia.seller.topads.dashboard.domain.TopAdsGroupAdsRepository;
import com.tokopedia.seller.topads.dashboard.domain.TopAdsShopAdsRepository;
import com.tokopedia.seller.topads.dashboard.domain.model.TopAdsDetailShopDomainModel;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 2/28/17.
 */

public class TopAdsEditProductGroupToNewGroupUseCase extends UseCase<Boolean> {

    public static final String AD_ID = "AD_ID";
    public static final String GROUP_NAME = "GROUP_NAME";
    public static final String SHOP_ID = "SHOP_ID";
    public static final String GROUP_TOTAL = "1";
    public static final String SOURCE = "sellerapp";
    public static final String PRODUCT_AD_TYPE = "1";

    TopAdsShopAdsRepository topAdsShopAdsRepository;
    TopAdsGroupAdsRepository topAdsGroupAdsRepository;

    public TopAdsEditProductGroupToNewGroupUseCase(ThreadExecutor threadExecutor,
                                                   PostExecutionThread postExecutionThread,
                                                   TopAdsShopAdsRepository topAdsShopAdsRepository,
                                                   TopAdsGroupAdsRepository topAdsGroupAdsRepository) {
        super(threadExecutor, postExecutionThread);
        this.topAdsShopAdsRepository = topAdsShopAdsRepository;
        this.topAdsGroupAdsRepository = topAdsGroupAdsRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return topAdsShopAdsRepository.getDetail(requestParams.getString(AD_ID, ""))
                .flatMap(createNewGroup(requestParams))
                .map(new Func1<DataResponseCreateGroup, Boolean>() {
                    @Override
                    public Boolean call(DataResponseCreateGroup dataResponseCreateGroup) {
                        if(dataResponseCreateGroup != null){
                            return true;
                        }else{
                            return false;
                        }
                    }
                });
    }

    private Func1<TopAdsDetailShopDomainModel, Observable<DataResponseCreateGroup>> createNewGroup(final RequestParams requestParams) {
        return new Func1<TopAdsDetailShopDomainModel, Observable<DataResponseCreateGroup>>() {
            @Override
            public Observable<DataResponseCreateGroup> call(TopAdsDetailShopDomainModel topAdsDetailShopDomainModel) {
                CreateGroupRequest createGroupRequest = convertRequest(topAdsDetailShopDomainModel, requestParams);
                return topAdsGroupAdsRepository.createGroup(createGroupRequest);
            }
        };
    }

    private CreateGroupRequest convertRequest(TopAdsDetailShopDomainModel topAdsDetailShopDomainModel, RequestParams requestParams) {
        CreateGroupRequest createGroupRequest = new CreateGroupRequest();
        createGroupRequest.setGroupName(requestParams.getString(GROUP_NAME, ""));
        createGroupRequest.setShopId(requestParams.getString(SHOP_ID, ""));
        createGroupRequest.setStatus(topAdsDetailShopDomainModel.getStatus());
        createGroupRequest.setPriceBid(Math.round(topAdsDetailShopDomainModel.getPriceBid()));
        createGroupRequest.setPriceDaily(Math.round(topAdsDetailShopDomainModel.getPriceDaily()));
        createGroupRequest.setGroupBudget(topAdsDetailShopDomainModel.getAdBudget());
        createGroupRequest.setGroupSchedule(topAdsDetailShopDomainModel.getAdSchedule());
        createGroupRequest.setGroupStartDate(topAdsDetailShopDomainModel.getAdStartDate());
        createGroupRequest.setGroupEndDate(topAdsDetailShopDomainModel.getAdEndDate());
        createGroupRequest.setGroupStartTime(topAdsDetailShopDomainModel.getAdStartTime());
        createGroupRequest.setGroupEndTime(topAdsDetailShopDomainModel.getAdEndTime());
        createGroupRequest.setStickerId(topAdsDetailShopDomainModel.getStickerId());
        createGroupRequest.setGroupTotal(GROUP_TOTAL);
        createGroupRequest.setSource(SOURCE);
        AdCreateGroupRequest adCreateGroupRequest = new AdCreateGroupRequest();
        adCreateGroupRequest.setAdId(topAdsDetailShopDomainModel.getAdId());
        adCreateGroupRequest.setItemId(topAdsDetailShopDomainModel.getItemId());
        adCreateGroupRequest.setGroupId(topAdsDetailShopDomainModel.getGroupId());
        adCreateGroupRequest.setAdType(PRODUCT_AD_TYPE);
        List<AdCreateGroupRequest> ads = new ArrayList<>();
        ads.add(adCreateGroupRequest);
        createGroupRequest.setAds(ads);
        return createGroupRequest;
    }

    public static RequestParams createRequestParams(String adId, String groupName, String shopId){
        RequestParams params = RequestParams.create();
        params.putString(AD_ID, adId);
        params.putString(GROUP_NAME, groupName);
        params.putString(SHOP_ID, shopId);
        return params;
    }
}
