package com.tokopedia.movies.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ProgressBar;

import com.tkpd.library.ui.widget.TouchViewPager;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.movies.R;
import com.tokopedia.movies.R2;
import com.tokopedia.movies.di.DaggerEventComponent;
import com.tokopedia.movies.di.EventComponent;
import com.tokopedia.movies.di.EventModule;
import com.tokopedia.movies.view.adapter.CategoryFragmentPagerAdapter;
import com.tokopedia.movies.view.adapter.SlidingImageAdapter;
import com.tokopedia.movies.view.contractor.EventsContract;
import com.tokopedia.movies.view.customview.EventCategoryView;
import com.tokopedia.movies.view.customview.SearchInputView;
import com.tokopedia.movies.view.presenter.EventHomePresenter;
import com.tokopedia.movies.view.utils.CirclePageIndicator;
import com.tokopedia.movies.view.viewmodel.CategoryViewModel;
import com.tokopedia.movies.view.viewmodel.EventLocationViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by ashwanityagi on 02/11/17.
 */
public class MoviesHomeActivity extends TActivity implements HasComponent<EventComponent>, EventsContract.View, SearchInputView.Listener {

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
    @BindView(R2.id.progress_bar_layout)
    View progressBarLayout;
    @BindView(R2.id.prog_bar)
    ProgressBar progBar;

    private SlidingImageAdapter adapter;

    public static Intent getCallingIntent(Activity activity) {
        return new Intent(activity, com.tokopedia.movies.view.activity.MoviesHomeActivity.class);
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
        toolbar.setTitle("Movies");
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
//        return R.layout.activity_movies_home_new;
//    }
//
//    @Override
//    protected void initView() {
//
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_event_home, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_menu_location) {
//            navigateToActivityRequest(EventLocationActivity.getCallingIntent(MoviesHomeActivity.this), REQUEST_CODE_EVENTLOCATIONACTIVITY);
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
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
           // EventCategoryView eventCategoryView = new EventCategoryView(this);
            //eventCategoryView.renderData(categoryViewModel.getItems(), categoryViewModel.getTitle());
            if ("default".equalsIgnoreCase(categoryViewModel.getName())) {
                adapter = new SlidingImageAdapter(com.tokopedia.movies.view.activity.MoviesHomeActivity.this, mPresenter.getCarouselImages(categoryViewModel.getItems()), categoryViewModel.getItems());
                setViewPagerListener();
                tabLayout.setViewPager(viewPager);
            } else {

               // eventCategoryViews.add(eventCategoryView);
                // holderCategoryListLayout.addView(eventCategoryView);
            }
        }

        CategoryFragmentPagerAdapter categoryTabsPagerAdapter = new CategoryFragmentPagerAdapter(getSupportFragmentManager(), categoryList);
        categoryViewPager.setAdapter(categoryTabsPagerAdapter);
        tabs.setupWithViewPager(categoryViewPager);
        categoryViewPager.setCurrentItem(0);
        categoryViewPager.setSaveFromParentEnabled(false);
//        SlidingCategoryAdapter eventCategoryAdapter=new SlidingCategoryAdapter(MoviesHomeActivity.this,eventCategoryViews);
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

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

//    @Override
//    protected void setupToolbar() {
//        toolbar = (Toolbar) findViewById(R.id.app_bar);
//        toolbar.removeAllViews();
//        View view = getLayoutInflater().inflate(R.layout.custom_actionbar_search_view, null);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        view.setLayoutParams(params);
//        TextView tvCari = (TextView) view.findViewById(R.id.tv_cari);
//        final SearchInputView searchInputView = (SearchInputView) view.findViewById(R.id.search_input_view);
//        searchInputView.setPadding(0,0,0,0);
//        searchInputView.setListener(this);
//        searchInputView.setDelayTextChanged(DEFAULT_DELAY_TEXT_CHANGED);
//        tvCari.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String searchText = searchInputView.getSearchText();
//                mPresenter.getEventsListBySearch(searchText);
//            }
//        });
//        toolbar.addView(view);
//        if (isLightToolbarThemes()) {
//            setLightToolbarStyle();
//        }
//        setSupportActionBar(toolbar);
//    }

    @Override
    public void onSearchTextChanged(String text) {

    }

    @Override
    public void onSearchSubmitted(String text) {

    }

}
