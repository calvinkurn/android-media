package com.tokopedia.events.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.events.R;
import com.tokopedia.events.di.EventComponent;
import com.tokopedia.events.di.DaggerEventComponent;
import com.tokopedia.events.domain.model.Event;
import com.tokopedia.events.view.contractor.EventsContract;
import com.tokopedia.events.view.presenter.EventHomePresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by ashwanityagi on 02/11/17.
 */
public class EventsHomeActivity extends BaseActivity  implements HasComponent<EventComponent>, EventsContract.View {

    private Unbinder unbinder;

    EventComponent eventComponent;

    @Inject
    public EventHomePresenter mPresenter;

    public static Intent getCallingIntent(Activity activity) {
        return new Intent(activity, EventsHomeActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_home);
        unbinder = ButterKnife.bind(this);
        initInjector();
        executeInjector();
        mPresenter.attachView(this);
        mPresenter.initialize();
        ButterKnife.bind(this);
        mPresenter.getEventsList();
    }

    private void executeInjector() {
        if (eventComponent == null) initInjector();
        eventComponent.inject(this);
        /*EventComponent component = DaggerEventComponent.builder()
                //.eventComponent(eventComponent)
                .build();
        component.inject(this);*/
    }

    private void initInjector() {
        eventComponent = DaggerEventComponent.builder()
                .appComponent(getApplicationComponent())
                .build();
    }

    @Override
    public EventComponent getComponent() {
        if (eventComponent == null) initInjector();
        return eventComponent;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public RequestParams getParams() {
        RequestParams requestParams = RequestParams.create();
        return requestParams;
    }

    @Override
    public void showMessage(String message) {

    }
}
