package com.tokopedia.inbox.rescenter.detailv2.data.factory;

import android.content.Context;

import com.tokopedia.core.network.apiservices.rescenter.ResCenterActService;
import com.tokopedia.core.network.apiservices.rescenter.ResolutionService;
import com.tokopedia.core.network.apiservices.user.InboxResCenterService;
import com.tokopedia.inbox.rescenter.detailv2.data.mapper.DetailResCenterMapper;
import com.tokopedia.inbox.rescenter.discussion.data.mapper.DiscussionResCenterMapper;
import com.tokopedia.inbox.rescenter.historyaction.data.mapper.HistoryActionMapper;
import com.tokopedia.inbox.rescenter.historyaddress.data.mapper.HistoryAddressMapper;
import com.tokopedia.inbox.rescenter.historyawb.data.mapper.HistoryAwbMapper;
import com.tokopedia.inbox.rescenter.detailv2.data.source.CloudActionResCenterDataStore;
import com.tokopedia.inbox.rescenter.detailv2.data.source.CloudInboxResCenterDataSource;
import com.tokopedia.inbox.rescenter.detailv2.data.source.CloudResCenterDataSource;

/**
 * Created by hangnadi on 3/9/17.
 */

public class ResCenterDataSourceFactory {

    private Context context;
    private ResolutionService resCenterService;
    private InboxResCenterService inboxResCenterService;
    private DetailResCenterMapper detailResCenterMapper;
    private ResCenterActService resCenterActService;
    private HistoryAwbMapper historyAwbMapper;
    private HistoryAddressMapper historyAddressMapper;
    private HistoryActionMapper historyActionMapper;
    private DiscussionResCenterMapper discussionResCenterMapper;

    public ResCenterDataSourceFactory(Context context,
                                      ResolutionService resCenterService,
                                      InboxResCenterService inboxResCenterService,
                                      ResCenterActService resCenterActService,
                                      DetailResCenterMapper detailResCenterMapper,
                                      HistoryAwbMapper historyAwbMapper,
                                      HistoryAddressMapper historyAddressMapper,
                                      HistoryActionMapper historyActionMapper,
                                      DiscussionResCenterMapper discussionResCenterMapper) {
        this.context = context;
        this.resCenterService = resCenterService;
        this.inboxResCenterService = inboxResCenterService;
        this.detailResCenterMapper = detailResCenterMapper;
        this.resCenterActService = resCenterActService;
        this.historyAwbMapper = historyAwbMapper;
        this.historyAddressMapper = historyAddressMapper;
        this.historyActionMapper = historyActionMapper;
        this.discussionResCenterMapper = discussionResCenterMapper;
    }

    public CloudResCenterDataSource createCloudResCenterDataSource() {
        return new CloudResCenterDataSource(context, resCenterService,
                detailResCenterMapper,
                historyAwbMapper,
                historyAddressMapper,
                historyActionMapper,
                discussionResCenterMapper);
    }

    public CloudInboxResCenterDataSource createCloudInboxResCenterDataSource() {
        return new CloudInboxResCenterDataSource(context, inboxResCenterService);
    }

    public CloudActionResCenterDataStore createCloudActionResCenterDataStore() {
        return new CloudActionResCenterDataStore(context, resCenterActService);
    }
}
