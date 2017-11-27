package com.tokopedia.discovery.newdynamicfilter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.discovery.model.Filter;
import com.tokopedia.core.discovery.model.Option;
import com.tokopedia.discovery.newdynamicfilter.helper.DynamicFilterDbManager;
import com.tokopedia.discovery.newdynamicfilter.helper.FilterDbHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by henrypriyono on 11/24/17.
 */

public class DynamicFilterLocationActivity extends DynamicFilterDetailGeneralActivity {

    public static final int REQUEST_CODE = 222;

    @Override
    protected Observable<Boolean> retrieveOptionListData() {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                optionList = FilterDbHelper.loadLocationFilterOptions();
                subscriber.onNext(true);
            }
        });
    }

    public static void moveTo(AppCompatActivity activity,
                              String pageTitle,
                              boolean isSearchable,
                              String searchHint) {

        if (activity != null) {
            Intent intent = new Intent(activity, DynamicFilterLocationActivity.class);
            intent.putExtra(EXTRA_PAGE_TITLE, pageTitle);
            intent.putExtra(EXTRA_IS_SEARCHABLE, isSearchable);
            intent.putExtra(EXTRA_SEARCH_HINT, searchHint);
            activity.startActivityForResult(intent, REQUEST_CODE);
        }
    }

    @Override
    protected void applyFilter() {
        showLoading();
        Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                FilterDbHelper.storeLocationFilterOptions(optionList);
                subscriber.onNext(true);
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean aBoolean) {
                hideLoading();
                setResult(RESULT_OK);
                finish();
            }
        });
    }
}
