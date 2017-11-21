package com.tokopedia.inbox.inboxtalk.interactor;

import android.util.Log;

import com.tokopedia.core.talk.cache.database.InboxTalkCacheManager;
import com.tokopedia.inbox.inboxtalk.presenter.InboxTalkPresenterImpl;

import org.json.JSONException;
import org.json.JSONObject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by stevenfredian on 5/17/16.
 */
public class InboxTalkCacheInteractorImpl implements InboxTalkCacheInteractor {

    InboxTalkPresenterImpl presenter;
    CompositeSubscription subscription;

    public static InboxTalkCacheInteractor createInstance(InboxTalkPresenterImpl inboxTalkPresenter) {
        InboxTalkCacheInteractorImpl facade = new InboxTalkCacheInteractorImpl();
        facade.presenter = inboxTalkPresenter;
        facade.subscription = new CompositeSubscription();
        return facade;
    }

    @Override
    public void storeFirstPage(String nav, String filter, JSONObject result) {
        InboxTalkCacheManager cache = new InboxTalkCacheManager();
        cache.setNav(nav, filter);
        cache.setResult(result);
        cache.setCacheDuration(300);

        subscription.add(Observable.just(cache)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<InboxTalkCacheManager, InboxTalkCacheManager>() {
                    @Override
                    public InboxTalkCacheManager call(InboxTalkCacheManager inboxTalkCacheManager) {
                        inboxTalkCacheManager.saveCache();
                        return inboxTalkCacheManager;
                    }
                })
                .subscribe(new Subscriber<InboxTalkCacheManager>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(InboxTalkCacheManager inboxTalkCacheManager) {
                    }
                })
        );
    }

    @Override
    public void getInboxTalkFromCache(final String nav, final InboxTalkCacheInteractor.GetInboxTalkListener listener) {
        subscription.add(Observable.just(nav)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<String, JSONObject>() {
                    @Override
                    public JSONObject call(String s) {
                        InboxTalkCacheManager cache = new InboxTalkCacheManager();
                        try {
                            return cache.getCache(nav).getJson();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                })
                .subscribe(new Subscriber<JSONObject>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(JSONObject jsonObject) {
                        if (jsonObject != null)
                            Log.i("steven", "Get The Cache!! " + jsonObject.toString());
                        listener.onSuccess(jsonObject);
                    }
                })
        );
    }

    @Override
    public void unsubscribe() {
        this.subscription.unsubscribe();
    }
}
