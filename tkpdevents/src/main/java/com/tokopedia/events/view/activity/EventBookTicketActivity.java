package com.tokopedia.events.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
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
import com.tokopedia.events.view.fragment.LocationDateBottomSheetFragment;
import com.tokopedia.events.view.presenter.EventBookTicketPresenter;
import com.tokopedia.events.view.utils.CurrencyUtil;
import com.tokopedia.events.view.utils.ImageTextViewHolder;
import com.tokopedia.events.view.utils.Utils;
import com.tokopedia.events.view.viewmodel.EventsDetailsViewModel;
import com.tokopedia.events.view.viewmodel.SchedulesViewModel;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventBookTicketActivity
        extends TActivity implements EventBookTicketContract.EventBookTicketView {


    //    @BindView(R2.id.collasing_toolbar)
//    CollapsingToolbarLayout collasingToolbar;
    @BindView(R2.id.pay_tickets)
    View buttonPayTickets;
    @BindView(R2.id.button_textview)
    TextView buttonTextview;
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
    @BindView(R2.id.tv_ubah_jadwal)
    TextView tvUbahJadwal;
    @BindView(R2.id.tv_location_bta)
    TextView tvLocation;
    @BindView(R2.id.tv_day_time_bta)
    TextView tvDate;
    @BindView(R2.id.button_count_layout)
    View buttonCountLayout;

    private EventComponent eventComponent;
    @Inject
    EventBookTicketPresenter mPresenter;
    private String title;

    private LocationDateBottomSheetFragment locationFragment;

    private static final int EVENT_LOGIN_REQUEST = 1099;
    private static final String BOOK_TICKET_FRAGMENT = "bookticketfragment";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_ticket_layout);
        ButterKnife.bind(this);

        executeInjector();

        mPresenter.attachView(this);

        mPresenter.getTicketDetails();
        appBar.setTitle(R.string.book_ticket_title);
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
        hideProgressBar();
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void renderFromDetails(EventsDetailsViewModel detailsViewModel) {
        appBar.setTitle(detailsViewModel.getTitle());
        appBar.setNavigationIcon(R.drawable.ic_arrow_back_black);
        title = detailsViewModel.getTitle();
        if (detailsViewModel.getSchedulesViewModels() != null) {
            if (detailsViewModel.getSchedulesViewModels().size() > 1) {
                tvUbahJadwal.setVisibility(View.VISIBLE);
            } else
                tvUbahJadwal.setVisibility(View.GONE);
        } else {
            tvUbahJadwal.setVisibility(View.GONE);
        }
        tvLocation.setText(detailsViewModel.getSchedulesViewModels().get(0).getCityName());
        tvDate.setText(Utils.convertEpochToString(detailsViewModel.getSchedulesViewModels().get(0).getStartDate()));
        setFragmentData(detailsViewModel.getSchedulesViewModels().get(0));
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
    }

    @Override
    public void hidePayButton() {
        buttonCountLayout.setVisibility(View.INVISIBLE);
        buttonPayTickets.setVisibility(View.INVISIBLE);
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

    @Override
    public void setLocationDate(String location, String date, SchedulesViewModel datas) {
        tvLocation.setText(location);
        tvDate.setText(date);
        setFragmentData(datas);
        if (locationFragment != null)
            locationFragment.dismiss();
    }

    private void setFragmentData(SchedulesViewModel schedulesViewModel) {
        FragmentAddTickets fragmentAddTickets = (FragmentAddTickets) getSupportFragmentManager().
                findFragmentById(R.id.bookticket_fragment_holder);
        if (fragmentAddTickets == null) {
            fragmentAddTickets = FragmentAddTickets.newInstance(10);
            fragmentAddTickets.setData(schedulesViewModel.getPackages(), mPresenter);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.bookticket_fragment_holder, fragmentAddTickets);
            transaction.addToBackStack(BOOK_TICKET_FRAGMENT);
            transaction.commit();
        } else {
            fragmentAddTickets.setData(schedulesViewModel.getPackages(), mPresenter);
            fragmentAddTickets.resetAdapter();
        }
    }

    @Override
    protected void onResume() {
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

    @OnClick(R2.id.tv_ubah_jadwal)
    void selectLocationDate() {
        if (locationFragment == null)
            locationFragment = new LocationDateBottomSheetFragment();

        locationFragment.setData(mPresenter.getLocationDateModels());
        locationFragment.setPresenter(mPresenter);
        locationFragment.show(getSupportFragmentManager(), "bottomsheetfragment");
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

    @Override
    public void onBackPressed() {
        if (locationFragment != null && locationFragment.isVisible())
            super.onBackPressed();
        else
            finish();
    }
}
