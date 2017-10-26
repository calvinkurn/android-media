package com.tokopedia.inbox.inboxchat.data.repository;

import com.tokopedia.core.network.apiservices.kunyit.KunyitService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.inbox.inboxchat.data.mapper.SendMessageMapper;
import com.tokopedia.inbox.inboxchat.viewmodel.SendMessageViewModel;

import rx.Observable;

/**
 * @author by nisie on 10/25/17.
 */

public class SendMessageSource {
    private KunyitService kunyitService;
    private SendMessageMapper sendMessageMapper;

    public SendMessageSource(KunyitService kunyitService, SendMessageMapper sendMessageMapper) {
        this.kunyitService = kunyitService;
        this.sendMessageMapper = sendMessageMapper;
    }

    public Observable<SendMessageViewModel> sendMessage(TKPDMapParam<String, Object> requestParams) {
        return kunyitService.getApi()
                .sendMessage(requestParams)
                .map(sendMessageMapper);
    }
}
