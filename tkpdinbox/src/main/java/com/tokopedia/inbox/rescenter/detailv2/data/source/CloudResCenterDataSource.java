package com.tokopedia.inbox.rescenter.detailv2.data.source;

import android.content.Context;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.rescenter.apis.ResolutionApi;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.inbox.rescenter.detailv2.data.mapper.DetailResCenterMapper;
import com.tokopedia.inbox.rescenter.detailv2.data.mapper.DetailResCenterMapperV2;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.GetResCenterDetailV2UseCase;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2.DetailResponseData;
import com.tokopedia.inbox.rescenter.discussion.data.mapper.ReplyResolutionSubmitMapper;
import com.tokopedia.inbox.rescenter.discussion.data.mapper.ReplyResolutionMapper;
import com.tokopedia.inbox.rescenter.discussion.data.mapper.LoadMoreMapper;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.DetailResCenter;
import com.tokopedia.inbox.rescenter.discussion.data.mapper.DiscussionResCenterMapper;
import com.tokopedia.inbox.rescenter.discussion.domain.model.NewReplyDiscussionModel;
import com.tokopedia.inbox.rescenter.discussion.domain.model.getdiscussion.DiscussionModel;
import com.tokopedia.inbox.rescenter.discussion.domain.model.loadmore.LoadMoreModel;
import com.tokopedia.inbox.rescenter.historyaction.data.mapper.HistoryActionMapper;
import com.tokopedia.inbox.rescenter.historyaction.domain.model.HistoryActionData;
import com.tokopedia.inbox.rescenter.historyaddress.data.mapper.HistoryAddressMapper;
import com.tokopedia.inbox.rescenter.historyaddress.domain.model.HistoryAddressData;
import com.tokopedia.inbox.rescenter.historyawb.data.mapper.HistoryAwbMapper;
import com.tokopedia.inbox.rescenter.historyawb.domain.model.HistoryAwbData;
import com.tokopedia.inbox.rescenter.product.data.mapper.ListProductMapper;
import com.tokopedia.inbox.rescenter.product.data.mapper.ProductDetailMapper;
import com.tokopedia.inbox.rescenter.product.domain.model.ListProductDomainData;
import com.tokopedia.inbox.rescenter.product.domain.model.ProductDetailData;

import rx.Observable;

/**
 * Created by hangnadi on 3/9/17.
 */

public class CloudResCenterDataSource {

    private Context context;
    private ResolutionApi resolutionApi;
    private DetailResCenterMapper detailResCenterMapper;
    private HistoryAwbMapper historyAwbMapper;
    private HistoryAddressMapper historyAddressMapper;
    private HistoryActionMapper historyActionMapper;
    private ListProductMapper listProductMapper;
    private ProductDetailMapper productDetailMapper;
    private DiscussionResCenterMapper discussionResCenterMapper;
    private LoadMoreMapper loadMoreMapper;
    private ReplyResolutionMapper replyResolutionMapper;
    private ReplyResolutionSubmitMapper replyResolutionSubmitMapper;
    private DetailResCenterMapperV2 detailResCenterMapperV2;

    public CloudResCenterDataSource(Context context,
                                    ResolutionApi resolutionApi,
                                    DetailResCenterMapper detailResCenterMapper,
                                    HistoryAwbMapper historyAwbMapper,
                                    HistoryAddressMapper historyAddressMapper,
                                    HistoryActionMapper historyActionMapper,
                                    ListProductMapper listProductMapper,
                                    ProductDetailMapper productDetailMapper,
                                    DiscussionResCenterMapper discussionResCenterMapper,
                                    LoadMoreMapper loadMoreMapper,
                                    ReplyResolutionMapper replyResolutionMapper,
                                    ReplyResolutionSubmitMapper replyResolutionSubmitMapper,
                                    DetailResCenterMapperV2 detailResCenterMapperV2) {
        super();
        this.context = context;
        this.resolutionApi = resolutionApi;
        this.detailResCenterMapper = detailResCenterMapper;
        this.historyAwbMapper = historyAwbMapper;
        this.historyAddressMapper = historyAddressMapper;
        this.historyActionMapper = historyActionMapper;
        this.listProductMapper = listProductMapper;
        this.productDetailMapper = productDetailMapper;
        this.discussionResCenterMapper = discussionResCenterMapper;
        this.loadMoreMapper = loadMoreMapper;
        this.replyResolutionMapper = replyResolutionMapper;
        this.replyResolutionSubmitMapper = replyResolutionSubmitMapper;
        this.detailResCenterMapperV2 = detailResCenterMapperV2;
    }

    public Observable<DetailResCenter> getResCenterDetail(String resolutionID, TKPDMapParam<String, Object> parameters) {
        return resolutionApi.getResCenterDetail(
                resolutionID,
                AuthUtil.generateParamsNetwork2(context, parameters)
        ).map(detailResCenterMapper);
    }

    public Observable<DetailResponseData> getResCenterDetailV2(RequestParams requestParams) {
        return resolutionApi.getResCenterDetailV2(
                requestParams.getString(
                        GetResCenterDetailV2UseCase.PARAM_RESOLUTION_ID, ""))
                .map(detailResCenterMapperV2);
    }

 public Observable<DiscussionModel> getResCenterConversation(String resolutionID,
                                                             TKPDMapParam<String, Object> parameters) {
        return resolutionApi.getResCenterConversation(
                resolutionID, AuthUtil.generateParamsNetwork2(context, parameters)
        ).map(discussionResCenterMapper);
    }

    public Observable<LoadMoreModel> getResCenterConversationMore(String resolutionID,
                                                                  TKPDMapParam<String, Object> parameters) {
        return resolutionApi.getResCenterConversationMore(
                resolutionID,
                AuthUtil.generateParamsNetwork2(context, parameters)
        ).map(loadMoreMapper);
    }

    public Observable<HistoryAwbData> getHistoryAwb(String resolutionID,
                                                    TKPDMapParam<String, Object> parameters) {
        return resolutionApi.getHistoryAwb(
                resolutionID,
                AuthUtil.generateParamsNetwork2(context, parameters)
        ).map(historyAwbMapper);
    }

    public Observable<HistoryActionData> getHistoryAction(String resolutionID,
                                                          TKPDMapParam<String, Object> parameters) {
        return resolutionApi.getHistoryAction(
                resolutionID,
                AuthUtil.generateParamsNetwork2(context, parameters)
        ).map(historyActionMapper);
    }

    public Observable<HistoryActionData> getHistoryActionV2(String resolutionID,
                                                           TKPDMapParam<String, Object> parameters) {
        return resolutionApi.getHistoryActionV2(
                resolutionID,
                AuthUtil.generateParamsNetwork2(context, parameters)
        ).map(historyActionMapper);
    }

    public Observable<HistoryAddressData> getHistoryAddress(String resolutionID,
                                                            TKPDMapParam<String, Object> parameters) {
        return resolutionApi.getHistoryAddress(
                resolutionID,
                AuthUtil.generateParamsNetwork2(context, parameters)
        ).map(historyAddressMapper);
    }

    public Observable<ListProductDomainData> getListProduct(String resolutionID,
                                                            TKPDMapParam<String, Object> parameters) {
        return resolutionApi.getListProduct(
                resolutionID,
                AuthUtil.generateParamsNetwork2(context, parameters)
        ).map(listProductMapper);
    }

    public Observable<ProductDetailData> getProductDetail(String resolutionID,
                                                          String troubleID,
                                                          TKPDMapParam<String, Object> parameters) {
        return resolutionApi.getProductDetail(
                resolutionID,
                troubleID,
                AuthUtil.generateParamsNetwork2(context, parameters)
        ).map(productDetailMapper);
    }

    public Observable<NewReplyDiscussionModel> replyResolution(String resolutionID,
                                                               TKPDMapParam<String, Object> parameters) {
        return resolutionApi.replyResolution(resolutionID,
                AuthUtil.generateParamsNetwork2(context, parameters))
                .map(replyResolutionMapper);
    }

    public Observable<NewReplyDiscussionModel> replyResolutionSubmit(String resolutionID,
                                                                     TKPDMapParam<String, Object> parameters) {
        return resolutionApi.replyResolutionSubmit(resolutionID,
                AuthUtil.generateParamsNetwork2(context, parameters))
                .map(replyResolutionSubmitMapper);
    }

}
