package com.tokopedia.inbox.rescenter.inbox.facade;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.inbox.rescenter.inbox.interactor.RetrofitInteractor;
import com.tokopedia.inbox.rescenter.inbox.interactor.RetrofitInteractor.ResCenterInboxListener;
import com.tokopedia.inbox.rescenter.inbox.interactor.RetrofitInteractorImpl;
import com.tokopedia.inbox.rescenter.inbox.model.ResCenterInboxData;
import com.tokopedia.inbox.rescenter.inbox.presenter.InboxResCenterPresenter;

import java.lang.reflect.Type;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created on 4/7/16.
 */
public class FacadeImpl implements Facade {

    private static final String TAG = FacadeImpl.class.getSimpleName();

    private final InboxResCenterPresenter presenter;
    private final RetrofitInteractor retrofit;

    public FacadeImpl(InboxResCenterPresenter presenter) {
        this.presenter = presenter;
        this.retrofit = new RetrofitInteractorImpl();
    }

    @Override
    public void initInboxData(Activity activity, Map<String, String> params) {
        requestData(activity, params, new ResCenterInboxListener() {
            @Override
            public void onPreExecute(Map<String, String> params) {
                presenter.setAllowConnection(false);
                presenter.setBeforeInitInboxData();
//                getCacheResCenterInbox(generateCacheKey(params));
            }

            @Override
            public void onSuccess(Map<String, String> params, ResCenterInboxData data) {
                setCacheResCenterInbox(generateCacheKey(params), data);
                presenter.setOnSuccessInitInboxData(data);
                presenter.setAllowConnection(true);
                presenter.setOnRequestSuccess();
                presenter.finishRequest();
            }

            @Override
            public void onTimeout(NetworkErrorHelper.RetryClickedListener listener) {
                presenter.finishRequest();
                presenter.setOnResponseTimeOut(listener);
            }

            @Override
            public void onError(String message) {
                presenter.setAllowConnection(true);
                presenter.setOnRequestSuccess();
                presenter.finishRequest();
                presenter.setOnRequestError(message);
            }

            @Override
            public void onNullData() {
                presenter.setAllowConnection(true);
                presenter.setOnRequestSuccess();
                presenter.finishRequest();
                presenter.setOnResponseNull();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private String generateCacheKey(Map<String, String> params) {
        return "as=" + params.get("as") +
                "&page=" + params.get("page") +
                "&sort_type=" + params.get("sort_type") +
                "&status=" + params.get("status") +
                "&unread=" + params.get("unread") +
                "&user_id=" + params.get("user_id");
    }

    private void getCacheResCenterInbox(String cacheKey) {
        Observable.just(cacheKey)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<String, ResCenterInboxData>() {
                    @Override
                    public ResCenterInboxData call(String key) {
                        // initialize local variable CacheManager
                        GlobalCacheManager cacheManager = new GlobalCacheManager();

                        // initialize class you want to be converted from string
                        Type type = new TypeToken<ResCenterInboxData>() {}.getType();

                        // get json string which already cached
                        String jsonCachedString = cacheManager.getValueString(key);

                        return CacheUtil.convertStringToModel(jsonCachedString, type);
                    }
                })
                .subscribe(new Subscriber<ResCenterInboxData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                        presenter.onErrorGetCache();
                    }

                    @Override
                    public void onNext(ResCenterInboxData resCenterInboxData) {
                        if (resCenterInboxData != null) {
                            presenter.onSuccessGetCache(resCenterInboxData);
                        }
                    }
                });
    }

    private void setCacheResCenterInbox(String cacheKey, ResCenterInboxData dataValue) {
        Observable.zip(
                Observable.just(cacheKey),
                Observable.just(dataValue),
                new Func2<String, ResCenterInboxData, Boolean>() {
                    @Override
                    public Boolean call(String cacheKey, ResCenterInboxData cacheData) {
                        // initialize local variable CacheManager
                        GlobalCacheManager cacheManager = new GlobalCacheManager();

                        // initialize class you want to be converted from string
                        Type type = new TypeToken<ResCenterInboxData>() {}.getType();

                        // set value
                        cacheManager.setKey(cacheKey);
                        cacheManager.setValue(CacheUtil.convertModelToString(cacheData, type));
                        cacheManager.setCacheDuration(5000);
                        cacheManager.store();

                        return true;
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        Log.e(TAG, "storing cache success");
                    }
                });
    }

    private void requestData(@NonNull Context context,
                             @NonNull Map<String, String> params,
                             @NonNull RetrofitInteractor.ResCenterInboxListener listener) {
        retrofit.getResCenterInbox(context, params, listener);
    }

    @Override
    public void loadMoreInboxData(Activity activity, Map<String, String> params) {
        Log.d(TAG + "hang", "param: " + params.entrySet());
        requestData(activity, params, new ResCenterInboxListener() {

            @Override
            public void onPreExecute(Map<String, String> params) {
                presenter.setAllowConnection(false);
                presenter.setBeforeLoadMoreData();
            }

            @Override
            public void onSuccess(Map<String, String> params, ResCenterInboxData data) {
                presenter.setOnSuccessLoadMoreData(data);
                presenter.setAllowConnection(true);
                presenter.setOnRequestSuccess();
            }

            @Override
            public void onTimeout(NetworkErrorHelper.RetryClickedListener listener) {
                presenter.setOnResponseTimeOut(listener);
            }

            @Override
            public void onError(String message) {
                presenter.setOnRequestError(message);
                presenter.setOnRequestSuccess();
            }

            @Override
            public void onNullData() {
                presenter.setOnRequestError(null);
                presenter.setAllowConnection(true);
                presenter.setOnRequestSuccess();
            }

            @Override
            public void onComplete() {
                presenter.finishRequest();
            }
        });
    }

    @Override
    public void forceFinish() {
        retrofit.unsubscribe();
    }
}
