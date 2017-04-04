package com.tokopedia.inbox.rescenter.discussion.data.source;

import android.content.Context;

import com.tokopedia.core.network.apiservices.rescenter.ResCenterActService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.inbox.rescenter.discussion.data.mapper.SubmitImageMapper;
import com.tokopedia.inbox.rescenter.discussion.domain.model.replysubmit.ReplySubmitModel;

import rx.Observable;

/**
 * Created by nisie on 4/3/17.
 */

public class CloudSubmitImageDataSource {
    private Context context;
    private final ResCenterActService resCenterActService;
    private final SubmitImageMapper submitImageMapper;

    public CloudSubmitImageDataSource(Context context,
                                      ResCenterActService resCenterActService,
                                      SubmitImageMapper submitImageMapper) {
        this.context = context;
        this.resCenterActService = resCenterActService;
        this.submitImageMapper = submitImageMapper;
    }

    public Observable<ReplySubmitModel> submitImage(TKPDMapParam<String, Object> params) {
        return resCenterActService.getApi().replyConversationSubmit2(
                AuthUtil.generateParamsNetwork2(context, params))
                .map(submitImageMapper);
    }
}
