package com.tokopedia.inbox.rescenter.detailv2.data.factory;

import android.content.Context;

import com.tokopedia.core.network.apiservices.rescenter.ResolutionService;
import com.tokopedia.inbox.rescenter.detailv2.data.mapper.DetailResCenterMapper;
import com.tokopedia.inbox.rescenter.detailv2.data.source.CloudResCenterDataSource;

/**
 * Created by hangnadi on 3/9/17.
 */

public class ResCenterDataSourceFactory {

    private Context context;
    private ResolutionService resCenterService;
    private DetailResCenterMapper detailResCenterMapper;

    public ResCenterDataSourceFactory(Context context,
                                      ResolutionService resCenterService,
                                      DetailResCenterMapper detailResCenterMapper) {
        this.context = context;
        this.resCenterService = resCenterService;
        this.detailResCenterMapper = detailResCenterMapper;
    }

    public CloudResCenterDataSource createCloudResCenterDataSource() {
        return new CloudResCenterDataSource(context, resCenterService, detailResCenterMapper);
    }
}
