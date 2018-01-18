package com.tokopedia.mitratoppers.dashboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.mitratoppers.MitraToppersComponentInstance;
import com.tokopedia.mitratoppers.R;
import com.tokopedia.mitratoppers.common.data.source.cloud.api.MitraToppersApi;
import com.tokopedia.mitratoppers.common.di.component.DaggerMitraToppersComponent;
import com.tokopedia.mitratoppers.common.di.component.MitraToppersComponent;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TestFragment extends BaseDaggerFragment {

    @Inject
    MitraToppersApi mitraToppersApi;
    @Inject
    UserSession userSession;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initInjector() {
        MitraToppersComponent mitraToppersComponent = MitraToppersComponentInstance.get(
                (BaseMainApplication)getActivity().getApplication());
        mitraToppersComponent.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(getContext()).inflate(R.layout.test, container, false);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        mitraToppersApi.preApproveBalance(userSession.getShopId())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<Response<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("Test", "error");
            }

            @Override
            public void onNext(Response<String> stringResponse) {
                Log.i("Test", "success");
            }
        });
    }
}
