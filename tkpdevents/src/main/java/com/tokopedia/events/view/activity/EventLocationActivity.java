package com.tokopedia.events.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.design.text.SearchInputView;
import com.tokopedia.events.R;
import com.tokopedia.events.R2;
import com.tokopedia.events.di.DaggerEventComponent;
import com.tokopedia.events.di.EventComponent;
import com.tokopedia.events.di.EventModule;
import com.tokopedia.events.view.adapter.EventLocationAdapter;
import com.tokopedia.events.view.contractor.EventsLocationContract;
import com.tokopedia.events.view.presenter.EventLocationsPresenter;
import com.tokopedia.events.view.viewmodel.EventLocationViewModel;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventLocationActivity extends TActivity implements HasComponent<EventComponent>, SearchInputView.Listener, EventsLocationContract.View, EventLocationAdapter.ActionListener {
    protected static final long DEFAULT_DELAY_TEXT_CHANGED = TimeUnit.MILLISECONDS.toMillis(300);
    public static final String EXTRA_CALLBACK_LOCATION = "EXTRA_CALLBACK_LOCATION";

    @BindView(R2.id.search_input_view)
    SearchInputView searchInputView;
    @BindView(R2.id.recyclerview_city_List)
    RecyclerView recyclerview;

    EventComponent eventComponent;
    @Inject
    public EventLocationsPresenter mPresenter;

    private EventLocationAdapter eventLocationAdapter;
    private List<EventLocationViewModel> eventLocationViewModels;

    public static Intent getCallingIntent(Activity activity) {
        return new Intent(activity, EventLocationActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_location);
        initInjector();
        executeInjector();
        mPresenter.attachView(this);
        ButterKnife.bind(this);
        mPresenter.getLocationsListList();
        searchInputView.setListener(this);
        searchInputView.setDelayTextChanged(DEFAULT_DELAY_TEXT_CHANGED);


    }

    @Override
    public void onSearchSubmitted(String text) {
        Log.d("onSearchSubmitted", text);
    }

    @Override
    public void onSearchTextChanged(String text) {
        Log.d("onSearchTextChanged", text);
        filter(text);
    }

    @Override
    public void renderLocationList(List<EventLocationViewModel> eventLocationViewModels) {
        this.eventLocationViewModels = eventLocationViewModels;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        eventLocationAdapter = new EventLocationAdapter(this, eventLocationViewModels, this);
        recyclerview.setLayoutManager(linearLayoutManager);
        recyclerview.setAdapter(eventLocationAdapter);
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public RequestParams getParams() {
        return RequestParams.EMPTY;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public EventComponent getComponent() {
        if (eventComponent == null) initInjector();
        return eventComponent;
    }

    private void executeInjector() {
        if (eventComponent == null) initInjector();
        eventComponent.inject(this);
    }

    private void initInjector() {
        eventComponent = DaggerEventComponent.builder()
                .appComponent(getApplicationComponent())
                .eventModule(new EventModule(this))
                .build();
    }

    void filter(String text) {
        //update recyclerview
        eventLocationAdapter.updateList(eventLocationViewModels, text);
    }

    @Override
    public void onLocationItemSelected(EventLocationViewModel locationViewModel) {
        setResult(RESULT_OK, new Intent().putExtra(EXTRA_CALLBACK_LOCATION, locationViewModel));
        finish();
    }
}
