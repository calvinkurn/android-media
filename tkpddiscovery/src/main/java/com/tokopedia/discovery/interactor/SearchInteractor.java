package com.tokopedia.discovery.interactor;

import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Toped18 on 7/5/2016.
 */
public interface SearchInteractor {
    String TAG = "STUART";
    String SEARCH_CACHE = "SEARCH_CACHE";

    void deleteItem(String s);


    interface GetSearchCacheListener {
        void onSuccess(List<String> cacheListener);
        void onError(Throwable e);
    }

    void getSearchCache();
    void storeSearchCache(List<String> query);
    void clearSearchCache();
    void setCompositeSubscription(CompositeSubscription compositeSubscription);
    void setListener(GetSearchCacheListener getSearchCacheListener);

}
