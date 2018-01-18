package com.tokopedia.transaction.purchase.detail.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.router.InboxRouter;
import com.tokopedia.core.router.TkpdInboxRouter;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter;
import com.tokopedia.core.router.transactionmodule.TransactionRouter;
import com.tokopedia.core.tracking.activity.TrackingActivity;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.design.bottomsheet.BottomSheetCallAction;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.purchase.detail.adapter.OrderItemAdapter;
import com.tokopedia.transaction.purchase.detail.customview.OrderDetailButtonLayout;
import com.tokopedia.transaction.purchase.detail.di.DaggerOrderDetailComponent;
import com.tokopedia.transaction.purchase.detail.di.OrderDetailComponent;
import com.tokopedia.transaction.purchase.detail.dialog.ComplaintDialog;
import com.tokopedia.transaction.purchase.detail.dialog.FinishOrderDialog;
import com.tokopedia.transaction.purchase.detail.fragment.CancelOrderFragment;
import com.tokopedia.transaction.purchase.detail.fragment.CancelSearchFragment;
import com.tokopedia.transaction.purchase.detail.model.detail.viewmodel.OrderDetailData;
import com.tokopedia.transaction.purchase.detail.presenter.OrderDetailPresenterImpl;
import com.tokopedia.transaction.purchase.receiver.TxListUIReceiver;

import javax.inject.Inject;

/**
 * Created by kris on 11/2/17. Tokopedia
 */

public class OrderDetailActivity extends TActivity
        implements OrderDetailView,
        FinishOrderDialog.FinishOrderDialogListener,
        ComplaintDialog.ComplaintDialogListener,
        CancelOrderFragment.CancelOrderListener,
        CancelSearchFragment.CancelSearchReplacementListener {

    public static final int REQUEST_CODE_ORDER_DETAIL = 111;
    private static final String VALIDATION_FRAGMENT_TAG = "validation_fragments";
    private static final String EXTRA_ORDER_ID = "EXTRA_ORDER_ID";
    private static final String EXTRA_USER_MODE = "EXTRA_USER_MODE";
    private static final int BUYER_MODE = 1;
    private static final int SELLER_MODE = 2;

    @Inject
    OrderDetailPresenterImpl presenter;

    private TkpdProgressDialog mainProgressDialog;

    private TkpdProgressDialog smallProgressDialog;

    public static Intent createInstance(Context context, String orderId) {
        Intent intent = new Intent(context, OrderDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_ORDER_ID, orderId);
        bundle.putInt(EXTRA_USER_MODE, BUYER_MODE);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent createSellerInstance(Context context, String orderId) {
        Intent intent = new Intent(context, OrderDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_ORDER_ID, orderId);
        bundle.putInt(EXTRA_USER_MODE, SELLER_MODE);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.order_detail_page);
        mainProgressDialog = new TkpdProgressDialog(this, TkpdProgressDialog.MAIN_PROGRESS);
        smallProgressDialog = new TkpdProgressDialog(this, TkpdProgressDialog.NORMAL_PROGRESS);
        initInjector();
        presenter.setMainViewListener(this);
        presenter.fetchData(this, getExtraOrderId(), getExtraUserMode());
    }

    private void initInjector() {
        OrderDetailComponent component = DaggerOrderDetailComponent
                .builder()
                .appComponent(getApplicationComponent())
                .build();
        component.inject(this);
    }

    private void initView(OrderDetailData data) {
        setStatusView(data);
        setDriverInfoView(data);
        setItemListView(data);
        setInvoiceView(data);
        setDescriptionView(data);
        setPriceView(data);
        setButtonView(data);

    }

    private void setStatusView(OrderDetailData data) {
        ViewGroup statusLayout = findViewById(R.id.header_view);
        TextView statusTextView = findViewById(R.id.text_view_status);
        ImageView imageView = findViewById(R.id.order_detail_status_image);

        statusLayout.setOnClickListener(onStatusLayoutClickedListener(data.getOrderId()));

        if (null != data.getOrderImage() && !data.getOrderImage().equals("")) {
            statusLayout.setVisibility(View.VISIBLE);
            statusTextView.setText(data.getOrderStatus());
            ImageHandler.LoadImage(imageView, data.getOrderImage());
        } else {
            statusLayout.setVisibility(View.GONE);
        }
    }

    private void setPriceView(OrderDetailData data) {
        TextView itemAmount = findViewById(R.id.item_amount);
        TextView productPrice = findViewById(R.id.product_price);
        TextView deliveryPrice = findViewById(R.id.delivery_price);
        TextView insurancePrice = findViewById(R.id.insurance_price);
        TextView additionalFee = findViewById(R.id.additional_fee);
        TextView totalPayment = findViewById(R.id.total_payment);
        itemAmount.setText(data.getTotalItemQuantity());
        productPrice.setText(data.getProductPrice());
        deliveryPrice.setText(data.getDeliveryPrice());
        insurancePrice.setText(data.getInsurancePrice());
        additionalFee.setText(data.getAdditionalFee());
        totalPayment.setText(data.getTotalPayment());
    }

    private void setDriverInfoView(OrderDetailData data) {
        LinearLayout layoutDriverInfo = findViewById(R.id.layout_driver_info);

        if (data.getDriverName() != null) {
            ImageView driverPhoto = findViewById(R.id.driver_photo);
            TextView driverName = findViewById(R.id.driver_name);
            TextView driverPhone = findViewById(R.id.driver_phone);
            TextView driverVehicle = findViewById(R.id.driver_vehicle);
            TextView btnCallDriver = findViewById(R.id.btn_call_driver);

            driverName.setText(data.getDriverName());
            driverPhone.setText(data.getDriverPhone());
            driverVehicle.setText(data.getDriverVehicle());

            ImageHandler.loadImageCircle2(this, driverPhoto, data.getDriverImage());

            btnCallDriver.setOnClickListener(getClickListenerCallDriver(data.getDriverPhone()));

            layoutDriverInfo.setVisibility(View.VISIBLE);
        } else {
            layoutDriverInfo.setVisibility(View.GONE);
        }
    }

    private View.OnClickListener getClickListenerCallDriver(final String driverPhone) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BottomSheetCallAction.Builder(OrderDetailActivity.this)
                        .actionListener(new BottomSheetCallAction.ActionListener() {
                            @Override
                            public void onCallAction1Clicked(BottomSheetCallAction.CallActionData callActionData) {
                                openDialCaller(callActionData.getPhoneNumber());
                            }

                            @Override
                            public void onCallAction2Clicked(BottomSheetCallAction.CallActionData callActionData) {
                                openSmsApplication(callActionData.getPhoneNumber());
                            }
                        })
                        .callActionData(
                                new BottomSheetCallAction.CallActionData().setPhoneNumber(driverPhone
                                ).setLabelAction1(
                                        getString(R.string.label_call_ondemand_driver_logistic_action_1)
                                ).setLabelAction2(
                                        getString(R.string.label_call_ondemand_driver_logistic_action_2)
                                )
                        ).build().show();
            }
        };
    }

    void openSmsApplication(String phoneNumber) {
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));
        try {
            startActivity(smsIntent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, getString(R.string.error_message_sms_app_not_found),
                    Toast.LENGTH_SHORT).show();
        }
    }

    void openDialCaller(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, getString(R.string.error_message_phone_dialer_app_not_found),
                    Toast.LENGTH_SHORT).show();
        }
    }


    private void setButtonView(OrderDetailData data) {
        OrderDetailButtonLayout buttonLayout = findViewById(R.id.button_layout);
        buttonLayout.initButton(this, presenter, data);
    }

    private void setItemListView(OrderDetailData data) {
        RecyclerView itemListRecycleView = findViewById(R.id.item_list);
        itemListRecycleView.setLayoutManager(new LinearLayoutManager(this));
        itemListRecycleView.setAdapter(new OrderItemAdapter(data.getItemList(), this));
        itemListRecycleView.setNestedScrollingEnabled(false);
    }

    private void setInvoiceView(OrderDetailData data) {
        ViewGroup invoiceLayout = findViewById(R.id.invoice_layout);
        TextView invoiceNumber = findViewById(R.id.invoice_number);
        invoiceNumber.setText(data.getInvoiceNumber());
        invoiceLayout.setOnClickListener(onInvoiceClickedListener(data));
    }

    private void setDescriptionView(OrderDetailData data) {
        TextView descriptionDate = findViewById(R.id.description_date);
        TextView descriptionBuyerName = findViewById(R.id.description_buyer_name);
        TextView descriptionCourierName = findViewById(R.id.description_courier_name);
        TextView descriptionShippingAddess = findViewById(R.id.description_shipping_address);
        TextView descriptionPartialOrderStatus = findViewById(R.id.description_partial_order_status);
        descriptionDate.setText(data.getPurchaseDate());
        descriptionBuyerName.setText(data.getBuyerName());
        descriptionCourierName.setText(data.getCourierName());
        descriptionShippingAddess.setText(data.getShippingAddress());
        descriptionPartialOrderStatus.setText(data.getPartialOrderStatus());
        setResponseTimeView(data);
        setPreorderView(data);
        setDropshipperView(data);
    }

    private void setDropshipperView(OrderDetailData data) {
        TextView dropshipperMode = findViewById(R.id.dropshipper_mode);
        LinearLayout dropshipperNameLayout = findViewById(R.id.dropshipper_name_layout);
        TextView dropshipperName = findViewById(R.id.dropshipper_name);
        LinearLayout dropshipperPhoneLayout = findViewById(R.id.dropshipper_phone_layout);
        TextView dropshipperPhone = findViewById(R.id.dropshipper_phone);
        if (data.getDropshipperName() == null || data.getDropshipperName().isEmpty()) {
            dropshipperMode.setText(getString(R.string.label_title_button_no));
            dropshipperNameLayout.setVisibility(View.GONE);
            dropshipperPhoneLayout.setVisibility(View.GONE);
        } else {
            dropshipperMode.setText(getString(R.string.label_title_button_yes));
            dropshipperName.setText(data.getDropshipperName());
            dropshipperPhone.setText(data.getDropshipperPhone());
        }
    }

    private void setPreorderView(OrderDetailData data) {
        LinearLayout preorderLayout = findViewById(R.id.preorder_layout);
        TextView preorderTime = findViewById(R.id.preorder_time);
        if (data.isPreorder()) preorderTime.setText(data.getPreorderPeriodText());
        else preorderLayout.setVisibility(View.GONE);
    }

    private void setResponseTimeView(OrderDetailData data) {
        LinearLayout timeLimitLayout = findViewById(R.id.time_limit_layout);
        TextView responseTime = findViewById(R.id.description_response_time);
        if (data.getResponseTimeLimit() == null || data.getResponseTimeLimit().isEmpty()) {
            timeLimitLayout.setVisibility(View.GONE);
        } else {
            responseTime.setText(data.getResponseTimeLimit());
            responseTime.setBackgroundResource(R.drawable.dark_blue_rounded_label);
            GradientDrawable drawableBorder = (GradientDrawable) responseTime.getBackground().getCurrent().mutate();
            drawableBorder.setColor(Color.parseColor(data.getDeadlineColorString()));
        }
    }

    private View.OnClickListener onStatusLayoutClickedListener(final String orderId) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = OrderHistoryActivity
                        .createInstance(OrderDetailActivity.this, orderId, getExtraUserMode());
                startActivity(intent);
            }
        };
    }

    @Override
    public void onReceiveDetailData(OrderDetailData data) {
        initView(data);
    }

    @Override
    public void onError(String errorMessage) {
        NetworkErrorHelper.showEmptyState(this,
                getMainView(),
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        presenter.fetchData(OrderDetailActivity.this,
                                getExtraOrderId(),
                                getExtraUserMode());
                    }
                });
    }

    @Override
    public void goToProductInfo(ProductPass productPass) {
        Intent intent = ProductDetailRouter
                .createInstanceProductDetailInfoActivity(this, productPass);
        startActivity(intent);
    }

    @Override
    public void trackShipment(String orderId) {
        Intent intent = new Intent(OrderDetailActivity.this, TrackingActivity.class);
        intent.putExtra("OrderID", orderId);
        startActivity(intent);
    }

    @Override
    public void showConfirmDialog(String orderId, String orderStatus) {
        FinishOrderDialog dialog = FinishOrderDialog.createDialog(orderId, orderStatus);
        dialog.show(getFragmentManager(), dialog.getClass().getSimpleName());
    }

    @Override
    public void showComplaintDialog(String shopName, String orderId) {
        ComplaintDialog dialog = ComplaintDialog.createDialog(orderId, shopName);
        dialog.show(getFragmentManager(), dialog.getClass().getSimpleName());
    }

    @Override
    public void onAskSeller(OrderDetailData orderData) {
        Intent intent = ((TkpdInboxRouter) MainApplication.getAppContext())
                .getAskSellerIntent(this,
                        orderData.getShopId(),
                        orderData.getShopName(),
                        orderData.getInvoiceNumber(),
                        MethodChecker.fromHtml(
                                getString(R.string.dialog_message_ask_seller)
                                        .replace("XXX",
                                                orderData.getInvoiceUrl())
                        ).toString(),
                        TkpdInboxRouter.TX_ASK_SELLER, orderData.getShopLogo());
        startActivity(intent);
    }

    @Override
    public void onAskBuyer(OrderDetailData orderData) {
        //TODO later change get Shop Logo with get buyer logo once available
        Intent intent = ((TkpdInboxRouter) MainApplication.getAppContext())
                .getAskBuyerIntent(this,
                        orderData.getShopId(),
                        orderData.getShopName(),
                        orderData.getInvoiceNumber(),
                        MethodChecker.fromHtml(
                                getString(R.string.dialog_message_ask_seller)
                                        .replace("XXX",
                                                orderData.getInvoiceUrl())
                        ).toString(),
                        TkpdInboxRouter.TX_ASK_BUYER, orderData.getShopLogo());
        startActivity(intent);
    }

    @Override
    public void onOrderFinished(String message) {
        Toast.makeText(this, getString(R.string.success_finish_order_message), Toast.LENGTH_LONG).show();
        TxListUIReceiver.sendBroadcastForceRefreshListData(this);
        finish();
    }

    @Override
    public void onOrderCancelled(String message) {
        Toast.makeText(this, getString(R.string.success_request_cancel_order), Toast.LENGTH_LONG).show();
        TxListUIReceiver.sendBroadcastForceRefreshListData(this);
        finish();
    }

    @Override
    public void onRequestCancelOrder(OrderDetailData data) {
        if (getFragmentManager().findFragmentByTag(VALIDATION_FRAGMENT_TAG) == null) {
            CancelOrderFragment cancelOrderFragment = CancelOrderFragment.createFragment(data.getOrderId());
            getFragmentManager().beginTransaction()
                    .setCustomAnimations(R.animator.enter_bottom, R.animator.enter_bottom)
                    .add(R.id.main_view, cancelOrderFragment, VALIDATION_FRAGMENT_TAG)
                    .commit();
        }
    }

    @Override
    public void onCancelSearchReplacement(OrderDetailData data) {
        if (getFragmentManager().findFragmentByTag(VALIDATION_FRAGMENT_TAG) == null) {
            CancelSearchFragment cancelSearchFragment = CancelSearchFragment
                    .createFragment(data.getOrderId());
            getFragmentManager().beginTransaction()
                    .setCustomAnimations(R.animator.enter_bottom, R.animator.enter_bottom)
                    .add(R.id.main_view, cancelSearchFragment, VALIDATION_FRAGMENT_TAG)
                    .commit();
        }
    }

    @Override
    public void onSellerConfirmShipping(OrderDetailData data) {
        //TODO Bundle important things here, dont put entire model in the bundle!!
    }

    @Override
    public void onAcceptOrder(OrderDetailData data) {
        //TODO Bundle important things here, dont put entire model in the bundle!!
    }

    @Override
    public void onRequestPickup(OrderDetailData data) {
        //TODO Bundle important things here, dont put entire model in the bundle!!
    }

    @Override
    public void onChangeCourier(OrderDetailData data) {
        //TODO Bundle important things here, dont put entire model in the bundle!!
    }

    @Override
    public void onRejectOrder(OrderDetailData data) {
        //TODO Bundle important things here, dont put entire model in the bundle!!
    }

    @Override
    public void onChangeAwb(OrderDetailData data) {
        //TODO Bundle important things here, dont put entire model in the bundle!!
    }

    @Override
    public void onSearchCancelled(String message) {
        getFragmentManager().beginTransaction().remove(getFragmentManager()
                .findFragmentByTag(VALIDATION_FRAGMENT_TAG)).commit();
        Intent backIntent = new Intent();
        setResult(Activity.RESULT_OK, backIntent);
        finish();
    }

    @Override
    public void showMainViewLoadingPage() {
        mainProgressDialog.showDialog();
        getMainView().setVisibility(View.GONE);
    }

    @Override
    public void hideMainViewLoadingPage() {
        mainProgressDialog.dismiss();
        getMainView().setVisibility(View.VISIBLE);
    }

    @Override
    public void onViewComplaint(OrderDetailData data) {
        if (MainApplication.getAppContext() instanceof TransactionRouter) {
            Intent intent = ((TransactionRouter) MainApplication.getAppContext())
                    .getDetailResChatIntentBuyer(this, data.getResoId(), data.getShopName());
            startActivity(intent);
        }
    }

    @Override
    public void showErrorSnackbar(String errorMessage) {
        NetworkErrorHelper.showSnackbar(this, errorMessage);
    }

    @Override
    public void showProgressDialog() {
        smallProgressDialog.showDialog();
    }

    @Override
    public void dismissProgressDialog() {
        smallProgressDialog.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroyed();
    }

    private View.OnClickListener onInvoiceClickedListener(final OrderDetailData data) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.processInvoice(OrderDetailActivity.this, data);
            }
        };
    }

    private String getExtraOrderId() {
        return getIntent().getExtras().getString(EXTRA_ORDER_ID);
    }

    private int getExtraUserMode() {
        return getIntent().getExtras().getInt(EXTRA_USER_MODE, 0);
    }

    @Override
    public void onConfirmFinish(String orderId, String orderStatus) {
        presenter.processFinish(this, orderId, orderStatus);
        TxListUIReceiver.sendBroadcastForceRefreshListData(this);
    }

    @Override
    public void onComplaintClicked(String orderId) {
        Intent intent = InboxRouter.getCreateResCenterActivityIntent(this,
                orderId);
        startActivityForResult(intent, TransactionPurchaseRouter.CREATE_RESCENTER_REQUEST_CODE);
    }

    private View getMainView() {
        return findViewById(R.id.main_view);
    }

    @Override
    public void cancelOrder(String orderId, String notes) {
        presenter.cancelOrder(this, orderId, notes);
    }

    @Override
    public void cancelSearch(String orderId, int reasonId, String notes) {
        presenter.cancelReplacement(this, orderId, reasonId, notes);
        TxListUIReceiver.sendBroadcastForceRefreshListData(this);
    }

    @Override
    public void setToolbarCancelSearch(String titleToolbar, int drawable) {
        toolbar.setTitle(titleToolbar);
        toolbar.setNavigationIcon(drawable);
    }

    @Override
    public void onBackPressed() {
        setToolbarCancelSearch(getString(R.string.title_detail_transaction), R.drawable.ic_arrow_back_black);
        if (getFragmentManager().findFragmentByTag(VALIDATION_FRAGMENT_TAG) != null) {
            getFragmentManager().beginTransaction().remove(getFragmentManager()
                    .findFragmentByTag(VALIDATION_FRAGMENT_TAG)).commit();
        } else super.onBackPressed();
    }
}
