package com.tokopedia.events.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.widget.LinearLayout;

import com.tkpd.library.ui.widget.TouchViewPager;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.events.R;
import com.tokopedia.events.R2;
import com.tokopedia.events.di.DaggerEventComponent;
import com.tokopedia.events.di.EventComponent;
import com.tokopedia.events.view.adapter.SlidingImageAdapter;
import com.tokopedia.events.view.contractor.EventsContract;
import com.tokopedia.events.view.customview.EventCategoryView;
import com.tokopedia.events.view.presenter.EventHomePresenter;
import com.tokopedia.events.view.viewmodel.CategoryViewModel;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by ashwanityagi on 02/11/17.
 */
public class EventsHomeActivity extends BasePresenterActivity implements HasComponent<EventComponent>, EventsContract.View {

    private Unbinder unbinder;

    EventComponent eventComponent;
    @Inject
    public EventHomePresenter mPresenter;

    @BindView(R2.id.holder_category_list)
    LinearLayout holderCategoryListLayout;
    @BindView(R2.id.event_bannerpager)
    TouchViewPager viewPager;
    @BindView(R2.id.tab_layout)
    TabLayout tabLayout;


    private SlidingImageAdapter adapter;

    public static Intent getCallingIntent(Activity activity) {
        return new Intent(activity, EventsHomeActivity.class);
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
        return RequestParams.EMPTY;
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {
        mPresenter.attachView(this);
        mPresenter.initialize();
        mPresenter.getEventsList();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_events_home;
    }

    @Override
    protected void initView() {

    }


    @Override
    protected void initVar() {
        unbinder = ButterKnife.bind(this);
        initInjector();
        executeInjector();
        ButterKnife.bind(this);
    }

    @Override
    protected void setActionVar() {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        invalidateTitleToolBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateTitleToolBar();
    }

    private void invalidateTitleToolBar() {
        String titleToolbar = getString(R.string.drawer_title_appshare);
        if (!TextUtils.isEmpty(titleToolbar)) toolbar.setTitle(titleToolbar);
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    public void renderCategoryList(List<CategoryViewModel> categoryList) {
        holderCategoryListLayout.removeAllViews();
        for (CategoryViewModel categoryViewModel : categoryList
                ) {
            EventCategoryView eventCategoryView = new EventCategoryView(this);
            eventCategoryView.renderData(categoryViewModel.getItems(), categoryViewModel.getTitle());
            if ("carousel".equalsIgnoreCase(categoryViewModel.getName())) {
                adapter = new SlidingImageAdapter(EventsHomeActivity.this, mPresenter.getCarouselImages(categoryViewModel.getItems()));
                setViewPagerListener();
                tabLayout.setupWithViewPager(viewPager, true);
            } else {
                holderCategoryListLayout.addView(eventCategoryView);
            }
        }

    }



    private void setViewPagerListener() {

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
//                if (arg0 != lastPos) {
//                    lastPos = arg0;
//                }
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
    }

}
