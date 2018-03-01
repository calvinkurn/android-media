package com.tokopedia.events.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.events.R;
import com.tokopedia.events.R2;
import com.tokopedia.events.di.DaggerEventComponent;
import com.tokopedia.events.di.EventComponent;
import com.tokopedia.events.di.EventModule;
import com.tokopedia.events.view.contractor.EventsDetailsContract;
import com.tokopedia.events.view.presenter.EventsDetailsPresenter;
import com.tokopedia.events.view.utils.CurrencyUtil;
import com.tokopedia.events.view.utils.ImageTextViewHolder;
import com.tokopedia.events.view.utils.Utils;
import com.tokopedia.events.view.viewmodel.CategoryItemsViewModel;
import com.tokopedia.events.view.viewmodel.EventsDetailsViewModel;

import javax.inject.Inject;

import at.blogc.android.views.ExpandableTextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class EventDetailsActivity extends TActivity implements HasComponent<EventComponent>,
        EventsDetailsContract.EventDetailsView {


    @BindView(R2.id.event_detail_banner)
    ImageView eventDetailBanner;
    @BindView(R2.id.tv_expandable_description)
    ExpandableTextView tvExpandableDescription;
    @BindView(R2.id.seemorebutton)
    TextView seemorebutton;
    @BindView(R2.id.seemorebutton_tnc)
    TextView seemorebuttonTnC;
    @BindView(R2.id.down_arrow)
    ImageView ivArrowSeating;
    @BindView(R2.id.tv_expandable_tnc)
    ExpandableTextView tvExpandableTermsNCondition;
    @BindView(R2.id.app_bar)
    Toolbar appBar;
    @BindView(R2.id.event_time)
    LinearLayout timeView;
    @BindView(R2.id.event_location)
    LinearLayout locationView;
    @BindView(R2.id.event_address)
    LinearLayout addressView;
    @BindView(R2.id.text_view_title)
    TextView textViewTitle;
    @BindView(R2.id.btn_book)
    View btnBook;
    @BindView(R2.id.progress_bar_layout)
    View progressBarLayout;
    @BindView(R2.id.prog_bar)
    ProgressBar progBar;
    @BindView(R2.id.main_content)
    FrameLayout mainContent;
    @BindView(R2.id.button_textview)
    TextView buttonTextView;
    @BindView(R2.id.expand_view)
    LinearLayout expandLayout;
    @BindView(R2.id.expand_view_tnc)
    LinearLayout expandTnc;
    @BindView(R2.id.down_arrow_tnc)
    ImageView ivArrowSeatingTnC;
    @BindView(R2.id.tv_event_price)
    TextView eventPrice;

    ImageTextViewHolder timeHolder;
    ImageTextViewHolder locationHolder;
    ImageTextViewHolder addressHolder;

    EventComponent eventComponent;
    @Inject
    EventsDetailsPresenter mPresenter;

    public static String FROM = "from";

    public static String EXTRA_EVENT_NAME_KEY = "event";

    public static final int FROM_HOME_OR_SEARCH = 1;

    public static final int FROM_DEEPLINK = 2;


    @DeepLink({Constants.Applinks.EVENTS_DETAILS})
    public static Intent getCallingApplinksTaskStask(Context context, Bundle extras) {
        String deepLink = extras.getString(DeepLink.URI);
        Uri.Builder uri = Uri.parse(deepLink).buildUpon();
        Intent destination = new Intent(context, EventDetailsActivity.class)
                .setData(uri.build())
                .putExtras(extras);
        destination.putExtra(Constants.EXTRA_FROM_PUSH, true);
        destination.putExtra(FROM, FROM_DEEPLINK);
        return destination;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_detail_activity);
        ButterKnife.bind(this);
        executeInjector();

        timeHolder = new ImageTextViewHolder();
        locationHolder = new ImageTextViewHolder();
        addressHolder = new ImageTextViewHolder();

        ButterKnife.bind(timeHolder, timeView);
        ButterKnife.bind(locationHolder, locationView);
        ButterKnife.bind(addressHolder, addressView);

        tvExpandableDescription.setInterpolator(new OvershootInterpolator());
        tvExpandableTermsNCondition.setInterpolator(new OvershootInterpolator());

        setupToolbar();
        toolbar.setTitle("Events");

        AppBarLayout appBarLayout = findViewById(R.id.appbarlayout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
                Drawable upArrow = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_arrow_back_white, null);
                if (offset < -200) {
                    upArrow.setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP);
                    getSupportActionBar().setHomeAsUpIndicator(upArrow);

                } else {

                    upArrow.setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP);
                    getSupportActionBar().setHomeAsUpIndicator(upArrow);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                }
            }
        });

        mPresenter.initialize();
        mPresenter.attachView(this);
        mPresenter.getEventDetails();
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
        startActivity(intent);

    }

    @Override
    public void renderFromHome(CategoryItemsViewModel homedata) {
        toolbar.setTitle(homedata.getTitle());
        ImageHandler.loadImageCover2(eventDetailBanner, homedata.getImageApp());
        String dateRange = "";

        if (homedata.getMinStartDate() > 0) {
            if (homedata.getMinStartDate().equals(homedata.getMaxEndDate())) {
                dateRange = Utils.convertEpochToString(homedata.getMinStartDate());
            } else {
                dateRange = Utils.convertEpochToString(homedata.getMinStartDate())
                        + " - " + Utils.convertEpochToString(homedata.getMaxEndDate());
            }
        } else {
            timeView.setVisibility(View.GONE);
        }

        setHolder(R.drawable.ic_time, dateRange, timeHolder);
        setHolder(R.drawable.ic_placeholder, homedata.getCityName(), locationHolder);
        setHolder(R.drawable.ic_skyline, homedata.getCityName(), addressHolder);
        textViewTitle.setText(homedata.getTitle());
        tvExpandableDescription.setText(Html.fromHtml(homedata.getLongRichDesc()));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        String tnc = homedata.getTnc();
        String splitArray[] = tnc.split("~");
        int flag = 1;

        StringBuilder tncBuffer = new StringBuilder();

        for (String line : splitArray) {
            if (flag == 1) {
                tncBuffer.append("<i>").append(line).append("</i>").append("<br>");
                flag = 2;
            } else {
                tncBuffer.append("<b>").append(line).append("</b>").append("<br>");
                flag = 1;
            }
        }
        tvExpandableTermsNCondition.setText(Html.fromHtml(tncBuffer.toString()));

        eventPrice.setText("Rp " + CurrencyUtil.convertToCurrencyString(homedata.getSalesPrice()));
    }

    @Override
    public void renderSeatLayout(String url) {
    }

    @Override
    public void renderSeatmap(String url) {
//        ImageHandler.loadImageCover2(imgvSeatingLayout, url);
    }

    @Override
    public void renderFromCloud(EventsDetailsViewModel data) {
        toolbar.setTitle(data.getTitle());
        ImageHandler.loadImageCover2(eventDetailBanner, data.getImageApp());

        if (data.getTimeRange() != null && data.getTimeRange().length() > 3)
            setHolder(R.drawable.ic_time, data.getTimeRange(), timeHolder);
        else
            timeView.setVisibility(View.GONE);

        setHolder(R.drawable.ic_placeholder, data.getCityName(), locationHolder);
        setHolder(R.drawable.ic_skyline, data.getSchedulesViewModels().get(0).getaDdress(), addressHolder);
        textViewTitle.setText(data.getTitle());
        tvExpandableDescription.setText(Html.fromHtml(data.getLongRichDesc()));

        String tnc = data.getTnc();
        if (Utils.isNotNullOrEmpty(tnc)) {
            String splitArray[] = tnc.split("~");
            int flag = 1;

            StringBuilder tncBuffer = new StringBuilder();

            for (String line : splitArray) {
                if (flag == 1) {
                    tncBuffer.append("<i>").append(line).append("</i>").append("<br>");
                    flag = 2;
                } else {
                    tncBuffer.append("<b>").append(line).append("</b>").append("<br>");
                    flag = 1;
                }
            }
            tvExpandableTermsNCondition.setText(Html.fromHtml(tncBuffer.toString()));
        }

//            seatingLayoutCard.setVisibility(View.GONE);
        eventPrice.setText("Rp " + CurrencyUtil.convertToCurrencyString(data.getSalesPrice()));
    }

    @Override
    public RequestParams getParams() {
        return RequestParams.EMPTY;
    }

    @Override
    public void setHolder(int resID, String label, ImageTextViewHolder holder) {

        holder.setImage(resID);
        holder.setTextView(label);

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
    public View getRootView() {
        return mainContent;
    }

    @OnClick(R2.id.expand_view)
    void setSeemorebutton() {
        if (tvExpandableDescription.isExpanded()) {
            seemorebutton.setText(R.string.expand);
            ivArrowSeating.animate().rotation(0f);

        } else {
            seemorebutton.setText(R.string.collapse);
            ivArrowSeating.animate().rotation(180f);
        }
        tvExpandableDescription.toggle();
    }

    @OnClick(R2.id.expand_view_tnc)
    void setSeemorebuttontnc() {
        if (tvExpandableTermsNCondition.isExpanded()) {
            seemorebuttonTnC.setText(R.string.expand);
            ivArrowSeatingTnC.animate().rotation(0f);

        } else {
            seemorebuttonTnC.setText(R.string.collapse);
            ivArrowSeatingTnC.animate().rotation(180f);
        }
        tvExpandableTermsNCondition.toggle();
    }

    @OnClick(R2.id.btn_book)
    void book() {
        mPresenter.bookBtnClick();
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
    protected boolean isLightToolbarThemes() {
        return true;
    }
}