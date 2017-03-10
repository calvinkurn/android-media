package com.tokopedia.inbox.rescenter.detailv2.data.source;

import android.content.Context;

import com.tokopedia.core.network.apiservices.rescenter.ResolutionService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.inbox.rescenter.detailv2.data.mapper.DetailResCenterMapper;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.DetailResCenter;

import rx.Observable;

/**
 * Created by hangnadi on 3/9/17.
 */

public class CloudResCenterDataSource {

    private Context context;
    private ResolutionService resolutionService;
    private DetailResCenterMapper detailResCenterMapper;

    public CloudResCenterDataSource(Context context,
                                    ResolutionService resolutionService,
                                    DetailResCenterMapper detailResCenterMapper) {
        super();
        this.context = context;
        this.resolutionService = resolutionService;
        this.detailResCenterMapper = detailResCenterMapper;
    }

    public Observable<DetailResCenter> getResCenterDetail(String resolutionID, TKPDMapParam<String, Object> parameters) {
        return resolutionService.getApi()
                .getResCenterDetail(resolutionID, AuthUtil.generateParamsNetwork2(context, parameters))
                .map(detailResCenterMapper);
    }

    public Observable<DetailResCenter> getResCenterConversation(String resolutionID) {
        return resolutionService.getApi()
                .getResCenterConversation(resolutionID)
                .map(detailResCenterMapper);
    }
}
