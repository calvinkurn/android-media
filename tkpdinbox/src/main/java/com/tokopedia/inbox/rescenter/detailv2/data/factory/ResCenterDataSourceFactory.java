package com.tokopedia.inbox.rescenter.detailv2.data.factory;

import android.content.Context;

import com.tokopedia.core.network.apiservices.rescenter.ResCenterActService;
import com.tokopedia.core.network.apiservices.rescenter.ResolutionService;
import com.tokopedia.core.network.apiservices.user.InboxResCenterService;
import com.tokopedia.inbox.rescenter.detailv2.data.mapper.DetailResCenterMapper;
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

    public ResCenterDataSourceFactory(Context context,
                                      ResolutionService resCenterService,
                                      InboxResCenterService inboxResCenterService,
                                      ResCenterActService resCenterActService,
                                      DetailResCenterMapper detailResCenterMapper) {
        this.context = context;
        this.resCenterService = resCenterService;
        this.inboxResCenterService = inboxResCenterService;
        this.detailResCenterMapper = detailResCenterMapper;
        this.resCenterActService = resCenterActService;
    }

    public CloudResCenterDataSource createCloudResCenterDataSource() {
        return new CloudResCenterDataSource(context, resCenterService, detailResCenterMapper);
    }

    public CloudInboxResCenterDataSource createCloudInboxResCenterDataSource() {
        return new CloudInboxResCenterDataSource(context, inboxResCenterService);
    }

    public CloudActionResCenterDataStore createCloudActionResCenterDataStore() {
        return new CloudActionResCenterDataStore(context, resCenterActService);
    }
}
