package com.tokopedia.inbox.inboxmessageold.interactor;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.inbox.inboxmessageold.model.inboxmessage.InboxMessage;
import com.tokopedia.inbox.inboxmessageold.model.inboxmessagedetail.InboxMessageDetail;
import com.tokopedia.inbox.inboxmessageold.util.InboxMessageCacheManager;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Nisie on 5/10/16.
 */
public class InboxMessageCacheInteractorImpl implements InboxMessageCacheInteractor{

    private static String TAG = "CacheInboxMessage";
    private static final String CACHE_INBOX_MESSAGE = "CACHE_INBOX_MESSAGE";

    @Override
    public void getInboxMessageCache(String nav, final GetInboxMessageCacheListener listener) {
        Observable.just(CACHE_INBOX_MESSAGE + "_" + nav)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<String, InboxMessage>() {
                    @Override
                    public InboxMessage call(String s) {
                        GlobalCacheManager cache = new GlobalCacheManager();
                        return convertToInboxMessage(cache.getValueString(s));
                    }
                })
                .subscribe(new Subscriber<InboxMessage>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                        listener.onError(e);
                    }

                    @Override
                    public void onNext(InboxMessage inboxReputation) {
                        Log.i(TAG, "Get The Cache!! " + inboxReputation.toString());
                        listener.onSuccess(inboxReputation);
                    }

                });
    }

    private InboxMessage convertToInboxMessage(String response) {
        if (response != null) {
            return new Gson().fromJson(response, InboxMessage.class);
        } else {
            throw new RuntimeException("Cache doesn't exist");
        }
    }

    @Override
    public void setInboxMessageCache(String nav, InboxMessage inboxMessage) {
        GlobalCacheManager cache = new GlobalCacheManager();
        cache.setKey(CACHE_INBOX_MESSAGE + "_" + nav);
        cache.setValue(CacheUtil.convertModelToString(inboxMessage,
                new TypeToken<InboxMessage>() {
                }.getType()));
        cache.setCacheDuration(300);
        cache.store();
        Log.i(TAG, "End of storing the cache.....");
    }

    @Override
    public void getInboxMessageDetailCache(String messageId, final GetInboxMessageDetailCacheListener listener) {
        Observable.just(messageId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<String, InboxMessageDetail>() {
                    @Override
                    public InboxMessageDetail call(String messageId) {
                        InboxMessageCacheManager cache = new InboxMessageCacheManager();
                        return convertToInboxMessageDetail(cache.getCache(messageId));
                    }
                })
                .subscribe(new Subscriber<InboxMessageDetail>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                        listener.onError(e);
                    }

                    @Override
                    public void onNext(InboxMessageDetail inboxMessageDetail) {
                        Log.i(TAG, "Get The Cache!! " + inboxMessageDetail.toString());
                        listener.onSuccess(inboxMessageDetail);
                    }

                });
    }

    private InboxMessageDetail convertToInboxMessageDetail(String cache) {
        if (cache != null) {
            return new Gson().fromJson(cache, InboxMessageDetail.class);
        } else {
            throw new RuntimeException("Cache doesn't exist");
        }
    }

    @Override
    public void setInboxMessageDetailCache(String messageId, InboxMessageDetail response) {
        InboxMessageCacheManager cache = new InboxMessageCacheManager();
        cache.setMessageId(messageId);
        cache.setData(CacheUtil.convertModelToString(response,
                new TypeToken<InboxMessageDetail>() {
                }.getType()));
        cache.setCacheDuration(300);
        Observable.just(cache)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<InboxMessageCacheManager, InboxMessageCacheManager>() {
                    @Override
                    public InboxMessageCacheManager call(InboxMessageCacheManager inboxMessageCacheManager) {
                        inboxMessageCacheManager.save();
                        return inboxMessageCacheManager;
                    }
                })
                .subscribe(new Subscriber<InboxMessageCacheManager>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(InboxMessageCacheManager inboxMessageCacheManager) {

                    }
                });
    }

    @Override
    public void deleteCache() {

    }
}
