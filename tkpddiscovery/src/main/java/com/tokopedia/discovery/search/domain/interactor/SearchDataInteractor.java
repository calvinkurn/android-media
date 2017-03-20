package com.tokopedia.discovery.search.domain.interactor;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.apiservices.ace.UniverseService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.discovery.search.domain.model.SearchData;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.OnTextChanged;
import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;

/**
 * @author erry on 23/02/17.
 */

public class SearchDataInteractor {
    private Context context;
    private Gson gson;
    private final UniverseService universeService;

    public SearchDataInteractor(Context context) {
        this.context = context;
        this.gson = new Gson();
        this.universeService = new UniverseService();
    }

    public Observable<List<SearchData>> getSearchData(TKPDMapParam<String, String> params){
        return universeService.getApi().getUniverseSearch(params)
                .doOnNext(saveToCache())
                .debounce(1, TimeUnit.SECONDS)
                .map(new SearchMapper(context, gson));
    }

    public Observable<Response<Void>> deleteRecentSearch(TKPDMapParam<String, String> params) {
        return universeService.getApi().deleteRecentSearch(params);
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
