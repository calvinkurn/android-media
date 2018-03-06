package com.tokopedia.events.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AlignmentSpan;
import android.text.style.StyleSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tkpd.library.ui.widget.TouchViewPager;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.events.R;
import com.tokopedia.events.R2;
import com.tokopedia.events.di.DaggerEventComponent;
import com.tokopedia.events.di.EventComponent;
import com.tokopedia.events.di.EventModule;
import com.tokopedia.events.view.adapter.CategoryFragmentPagerAdapter;
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
public class EventsHomeActivity extends TActivity
        implements HasComponent<EventComponent>,
        EventsContract.View {

    private Unbinder unbinder;
    public static final int REQUEST_CODE_EVENTLOCATIONACTIVITY = 101;
    public static final int REQUEST_CODE_EVENTSEARCHACTIVITY = 901;

    private Menu mMenu;


    EventComponent eventComponent;
    @Inject
    public EventHomePresenter mPresenter;

    @BindView(R2.id.event_bannerpager)
    TouchViewPager viewPager;
    @BindView(R2.id.pager_indicator)
    CirclePageIndicator tabLayout;

    @BindView(R2.id.category_view_pager)
    ViewPager categoryViewPager;
    @BindView(R2.id.tabs)
    TabLayout tabs;
    @BindView(R2.id.main_content)
    FrameLayout mainContent;

    @BindView(R2.id.progress_bar_layout)
    View progressBarLayout;
    @BindView(R2.id.prog_bar)
    ProgressBar progBar;
    @BindView(R2.id.indicator_promo_layout)
    View indicatorLayout;


    int mBannnerPos;
    int defaultViewPagerPos;

    public final static String EXTRA_SECTION = "extra_section";

    private SlidingImageAdapter adapter;

    @DeepLink({Constants.Applinks.EVENTS, Constants.Applinks.EVENTS_ACTIVITIES})
    public static Intent getCallingApplinksTaskStask(Context context, Bundle extras) {
        String deepLink = extras.getString(DeepLink.URI);
        Uri.Builder uri = Uri.parse(deepLink).buildUpon();
        Intent destination = new Intent(context, EventsHomeActivity.class)
                .setData(uri.build())
                .putExtras(extras);
        destination.putExtra(Constants.EXTRA_FROM_PUSH, true);
        if (Constants.Applinks.EVENTS.equals(deepLink)) {
            destination.putExtra(EXTRA_SECTION, 0);
        } else if (Constants.Applinks.EVENTS_ACTIVITIES.equals(deepLink)) {
            destination.putExtra(EXTRA_SECTION, 1);
        }
        return destination;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_home_new);
        defaultViewPagerPos = getIntent().getIntExtra(EXTRA_SECTION, 0);
        unbinder = ButterKnife.bind(this);
        initInjector();
        executeInjector();
        mPresenter.attachView(this);
        ButterKnife.bind(this);
        mPresenter.getEventsList();
        setupToolbar();
        toolbar.setTitle("Events");
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
    public View getRootView() {
        return mainContent;
    }

    @Override
    public void showProgressBar() {
        progBar.setVisibility(View.VISIBLE);
        progressBarLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progBar.setVisibility(View.GONE);
        progressBarLayout.setVisibility(View.GONE);
    }

    @Override
    public void hideSearchButton() {
        MenuItem item = mMenu.findItem(R.id.action_menu_search);
        item.setVisible(false);
        item.setEnabled(false);
    }

    @Override
    public void showSearchButton() {
        MenuItem item = mMenu.findItem(R.id.action_menu_search);
        item.setVisible(true);
        item.setEnabled(true);
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_event_home, menu);
        mMenu = menu;
        for (int i = 0; i < mMenu.size(); i++) {
            MenuItem item = menu.getItem(i);
            SpannableString s = new SpannableString(item.getTitle());
            s.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, s.length(), 0);
            s.setSpan(new StyleSpan(Typeface.NORMAL), 0, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            item.setTitle(s);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return mPresenter.onOptionMenuClick(id);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUEST_CODE_EVENTLOCATIONACTIVITY:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    EventLocationViewModel eventLocationViewModel = (EventLocationViewModel) data.getParcelableExtra(EventLocationActivity.EXTRA_CALLBACK_LOCATION);
                    mPresenter.getEventsListByLocation(eventLocationViewModel.getSearchName());
                }

                break;
            case REQUEST_CODE_EVENTSEARCHACTIVITY:
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void renderCategoryList(List<CategoryViewModel> categoryList) {
        ArrayList<EventCategoryView> eventCategoryViews = new ArrayList<>();
        for (CategoryViewModel categoryViewModel : categoryList) {
            if (categoryViewModel.getItems() == null || categoryViewModel.getItems().size() == 0) {
                continue;
            }
            if ("carousel".equalsIgnoreCase(categoryViewModel.getName())) {
                adapter = new SlidingImageAdapter(EventsHomeActivity.this, mPresenter.getCarouselImages(categoryViewModel.getItems()), mPresenter);
                setViewPagerListener();
                tabLayout.setViewPager(viewPager);
                mPresenter.startBannerSlide(viewPager);
            }
        }

        CategoryFragmentPagerAdapter categoryTabsPagerAdapter =
                new CategoryFragmentPagerAdapter(getSupportFragmentManager(), categoryList);
        categoryViewPager.setAdapter(categoryTabsPagerAdapter);
        tabs.setupWithViewPager(categoryViewPager);
        categoryViewPager.setCurrentItem(defaultViewPagerPos);
        categoryViewPager.setSaveFromParentEnabled(false);
        indicatorLayout.setVisibility(View.VISIBLE);
    }


    private void setViewPagerListener() {

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                mPresenter.onBannerSlide(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });

        viewPager.setAdapter(adapter);
    }

    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
