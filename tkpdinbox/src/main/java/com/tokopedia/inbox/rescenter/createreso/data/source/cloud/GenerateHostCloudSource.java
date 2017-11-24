package com.tokopedia.inbox.rescenter.createreso.data.source.cloud;

import android.content.Context;

import com.tokopedia.core.network.apiservices.rescenter.ResCenterActService;
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
    private ResCenterActService resCenterActService;

    public GenerateHostCloudSource(Context context,
                                   GenerateHostMapper generateHostMapper,
                                   ResCenterActService resCenterActService) {
        this.context = context;
        this.generateHostMapper = generateHostMapper;
        this.resCenterActService = resCenterActService;
    }

    public Observable<GenerateHostDomain> generateHost(TKPDMapParam<String, Object> params) {
        try {
            return resCenterActService.getApi().generateTokenHostWithoutHeader(
                    AuthUtil.generateParamsNetwork2(context, params))
                    .map(generateHostMapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
