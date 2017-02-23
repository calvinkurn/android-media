package com.tokopedia.discovery.search.domain.interactor;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.base.common.service.AceService;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.discovery.search.domain.SearchParam;
import com.tokopedia.discovery.search.domain.model.SearchData;

import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;

/**
 * @author erry on 23/02/17.
 */

public class SearchDataFactory {
    private Context context;
    private Gson gson;
    private final AceService aceService;

    public SearchDataFactory(Context context, Gson gson, AceService aceService) {
        this.context = context;
        this.gson = gson;
        this.aceService = aceService;
    }

    public Observable<List<SearchData>> getSearchData(TKPDMapParam<String, String> params){
        return aceService.getUniverseSearch(params)
                .doOnNext(saveToCache())
                .debounce(150, TimeUnit.MICROSECONDS)
                .map(new SearchMapper(context, gson));
    }

    private Action1<Response<String>> saveToCache() {
        return new Action1<Response<String>>() {
            @Override
            public void call(Response<String> response) {
                int tenMinute = 600000;
                new GlobalCacheManager()
                        .setKey(TkpdCache.Key.UNIVERSEARCH)
                        .setCacheDuration(tenMinute)
                        .setValue(response.body())
                        .store();
            }
        };
    }
}
