package com.tokopedia.tkpd.tkpdreputation.uploadimage.data.source;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.upload.GenerateHostActService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.tkpd.tkpdreputation.uploadimage.data.mapper.GenerateHostMapper;
import com.tokopedia.tkpd.tkpdreputation.uploadimage.domain.model.GenerateHostDomain;

import rx.Observable;

/**
 * @author by nisie on 9/5/17.
 */

public class CloudGenerateHostDataSource {

    private final GenerateHostActService generateHostActService;
    private final GenerateHostMapper generateHostMapper;

    public CloudGenerateHostDataSource(GenerateHostActService generateHostActService,
                                       GenerateHostMapper generateHostMapper) {
        this.generateHostActService = generateHostActService;
        this.generateHostMapper = generateHostMapper;
    }


    public Observable<GenerateHostDomain> generateHost(RequestParams parameters) {
        return generateHostActService.getApi()
                .generateHost4(AuthUtil.generateParamsNetwork(MainApplication.getAppContext(),
                        parameters.getParamsAllValueInString()))
                .map(generateHostMapper);
    }
}
