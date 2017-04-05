package com.tokopedia.inbox.rescenter.discussion.data.source;

import android.content.Context;

import com.tokopedia.core.network.apiservices.upload.GenerateHostActService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.inbox.rescenter.discussion.data.mapper.GenerateHostMapper;
import com.tokopedia.inbox.rescenter.discussion.domain.model.generatehost.GenerateHostModel;

import rx.Observable;

/**
 * Created by nisie on 4/3/17.
 */

public class CloudGenerateHostSource {
    private Context context;
    private final GenerateHostActService generateHostActService;
    private final GenerateHostMapper generateHostMapper;

    public CloudGenerateHostSource(Context context,
                                   GenerateHostActService generateHostActService,
                                   GenerateHostMapper generateHostMapper) {
        this.context = context;
        this.generateHostActService = generateHostActService;
        this.generateHostMapper = generateHostMapper;
    }

    public Observable<GenerateHostModel> generateHost(TKPDMapParam<String, Object> params) {
        return generateHostActService.getApi()
                .generateHost3(AuthUtil.generateParamsNetwork2(context, params))
                .map(generateHostMapper);
    }
}
