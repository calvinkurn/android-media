package com.tokopedia.movies.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.movies.R;
import com.tokopedia.movies.R2;
import com.tokopedia.movies.di.DaggerEventComponent;
import com.tokopedia.movies.di.EventComponent;
import com.tokopedia.movies.di.EventModule;
import com.tokopedia.movies.view.adapter.CalendarItemHolder;
import com.tokopedia.movies.view.contractor.EventBookTicketContract;
import com.tokopedia.movies.view.fragment.FragmentAddTickets;
import com.tokopedia.movies.view.presenter.EventBookTicketPresenter;
import com.tokopedia.movies.view.utils.CurrencyUtil;
import com.tokopedia.movies.view.utils.ImageTextViewHolder;
import com.tokopedia.movies.view.utils.Utils;
import com.tokopedia.movies.view.viewmodel.EventsDetailsViewModel;
import com.tokopedia.movies.view.viewmodel.SchedulesViewModel;

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
    @BindView(R2.id.progress_bar_layout)
    View progressBarLayout;
    @BindView(R2.id.prog_bar)
    ProgressBar progBar;
    @BindView(R2.id.app_bar)
    Toolbar appBar;
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
        executeInjector();
        mPresenter.attachView(this);
        mPresenter.getTicketDetails();
        setSupportActionBar(appBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
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
    public void renderFromDetails(final EventsDetailsViewModel detailsViewModel) {
        appBar.setTitle(detailsViewModel.getTitle());
        appBar.setNavigationIcon(R.drawable.ic_arrow_back_black);
        AddTicketFragmentAdapter fragmentAdapter = new AddTicketFragmentAdapter(getSupportFragmentManager(),
                detailsViewModel.getSchedulesViewModels(), detailsViewModel.getTitle());
        bookTicketViewPager.setAdapter(fragmentAdapter);
        bookTicketViewPager.addOnPageChangeListener(new PageChangeListener());
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View tV = tab.getCustomView();
                if (tV != null) {
                    CalendarItemHolder holder = (CalendarItemHolder) tV.getTag();
                    tV.setBackgroundResource(R.drawable.rounded_rectangle_green_solid);
                } else {
                    tab.setCustomView(R.layout.calendar_item);
                    tV = tab.getCustomView();
                    CalendarItemHolder holder = new CalendarItemHolder();
                    ButterKnife.bind(holder, tV);
                    tV.setTag(holder);
                    tV.setBackgroundResource(R.drawable.rounded_rectangle_green_solid);
                }
                bookTicketViewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.d("PranayTabLogs", "OnTabUnselectedSelected");
                View tV = tab.getCustomView();
                CalendarItemHolder holder = (CalendarItemHolder) tV.getTag();
                tV.setBackgroundResource(R.drawable.rounded_rectangle_grey_solid);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.d("PranayTabLogs", "OnTabReSelected");
                View tV = tab.getCustomView();
                CalendarItemHolder holder = (CalendarItemHolder) tV.getTag();
                String[] date = Utils.getDateArray(Utils.convertEpochToString(detailsViewModel.getSchedulesViewModels().get(tab.getPosition()).getStartDate()));
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
            customView.setTag(holder);
            ButterKnife.bind(holder, customView);
            String scheduleDate = Utils.convertEpochToString(detailsViewModel.getSchedulesViewModels().get(i).getStartDate());
            String[] date = Utils.getDateArray(scheduleDate);
            holder.setTvDate(date[1].toString());
            holder.setTvDay(date[0].toString());
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

    public class AddTicketFragmentAdapter extends FragmentStatePagerAdapter {

        List<SchedulesViewModel> mScheduleList;
        String title;

        public AddTicketFragmentAdapter(FragmentManager manager, List<SchedulesViewModel> scheduleList, String title) {
            super(manager);
            this.mScheduleList = scheduleList;
            this.title = title;
        }

        @Override
        public Fragment getItem(int position) {
            FragmentAddTickets fragmentAddTickets = FragmentAddTickets.
                    newInstance(mScheduleList.get(position).getGroups().size());
            fragmentAddTickets.setData(mScheduleList.get(position).getGroups(), mPresenter, title);
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
