package com.tokopedia.events.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.ArrowKeyMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.events.R;
import com.tokopedia.events.R2;
import com.tokopedia.events.di.DaggerEventComponent;
import com.tokopedia.events.di.EventComponent;
import com.tokopedia.events.di.EventModule;
import com.tokopedia.events.view.contractor.EventReviewTicketsContractor;
import com.tokopedia.events.view.presenter.EventReviewTicketPresenter;
import com.tokopedia.events.view.utils.CurrencyUtil;
import com.tokopedia.events.view.utils.ImageTextViewHolder;
import com.tokopedia.events.view.viewmodel.PackageViewModel;
import com.tokopedia.events.view.viewmodel.SelectedSeatViewModel;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;

public class ReviewTicketActivity extends TActivity implements HasComponent<EventComponent>,
        EventReviewTicketsContractor.EventReviewTicketsView {

    @BindView(R2.id.event_image_small)
    ImageView eventImageSmall;
    @BindView(R2.id.event_name_tv)
    TextView eventNameTv;
    @BindView(R2.id.event_time_tv)
    View eventTimeTv;
    @BindView(R2.id.event_address_tv)
    View eventAddressTv;
    @BindView(R2.id.event_total_tickets)
    TextView eventTotalTickets;
    @BindView(R2.id.tv_ticket_summary)
    TextView tvTicketSummary;
    @BindView(R2.id.tv_visitor_names)
    EditText tvVisitorNames;
    @BindView(R2.id.tv_telephone)
    EditText tvTelephone;
    @BindView(R2.id.tv_base_fare)
    TextView tvBaseFare;
    @BindView(R2.id.tv_conv_fees)
    TextView tvConvFees;
    @BindView(R2.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R2.id.button_textview)
    TextView buttonTextview;
    @BindView(R2.id.base_fare_break)
    TextView baseFareBreak;
    @BindView(R2.id.update_promo)
    TextView updatePromo;
    @BindView(R2.id.ed_promo)
    EditText edPromo;
    @BindView(R2.id.btn_go_to_payment)
    View btnGoToPayment;
    @BindView(R2.id.promo_checkbox)
    CheckBox promoCheckbox;
    @BindView(R2.id.ed_promo_layout)
    View edPromoLayout;
    @BindView(R2.id.progress_bar_layout)
    View progressBarLayout;
    @BindView(R2.id.prog_bar)
    ProgressBar progBar;
    @BindView(R2.id.update_email)
    View updateEmail;
    @BindView(R2.id.update_number)
    View updateNumber;
    @BindView(R2.id.app_bar)
    Toolbar appBar;
    @BindView(R2.id.scroll_view)
    ScrollView scrollView;
    @BindView(R2.id.form_layout)
    View formLayout;
    @BindView(R2.id.ed_form_1)
    EditText edForm1;
    @BindView(R2.id.ed_form_2)
    EditText edForm2;
    @BindView(R2.id.ed_form_3)
    EditText edForm3;
    @BindView(R2.id.ed_form_4)
    EditText edForm4;
    @BindView(R2.id.main_content)
    FrameLayout mainContent;
    @BindView(R2.id.tv_promo_success_msg)
    TextView tvPromoSuccessMsg;
    @BindView(R2.id.tv_promo_cashback_msg)
    TextView tvPromoCashbackMsg;
    @BindView(R2.id.batal)
    TextView batal;
    @BindView(R2.id.tooltip_layout)
    View tooltipLayout;
    @BindView(R2.id.info_email)
    ImageView infoEmail;
    @BindView(R2.id.info_moreinfo)
    ImageView infoMoreinfo;
    @BindView(R2.id.tooltipinfo_title)
    TextView tooltipTitle;
    @BindView(R2.id.tooltipinfo_subtitle)
    TextView tooltipSubtitle;
    @BindView(R2.id.button_dismisstooltip)
    TextView dismissTooltip;
    @BindView(R2.id.tv_ticket_cnt_type)
    TextView tvTicketCntType;
    @BindView(R2.id.tv_someinfo)
    TextView tvSomeInfo;
    @BindView(R2.id.selected_seats_layout)
    View selectedSeatLayout;
    @BindView(R2.id.seat_numbers)
    TextView seatNumbers;

    EventComponent eventComponent;
    @Inject
    EventReviewTicketPresenter mPresenter;

    public static final int PAYMENT_REQUEST_CODE = 65000;
    private ImageTextViewHolder timeHolder;
    private ImageTextViewHolder addressHolder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_booking_layout);
        ButterKnife.bind(this);
        executeInjector();
        timeHolder = new ImageTextViewHolder();
        addressHolder = new ImageTextViewHolder();

        ButterKnife.bind(timeHolder, eventTimeTv);
        ButterKnife.bind(addressHolder, eventAddressTv);

        mPresenter.attachView(this);
        mPresenter.initialize();

        promoCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                if (isChecked) {
                    edPromoLayout.setVisibility(View.VISIBLE);
                    edPromo.setTextIsSelectable(false);
                    edPromo.setFocusable(true);
                    edPromo.setFocusableInTouchMode(true);
                    edPromo.setClickable(true);
                    edPromo.setLongClickable(true);
                    edPromo.setMovementMethod(ArrowKeyMovementMethod.getInstance());
                    edPromo.setText(edPromo.getText(), TextView.BufferType.SPANNABLE);
                    edPromo.requestFocus();
                    im.showSoftInput(edPromo, 0);
                    scrollView.smoothScrollTo(0, edPromoLayout.getBottom());
                } else {
                    edPromoLayout.setVisibility(View.GONE);
                    edPromo.setText("");
                    mPresenter.updatePromoCode("");
                    im.hideSoftInputFromWindow(edPromo.getWindowToken(), 0);
                }
            }
        });

        setSupportActionBar(appBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            tvVisitorNames.setShowSoftInputOnFocus(false);
//            tvTelephone.setShowSoftInputOnFocus(false);
//        }
//        else {
//            tvVisitorNames.setTextIsSelectable(true);
//            tvTelephone.setTextIsSelectable(true);
//        }


    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }


    @Override
    public void renderFromPackageVM(PackageViewModel packageViewModel, SelectedSeatViewModel selectedSeats) {
        appBar.setTitle(packageViewModel.getTitle());
        appBar.setNavigationIcon(R.drawable.ic_arrow_back_black);
        String timerange = packageViewModel.getTimeRange();
        ImageHandler.loadImageCover2(eventImageSmall, packageViewModel.getThumbnailApp());
        eventNameTv.setText(packageViewModel.getDisplayName());
        setHolder(R.drawable.ic_time, timerange, timeHolder);
        setHolder(R.drawable.skyline, packageViewModel.getAddress(), addressHolder);
        eventTotalTickets.setText(String.format(getString(R.string.jumlah_tiket),
                packageViewModel.getSelectedQuantity()));

//        tvTelephone.setText(SessionHandler.getPhoneNumber());
        int baseFare = packageViewModel.getSelectedQuantity() * packageViewModel.getSalesPrice();
        tvBaseFare.setText("Rp " + CurrencyUtil.convertToCurrencyString(baseFare));
        int convFees = packageViewModel.getConvenienceFee();
        tvConvFees.setText("Rp " + CurrencyUtil.convertToCurrencyString(convFees));
        tvTotalPrice.setText("Rp " + CurrencyUtil.convertToCurrencyString(baseFare + convFees));
        buttonTextview.setText(getString(R.string.pay_button));
        SpannableString someinfo = new SpannableString(getResources().getString(R.string.some_info));
        someinfo.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.green_nob)), 54, 74, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvSomeInfo.setText(someinfo);
        tvTicketCntType.setText(String.format(getString(R.string.x_type),
                packageViewModel.getSelectedQuantity(), packageViewModel.getDisplayName()));
        String baseBreak = String.format(getString(R.string.x_type),
                packageViewModel.getSelectedQuantity(), CurrencyUtil.convertToCurrencyString(packageViewModel.getSalesPrice()));
        baseFareBreak.setText("(" + baseBreak + ")");
        if (selectedSeats != null && selectedSeats.getSeatIds() != null && selectedSeats.getPhysicalRowIds() != null) {
            List<String> seatID = selectedSeats.getSeatIds();
            List<String> rowID = selectedSeats.getPhysicalRowIds();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < seatID.size(); i++) {
                builder.append(rowID.get(i)).append(seatID.get(i));
                if (i != seatID.size() - 1)
                    builder.append(", ");
                else
                    builder.append("");
            }
            seatNumbers.setText(builder.toString());
            selectedSeatLayout.setVisibility(View.VISIBLE);
        }
        hideProgressBar();
    }

    @Override
    public void setEmailID(String emailID) {
        tvVisitorNames.setText(emailID);
    }

    @Override
    public void setPhoneNumber(String number) {
        tvTelephone.setText(number);
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
    public void initForms(String[] hintText, String[] regex) {
        formLayout.setVisibility(View.VISIBLE);
        try {
            edForm1.setHint(hintText[0]);
            edForm1.setVisibility(View.VISIBLE);
            edForm1.setTag(regex[0]);

            edForm2.setHint(hintText[1]);
            edForm2.setVisibility(View.VISIBLE);
            edForm2.setTag(regex[1]);

            edForm3.setHint(hintText[2]);
            edForm3.setVisibility(View.VISIBLE);
            edForm3.setTag(regex[2]);

            edForm4.setHint(hintText[3]);
            edForm4.setVisibility(View.VISIBLE);
            edForm4.setTag(regex[3]);

        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
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
    public RequestParams getParams() {
        return null;
    }

    @Override
    public View getRootView() {
        return mainContent;
    }

    @Override
    public void showPromoSuccessMessage(String text, int color) {
        tvPromoSuccessMsg.setText(text);
        tvPromoSuccessMsg.setTextColor(color);
        tvPromoSuccessMsg.setVisibility(View.VISIBLE);
    }

    @Override
    public void showCashbackMessage(String text) {
        tvPromoCashbackMsg.setText(text);
        tvPromoCashbackMsg.setVisibility(View.VISIBLE);
        batal.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideSuccessMessage() {
        tvPromoSuccessMsg.setVisibility(View.GONE);
        tvPromoCashbackMsg.setVisibility(View.GONE);
        batal.setVisibility(View.GONE);
    }

    @Override
    public void showEmailTooltip() {
        tooltipTitle.setText(getResources().getString(R.string.tujuan_pengiriman_tiket));
        tooltipSubtitle.setText(getResources().getString(R.string.emailinfo_text));
        tooltipLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMoreinfoTooltip() {
        tooltipTitle.setText(getResources().getString(R.string.data_pelanggan_tambahan));
        tooltipSubtitle.setText(getResources().getString(R.string.additionallinfo_text));
        tooltipLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideTooltip() {
        tooltipLayout.setVisibility(View.GONE);
    }

    @OnClick(R2.id.btn_go_to_payment)
    void clickPay() {
        mPresenter.proceedToPayment();
    }

    @OnClick(R2.id.update_promo)
    void clickUpdatePromo() {
        mPresenter.updatePromoCode(edPromo.getText().toString());
    }

    @OnClick(R2.id.update_email)
    void updateEmail() {
        mPresenter.updateEmail(tvVisitorNames.getText().toString());
    }

    @OnClick(R2.id.update_number)
    void updateNumber() {
        mPresenter.updateNumber(tvTelephone.getText().toString());
    }

    @OnClick(R2.id.batal)
    void dismissPromoCode() {
        mPresenter.updatePromoCode("");
    }

    @OnClick({R2.id.info_email,
            R2.id.info_moreinfo,
            R2.id.button_dismisstooltip})
    void onClickInfoIcon(View view) {
        if (view.getId() == R.id.info_email) {
            mPresenter.clickEmailIcon();
        } else if (view.getId() == R.id.info_moreinfo) {
            mPresenter.clickMoreinfoIcon();
        } else if (view.getId() == R.id.button_dismisstooltip) {
            mPresenter.clickDismissTooltip();
        }
    }

    @Override
    public EventComponent getComponent() {
        if (eventComponent == null) initInjector();
        return eventComponent;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYMENT_REQUEST_CODE) {
            switch (resultCode) {
                case com.tokopedia.payment.activity.TopPayActivity.PAYMENT_SUCCESS:
                    getActivity().setResult(IDigitalModuleRouter.PAYMENT_SUCCESS);
                    finish();
                    break;
                case com.tokopedia.payment.activity.TopPayActivity.PAYMENT_FAILED:
                    showToastMessage(
                            getString(R.string.alert_payment_canceled_or_failed_digital_module)
                    );
                    //presenter.processGetCartDataAfterCheckout();
                    break;
                case com.tokopedia.payment.activity.TopPayActivity.PAYMENT_CANCELLED:
                    showToastMessage(getString(R.string.alert_payment_canceled_digital_module));
                    //presenter.processGetCartDataAfterCheckout();
                    break;
                default:
                    //presenter.processGetCartData();
                    break;
            }
        }
    }

    public void showToastMessage(String message) {
        View view = null;
        if (view != null) NetworkErrorHelper.showSnackbar(getActivity(), message);
        else Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @OnFocusChange({
            R2.id.ed_form_3,
            R2.id.ed_form_1,
            R2.id.ed_form_2,
            R2.id.ed_form_4})
    void validateEditText(EditText view) {
        mPresenter.validateEditText(view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getProfile();
    }

    public void setHolder(int resID, String label, ImageTextViewHolder holder) {

        holder.setImage(resID);
        holder.setTextView(label);

    }
}
