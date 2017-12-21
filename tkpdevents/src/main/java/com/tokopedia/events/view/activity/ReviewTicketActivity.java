package com.tokopedia.events.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.events.R;
import com.tokopedia.events.R2;
import com.tokopedia.events.di.DaggerEventComponent;
import com.tokopedia.events.di.EventComponent;
import com.tokopedia.events.di.EventModule;
import com.tokopedia.events.view.contractor.EventReviewTicketsContractor;
import com.tokopedia.events.view.presenter.EventReviewTicketPresenter;
import com.tokopedia.events.view.utils.CurrencyUtil;
import com.tokopedia.events.view.viewmodel.PackageViewModel;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReviewTicketActivity extends TActivity implements HasComponent<EventComponent>,
        EventReviewTicketsContractor.EventReviewTicketsView {

    @BindView(R2.id.event_image_small)
    ImageView eventImageSmall;
    @BindView(R2.id.event_name_tv)
    TextView eventNameTv;
    @BindView(R2.id.event_time_tv)
    TextView eventTimeTv;
    @BindView(R2.id.event_address_tv)
    TextView eventAddressTv;
    @BindView(R2.id.event_total_tickets)
    TextView eventTotalTickets;
    @BindView(R2.id.tv_ticket_summary)
    TextView tvTicketSummary;
    @BindView(R2.id.tv_visitor_names)
    EditText tvVisitorNames;
    @BindView(R2.id.tv_telephone)
    EditText tvTelephone;
    @BindView(R2.id.tv_provider_name)
    TextView tvProviderName;
    @BindView(R2.id.tv_base_fare)
    TextView tvBaseFare;
    @BindView(R2.id.tv_conv_fees)
    TextView tvConvFees;
    @BindView(R2.id.button_textview)
    TextView buttonTextview;
    @BindView(R2.id.btn_go_to_payment)
    View btnGoToPayment;


    EventComponent eventComponent;
    @Inject
    EventReviewTicketPresenter mPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_booking_layout);
        ButterKnife.bind(this);
        executeInjector();
        mPresenter.attachView(this);
        mPresenter.initialize();
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

        startActivityForResult(intent, requestCode);
        //startActivity(intent);

    }

    @Override
    public void renderFromPackageVM(PackageViewModel packageViewModel) {
        String timerange = packageViewModel.getTimeRange();
        ImageHandler.loadImageCover2(eventImageSmall, packageViewModel.getThumbnailApp());
        eventNameTv.setText(packageViewModel.getDisplayName());
        eventTimeTv.setText(timerange);
        eventAddressTv.setText(packageViewModel.getAddress());
        eventTotalTickets.setText(String.format(getString(R.string.jumlah_tiket),
                packageViewModel.getSelectedQuantity()));

        tvTelephone.setText(SessionHandler.getPhoneNumber());
        tvProviderName.setText(String.format(getString(R.string.fare_breakup), packageViewModel.getDisplayName()));
        int baseFare = packageViewModel.getSelectedQuantity() * packageViewModel.getSalesPrice();
        tvBaseFare.setText("Rp " + CurrencyUtil.convertToCurrencyString(baseFare));
        int convFees = packageViewModel.getConvenienceFee();
        tvConvFees.setText("Rp " + CurrencyUtil.convertToCurrencyString(convFees));
        buttonTextview.setText(String.format(getString(R.string.pay_button), CurrencyUtil.convertToCurrencyString(baseFare + convFees)));
        tvTicketSummary.setText(String.format(getString(R.string.x_type),
                packageViewModel.getSelectedQuantity(), packageViewModel.getDisplayName()));


    }

    @Override
    public void setEmailID(String emailID) {
        tvVisitorNames.setText(emailID);
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

    @OnClick(R2.id.btn_go_to_payment)
    void clickPay() {
        mPresenter.proceedToPayment();
    }

    @Override
    public EventComponent getComponent() {
        if (eventComponent == null) initInjector();
        return eventComponent;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == com.tokopedia.payment.activity.TopPayActivity.REQUEST_CODE) {
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
}
