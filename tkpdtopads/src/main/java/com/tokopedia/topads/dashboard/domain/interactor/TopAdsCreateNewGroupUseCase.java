package com.tokopedia.topads.dashboard.domain.interactor;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.topads.dashboard.constant.TopAdsConstant;
import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.topads.dashboard.data.model.request.AdCreateGroupRequest;
import com.tokopedia.topads.dashboard.data.model.request.CreateGroupRequest;
import com.tokopedia.topads.dashboard.data.model.response.DataResponseCreateGroup;
import com.tokopedia.topads.dashboard.domain.TopAdsGroupAdsRepository;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailGroupViewModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsProductViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 2/20/17.
 */
public class TopAdsCreateNewGroupUseCase extends UseCase<TopAdsDetailGroupViewModel> {

    public static final String REQ_GROUP_NAME = "grp_nm";
    public static final String REQ_GROUP_VIEW_MODEL = "grp_mdl";
    public static final String REQ_PRODUCT_LIST = "prod_ls";

    public static final String DEFAULT_GROUP_ID = "0";

    private final TopAdsGroupAdsRepository topAdsGroupAdsRepository;

    @Inject
    public TopAdsCreateNewGroupUseCase(ThreadExecutor threadExecutor,
                                       PostExecutionThread postExecutionThread,
                                       TopAdsGroupAdsRepository topAdsGroupAdsRepository) {
        super(threadExecutor, postExecutionThread);
        this.topAdsGroupAdsRepository = topAdsGroupAdsRepository;
    }

    @Override
    public Observable<TopAdsDetailGroupViewModel> createObservable(RequestParams requestParams) {
        return topAdsGroupAdsRepository.createGroup(
                getCreateGroupRequest(requestParams))
                .map(new Func1<DataResponseCreateGroup, TopAdsDetailGroupViewModel>() {
                    @Override
                    public TopAdsDetailGroupViewModel call(DataResponseCreateGroup dataResponseCreateGroup) {
                        TopAdsDetailGroupViewModel topAdsDetailGroupViewModel = new TopAdsDetailGroupViewModel();
                        topAdsDetailGroupViewModel.setStartDate(dataResponseCreateGroup.getGroupStartDate());
                        topAdsDetailGroupViewModel.setEndDate(dataResponseCreateGroup.getGroupEndDate());
                        topAdsDetailGroupViewModel.setPriceBid(dataResponseCreateGroup.getPriceBid());
                        topAdsDetailGroupViewModel.setShopId(Long.valueOf(dataResponseCreateGroup.getShopId()));
                        topAdsDetailGroupViewModel.setStartTime(dataResponseCreateGroup.getGroupStartTime());
                        topAdsDetailGroupViewModel.setEndTime(dataResponseCreateGroup.getGroupEndTime());
                        topAdsDetailGroupViewModel.setScheduled("1".equals(dataResponseCreateGroup.getGroupSchedule()));
                        topAdsDetailGroupViewModel.setStatus(Integer.parseInt(dataResponseCreateGroup.getStatus()));
                        return topAdsDetailGroupViewModel;
                    }
                });
    }

    private CreateGroupRequest getCreateGroupRequest(RequestParams requestParams) {
        CreateGroupRequest createGroupRequest = new CreateGroupRequest();
        createGroupRequest.setGroupName(requestParams.getString(REQ_GROUP_NAME, ""));
        createGroupRequest.setShopId(SessionHandler.getShopID(MainApplication.getAppContext()));

        TopAdsDetailGroupViewModel viewModel = (TopAdsDetailGroupViewModel) requestParams.getObject(REQ_GROUP_VIEW_MODEL);
        List<TopAdsProductViewModel> productList = (List<TopAdsProductViewModel>) requestParams.getObject(REQ_PRODUCT_LIST);

        createGroupRequest.setStatus(String.valueOf(TopAdsConstant.STATUS_AD_ACTIVE)); // default is active "1"
        createGroupRequest.setPriceBid(Math.round(viewModel.getPriceBid()));
        createGroupRequest.setPriceDaily(Math.round(viewModel.getPriceDaily()));
        createGroupRequest.setGroupBudget(viewModel.isBudget() ? "1" : "0");
        createGroupRequest.setGroupSchedule(viewModel.isScheduled() ? "1" : "0");
        createGroupRequest.setGroupStartDate(viewModel.getStartDate());
        createGroupRequest.setGroupEndDate(viewModel.getEndDate());
        createGroupRequest.setGroupStartTime(viewModel.getStartTime());
        createGroupRequest.setGroupEndTime(viewModel.getEndTime());
        createGroupRequest.setStickerId(String.valueOf(viewModel.getStickerId()));
        createGroupRequest.setSource(TopAdsNetworkConstant.VALUE_SOURCE_ANDROID);

        int productSize = productList.size();
        createGroupRequest.setGroupTotal(String.valueOf(productSize));

        List<AdCreateGroupRequest> ads = new ArrayList<>();
        for (int i = 0; i < productSize; i++) {
            TopAdsProductViewModel productModel = productList.get(i);
            AdCreateGroupRequest adCreateGroupRequest = new AdCreateGroupRequest();
            adCreateGroupRequest.setAdId(String.valueOf(productModel.getAdId())); // default "0" is not promoted
            adCreateGroupRequest.setItemId(String.valueOf(productModel.getId()));
            adCreateGroupRequest.setGroupId(DEFAULT_GROUP_ID);
            adCreateGroupRequest.setAdType(String.valueOf(TopAdsConstant.AD_TYPE_PRODUCT));
            ads.add(adCreateGroupRequest);
        }
        createGroupRequest.setAds(ads);
        createGroupRequest.setSuggestionBidValue(viewModel.getSuggestionBidValue());
        createGroupRequest.setSuggestionBidButton(viewModel.getSuggestionBidButton());
        return createGroupRequest;
    }

    public static RequestParams createRequestParams(String groupName,
                                                    TopAdsDetailGroupViewModel topAdsDetailProductViewModel,
                                                    List<TopAdsProductViewModel> topAdsProductViewModelList) {
        RequestParams params = RequestParams.create();
        params.putString(REQ_GROUP_NAME, groupName);
        params.putObject(REQ_GROUP_VIEW_MODEL, topAdsDetailProductViewModel);
        params.putObject(REQ_PRODUCT_LIST, topAdsProductViewModelList);
        return params;
    }
}