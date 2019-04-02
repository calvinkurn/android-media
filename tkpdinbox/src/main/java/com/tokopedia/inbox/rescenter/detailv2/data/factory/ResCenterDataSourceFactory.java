package com.tokopedia.inbox.rescenter.detailv2.data.factory;

import android.content.Context;

import com.tokopedia.core.network.apiservices.rescenter.apis.ResCenterActApi;
import com.tokopedia.core.network.apiservices.rescenter.apis.ResolutionApi;
import com.tokopedia.core.network.apiservices.user.apis.InboxResCenterApi;
import com.tokopedia.inbox.rescenter.detailv2.data.mapper.DetailResCenterMapper;
import com.tokopedia.inbox.rescenter.detailv2.data.mapper.DetailResCenterMapperV2;
import com.tokopedia.inbox.rescenter.detailv2.data.mapper.GetDetailResChatMapper;
import com.tokopedia.inbox.rescenter.detailv2.data.mapper.GetDetailResChatMoreMapper;
import com.tokopedia.inbox.rescenter.detailv2.data.mapper.GetNextActionMapper;
import com.tokopedia.inbox.rescenter.detailv2.data.source.NextActionCloudSource;
import com.tokopedia.inbox.rescenter.detailv2.data.source.ResChatCloudSource;
import com.tokopedia.inbox.rescenter.detailv2.data.source.ResChatMoreCloudSource;
import com.tokopedia.inbox.rescenter.discussion.data.mapper.ReplyResolutionMapper;
import com.tokopedia.inbox.rescenter.detailv2.data.source.CloudActionResCenterDataStore;
import com.tokopedia.inbox.rescenter.detailv2.data.source.CloudInboxResCenterDataSource;
import com.tokopedia.inbox.rescenter.detailv2.data.source.CloudResCenterDataSource;
import com.tokopedia.inbox.rescenter.discussion.data.mapper.DiscussionResCenterMapper;
import com.tokopedia.inbox.rescenter.discussion.data.mapper.LoadMoreMapper;
import com.tokopedia.inbox.rescenter.discussion.data.mapper.ReplyResolutionSubmitMapper;
import com.tokopedia.inbox.rescenter.historyaction.data.mapper.HistoryActionMapper;
import com.tokopedia.inbox.rescenter.historyaddress.data.mapper.HistoryAddressMapper;
import com.tokopedia.inbox.rescenter.historyawb.data.mapper.HistoryAwbMapper;
import com.tokopedia.inbox.rescenter.product.data.mapper.ListProductMapper;
import com.tokopedia.inbox.rescenter.product.data.mapper.ProductDetailMapper;

/**
 * Created by hangnadi on 3/9/17.
 */

public class ResCenterDataSourceFactory {

    private Context context;
    private ResolutionApi resolutionApi;
    private InboxResCenterApi inboxResCenterApi;
    private ResCenterActApi resCenterActApi;
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
    private GetDetailResChatMapper getDetailResChatMapper;
    private GetDetailResChatMoreMapper getDetailResChatMoreMapper;
    private GetNextActionMapper getNextActionMapper;
    private DetailResCenterMapperV2 detailResCenterMapperV2;

    public ResCenterDataSourceFactory(Context context,
                                      ResolutionApi resolutionApi,
                                      InboxResCenterApi inboxResCenterApi,
                                      ResCenterActApi resCenterActApi,
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
                                      GetDetailResChatMapper getDetailResChatMapper,
                                      GetDetailResChatMoreMapper getDetailResChatMoreMapper,
                                      GetNextActionMapper getNextActionMapper,
                                      DetailResCenterMapperV2 detailResCenterMapperV2) {
        this.context = context;
        this.resolutionApi = resolutionApi;
        this.inboxResCenterApi = inboxResCenterApi;
        this.resCenterActApi = resCenterActApi;
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
        this.getDetailResChatMapper = getDetailResChatMapper;
        this.getDetailResChatMoreMapper = getDetailResChatMoreMapper;
        this.getNextActionMapper = getNextActionMapper;
        this.detailResCenterMapperV2 = detailResCenterMapperV2;
    }

    public CloudResCenterDataSource createCloudResCenterDataSource() {
        return new CloudResCenterDataSource(context, resolutionApi,
                detailResCenterMapper,
                historyAwbMapper,
                historyAddressMapper,
                historyActionMapper,
                listProductMapper,
                productDetailMapper,
                discussionResCenterMapper,
                loadMoreMapper,
                replyResolutionMapper,
                replyResolutionSubmitMapper,
                detailResCenterMapperV2
        );
    }

    public CloudInboxResCenterDataSource createCloudInboxResCenterDataSource() {
        return new CloudInboxResCenterDataSource(context, inboxResCenterApi);
    }

    public CloudActionResCenterDataStore createCloudActionResCenterDataStore() {
        return new CloudActionResCenterDataStore(context, resCenterActApi, resolutionApi);
    }

    public ResChatCloudSource createResChatCloudSource() {
        return new ResChatCloudSource(getDetailResChatMapper, resolutionApi);
    }

    public ResChatMoreCloudSource createResChatMoreCloudSource() {
        return new ResChatMoreCloudSource(getDetailResChatMoreMapper, resolutionApi);
    }

    public NextActionCloudSource createNextActionCloudSource() {
        return new NextActionCloudSource(getNextActionMapper, resolutionApi);
    }
}
