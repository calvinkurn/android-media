package com.tokopedia.transaction.purchase.detail.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.router.InboxRouter;
import com.tokopedia.core.router.TkpdInboxRouter;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter;
import com.tokopedia.core.router.transactionmodule.TransactionRouter;
import com.tokopedia.core.tracking.activity.TrackingActivity;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.design.bottomsheet.BottomSheetCallAction;
import com.tokopedia.design.bottomsheet.BottomSheetView;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.purchase.detail.adapter.OrderItemAdapter;
import com.tokopedia.transaction.purchase.detail.customview.OrderDetailButtonLayout;
import com.tokopedia.transaction.purchase.detail.di.DaggerOrderDetailComponent;
import com.tokopedia.transaction.purchase.detail.di.OrderDetailComponent;
import com.tokopedia.transaction.purchase.detail.dialog.AcceptOrderDialog;
import com.tokopedia.transaction.purchase.detail.dialog.AcceptPartialOrderDialog;
import com.tokopedia.transaction.purchase.detail.dialog.ComplaintDialog;
import com.tokopedia.transaction.purchase.detail.dialog.FinishOrderDialog;
import com.tokopedia.transaction.purchase.detail.fragment.CancelOrderFragment;
import com.tokopedia.transaction.purchase.detail.fragment.CancelSearchFragment;
import com.tokopedia.transaction.purchase.detail.fragment.CancelShipmentFragment;
import com.tokopedia.transaction.purchase.detail.fragment.ChangeAwbFragment;
import com.tokopedia.transaction.purchase.detail.fragment.RejectOrderFragment;
import com.tokopedia.transaction.purchase.detail.fragment.RequestPickupFragment;
import com.tokopedia.transaction.purchase.detail.model.detail.viewmodel.OrderDetailData;
import com.tokopedia.transaction.purchase.detail.model.rejectorder.EmptyVarianProductEditable;
import com.tokopedia.transaction.purchase.detail.model.rejectorder.WrongProductPriceWeightEditable;
import com.tokopedia.transaction.purchase.detail.presenter.OrderDetailPresenterImpl;
import com.tokopedia.transaction.purchase.receiver.TxListUIReceiver;

import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.transaction.purchase.detail.fragment.RejectOrderBaseFragment.FRAGMENT_REJECT_ORDER_SUB_MENU_TAG;
import static com.tokopedia.transaction.purchase.detail.fragment.RejectOrderFragment.REJECT_ORDER_MENU_FRAGMENT_TAG;
import static com.tokopedia.transaction.purchase.detail.fragment.RequestPickupFragment.INFO_FRAGMENT_TAG;

/**
 * Created by kris on 11/2/17. Tokopedia
 */

public class OrderDetailActivity extends TActivity
        implements OrderDetailView {

    public static final int REQUEST_CODE_ORDER_DETAIL = 111;
    private static final String VALIDATION_FRAGMENT_TAG = "validation_fragments";
    private static final String REJECT_ORDER_FRAGMENT_TAG = "reject_order_fragment_teg";
    private static final String EXTRA_ORDER_ID = "EXTRA_ORDER_ID";
    private static final String EXTRA_USER_MODE = "EXTRA_USER_MODE";
    private static final int CONFIRM_SHIPMENT_REQUEST_CODE = 16;
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
        setInsuranceNotificationView(data);
        setRejectionNoticeLayout(data);
        setStatusView(data);
        setDriverInfoView(data);
        setItemListView(data);
        setInvoiceView(data);
        setDescriptionView(data);
        setPriceView(data);
        setButtonView(data);
        setPickupPointView(data);
    }

    private void setInsuranceNotificationView(OrderDetailData data) {
        if(data.isShowInsuranceNotification() && getExtraUserMode() == SELLER_MODE) {
            ViewGroup notificationLayout = findViewById(R.id.notification_layout);
            TextView notificationTextView = findViewById(R.id.notification_text_view);
            notificationLayout.setVisibility(View.VISIBLE);
            notificationTextView.setText(Html.fromHtml(data.getInsuranceNotification()));
        }
    }

    private void setRejectionNoticeLayout(OrderDetailData data) {
        if (data.isRequestCancel()) {
            ViewGroup rejectionNoticeLayout = findViewById(R.id.rejection_notice_layout);
            TextView rejectionNoticeSubtitle = findViewById(R.id.rejection_notice_subtitle);
            rejectionNoticeLayout.setVisibility(View.VISIBLE);
            rejectionNoticeSubtitle.setText(
                    rejectionNoticeSubtitle
                            .getText()
                            .toString()
                            .replace("#", Html.fromHtml(data.getRequestCancelReason()))
            );
        }
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

    private void setPriceView(OrderDetailData data) {
        TextView itemAmount = findViewById(R.id.item_amount);
        TextView itemTotalWeight = findViewById(R.id.item_total_weight);
        TextView productPrice = findViewById(R.id.product_price);
        TextView deliveryPrice = findViewById(R.id.delivery_price);
        TextView insurancePrice = findViewById(R.id.insurance_price);
        TextView additionalFee = findViewById(R.id.additional_fee);
        TextView totalPayment = findViewById(R.id.total_payment);
        itemAmount.setText(data.getTotalItemQuantity());
        itemTotalWeight.setText(getString(R.string.weight_place_holder)
                .replace("WEIGHT", data.getTotalItemWeight()));
        productPrice.setText(data.getProductPrice());
        deliveryPrice.setText(data.getDeliveryPrice());
        insurancePrice.setText(data.getInsurancePrice());
        additionalFee.setText(data.getAdditionalFee());
        totalPayment.setText(data.getTotalPayment());
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
        if (getExtraUserMode() == SELLER_MODE) setBuyerInfo(data);
        else setShopInfo(data);
        TextView descriptionCourierName = findViewById(R.id.description_courier_name);
        TextView descriptionShippingAddess = findViewById(R.id.description_shipping_address);
        TextView descriptionPartialOrderStatus = findViewById(R.id.description_partial_order_status);
        descriptionDate.setText(data.getPurchaseDate());
        descriptionCourierName.setText(data.getCourierName());
        descriptionShippingAddess.setText(data.getShippingAddress());
        descriptionPartialOrderStatus.setText(data.getPartialOrderStatus());
        setResponseTimeView(data);
        setPreorderView(data);
        setDropshipperView(data);
    }

    private void setShopInfo(OrderDetailData data) {
        ViewGroup descriptionSellerLayout = findViewById(R.id.seller_description_layout);
        TextView descriptionShopName = findViewById(R.id.description_shop_name);
        descriptionShopName.setText(data.getShopName());
        descriptionSellerLayout.setVisibility(View.VISIBLE);
    }

    private void setBuyerInfo(OrderDetailData data) {
        ViewGroup descriptionBuyerLayout = findViewById(R.id.buyer_description_layout);
        TextView descriptionBuyerName = findViewById(R.id.description_buyer_name);
        descriptionBuyerLayout.setVisibility(View.VISIBLE);
        descriptionBuyerName.setText(data.getBuyerUserName());
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

    private void setPickupPointView(OrderDetailData data) {
        LinearLayout layoutPickupPointPinCode = findViewById(R.id.layout_pickup_point_pin_code);
        if (data.getPickupPinCode() != null) {
            ImageButton btPinCodeInfo = findViewById(R.id.bt_pin_code_info);
            TextView tvPinCode = findViewById(R.id.tv_pin_code);
            tvPinCode.setText(data.getPickupPinCode());
            btPinCodeInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BottomSheetView bottomSheetView = new BottomSheetView(OrderDetailActivity.this);
                    bottomSheetView.renderBottomSheet(new BottomSheetView.BottomSheetField
                            .BottomSheetFieldBuilder()
                            .setTitle(getString(R.string.title_bottomsheet_pin_code_pickup_booth))
                            .setBody(getString(R.string.message_bottomsheet_pin_code_pickup_booth))
                            .setImg(R.drawable.ic_pickup_point_pin_code)
                            .build());

                    bottomSheetView.show();
                }
            });
            layoutPickupPointPinCode.setVisibility(View.VISIBLE);
        } else {
            layoutPickupPointPinCode.setVisibility(View.GONE);
        }
    }

    private View.OnClickListener onStatusLayoutClickedListener(final String orderId) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = OrderHistoryActivity
                        .createInstance(OrderDetailActivity.this, orderId, getExtraUserMode());
                startActivity(intent);
                overridePendingTransition(0, R.anim.right_to_left_out);
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
        String id;
        String name;
        String logoUrl;
        if(getExtraUserMode() == SELLER_MODE) {
            id = orderData.getBuyerId();
            name = orderData.getBuyerUserName();
            logoUrl = orderData.getBuyerLogo();
        } else {
            id = orderData.getShopId();
            name = orderData.getShopName();
            logoUrl = orderData.getShopLogo();
        }
        Intent intent = ((TkpdInboxRouter) MainApplication.getAppContext())
                .getAskBuyerIntent(this,
                        id,
                        name,
                        orderData.getInvoiceNumber(),
                        MethodChecker.fromHtml(
                                getString(R.string.dialog_message_ask_seller)
                                        .replace("XXX",
                                                orderData.getInvoiceUrl())
                        ).toString(),
                        TkpdInboxRouter.TX_ASK_BUYER, logoUrl);
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
        Intent intent = ConfirmShippingActivity.createInstance(this, data);
        startActivityForResult(intent, CONFIRM_SHIPMENT_REQUEST_CODE);
    }

    @Override
    public void onAcceptOrder(OrderDetailData data) {
        AcceptOrderDialog dialog = AcceptOrderDialog.createDialog(data.getOrderId());
        dialog.show(getFragmentManager(), dialog.getClass().getSimpleName());
    }

    @Override
    public void onAcceptOrderPartially(OrderDetailData data) {
        AcceptPartialOrderDialog dialog = AcceptPartialOrderDialog.createDialog(data);
        dialog.show(getFragmentManager(), dialog.getClass().getSimpleName());
    }

    @Override
    public void onRequestPickup(OrderDetailData data) {
        if (getFragmentManager().findFragmentByTag(VALIDATION_FRAGMENT_TAG) == null) {
            RequestPickupFragment requestPickupFragment = RequestPickupFragment
                    .createFragment(data.getOrderId());
            getFragmentManager().beginTransaction()
                    .setCustomAnimations(R.animator.enter_bottom, R.animator.enter_bottom)
                    .add(R.id.main_view, requestPickupFragment, VALIDATION_FRAGMENT_TAG)
                    .commit();
        }
    }

    @Override
    public void onChangeCourier(OrderDetailData data) {
        //TODO Check Again Later
        Intent intent = ConfirmShippingActivity.createChangeCourierInstance(this, data);
        startActivityForResult(intent, CONFIRM_SHIPMENT_REQUEST_CODE);
    }

    @Override
    public void onRejectOrder(OrderDetailData data) {
        //TODO Change LATER
        if (getFragmentManager().findFragmentByTag(REJECT_ORDER_FRAGMENT_TAG) == null) {
            RejectOrderFragment rejectOrderFragment = RejectOrderFragment
                    .createFragment(data);
            getFragmentManager().beginTransaction()
                    .setCustomAnimations(R.animator.enter_bottom, R.animator.enter_bottom)
                    .add(R.id.main_view, rejectOrderFragment, REJECT_ORDER_FRAGMENT_TAG)
                    .commit();
            toolbar.setTitle("");
        }
    }

    @Override
    public void onRejectShipment(OrderDetailData data) {
        if (getFragmentManager().findFragmentByTag(VALIDATION_FRAGMENT_TAG) == null) {
            CancelShipmentFragment cancelShipmentFragment = CancelShipmentFragment
                    .createFragment(data.getOrderId());
            getFragmentManager().beginTransaction()
                    .setCustomAnimations(R.animator.enter_bottom, R.animator.enter_bottom)
                    .add(R.id.main_view, cancelShipmentFragment, VALIDATION_FRAGMENT_TAG)
                    .commit();
        }
    }

    @Override
    public void onChangeAwb(OrderDetailData data) {
        if (getFragmentManager().findFragmentByTag(VALIDATION_FRAGMENT_TAG) == null) {
            ChangeAwbFragment changeAwbFragment = ChangeAwbFragment
                    .createFragment(data.getOrderId(), data.getAwb());
            getFragmentManager().beginTransaction()
                    .setCustomAnimations(R.animator.enter_bottom, R.animator.enter_bottom)
                    .add(R.id.main_view, changeAwbFragment, VALIDATION_FRAGMENT_TAG)
                    .commit();
        }
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
    public void showSnackbar(String errorMessage) {
        NetworkErrorHelper.showSnackbar(this, errorMessage);
    }

    @Override
    public void dismissSellerActionFragment() {
        //Alternative 1 refresh activity
        /*getFragmentManager().beginTransaction()
                .setCustomAnimations(R.animator.exit_bottom, R.animator.exit_bottom)
                .remove(getFragmentManager()
                        .findFragmentByTag(VALIDATION_FRAGMENT_TAG)).commit();
        toolbar.setTitle(getString(R.string.title_detail_transaction));
        onRefreshActivity();*/

        //Alternative 2 close activity
        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    public void dismissRejectOrderActionFragment() {
        /*if (getFragmentManager().findFragmentByTag(FRAGMENT_REJECT_ORDER_SUB_MENU_TAG) != null) {
            getFragmentManager().beginTransaction()
                    .remove(getFragmentManager()
                    .findFragmentByTag(FRAGMENT_REJECT_ORDER_SUB_MENU_TAG)).commit();
        }
        getFragmentManager().beginTransaction()
                .remove(getFragmentManager()
                .findFragmentByTag(REJECT_ORDER_MENU_FRAGMENT_TAG)).commit();
        getFragmentManager().beginTransaction()
                .setCustomAnimations(R.animator.exit_bottom, R.animator.exit_bottom)
                .remove(getFragmentManager()
                .findFragmentByTag(VALIDATION_FRAGMENT_TAG)).commit();*/
        Toast.makeText(
                this, getString(R.string.default_success_message_reject_order),
                Toast.LENGTH_LONG).show();
        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    public void onRefreshActivity() {
        presenter.fetchData(this, getExtraOrderId(), getExtraUserMode());

    }

    @Override
    public void dismissActivity() {
        setResult(Activity.RESULT_OK);
        finish();
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
        if (requestCode == CONFIRM_SHIPMENT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            //TODO Alternative kalo mau langsung ganti status di halaman detail
            //presenter.fetchData(this, getExtraOrderId(), getExtraUserMode());
            //TODO Alternative kalo langsung balik ke halaman order list
            setResult(Activity.RESULT_OK);
            finish();
        }
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
        if (getFragmentManager().findFragmentByTag(INFO_FRAGMENT_TAG) != null) {
            removeFragmentOnBackPressed(INFO_FRAGMENT_TAG);
        } else if (getFragmentManager().findFragmentByTag(VALIDATION_FRAGMENT_TAG) != null) {
            toolbar.setTitle(getString(R.string.title_detail_transaction));
            removeFragmentOnBackPressed(VALIDATION_FRAGMENT_TAG);
        } else if (getFragmentManager().findFragmentByTag(FRAGMENT_REJECT_ORDER_SUB_MENU_TAG) != null) {
            toolbar.setTitle("");
            removeFragmentOnBackPressed(FRAGMENT_REJECT_ORDER_SUB_MENU_TAG);
        } else if (getFragmentManager().findFragmentByTag(REJECT_ORDER_MENU_FRAGMENT_TAG) != null) {
            removeFragmentOnBackPressed(REJECT_ORDER_MENU_FRAGMENT_TAG);
        } else if (getFragmentManager().findFragmentByTag(REJECT_ORDER_FRAGMENT_TAG) != null) {
            toolbar.setTitle(getString(R.string.title_detail_transaction));
            removeFragmentOnBackPressed(REJECT_ORDER_FRAGMENT_TAG);
        } else super.onBackPressed();
    }

    private void removeFragmentOnBackPressed(String tag) {
        getFragmentManager().beginTransaction()
                .setCustomAnimations(R.animator.slide_out_right, R.animator.slide_out_right)
                .remove(getFragmentManager()
                        .findFragmentByTag(tag)).commit();
    }

    @Override
    public void onAcceptOrder(String orderId) {
        presenter.acceptOrder(this, orderId);
    }

    @Override
    public void changeAwb(String orderId, String refNumber) {
        presenter.confirmChangeAwb(this, orderId, refNumber);
    }

    @Override
    public void onAcceptPartialOrderCreated(String orderId, String remark, String param) {
        presenter.partialOrder(this, orderId, remark, param);
    }

    @Override
    public void cancelShipment(String orderId, String notes) {
        presenter.cancelShipping(this, orderId, notes);
    }

    @Override
    public void onRejectEmptyStock(TKPDMapParam<String, String> param) {
        presenter.rejectOrderGenericReason(this, param);
    }

    @Override
    public void rejectOrderCourierReason(TKPDMapParam<String, String> param) {
        presenter.rejectOrderGenericReason(this, param);
    }

    @Override
    public void rejectOrderBuyerRequest(TKPDMapParam<String, String> rejectParam) {
        presenter.rejectOrderGenericReason(this, rejectParam);
    }

    @Override
    public void onClosedDateSelected(TKPDMapParam<String, String> rejectParam) {
        presenter.rejectOrderGenericReason(this, rejectParam);
    }

    @Override
    public void onRejectEmptyVarian(List<EmptyVarianProductEditable> editableList) {
        presenter.rejectOrderChangeVarian(this, editableList);
    }

    @Override
    public void onConfirmWeightPrice(List<WrongProductPriceWeightEditable> listOfEditable) {
        presenter.rejectOrderChangeWeightPrice(this, listOfEditable);
    }

    @Override
    public void onConfirmPickup(String orderId) {
        presenter.processInstantCourierShipping(this, orderId);
    }

    @Override
    public void onWebViewSuccessLoad() {

    }

    @Override
    public void onWebViewErrorLoad() {

    }

    @Override
    public void onWebViewProgressLoad() {

    }

    @Override
    public void onRemoveTitle() {
        toolbar.setTitle("");
    }

    @Override
    public void onChangeTitle(String toolbarTitle) {
        toolbar.setTitle(toolbarTitle);
    }
}
