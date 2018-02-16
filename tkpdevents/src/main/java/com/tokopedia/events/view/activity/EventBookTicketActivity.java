package com.tokopedia.events.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.events.R;
import com.tokopedia.events.R2;
import com.tokopedia.events.di.DaggerEventComponent;
import com.tokopedia.events.di.EventComponent;
import com.tokopedia.events.di.EventModule;
import com.tokopedia.events.view.contractor.EventBookTicketContract;
import com.tokopedia.events.view.fragment.FragmentAddTickets;
import com.tokopedia.events.view.presenter.EventBookTicketPresenter;
import com.tokopedia.events.view.utils.CalendarItemHolder;
import com.tokopedia.events.view.utils.CurrencyUtil;
import com.tokopedia.events.view.utils.ImageTextViewHolder;
import com.tokopedia.events.view.utils.Utils;
import com.tokopedia.events.view.viewmodel.EventsDetailsViewModel;
import com.tokopedia.events.view.viewmodel.SchedulesViewModel;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventBookTicketActivity
        extends TActivity implements EventBookTicketContract.EventBookTicketView {


    //    @BindView(R2.id.collasing_toolbar)
//    CollapsingToolbarLayout collasingToolbar;
    @BindView(R2.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R2.id.pay_tickets)
    View buttonPayTickets;
    @BindView(R2.id.button_textview)
    TextView buttonTextview;
    @BindView(R2.id.viewpager_book_ticket)
    ViewPager bookTicketViewPager;
    @BindView(R2.id.progress_bar_layout)
    View progressBarLayout;
    @BindView(R2.id.prog_bar)
    ProgressBar progBar;
    @BindView(R2.id.app_bar)
    Toolbar appBar;
    @BindView(R2.id.imgv_seating_layout)
    ImageView imgvSeatingLayout;
    @BindView(R2.id.main_content)
    FrameLayout mainContent;
    @BindView(R2.id.seating_layout_card)
    View seatMap;
    @BindView(R2.id.ticketcount)
    TextView ticketCount;
    @BindView(R2.id.totalprice)
    TextView totalPrice;
    @BindView(R2.id.button_count_layout)
    View buttonCountLayout;
//    @BindView(R2.id.event_address)
//    View addressView;
//    @BindView(R2.id.event_time)
//    View timeView;

//    @BindView(R2.id.banner_image)
//    ImageView bannerImage;

//    ImageTextViewHolder addressHolder;
//    ImageTextViewHolder timeHolder;

    private EventComponent eventComponent;
    @Inject
    EventBookTicketPresenter mPresenter;
    private String title;

    private static final int EVENT_LOGIN_REQUEST = 1099;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_ticket_layout);
        ButterKnife.bind(this);

//        addressHolder = new ImageTextViewHolder();
//        timeHolder = new ImageTextViewHolder();

//        ButterKnife.bind(addressHolder, addressView);
//        ButterKnife.bind(timeHolder, timeView);


//        collasingToolbar.setExpandedTitleColor(Color.parseColor("#00ffffff"));
//        collasingToolbar.setCollapsedTitleTextColor(Color.parseColor("#ff000000"));

        executeInjector();

        mPresenter.attachView(this);

        mPresenter.getTicketDetails();
//        setupToolbar();
        appBar.setTitle(R.string.book_ticket_title);
        setSupportActionBar(appBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
//        toolbar.setTitle("Events");

    }


    @Override
    public void showMessage(String message) {

    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {
        hideProgressBar();
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void renderFromDetails(EventsDetailsViewModel detailsViewModel) {
        appBar.setTitle(detailsViewModel.getTitle());
        appBar.setNavigationIcon(R.drawable.ic_arrow_back_black);
        title = detailsViewModel.getTitle();
//        ImageHandler.loadImageCover2(bannerImage, detailsViewModel.getImageApp());
//        setHolder(R.drawable.ic_time, detailsViewModel.getTimeRange(), timeHolder);
//        setHolder(R.drawable.ic_skyline, detailsViewModel.getSchedulesViewModels().get(0).getaDdress(), addressHolder);

        AddTicketFragmentAdapter fragmentAdapter = new AddTicketFragmentAdapter(getSupportFragmentManager(),
                detailsViewModel.getSchedulesViewModels());
        bookTicketViewPager.setAdapter(fragmentAdapter);
        bookTicketViewPager.addOnPageChangeListener(new PageChangeListener());
        tabLayout.setVisibility(View.GONE);
    }

    @Override
    public RequestParams getParams() {
        return null;
    }

    @Override
    public void setHolder(int resID, String label, ImageTextViewHolder holder) {
        holder.setImage(resID);
        holder.setImageTint(getResources().getColor(R.color.white));
        holder.setTextView(label);
        holder.setTextColor(getResources().getColor(R.color.white));
        holder.setTextSize(16);
    }

    @Override
    public void showPayButton(int ticketQuantity, int price, String type) {
        totalPrice.setText(String.format(getString(R.string.price_holder),
                CurrencyUtil.convertToCurrencyString(ticketQuantity * price)));
        ticketCount.setText(String.format(getString(R.string.x_type), ticketQuantity, type));
        buttonPayTickets.setVisibility(View.VISIBLE);
        buttonPayTickets.setBackgroundColor(getResources().getColor(R.color.white));
        if (buttonCountLayout.getVisibility() != View.VISIBLE)
            buttonCountLayout.setVisibility(View.VISIBLE);
//        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 116.0f, getResources().getDisplayMetrics());
//        ViewGroup.LayoutParams params = bookTicketViewPager.getLayoutParams();
//        params.
//
//        bookTicketViewPager.(0, 0, 0, px);
    }

    @Override
    public void hidePayButton() {
        buttonCountLayout.setVisibility(View.INVISIBLE);
        buttonPayTickets.setVisibility(View.INVISIBLE);
//        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics());
//        bookTicketViewPager.setPadding(0, 0, 0, px);
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
    public void initTablayout() {

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View tV = tab.getCustomView();
                if (tV != null) {
                    CalendarItemHolder holder = (CalendarItemHolder) tV.getTag();
                    String[] date = Utils.getDateArray(mPresenter.getDateArray());
                    holder.setTvMonth(date[2]);
                    holder.setTvDate(date[1]);
                    holder.setTvDay(date[0]);
                    tV.setBackgroundResource(R.drawable.rounded_rectangle_green_solid);
                } else {
                    tab.setCustomView(R.layout.calendar_item);
                    tV = tab.getCustomView();
                    CalendarItemHolder holder = new CalendarItemHolder();
                    ButterKnife.bind(holder, tV);
                    tV.setTag(holder);
                    String[] date = Utils.getDateArray(mPresenter.getDateArray());
                    holder.setTvMonth(date[2]);
                    holder.setTvDate(date[1]);
                    holder.setTvDay(date[0]);
                    tV.setBackgroundResource(R.drawable.rounded_rectangle_green_solid);
                }
                bookTicketViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View tV = tab.getCustomView();
                tV.setBackgroundResource(R.drawable.rounded_rectangle_grey_solid);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                View tV = tab.getCustomView();
                CalendarItemHolder holder = (CalendarItemHolder) tV.getTag();
                String[] date = Utils.getDateArray(mPresenter.getDateArray());
                holder.setTvMonth(date[2]);
                holder.setTvDate(date[1]);
                holder.setTvDay(date[0]);
                tV.setBackgroundResource(R.drawable.rounded_rectangle_green_solid);
                bookTicketViewPager.setCurrentItem(tab.getPosition());
            }
        });

        tabLayout.setupWithViewPager(bookTicketViewPager);

        for (int i = 1; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            View customView = tab.setCustomView(R.layout.calendar_item).getCustomView();
            CalendarItemHolder holder = new CalendarItemHolder();
            ButterKnife.bind(holder, customView);
            customView.setTag(holder);
        }

        tabLayout.setSelectedTabIndicatorHeight(0);

        tabLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void renderSeatmap(String url) {
        ImageHandler.loadImageCover2(imgvSeatingLayout, url);
    }

    @Override
    public void hideSeatmap() {
        seatMap.setVisibility(View.GONE);
    }

    @Override
    public View getRootView() {
        return mainContent;
    }

    @Override
    public int getButtonLayoutHeight() {
        return buttonCountLayout.getHeight();
    }

    @Override
    public int getRequestCode() {
        return EVENT_LOGIN_REQUEST;
    }

    public class AddTicketFragmentAdapter extends FragmentStatePagerAdapter {

        List<SchedulesViewModel> mScheduleList;

        public AddTicketFragmentAdapter(FragmentManager manager, List<SchedulesViewModel> scheduleList) {
            super(manager);
            this.mScheduleList = scheduleList;
        }

        @Override
        public Fragment getItem(int position) {
            FragmentAddTickets fragmentAddTickets = FragmentAddTickets.
                    newInstance(mScheduleList.get(position).getPackages().size());
            fragmentAddTickets.setData(mScheduleList.get(position).getPackages(), mPresenter);
            return fragmentAddTickets;
        }

        @Override
        public int getCount() {
            return mScheduleList.size();
        }
    }


    public class PageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            mPresenter.onPageChange(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    @Override
    protected void onResume() {
//        bookTicketViewPager.setCurrentItem(1);
//        bookTicketViewPager.setCurrentItem(0);
        if (tabLayout.getVisibility() == View.VISIBLE)
            tabLayout.getTabAt(0).select();
        super.onResume();
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

    @OnClick(R2.id.pay_tickets)
    void payTickets() {
        mPresenter.payTicketsClick(title);
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.onActivityResult(requestCode);
    }
}
