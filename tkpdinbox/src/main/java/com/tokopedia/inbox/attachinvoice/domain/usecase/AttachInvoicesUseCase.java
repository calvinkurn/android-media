package com.tokopedia.inbox.attachinvoice.domain.usecase;

import android.content.Context;

import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.inbox.attachinvoice.data.repository.AttachInvoicesRepository;
import com.tokopedia.inbox.attachinvoice.domain.model.Invoice;
import com.tokopedia.inbox.attachinvoice.view.model.InvoiceToInvoiceViewModelMapper;
import com.tokopedia.inbox.attachinvoice.view.model.InvoiceViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Hendri on 21/03/18.
 */

public class AttachInvoicesUseCase extends UseCase<List<InvoiceViewModel>>{
    private static final String KEYWORD_KEY = "keyword";
    private static final String USER_ID_KEY = "user_id";
    private static final String PAGE_KEY = "page";
    AttachInvoicesRepository repository;

    public AttachInvoicesUseCase(AttachInvoicesRepository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<List<InvoiceViewModel>> createObservable(RequestParams requestParams) {
        return repository.getUserInvoices(requestParams.getParamsAllValueInString()).map(new InvoiceToInvoiceViewModelMapper());
    }

    public static RequestParams createRequestParam(String query,String userId, int page, Context context){
        if(page == 0) page = 1;
        RequestParams param = RequestParams.create();
//        param.putString(KEYWORD_KEY,query);
        param.putString(USER_ID_KEY,userId);
        param.putString(PAGE_KEY,String.valueOf(page));
        param.putString("start","1/1/2018");

        param.putString("end","12/12/2018");

//        param.putAll((Map<String,String>)AuthUtil.generateParams(context));
        Map<String,String> authParam = AuthUtil.generateParams(context);
        for(String key:authParam.keySet()){
            param.putString(key,authParam.get(key));
        }
        return param;
    }
}
