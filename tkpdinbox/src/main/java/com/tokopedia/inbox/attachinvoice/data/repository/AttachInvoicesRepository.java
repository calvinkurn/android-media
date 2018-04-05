package com.tokopedia.inbox.attachinvoice.data.repository;

import com.raizlabs.android.dbflow.sql.language.Condition;
import com.tokopedia.inbox.attachinvoice.data.mapper.TkpdResponseToInvoicesDataModelMapper;
import com.tokopedia.inbox.attachinvoice.data.model.GetInvoicePostRequest;
import com.tokopedia.inbox.attachinvoice.data.source.service.GetTxInvoicesService;
import com.tokopedia.inbox.attachinvoice.domain.model.Invoice;
import com.tokopedia.inbox.attachinvoice.domain.usecase.AttachInvoicesUseCase;
import com.tokopedia.inbox.inboxchat.data.network.ChatBotApi;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Hendri on 21/03/18.
 */

public class AttachInvoicesRepository {
//    private final GetTxInvoicesService service;
    private final TkpdResponseToInvoicesDataModelMapper mapper;
    private final ChatBotApi api;

    @Inject
    public AttachInvoicesRepository(ChatBotApi api, TkpdResponseToInvoicesDataModelMapper mapper) {
//        this.service = service;
        this.mapper = mapper;
        this.api = api;
    }

    public Observable<List<Invoice>> getUserInvoices(Map<String, String> params) {
        return api.getTXOrderList(params,buildRequestBody(params)).map(mapper);
    }

    public static GetInvoicePostRequest buildRequestBody(Map<String, String> params){
        int messageId = Integer.parseInt(params.get(AttachInvoicesUseCase.MESSAGE_ID_KEY));
        int userId = Integer.parseInt(params.get(AttachInvoicesUseCase.USER_ID_KEY));
        int page = Integer.parseInt(params.get(AttachInvoicesUseCase.PAGE_KEY));
        GetInvoicePostRequest body = new GetInvoicePostRequest(messageId,userId,true,page,AttachInvoicesUseCase.DEFAULT_LIMIT);
        params.remove(AttachInvoicesUseCase.MESSAGE_ID_KEY);
        params.remove(AttachInvoicesUseCase.USER_ID_KEY);
        params.remove(AttachInvoicesUseCase.PAGE_KEY);
        return body;
    }
}
