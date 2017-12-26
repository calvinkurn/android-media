package com.tokopedia.events.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.tokopedia.events.view.utils.CurrencyUtil;
import com.tokopedia.events.view.utils.ImageTextViewHolder;
import com.tokopedia.events.view.viewmodel.EventsDetailsViewModel;
import com.tokopedia.events.view.viewmodel.SchedulesViewModel;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventBookTicketActivity extends TActivity implements EventBookTicketContract.EventBookTicketView {


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
//    @BindView(R2.id.event_address)
//    View addressView;
//    @BindView(R2.id.event_time)
//    View timeView;

//    @BindView(R2.id.banner_image)
//    ImageView bannerImage;

//    ImageTextViewHolder addressHolder;
//    ImageTextViewHolder timeHolder;

    EventComponent eventComponent;
    @Inject
    EventBookTicketPresenter mPresenter;


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
        setupToolbar();
        toolbar.setTitle("Events");

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
        startActivity(intent);
    }

    @Override
    public void renderFromDetails(EventsDetailsViewModel detailsViewModel) {
        toolbar.setTitle(detailsViewModel.getTitle());
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);
//        ImageHandler.loadImageCover2(bannerImage, detailsViewModel.getImageApp());
//        setHolder(R.drawable.ic_time, detailsViewModel.getTimeRange(), timeHolder);
//        setHolder(R.drawable.ic_skyline, detailsViewModel.getSchedulesViewModels().get(0).getaDdress(), addressHolder);

        AddTicketFragmentAdapter fragmentAdapter = new AddTicketFragmentAdapter(getSupportFragmentManager(),
                detailsViewModel.getSchedulesViewModels());
        bookTicketViewPager.setAdapter(fragmentAdapter);
        bookTicketViewPager.addOnPageChangeListener(new PageChangeListener());
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("PranayTabLogs", "OnTabSelected");
                View tV = tab.getCustomView();
                if (tV != null)
                    tV.setBackgroundResource(R.drawable.rounded_rectangle_green_solid);
                else {
                    tab.setCustomView(R.layout.calendar_item);
                    tV = tab.getCustomView();
                    tV.setBackgroundResource(R.drawable.rounded_rectangle_green_solid);
                }

                bookTicketViewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.d("PranayTabLogs", "OnTabUnselectedSelected");

                View tV = tab.getCustomView();
                tV.setBackgroundResource(R.drawable.rounded_rectangle_grey_solid);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.d("PranayTabLogs", "OnTabReSelected");
                View tV = tab.getCustomView();
                tV.setBackgroundResource(R.drawable.rounded_rectangle_green_solid);
                bookTicketViewPager.setCurrentItem(tab.getPosition());
            }
        });


        tabLayout.setupWithViewPager(bookTicketViewPager);

        for (int i = 1; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(R.layout.calendar_item);
        }

        tabLayout.setSelectedTabIndicatorHeight(0);
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
    public void showPayButton(int ticketQuantity, int price) {
        buttonTextview.setText(String.format(getString(R.string.pay_button),
                CurrencyUtil.convertToCurrencyString(ticketQuantity * price)));
        buttonPayTickets.setVisibility(View.VISIBLE);
    }

    @Override
    public void hidePayButton() {
        buttonPayTickets.setVisibility(View.GONE);
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
            Log.d("PranayTabLogs", "onPAgeScrolled");


        }

        @Override
        public void onPageSelected(int position) {
            mPresenter.onPageChange(position);
            Log.d("PranayTabLogs", "OnPageSelected");

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            Log.d("PranayTabLogs", "OnPageScrollStateChanged");


        }
    }

    @Override
    protected void onResume() {
        bookTicketViewPager.setCurrentItem(1);
        bookTicketViewPager.setCurrentItem(0);
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
        mPresenter.payTicketsClick();
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
