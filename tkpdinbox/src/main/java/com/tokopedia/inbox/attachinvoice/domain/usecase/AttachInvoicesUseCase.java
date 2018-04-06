package com.tokopedia.inbox.attachinvoice.domain.usecase;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.inbox.attachinvoice.data.repository.AttachInvoicesRepository;
import com.tokopedia.inbox.attachinvoice.view.model.InvoiceToInvoiceViewModelMapper;
import com.tokopedia.inbox.attachinvoice.view.model.InvoiceViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Hendri on 21/03/18.
 */

public class AttachInvoicesUseCase extends UseCase<List<InvoiceViewModel>> {
    public static final String KEYWORD_KEY = "keyword";
    public static final String USER_ID_KEY = "user_id";
    public static final String PAGE_KEY = "page";
    public static final String MESSAGE_ID_KEY = "message_id";
    public static int DEFAULT_LIMIT = 10;
    AttachInvoicesRepository repository;

    @Inject
    public AttachInvoicesUseCase(AttachInvoicesRepository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<List<InvoiceViewModel>> createObservable(RequestParams requestParams) {
        return repository.getUserInvoices(requestParams.getParamsAllValueInString()).map(new
                InvoiceToInvoiceViewModelMapper());
    }


    public static RequestParams createRequestParam(String query, String userId, int page, int
            messageId, Context context) {
        if (page == 0) page = 1;
        RequestParams param = RequestParams.create();
        if (!TextUtils.isEmpty(query)) param.putString(KEYWORD_KEY, query);
        param.putString(USER_ID_KEY, userId);
        param.putString(PAGE_KEY, String.valueOf(page));
        param.putString(MESSAGE_ID_KEY, String.valueOf(messageId));
        return param;
    }
}
