package com.tokopedia.events.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.tkpd.library.ui.widget.TouchViewPager;
import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.events.R;
import com.tokopedia.events.R2;
import com.tokopedia.events.di.DaggerEventComponent;
import com.tokopedia.events.di.EventComponent;
import com.tokopedia.events.view.adapter.CategoryFragmentPagerAdapter;
import com.tokopedia.events.view.adapter.CategoryTabsPagerAdapter;
import com.tokopedia.events.view.adapter.SlidingImageAdapter;
import com.tokopedia.events.view.contractor.EventsContract;
import com.tokopedia.events.view.customview.EventCategoryView;
import com.tokopedia.events.view.presenter.EventHomePresenter;
import com.tokopedia.events.view.utils.CirclePageIndicator;
import com.tokopedia.events.view.viewmodel.CategoryViewModel;
import com.tokopedia.events.view.viewmodel.EventLocationViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by ashwanityagi on 02/11/17.
 */
public class EventsHomeActivity extends TActivity implements HasComponent<EventComponent>, EventsContract.View {

    private Unbinder unbinder;
    public static final int REQUEST_CODE_EVENTLOCATIONACTIVITY = 101;

    EventComponent eventComponent;
    @Inject
    public EventHomePresenter mPresenter;

    //@BindView(R2.id.holder_category_list)
    //LinearLayout holderCategoryListLayout;
    @BindView(R2.id.event_bannerpager)
    TouchViewPager viewPager;
    @BindView(R2.id.pager_indicator)
    CirclePageIndicator tabLayout;

    @BindView(R2.id.category_view_pager)
    ViewPager categoryViewPager;
    @BindView(R2.id.tabs)
    TabLayout tabs;

    private SlidingImageAdapter adapter;

    public static Intent getCallingIntent(Activity activity) {
        return new Intent(activity, EventsHomeActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_home_new);
        unbinder = ButterKnife.bind(this);
        initInjector();
        executeInjector();
        mPresenter.attachView(this);
        ButterKnife.bind(this);
        mPresenter.getEventsList();
        setupToolbar();
    }


    private void executeInjector() {
        if (eventComponent == null) initInjector();
        eventComponent.inject(this);
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

//    @Override
//    protected void setupURIPass(Uri data) {
//
//    }
//
//    @Override
//    protected void setupBundlePass(Bundle extras) {
//
//    }
//
//    @Override
//    protected void initialPresenter() {
//
//    }
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.activity_events_home_new;
//    }
//
//    @Override
//    protected void initView() {
//
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_event_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_menu_location) {
            navigateToActivityRequest(EventLocationActivity.getCallingIntent(EventsHomeActivity.this), REQUEST_CODE_EVENTLOCATIONACTIVITY);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
//
//    @Override
//    protected void initVar() {
//        unbinder = ButterKnife.bind(this);
//        initInjector();
//        executeInjector();
//        mPresenter.attachView(this);
//        // mPresenter.initialize();
//        ButterKnife.bind(this);
//        mPresenter.getEventsList();
//    }

//    @Override
//    protected void setActionVar() {
//
//    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // invalidateTitleToolBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // invalidateTitleToolBar();
    }

//    private void invalidateTitleToolBar() {
//        String titleToolbar = getString(R.string.drawer_title_appshare);
//        if (!TextUtils.isEmpty(titleToolbar)) toolbar.setTitle(titleToolbar);
//    }

//    @Override
//    protected void setViewListener() {
//
//    }
//
//    @Override
//    protected boolean isLightToolbarThemes() {
//        return true;
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUEST_CODE_EVENTLOCATIONACTIVITY:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    EventLocationViewModel eventLocationViewModel = (EventLocationViewModel) data.getParcelableExtra(EventLocationActivity.EXTRA_CALLBACK_LOCATION);
                    mPresenter.getEventsListByLocation(eventLocationViewModel.getSearchName());
                }

                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void renderCategoryList(List<CategoryViewModel> categoryList) {
        // holderCategoryListLayout.removeAllViews();
        ArrayList<EventCategoryView> eventCategoryViews = new ArrayList<>();
        for (CategoryViewModel categoryViewModel : categoryList) {
            if (categoryViewModel.getItems() == null || categoryViewModel.getItems().size() == 0) {
                continue;
            }
            EventCategoryView eventCategoryView = new EventCategoryView(this);
            eventCategoryView.renderData(categoryViewModel.getItems(), categoryViewModel.getTitle());
            if ("carousel".equalsIgnoreCase(categoryViewModel.getName())) {
                adapter = new SlidingImageAdapter(EventsHomeActivity.this, mPresenter.getCarouselImages(categoryViewModel.getItems()));
                setViewPagerListener();
                tabLayout.setViewPager(viewPager);
            } else {

                eventCategoryViews.add(eventCategoryView);
                // holderCategoryListLayout.addView(eventCategoryView);
            }
        }

        CategoryFragmentPagerAdapter categoryTabsPagerAdapter = new CategoryFragmentPagerAdapter(getSupportFragmentManager(), categoryList);
        categoryViewPager.setAdapter(categoryTabsPagerAdapter);
        tabs.setupWithViewPager(categoryViewPager);
        categoryViewPager.setCurrentItem(0);
        categoryViewPager.setSaveFromParentEnabled(false);
//        SlidingCategoryAdapter eventCategoryAdapter=new SlidingCategoryAdapter(EventsHomeActivity.this,eventCategoryViews);
//        categoryViewPager.setAdapter(eventCategoryAdapter);
//        categoryTabLayout.setupWithViewPager(categoryViewPager, true);


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
        //viewPager.setCurrentItem(0);
    }

    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }
}
