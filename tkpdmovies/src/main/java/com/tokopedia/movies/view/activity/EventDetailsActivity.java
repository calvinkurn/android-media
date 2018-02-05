package com.tokopedia.movies.view.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.movies.R;
import com.tokopedia.movies.R2;
import com.tokopedia.movies.di.DaggerEventComponent;
import com.tokopedia.movies.di.EventComponent;
import com.tokopedia.movies.di.EventModule;
import com.tokopedia.movies.view.contractor.EventsDetailsContract;
import com.tokopedia.movies.view.presenter.EventsDetailsPresenter;
import com.tokopedia.movies.view.utils.ImageTextViewHolder;
import com.tokopedia.movies.view.utils.Utils;
import com.tokopedia.movies.view.viewmodel.CategoryItemsViewModel;
import com.tokopedia.movies.view.viewmodel.EventsDetailsViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import at.blogc.android.views.ExpandableTextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class EventDetailsActivity extends TActivity implements HasComponent<EventComponent>,
        EventsDetailsContract.EventDetailsView {


    @BindView(R2.id.event_detail_banner)
    ImageView eventDetailBanner;
    @BindView(R2.id.app_bar)
    Toolbar appBar;
    @BindView(R2.id.director_name)
    LinearLayout directorName;
    @BindView(R2.id.movie_duration)
    LinearLayout movieDuration;
    @BindView(R2.id.movie_starring)
    LinearLayout movieStarring;
    @BindView(R2.id.text_view_title)
    TextView textViewTitle;
    @BindView(R2.id.movie_language)
    TextView movieLanguage;
    @BindView(R2.id.movie_dimension)
    TextView movieDimesnion;
    @BindView(R2.id.seating_layout_card)
    View ratingCardLayout;
    @BindView(R2.id.btn_book)
    View btnBook;
    @BindView(R2.id.progress_bar_layout)
    View progressBarLayout;
    @BindView(R2.id.prog_bar)
    ProgressBar progBar;
    @BindView(R2.id.text_view_movie_censor)
    TextView movieCensorText;
    @BindView(R2.id.iv_arrow_description)
    ImageView showDescription;
    @BindView(R2.id.movie_synopsis)
    TextView movieSynopsis;


    @BindView(R2.id.movie_info_layout)
    LinearLayout movieInfoLayout;
    @BindView(R2.id.movie_genre_layout)
    LinearLayout movieGenreLayout;

    ImageTextViewHolder directorNameHolder;
    ImageTextViewHolder movieDurationHolder;
    ImageTextViewHolder movieStarringHolder;

    EventComponent eventComponent;
    @Inject
    EventsDetailsPresenter mPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R
                .layout.event_detail_activity);
        ButterKnife.bind(this);
        executeInjector();

        movieDurationHolder = new ImageTextViewHolder();
        directorNameHolder = new ImageTextViewHolder();
        movieStarringHolder = new ImageTextViewHolder();

        ButterKnife.bind(directorNameHolder, directorName);
        ButterKnife.bind(movieDurationHolder, movieDuration);
        ButterKnife.bind(movieStarringHolder, movieStarring);

        mPresenter.attachView(this);
        mPresenter.getEventDetails();

        setupToolbar();
        toolbar.setTitle("Movies");

        AppBarLayout appBarLayout = findViewById(R.id.appbarlayout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
                Drawable upArrow = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_arrow_back_white, null);
                if (offset < -200) {
                    upArrow.setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP);
                    getSupportActionBar().setHomeAsUpIndicator(upArrow);

//                    Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.option_menu_icon);
//                    drawable.setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP);
//                    toolbar.setOverflowIcon(drawable);
                } else {

                    upArrow.setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP);
                    getSupportActionBar().setHomeAsUpIndicator(upArrow);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//                    Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.option_menu_icon);
//                    drawable.setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);
//                    toolbar.setOverflowIcon(drawable);
                }
            }
        });

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
        movieLanguage.setText(Utils.parseCustomText3(homedata.getCustomText2()).getMovieLanguage());
        String directorName = (Utils.parseCustomText3(homedata.getCustomText2())).getDirectorName();
        String movieStarring = (Utils.parseCustomText3(homedata.getCustomText2())).getStarring();
        String movieDuration = (Utils.convertTime(homedata.getDuration()));
        setHolder(R.drawable.ic_time, directorName, directorNameHolder);
        setHolder(R.drawable.ic_placeholder, movieDuration, movieDurationHolder);
        setHolder(R.drawable.ic_skyline, movieStarring, movieStarringHolder);
        textViewTitle.setText(homedata.getTitle());
        movieCensorText.setText(homedata.getCensor());
        movieSynopsis.setText(homedata.getLongRichDesc());
        movieDimesnion.setText(Utils.movieDimension(homedata.getDisplayName()));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        List<TextView> list = addTextViews(Utils.getGenres(homedata.getGenre()));

        movieGenreLayout.removeAllViews();
        for (int i=0;i<list.size();i++) {
            movieGenreLayout.addView(list.get(i), params);
        }
    }

    @Override
    public void renderSeatLayout(String url) {
//        ImageHandler.loadImageCover2(imgvSeatingLayout,url);
    }

    @Override
    public void renderFromCloud(EventsDetailsViewModel data) {
//        setHolder(R.drawable.ic_skyline, data.getSchedulesViewModels().get(0).getaDdress(), addressHolder);
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
        btnBook.setClickable(false);
    }

    @Override
    public void hideProgressBar() {
        progBar.setVisibility(View.GONE);
        progressBarLayout.setVisibility(View.GONE);
        btnBook.setClickable(true);
    }

    public class ToolbarElevationOffsetListener implements AppBarLayout.OnOffsetChangedListener {
        private final Toolbar mToolbar;
        private float mTargetElevation;
        private final AppCompatActivity mActivity;

        public ToolbarElevationOffsetListener(AppCompatActivity appCompatActivity, Toolbar toolbar) {
            mActivity = appCompatActivity;
            mToolbar = toolbar;
            mTargetElevation = mToolbar.getContext().getResources().getDimension(R.dimen.appbar_elevation);
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
            offset = Math.abs(offset);
            mTargetElevation = Math.max(mTargetElevation, appBarLayout.getTargetElevation());
            if (offset >= appBarLayout.getTotalScrollRange() - mToolbar.getHeight()) {
                float flexibleSpace = appBarLayout.getTotalScrollRange() - offset;
                float ratio = 1 - (flexibleSpace / mToolbar.getHeight());
                float elevation = ratio * mTargetElevation;
                setToolbarElevation(elevation);
            } else {
                setToolbarElevation(0);
            }

        }

        private void setToolbarElevation(float targetElevation) {
            ActionBar supportActionBar = mActivity.getSupportActionBar();
            if (supportActionBar != null) supportActionBar.setElevation(targetElevation);
            else if (mToolbar != null)
                ViewCompat.setElevation(mToolbar, targetElevation);
        }
    }


    @OnClick(R2.id.iv_arrow_description)
    void onClickDescriptionArrow() {
        if(movieInfoLayout.getVisibility() == View.VISIBLE) {
            showDescription.animate().rotation(0f);
            movieInfoLayout.setVisibility(View.GONE);
        } else {
            movieInfoLayout.setVisibility(View.VISIBLE);
            showDescription.animate().rotation(180f);
        }
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


    private List<TextView> addTextViews(List<String> genreList) {

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        List<TextView> textViewList = new ArrayList<>();
        for (int i = 0; i<genreList.size(); i++){
            String s = genreList.get(i);
            TextView tv = new TextView(this);
            tv.setId(i);
            tv.setText(s+" " + ".");
            tv.setTextColor(ContextCompat.getColor(this, R.color.grey_400));
            tv.setPadding(10, 0, 10, 8);
            tv.setTextSize(14);
            tv.setLayoutParams(params);
            textViewList.add(tv);
        }

        return textViewList;
    }
}