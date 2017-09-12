package com.tokopedia.inbox.rescenter.createreso.data.source.cloud;

import android.content.Context;

import com.tokopedia.core.network.apiservices.rescenter.apis.ResolutionApi;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.GenerateHostMapper;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.GenerateHostDomain;

import rx.Observable;

/**
 * Created by yoasfs on 16/08/17.
 */

public class GenerateHostCloudSource {
    private Context context;
    private GenerateHostMapper generateHostMapper;
    private ResolutionApi resolutionApi;

    public GenerateHostCloudSource(Context context,
                                   GenerateHostMapper generateHostMapper,
                                   ResolutionApi resolutionApi) {
        this.context = context;
        this.generateHostMapper = generateHostMapper;
        this.resolutionApi = resolutionApi;
    }

    public Observable<GenerateHostDomain> generateHost(TKPDMapParam<String, Object> params) {
        try {
            return resolutionApi.generateHost(AuthUtil.generateParamsNetwork2(context, params))
                    .map(generateHostMapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
