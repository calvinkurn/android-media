package com.tokopedia.flight.detail.view.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.domain.subscriber.model.ProfileInfo;
import com.tokopedia.flight.booking.view.adapter.FlightSimpleAdapter;
import com.tokopedia.flight.booking.view.viewmodel.SimpleViewModel;
import com.tokopedia.flight.common.util.FlightErrorUtil;
import com.tokopedia.flight.contactus.model.FlightContactUsPassData;
import com.tokopedia.flight.dashboard.view.activity.FlightDashboardActivity;
import com.tokopedia.flight.detail.presenter.ExpandableOnClickListener;
import com.tokopedia.flight.detail.presenter.FlightDetailOrderContract;
import com.tokopedia.flight.detail.presenter.FlightDetailOrderPresenter;
import com.tokopedia.flight.detail.view.adapter.FlightDetailOrderAdapter;
import com.tokopedia.flight.detail.view.adapter.FlightDetailOrderTypeFactory;
import com.tokopedia.flight.orderlist.di.FlightOrderComponent;
import com.tokopedia.flight.orderlist.domain.model.FlightOrder;
import com.tokopedia.flight.orderlist.domain.model.FlightOrderJourney;
import com.tokopedia.flight.orderlist.view.fragment.FlightResendETicketDialogFragment;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderDetailPassData;
import com.tokopedia.flight.review.view.adapter.FlightBookingReviewPassengerAdapter;
import com.tokopedia.flight.review.view.adapter.FlightBookingReviewPassengerAdapterTypeFactory;
import com.tokopedia.flight.review.view.model.FlightDetailPassenger;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 12/12/17.
 */

public class FlightDetailOrderFragment extends BaseDaggerFragment implements FlightDetailOrderContract.View, ExpandableOnClickListener {

    private static final int REQUEST_CODE_RESEND_ETICKET_DIALOG = 1;
    private static final String RESEND_ETICKET_DIALOG_TAG = "resend_eticket_dialog_tag";
    public static final String EXTRA_ORDER_DETAIL_PASS = "EXTRA_ORDER_DETAIL_PASS";
    private static final String CANCEL_SOLUTION_ID = "1378";
    private static final int CONTACT_US_REQUEST_CODE = 100;
    @Inject
    FlightDetailOrderPresenter flightDetailOrderPresenter;
    private TextView orderId;
    private ImageView copyOrderId;
    private View containerDownloadEticket;
    private TextView orderStatus;
    private TextView transactionDate;
    private View layoutExpendablePassenger;
    private TextView titleExpendablePassenger;
    private AppCompatImageView imageExpendablePassenger;
    private VerticalRecyclerView recyclerViewFlight;
    private VerticalRecyclerView recyclerViewPassenger;
    private RecyclerView recyclerViewPrice;
    private View containerDownloadInvoice;
    private TextView totalPrice;
    private TextView orderHelp;
    private Button buttonCancelTicket;
    private Button buttonRescheduleTicket;
    private Button buttonReorder;
    private ProgressDialog progressDialog;
    private FlightDetailOrderAdapter flightDetailOrderAdapter;
    private FlightBookingReviewPassengerAdapter flightBookingReviewPassengerAdapter;
    private FlightSimpleAdapter flightBookingReviewPriceAdapter;
    private FlightOrderDetailPassData flightOrderDetailPassData;
    private FlightOrder flightOrder;
    private String eticketLink = "";
    private String invoiceLink = "";
    private String cancelMessage = "";
    private boolean isPassengerInfoShowed = true;
    private LinearLayout paymentInfoLayout;
    private LinearLayout paymentCostLayout;
    private LinearLayout paymentDueDateLayout;
    private TextView tvPaymentDescriptionLabel;
    private TextView tvPaymentDescription;
    private TextView tvPaymentCost;
    private TextView tvPaymentCostLabel;
    private TextView tvPaymentDueDate;

    public static Fragment createInstance(FlightOrderDetailPassData flightOrderDetailPassData) {
        FlightDetailOrderFragment flightDetailOrderFragment = new FlightDetailOrderFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_ORDER_DETAIL_PASS, flightOrderDetailPassData);
        flightDetailOrderFragment.setArguments(bundle);
        return flightDetailOrderFragment;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        getComponent(FlightOrderComponent.class)
                .inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flightOrderDetailPassData = getArguments().getParcelable(EXTRA_ORDER_DETAIL_PASS);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flight_detail_order, container, false);
        orderId = view.findViewById(R.id.order_id_detail);
        copyOrderId = view.findViewById(R.id.copy_order_id);
        containerDownloadEticket = view.findViewById(R.id.container_download_eticket);
        orderStatus = view.findViewById(R.id.status_ticket);
        transactionDate = view.findViewById(R.id.transaction_date);
        layoutExpendablePassenger = view.findViewById(R.id.layout_expendable_passenger);
        titleExpendablePassenger = view.findViewById(R.id.title_expendable_passenger);
        imageExpendablePassenger = view.findViewById(R.id.image_expendable_passenger);
        recyclerViewFlight = view.findViewById(R.id.recycler_view_flight);
        recyclerViewPassenger = view.findViewById(R.id.recycler_view_data_passenger);
        recyclerViewPrice = view.findViewById(R.id.recycler_view_detail_price);
        containerDownloadInvoice = view.findViewById(R.id.container_download_invoice);
        totalPrice = view.findViewById(R.id.total_price);
        orderHelp = view.findViewById(R.id.help);
        buttonCancelTicket = view.findViewById(R.id.button_cancel);
        buttonRescheduleTicket = view.findViewById(R.id.button_reschedule);
        buttonReorder = view.findViewById(R.id.button_reorder);

        paymentInfoLayout = (LinearLayout) view.findViewById(R.id.payment_info_layout);
        paymentCostLayout = (LinearLayout) view.findViewById(R.id.payment_cost_layout);
        paymentDueDateLayout = (LinearLayout) view.findViewById(R.id.payment_due_date_layout);
        tvPaymentDescriptionLabel = (TextView) view.findViewById(R.id.tv_payment_description_label);
        tvPaymentDescription = (TextView) view.findViewById(R.id.tv_payment_description);
        tvPaymentCost = (TextView) view.findViewById(R.id.tv_payment_cost);
        tvPaymentCostLabel = (TextView) view.findViewById(R.id.tv_payment_cost_label);
        tvPaymentDueDate = (TextView) view.findViewById(R.id.tv_payment_due_date);
        progressDialog = new ProgressDialog(getActivity());

        setViewClickListener();

        FlightDetailOrderTypeFactory flightDetailOrderTypeFactory = new FlightDetailOrderTypeFactory(this);
        flightDetailOrderAdapter = new FlightDetailOrderAdapter(flightDetailOrderTypeFactory);
        FlightBookingReviewPassengerAdapterTypeFactory flightBookingReviewPassengerAdapterTypeFactory = new FlightBookingReviewPassengerAdapterTypeFactory();
        flightBookingReviewPassengerAdapter = new FlightBookingReviewPassengerAdapter(flightBookingReviewPassengerAdapterTypeFactory);
        flightBookingReviewPriceAdapter = new FlightSimpleAdapter();

        recyclerViewFlight.setAdapter(flightDetailOrderAdapter);
        recyclerViewPassenger.setAdapter(flightBookingReviewPassengerAdapter);
        recyclerViewPrice.setAdapter(flightBookingReviewPriceAdapter);
        recyclerViewPrice.setLayoutManager(new LinearLayoutManager(getActivity()));
        progressDialog.setMessage(getString(R.string.flight_booking_loading_title));
        progressDialog.setCancelable(false);
        orderId.setText(flightOrderDetailPassData.getOrderId());

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        flightDetailOrderPresenter.attachView(this);
        flightDetailOrderPresenter.getDetail(flightOrderDetailPassData.getOrderId(), flightOrderDetailPassData);
        flightDetailOrderPresenter.onGetProfileData();
    }

    void setViewClickListener() {
        copyOrderId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(getString(R.string.flight_label_order_id), orderId.getText().toString());
                clipboard.setPrimaryClip(clip);
                clipboard.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
                    @Override
                    public void onPrimaryClipChanged() {
                        if (getActivity() != null) {
                            Toast.makeText(getActivity(), R.string.flight_label_copy_clipboard, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        buttonCancelTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flightDetailOrderPresenter.actionCancelOrderButtonClicked();
            }
        });

        buttonRescheduleTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        containerDownloadEticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flightDetailOrderPresenter.onDownloadETicketButtonClicked();
            }
        });

        containerDownloadInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity().getApplication() instanceof FlightModuleRouter
                        && ((FlightModuleRouter) getActivity().getApplication())
                        .getWebviewActivity(getActivity(), invoiceLink) != null) {
                    startActivity(((FlightModuleRouter) getActivity().getApplication())
                            .getWebviewActivity(getActivity(), invoiceLink));
                }
            }
        });

        orderHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flightDetailOrderPresenter.onHelpButtonClicked();
            }
        });
        buttonReorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flightDetailOrderPresenter.actionReorderButtonClicked();
            }
        });

        layoutExpendablePassenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageExpendablePassenger.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.rotate_reverse));
                togglePassengerInfo();
            }
        });
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        progressDialog.dismiss();
    }

    @Override
    public void onErrorGetOrderDetail(Throwable e) {
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), FlightErrorUtil.getMessageFromException(getActivity(), e), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                flightDetailOrderPresenter.getDetail(flightOrderDetailPassData.getOrderId(), flightOrderDetailPassData);
            }
        }).showRetrySnackbar();
    }

    @Override
    public void updateFlightList(List<FlightOrderJourney> journeys) {
        flightDetailOrderAdapter.addElement(journeys);
        flightDetailOrderAdapter.notifyDataSetChanged();
    }

    @Override
    public void updatePassengerList(List<FlightDetailPassenger> flightDetailPassengers) {
        if (flightBookingReviewPassengerAdapter.getDataSize() < 2) {
            removePassengerRecyclerDivider();
        }

        flightBookingReviewPassengerAdapter.addElement(flightDetailPassengers);
        flightBookingReviewPassengerAdapter.notifyDataSetChanged();
    }

    @Override
    public void updatePrice(List<SimpleViewModel> priceList, String totalPrice) {
        flightBookingReviewPriceAdapter.setViewModels(priceList);
        flightBookingReviewPriceAdapter.notifyDataSetChanged();
        this.totalPrice.setText(totalPrice);
    }

    @Override
    public void setTransactionDate(String transactionDate) {
        this.transactionDate.setText(transactionDate);
    }

    @Override
    public void updateOrderData(String eTicketLink, String invoiceLink, String cancelMessage) {
        this.eticketLink = eTicketLink;
        this.invoiceLink = invoiceLink;
        this.cancelMessage = cancelMessage;
    }

    private void removePassengerRecyclerDivider() {
        recyclerViewPassenger.clearItemDecoration();
    }

    private void togglePassengerInfo() {
        if (isPassengerInfoShowed) {
            hidePassengerInfo();
        } else {
            showPassengerInfo();
        }
    }

    private void hidePassengerInfo() {
        isPassengerInfoShowed = false;
        recyclerViewPassenger.setVisibility(View.GONE);
        imageExpendablePassenger.setRotation(180);
    }

    private void showPassengerInfo() {
        isPassengerInfoShowed = true;
        recyclerViewPassenger.setVisibility(View.VISIBLE);
        imageExpendablePassenger.setRotation(0);
    }

    @Override
    public void updateViewStatus(String orderStatusString, int color, boolean isTicketVisible, boolean isScheduleVisible,
                                 boolean isCancelVisible, boolean isReorderVisible) {
        orderStatus.setText(orderStatusString);
        orderStatus.setTextColor(ContextCompat.getColor(getActivity(), color));
        if (isTicketVisible) {
            containerDownloadEticket.setVisibility(View.VISIBLE);
        } else {
            containerDownloadEticket.setVisibility(View.GONE);
        }
        if (isScheduleVisible) {
            buttonRescheduleTicket.setVisibility(View.VISIBLE);
        } else {
            buttonRescheduleTicket.setVisibility(View.GONE);
        }
        if (isCancelVisible) {
            buttonCancelTicket.setVisibility(View.VISIBLE);
        } else {
            buttonCancelTicket.setVisibility(View.GONE);
        }
        if (isReorderVisible) {
            buttonReorder.setVisibility(View.VISIBLE);
        } else {
            buttonReorder.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        flightDetailOrderPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public String getCancelMessage() {
        return cancelMessage;
    }

    @Override
    public void navigateToWebview(String url) {
        if (getActivity().getApplication() instanceof FlightModuleRouter
                && ((FlightModuleRouter) getActivity().getApplication())
                .getDefaultContactUsIntent(getActivity()) != null) {
            startActivity(((FlightModuleRouter) getActivity().getApplication())
                    .getDefaultContactUsIntent(getActivity()));
        }
    }

    @Override
    public void navigateToFlightHomePage() {
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(getActivity());
        if (getActivity().getApplication() instanceof FlightModuleRouter
                && ((FlightModuleRouter) getActivity().getApplication())
                .getHomeIntent(getActivity()) != null) {
            Intent intent = ((FlightModuleRouter) getActivity().getApplication())
                    .getHomeIntent(getActivity());
            taskStackBuilder.addNextIntent(intent);
        }
        taskStackBuilder.addNextIntent(FlightDashboardActivity.getCallingIntent(getActivity()));
        taskStackBuilder.startActivities();
    }

    @Override
    public void renderFlightOrder(FlightOrder flightOrder) {
        this.flightOrder = flightOrder;
    }

    @Override
    public FlightOrder getFlightOrder() {
        return flightOrder;
    }

    @Override
    public void navigateToContactUs(FlightOrder flightOrder) {
        startActivityForResult(getCallintIntent(
                CANCEL_SOLUTION_ID,
                flightOrder.getId(),
                getString(R.string.flight_contact_us_cancel_desc),
                getString(R.string.flight_contact_us_cancel_attc),
                cancelMessage,
                getString(R.string.flight_contact_us_cancel_toolbar))
                , CONTACT_US_REQUEST_CODE);
    }

    private Intent getCallintIntent(String solutionId,
                                    String orderId,
                                    String descriptionTitle,
                                    String attachmentTitle,
                                    String description,
                                    String toolbarTitle) {

        FlightContactUsPassData passData = new FlightContactUsPassData();
        passData.setSolutionId(solutionId);
        passData.setOrderId(orderId);
        passData.setDescriptionTitle(descriptionTitle);
        passData.setAttachmentTitle(attachmentTitle);
        passData.setDescription(description);
        passData.setToolbarTitle(toolbarTitle);

        if (getActivity().getApplication() instanceof FlightModuleRouter) {
            return ((FlightModuleRouter) getActivity().getApplication()).getContactUsIntent(getActivity(), passData);
        } else {
            throw new RuntimeException("Application Module should implement FlightModuleRouter");
        }
    }

    @Override
    public void onCloseExpand(int position) {
        // do something to scroll the view
    }

    @Override
    public void showPaymentInfoLayout() {
        paymentInfoLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hidePaymentInfoLayout() {
        paymentInfoLayout.setVisibility(View.GONE);
    }

    @Override
    public void setPaymentLabel(int resId) {
        tvPaymentDescriptionLabel.setText(resId);
    }

    @Override
    public void setPaymentDescription(CharSequence description) {
        tvPaymentDescription.setText(description);
    }

    @Override
    public void setTotalTransfer(String price) {
        tvPaymentCost.setVisibility(View.VISIBLE);
        tvPaymentCost.setText(price);
    }

    @Override
    public void hideTotalTransfer() {
        paymentCostLayout.setVisibility(View.GONE);
        tvPaymentCostLabel.setVisibility(View.GONE);
        tvPaymentCost.setVisibility(View.GONE);
    }

    @Override
    public void setPaymentDueDate(String dueDate) {
        paymentDueDateLayout.setVisibility(View.VISIBLE);
        tvPaymentDueDate.setText(dueDate);
    }

    @Override
    public void hidePaymentDueDate() {
        paymentDueDateLayout.setVisibility(View.GONE);
    }

    @Override
    public void navigateToInputEmailForm(String userId, String userEmail) {
        DialogFragment dialogFragment = FlightResendETicketDialogFragment.newInstace(flightOrderDetailPassData.getOrderId(), userId, userEmail);
        dialogFragment.setTargetFragment(this, REQUEST_CODE_RESEND_ETICKET_DIALOG);
        dialogFragment.show(getFragmentManager().beginTransaction(), RESEND_ETICKET_DIALOG_TAG);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_RESEND_ETICKET_DIALOG:
                if (resultCode == Activity.RESULT_OK) {
                    showGreenSnackbar(R.string.resend_eticket_success);
                }
                break;
        }
    }


    @Override
    public Observable<ProfileInfo> getProfileObservable() {
        if (getActivity().getApplication() instanceof FlightModuleRouter
                && ((FlightModuleRouter) getActivity().getApplication())
                .getProfile() != null) {
            return ((FlightModuleRouter) getActivity().getApplication())
                    .getProfile();
        }
        return Observable.empty();
    }

    private void showGreenSnackbar(int resId) {
        NetworkErrorHelper.showGreenCloseSnackbar(getActivity(), getString(resId));
    }
}
