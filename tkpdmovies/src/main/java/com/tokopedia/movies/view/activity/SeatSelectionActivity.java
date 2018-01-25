package com.tokopedia.movies.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.readystatesoftware.chuck.internal.ui.MainActivity;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.movies.R;
import com.tokopedia.movies.R2;
import com.tokopedia.movies.di.EventComponent;
import com.tokopedia.movies.di.EventModule;
import com.tokopedia.movies.view.contractor.SeatSelectionContract;
import com.tokopedia.movies.di.DaggerEventComponent;
import com.tokopedia.movies.view.customview.CustomSeatAreaLayout;
import com.tokopedia.movies.view.customview.CustomSeatLayout;
import com.tokopedia.movies.view.customview.SeatLayoutInfo;
import com.tokopedia.movies.view.customview.VScrollView;
import com.tokopedia.movies.view.presenter.EventsDetailsPresenter;
import com.tokopedia.movies.view.presenter.SeatSelectionPresenter;
import com.tokopedia.movies.view.utils.Utils;
import com.tokopedia.movies.view.viewmodel.SeatLayoutViewModel;

import org.w3c.dom.Text;

import javax.annotation.Nullable;
import javax.inject.Inject;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by naveengoyal on 1/16/18.
 */

public class SeatSelectionActivity extends TActivity implements HasComponent<EventComponent>,
        SeatSelectionContract.SeatSelectionView {


    @BindView(R2.id.app_bar)
    Toolbar appBar;
    @BindView(R2.id.tv_movie_name)
    TextView movieName;
    @BindView(R2.id.tv_theatre_name)
    TextView theatreName;
    @BindView(R2.id.tv_theatre_class)
    TextView theatreClass;
    @BindView(R2.id.tv_show_timing)
    TextView showTiming;
    @BindView(R2.id.vertical_layout)
    LinearLayout seatTextLayout;
    @BindView(R2.id.small_preview)
    ImageView previewWindow;
    @BindView(R2.id.preview_window)
    View previewLayout;
    @BindView(R2.id.seatLayout)
    LinearLayout seatLayout;
    @BindView(R2.id.seat_plan)
    LinearLayout seatPlan;
    @BindView(R2.id.abc)
    ScrollView scrollView;
    @BindView(R2.id.horizontal_scroll)
    HorizontalScrollView horizontalScrollView;
    @BindView(R2.id.ticket_count)
    TextView ticketCount;
    @BindView(R2.id.ticket_price)
    TextView ticketPrice;
    @BindView(R2.id.progress_bar_layout)
    View progressBarLayout;
    @BindView(R2.id.prog_bar)
    ProgressBar progBar;

    EventComponent eventComponent;
    @Inject
    SeatSelectionPresenter mPresenter;

    int price;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seat_selection_layout);
        ButterKnife.bind(this);
        executeInjector();

        mPresenter.attachView(this);
        mPresenter.getSeatSelectionDetails();
        setupToolbar();
        toolbar.setTitle("Pilih Kursi");

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

    @Override
    public void showMessage(String message) {

    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {

    }

    @Override
    public void renderSeatSelection(SeatLayoutInfo seatLayoutInfo, SeatLayoutViewModel viewModel) {
        movieName.setText(seatLayoutInfo.getMovieName());
        appBar.setNavigationIcon(R.drawable.ic_arrow_back_black);
        theatreName.setText(seatLayoutInfo.getTheatreName());
        theatreClass.setText(seatLayoutInfo.getTheatreClass());
        showTiming.setText(seatLayoutInfo.getShowTiming());
        price = seatLayoutInfo.getTicketPrice();
        addSeatingPlan(viewModel);
    }

    private void addSeatingPlan(SeatLayoutViewModel seatLayoutViewModel) {
        int numOfRows = seatLayoutViewModel.getLayoutDetail().size();
        char prevChr = '\0';
        Log.d("Naveen", "Number of Rows" + numOfRows);
        // = seatLayoutViewModel.getLayoutDetail().get(0).getPhysicalRowId().charAt(0);
        for (int i = 0; i < numOfRows;) {
            CustomSeatAreaLayout customSeatAreaLayout = new CustomSeatAreaLayout(this, mPresenter);
            char currentChar = seatLayoutViewModel.getLayoutDetail().get(i).getPhysicalRowId().charAt(0);
            if(prevChr != '\0' && currentChar - prevChr > 1) {
                customSeatAreaLayout.setSeatRow("");
                prevChr++;
                seatTextLayout.addView(customSeatAreaLayout);
                continue;
            } else {
                customSeatAreaLayout.setSeatRow(currentChar+"");
                prevChr = currentChar;
            }
            int numOfColumns = seatLayoutViewModel.getLayoutDetail().get(i).getSeat().size();
            Log.d("Naveen", "Number of Columns" + numOfColumns);
            for (int j = 0; j < numOfColumns; j++) {
                if (seatLayoutViewModel.getLayoutDetail().get(i).getSeat().get(j).getNo() != 0) {
                    customSeatAreaLayout.addColumn("" + seatLayoutViewModel.getLayoutDetail().get(i).getSeat().get(j).getNo(), seatLayoutViewModel.getLayoutDetail().get(i).getSeat().get(j).getStatus());
                } else {
                    customSeatAreaLayout.addColumn("", 0);
                }
            }
            seatTextLayout.addView(customSeatAreaLayout);
            i++;
        }

        final Bitmap bp = Utils.getBitmap(this, seatTextLayout);
        Utils.saveImage(SeatSelectionActivity.this, bp);

        seatTextLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_DOWN) {
                    Log.d("Naveen", "OnTouch Event action down or action move");
                    previewLayout.setVisibility(View.VISIBLE);
                    previewWindow.setVisibility(View.VISIBLE);
                    previewWindow.setImageBitmap(bp);
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    Log.d("Naveen", "OnTouch Event action up");
                    previewLayout.setVisibility(View.GONE);
                    previewWindow.setVisibility(View.GONE);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public RequestParams getParams() {
        return RequestParams.EMPTY;
    }

    @Override
    public void showPayButton(int ticketQuantity, int price) {

    }

    @Override
    public void hidePayButton() {

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
    public void setTicketPrice(int numOfTickets) {
        ticketCount.setText("" + numOfTickets);
        ticketPrice.setText("" + numOfTickets * price);
    }
}
