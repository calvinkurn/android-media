package com.tokopedia.events.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.events.R;
import com.tokopedia.events.R2;
import com.tokopedia.events.di.DaggerEventComponent;
import com.tokopedia.events.di.EventComponent;
import com.tokopedia.events.di.EventModule;
import com.tokopedia.events.view.contractor.SeatSelectionContract;
import com.tokopedia.events.view.customview.CustomSeatAreaLayout;
import com.tokopedia.events.view.customview.CustomSeatLayout;
import com.tokopedia.events.view.presenter.SeatSelectionPresenter;
import com.tokopedia.events.view.utils.CurrencyUtil;
import com.tokopedia.events.view.utils.Utils;
import com.tokopedia.events.view.viewmodel.SeatLayoutViewModel;
import com.tokopedia.events.view.viewmodel.SelectedSeatViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SeatSelectionActivity extends TActivity implements HasComponent<EventComponent>,
        SeatSelectionContract.SeatSelectionView {


    @BindView(R2.id.app_bar)
    Toolbar appBar;
    @BindView(R2.id.tv_movie_name)
    TextView movieName;
    @BindView(R2.id.selected_seats)
    TextView selectedSeatText;
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
    @BindView(R2.id.main_content)
    FrameLayout mainContent;

    EventComponent eventComponent;
    @Inject
    SeatSelectionPresenter mPresenter;

    SelectedSeatViewModel selectedSeatViewModel;

    SeatLayoutViewModel seatLayoutViewModel;

    int price;
    int maxTickets;
    List<String> areacodes = new ArrayList<>();
    List<String> selectedSeats = new ArrayList<>();
    List<String> rowIds = new ArrayList<>();
    List<String> physicalRowIds = new ArrayList<>();
    List<String> seatIds = new ArrayList<>();
    String areaId;
    private int quantity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seat_selection_layout);
        ButterKnife.bind(this);
        executeInjector();
        selectedSeatViewModel = new SelectedSeatViewModel();
        seatLayoutViewModel = new SeatLayoutViewModel();

        mPresenter.attachView(this);
        mPresenter.initialize();
        mPresenter.getProfile();
        mPresenter.getSeatSelectionDetails();
        setupToolbar();
        toolbar.setTitle(R.string.seat_selection_title);


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
        startActivity(intent);
    }

    @Override
    public void renderSeatSelection(int salesPrice, int maxTickets, SeatLayoutViewModel viewModel) {
        appBar.setNavigationIcon(R.drawable.ic_arrow_back_black);
        price = salesPrice;
        this.maxTickets = maxTickets;
        this.seatLayoutViewModel = viewModel;
        addSeatingPlan(viewModel);
    }


    private void addSeatingPlan(SeatLayoutViewModel seatLayoutViewModel) {

        if (seatLayoutViewModel.getArea() != null) {
            areaId = seatLayoutViewModel.getArea().get(0).getAreaCode() + "-" + String.valueOf(seatLayoutViewModel.getArea().get(0).getAreaNo());
        }
        int numOfRows = seatLayoutViewModel.getLayoutDetail().size();
        String currentChar = "";
        for (int i = 0; i < numOfRows; ) {
            CustomSeatAreaLayout customSeatAreaLayout = new CustomSeatAreaLayout(this, mPresenter);
            int rowId = seatLayoutViewModel.getLayoutDetail().get(i).getRowId();
            if (Utils.isNotNullOrEmpty(seatLayoutViewModel.getLayoutDetail().get(i).getPhysicalRowId())) {
                currentChar = seatLayoutViewModel.getLayoutDetail().get(i).getPhysicalRowId();
                customSeatAreaLayout.setSeatRow(currentChar);
            }
            int numOfColumns = seatLayoutViewModel.getLayoutDetail().get(i).getSeat().size();
            for (int j = 0; j < numOfColumns; j++) {
                if (seatLayoutViewModel.getLayoutDetail().get(i).getSeat().get(j).getNo() != 0) {
                    customSeatAreaLayout.addColumn(String.valueOf(seatLayoutViewModel.getLayoutDetail().get(i).getSeat().get(j).getNo()),
                            seatLayoutViewModel.getLayoutDetail().get(i).getSeat().get(j).getStatus(),
                            maxTickets, rowId, currentChar);
                } else {
                    customSeatAreaLayout.addColumn("", 0, 0, 0, "");
                }
            }
            seatTextLayout.addView(customSeatAreaLayout);
            i++;
        }

        final Bitmap bp = Utils.getBitmap(this, seatTextLayout);
        Utils.saveImage(SeatSelectionActivity.this, bp);

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

    @OnClick(R2.id.verifySeat)
    void verifySeat() {
        setSelectedSeatModel();
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
        this.quantity = numOfTickets;
        ticketCount.setText("" + numOfTickets);
        ticketPrice.setText(String.format(CurrencyUtil.RUPIAH_FORMAT,
                CurrencyUtil.convertToCurrencyString(numOfTickets * price)));
    }

    @Override
    public void setSelectedSeatText() {
        String text = TextUtils.join(", ", selectedSeats);
        selectedSeatText.setText(text);
    }

    @Override
    public void initializeSeatLayoutModel(List<String> selectedSeatTextList, List<String> rowIds) {
        selectedSeats = selectedSeatTextList;
        this.rowIds = rowIds;
        selectedSeatViewModel.setAreaCodes(areacodes);
        selectedSeatViewModel.setPrice(price);
        selectedSeatViewModel.setSeatRowIds(this.rowIds);
        selectedSeatViewModel.setQuantity(quantity);
        selectedSeatViewModel.setSeatIds(seatIds);
        selectedSeatViewModel.setAreaId(areaId);
        selectedSeatViewModel.setPhysicalRowIds(physicalRowIds);
    }

    @Override
    public void setEventTitle(String text) {
        movieName.setText(text);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        CustomSeatLayout.destroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getProfile();
    }

    @Override
    public void setSelectedSeatModel() {
        seatIds.clear();
        physicalRowIds.clear();
        if (selectedSeats.size() > 0 && selectedSeats.size() == maxTickets) {
            for (int i = 0; i < selectedSeats.size(); i++) {
                Character firstChar = selectedSeats.get(i).charAt(0);

                if (Character.isLetter(firstChar)) {
                    physicalRowIds.add("" + selectedSeats.get(i).charAt(0));
                    seatIds.add(selectedSeats.get(i).substring(1, selectedSeats.get(i).length()));
                } else {
                    seatIds.add(selectedSeats.get(i).substring(0, selectedSeats.get(i).length()));
                }
                areacodes.add(seatLayoutViewModel.getArea().get(0).getAreaCode());
            }
            mPresenter.verifySeatSelection(selectedSeatViewModel);
        } else {

            Toast.makeText(this, String.format(getString(R.string.select_max_ticket), maxTickets),
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public View getRootView() {
        return mainContent;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.onActivityResult(requestCode);
    }
}
