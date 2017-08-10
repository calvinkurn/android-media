package com.tokopedia.transaction.purchase.presenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.inboxreputation.activity.InboxReputationActivity;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.onboarding.ConstantOnBoarding;
import com.tokopedia.core.router.InboxRouter;
import com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.tracking.activity.TrackingActivity;
import com.tokopedia.core.util.AppUtils;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.TagHandlerUtil;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.transaction.purchase.activity.TxDetailActivity;
import com.tokopedia.transaction.purchase.activity.TxHistoryActivity;
import com.tokopedia.transaction.purchase.interactor.TxOrderNetInteractor;
import com.tokopedia.transaction.purchase.interactor.TxOrderNetInteractorImpl;
import com.tokopedia.transaction.purchase.listener.TxDetailViewListener;
import com.tokopedia.transaction.purchase.model.response.txlist.OrderButton;
import com.tokopedia.transaction.purchase.model.response.txlist.OrderData;
import com.tokopedia.transaction.purchase.model.response.txlist.OrderShop;
import com.tokopedia.transaction.purchase.receiver.TxListUIReceiver;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author by Angga.Prasetiyo on 28/04/2016.
 */
public class TxDetailPresenterImpl implements TxDetailPresenter {
    private static final int CREATE_RESCENTER_REQUEST_CODE = 789;
    private final TxDetailViewListener viewListener;
    private final TxOrderNetInteractor netInteractor;
    private static final int FREE_RETURN = 1;
    public TxDetailPresenterImpl(TxDetailViewListener viewListener) {
        this.viewListener = viewListener;
        this.netInteractor = new TxOrderNetInteractorImpl();
    }

    @Override
    public void processInvoice(TxDetailActivity txDetailActivity, OrderData data) {
        AppUtils.InvoiceDialog(
                txDetailActivity, data.getOrderDetail().getDetailPdfUri(),
                data.getOrderDetail().getDetailInvoice()
        );
    }

    @Override
    public void processToShop(Context context, OrderShop orderShop) {
        Intent intent = new Intent(context, ShopInfoActivity.class);
        Bundle bundle = ShopInfoActivity.createBundle(orderShop.getShopId(), "");
        viewListener.navigateToActivity(intent.putExtras(bundle));
    }

    @Override
    public void processShowComplain(Context context, OrderButton orderButton) {
        Uri uri = Uri.parse(orderButton.getButtonResCenterUrl());
        String res_id = uri.getQueryParameter("id");
        viewListener.navigateToActivity(
                InboxRouter.getDetailResCenterActivityIntent(context, res_id)
        );
    }

    @SuppressWarnings("deprecation")
    @Override
    public void processOpenDispute(final Context context, final OrderData orderData, int state) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(
                MethodChecker.fromHtml(
                        context.getString(R.string.dialog_package_not_rcv)
                                .replace("XXX", orderData.getOrderShop().getShopName())
                )
        );
        builder.setPositiveButton(context.getString(R.string.action_ask_courier),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        /**
                         * create resolution with solution ask seller to check courier
                         * n=0&id=<ORDER_ID>&t=5&s=6 -> tanya kurir
                         */
                        viewListener.navigateToActivityRequest(
                                InboxRouter.getCreateResCenterActivityIntent(
                                        context, orderData.getOrderDetail().getDetailOrderId(), 5, InboxRouter.SOLUTION_CHECK_COURIER
                                ), TransactionPurchaseRouter.CREATE_RESCENTER_REQUEST_CODE
                        );
                    }
                });
        builder.setNegativeButton(context.getString(R.string.action_refund),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        /**
                         * create resolution with solution refund
                         * n=0&id=<ORDER_ID>&t=5&s=1 --> pengembalian dana
                         */
                        viewListener.navigateToActivityRequest(
                                InboxRouter.getCreateResCenterActivityIntent(
                                        context, orderData.getOrderDetail().getDetailOrderId(), 5, InboxRouter.SOLUTION_REFUND
                                ), TransactionPurchaseRouter.CREATE_RESCENTER_REQUEST_CODE
                        );
                    }
                });
        Dialog alertDialog = builder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.show();
    }

    @Override
    public void processConfirmDeliver(final Context context, final OrderData orderData) {
        Dialog dialogConfirm;
        if (orderData.getOrderDetail().getDetailFreeReturn() == 1) {
            dialogConfirm = generateDialogFreeReturn(context, orderData);
        } else {
            dialogConfirm = generateDialogConfirm(context, orderData);
        }
        viewListener.showDialog(dialogConfirm);
    }

    @Override
    public void processTrackOrder(Context context, OrderData orderData) {
        Intent intent = new Intent(context, TrackingActivity.class);
        intent.putExtra("OrderID", orderData.getOrderDetail().getDetailOrderId());
        viewListener.navigateToActivity(intent);
    }

    @Override
    public void processSeeAllHistories(Context context, OrderData orderData) {
        viewListener.navigateToActivity(TxHistoryActivity.createInstance(context,
                orderData.getOrderHistory()));

    }

    @SuppressWarnings("deprecation")
    @Override
    public void processAskSeller(Context context, OrderData orderData) {
        Intent intent = InboxRouter.getSendMessageActivityIntent(context);
        Bundle bundle = new Bundle();
        bundle.putString(InboxRouter.PARAM_SHOP_ID,
                orderData.getOrderShop().getShopId());
        bundle.putString(InboxRouter.PARAM_OWNER_FULLNAME,
                orderData.getOrderShop().getShopName());
        bundle.putString(InboxRouter.PARAM_CUSTOM_SUBJECT,
                orderData.getOrderDetail().getDetailInvoice());
        bundle.putString(InboxRouter.PARAM_CUSTOM_MESSAGE,
                MethodChecker.fromHtml(
                        context.getString(R.string.custom_content_message_ask_seller)
                                .replace("XXX",
                                        orderData.getOrderDetail().getDetailPdfUri())).toString()
        );
        viewListener.navigateToActivity(intent.putExtras(bundle));
    }

    @Override
    public void onDestroyView() {
        netInteractor.unSubscribeObservable();
    }

    @Override
    public void processRequestCancelOrder(final Activity activity, String reason,
                                          OrderData orderData) {
        viewListener.showProgressLoading();
        TKPDMapParam<String, String> params = new TKPDMapParam<>();
        params.put("order_id", orderData.getOrderDetail().getDetailOrderId());
        params.put("reason_cancel", reason);
        netInteractor.requestCancelOrder(AuthUtil.generateParamsNetwork(activity, params),
                new TxOrderNetInteractor.RequestCancelOrderListener() {
                    @Override
                    public void onSuccess(String message) {
                        viewListener.hideProgressLoading();
                        if (message == null || message.isEmpty()) message = activity.getString(
                                com.tokopedia.transaction.R.string
                                        .default_success_message_request_cancel_order
                        );
                        viewListener.renderSuccessRequestCancelOrder(message);
                    }

                    @Override
                    public void onError(String message) {
                        viewListener.hideProgressLoading();
                        viewListener.showToastMessage(message);
                    }

                    @Override
                    public void onTimeout(String message) {
                        viewListener.hideProgressLoading();
                        viewListener.showToastMessage(message);
                    }

                    @Override
                    public void onNoConnection(String message) {
                        viewListener.hideProgressLoading();
                        viewListener.showToastMessage(message);
                    }
                });
    }

    @Override
    public void processComplain(Context context, OrderData orderData){
        showComplainDialog(context, orderData);
    }

    @Override
    public void processFinish(Context context, OrderData orderData) {
        showFinishDialog(context, orderData);
    }

    private void processReview(final Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (message == null || message.isEmpty())
            message = context.getString(R.string.dialog_rating_review);
        builder.setMessage(message).setPositiveButton(context.getString(R.string.title_ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(context, InboxReputationActivity.class);
                        intent.putExtra("unread", true);
                        dialog.dismiss();
                        viewListener.navigateToActivity(intent);
                        viewListener.closeWithResult(
                                TkpdState.TxActivityCode.BuyerItemReceived, null
                        );
                    }
                });
        Dialog alertDialog = builder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        viewListener.showDialog(builder.create());
    }

    private void showFinishDialog(final Context context, final OrderData orderData) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_finish);
        TextView tvFinishTitle = (TextView) dialog.findViewById(R.id.tvFinishTitle);
        TextView tvFinishBody = (TextView) dialog.findViewById(R.id.tvFinishBody);
        Button btnFinish = (Button) dialog.findViewById(R.id.btnFinish);
        Button btnComplain = (Button) dialog.findViewById(R.id.btnComplain);
        tvFinishTitle.setText(Html.fromHtml(orderData.getOrderDetail().getDetailFinishPopupTitle(), null, new TagHandlerUtil()));
        tvFinishBody.setText(Html.fromHtml(orderData.getOrderDetail().getDetailFinishPopupMsg(), null, new TagHandlerUtil()));
        btnComplain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmPurchase(context, dialog, orderData);
            }
        });

        dialog.show();
    }

    private void showComplainDialog(final Context context, final OrderData orderData) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_complain);
        Button btnBack = (Button) dialog.findViewById(R.id.btnBack);
        Button btnNotReceive = (Button) dialog.findViewById(R.id.btnNotReceive);
        Button btnReceive = (Button) dialog.findViewById(R.id.btnReceive);
        LinearLayout llFreeReturn = (LinearLayout) dialog.findViewById(R.id.llFreeReturn);
        TextView tvFreeReturn = (TextView) dialog.findViewById(R.id.tvFreeReturn);
        TextView tvComplainTitle = (TextView) dialog.findViewById(R.id.tvComplainTitle);
        TextView tvComplainBody = (TextView) dialog.findViewById(R.id.tvComplainBody);
        tvComplainTitle.setText(Html.fromHtml(orderData.getOrderDetail().getDetailComplaintPopupTitle(), null, new TagHandlerUtil()));
        tvComplainBody.setText(Html.fromHtml(orderData.getOrderDetail().getDetailComplaintPopupMsg(), null, new TagHandlerUtil()));

        llFreeReturn.setVisibility(View.GONE);
        btnBack.setVisibility(View.GONE);
        btnNotReceive.setVisibility(View.GONE);
        if (orderData.getOrderButton().getButtonComplaintNotReceived().equals("1"))
            btnNotReceive.setVisibility(View.VISIBLE);
        else
            btnBack.setVisibility(View.VISIBLE);
      
        //will be used later
//        if (orderData.getOrderDetail().getDetailFreeReturn() == 1) {
//            llFreeReturn.setVisibility(View.VISIBLE);
//            tvFreeReturn.setText(Html.fromHtml(orderData.getOrderDetail().getDetailFreeReturnMsg()));
//        }
      
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnNotReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                showNotReceiveDialog(context, orderData);
            }
        });

        btnReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (orderData.getOrderDetail().getDetailFreeReturn() == FREE_RETURN) {
                    LocalCacheHandler cache = new LocalCacheHandler(context,
                            ConstantOnBoarding.CACHE_FREE_RETURN);
                    if (cache.getBoolean(ConstantOnBoarding.HAS_SEEN_FREE_RETURN_ONBOARDING)) {
                        viewListener.navigateToActivityRequest(
                                InboxRouter.getCreateResCenterActivityIntent(
                                        context, orderData.getOrderDetail().getDetailOrderId()
                                ), CREATE_RESCENTER_REQUEST_CODE
                        );
                    } else {
                        viewListener.navigateToActivityRequest(
                                InboxRouter.getFreeReturnOnBoardingActivityIntent(
                                        context, orderData.getOrderDetail().getDetailOrderId()
                                ), CREATE_RESCENTER_REQUEST_CODE
                        );
                    }
                } else {
                    viewListener.navigateToActivityRequest(
                            InboxRouter.getCreateResCenterActivityIntent(
                                    context, orderData.getOrderDetail().getDetailOrderId()
                            ), CREATE_RESCENTER_REQUEST_CODE
                    );
                }
            }
        });

        dialog.show();
    }

    private void showNotReceiveDialog(final Context context, final OrderData orderData) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_not_received);
        Button btnRefund = (Button) dialog.findViewById(R.id.btnRefund);
        Button btnCheckCourier = (Button) dialog.findViewById(R.id.btnCheckCourier);
        TextView tvNotReceivedTitle = (TextView) dialog.findViewById(R.id.tvNotReceivedTitle);
        TextView tvNotReceivedBody = (TextView) dialog.findViewById(R.id.tvNotReceivedBody);
        tvNotReceivedTitle.setText(Html.fromHtml(orderData.getOrderDetail().getDetailComplaintNotReceivedTitle(), null, new TagHandlerUtil()));
        tvNotReceivedBody.setText(Html.fromHtml(orderData.getOrderDetail().getDetailComplaintNotReceivedMsg(), null,
                new TagHandlerUtil()));
        btnRefund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                viewListener.navigateToActivityRequest(
                        InboxRouter.getCreateResCenterActivityIntent(
                                context, orderData.getOrderDetail().getDetailOrderId(), 5, InboxRouter.SOLUTION_REFUND
                        ), CREATE_RESCENTER_REQUEST_CODE
                );
            }
        });

        btnCheckCourier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                viewListener.navigateToActivityRequest(
                        InboxRouter.getCreateResCenterActivityIntent(
                                context, orderData.getOrderDetail().getDetailOrderId(), 5, InboxRouter.SOLUTION_CHECK_COURIER
                        ), CREATE_RESCENTER_REQUEST_CODE
                );
            }
        });

        dialog.show();
    }
    private void processResolution(final Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (message == null || message.isEmpty())
            message = context.getString(R.string.success_create_rescenter);
        builder.setMessage(message)
                .setPositiveButton(context.getString(R.string.title_ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                viewListener.navigateToActivity(
                                        InboxRouter.getInboxResCenterActivityIntent(context)
                                );
                                viewListener.closeWithResult(
                                        TkpdState.TxActivityCode.BuyerCreateResolution, null
                                );
                            }
                        });
        Dialog alertDialog = builder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        viewListener.showDialog(builder.create());
    }

    @SuppressWarnings("deprecation")
    private Dialog generateDialogConfirm(final Context context, final OrderData orderData) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.label_title_dialog_order_received));
        builder.setMessage(
                MethodChecker.fromHtml(context.getString(R.string.dialog_package_received).replace(
                        "xx_shop_name_xx", orderData.getOrderShop().getShopName()
                ))
        );
        builder.setNegativeButton(context.getString(R.string.title_cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.setPositiveButton(context.getString(R.string.title_done),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        confirmPurchase(context, dialog, orderData);
                    }
                });
        if (orderData.getOrderButton().getButtonComplaintReceived() != null
                && orderData.getOrderButton().getButtonComplaintReceived().equals("1")) {
            builder.setNeutralButton(R.string.title_open_dispute, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    viewListener.navigateToActivityRequest(
                            InboxRouter.getCreateResCenterActivityIntent(context,
                                    orderData.getOrderDetail().getDetailOrderId()),
                            TransactionPurchaseRouter.CREATE_RESCENTER_REQUEST_CODE
                    );
                }

            });
        }

        return builder.create();
    }

    @SuppressWarnings("deprecation")
    private Dialog generateDialogFreeReturn(final Context context, final OrderData orderData) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.label_title_dialog_order_received_free_return));
        builder.setMessage(
                MethodChecker.fromHtml(orderData.getOrderDetail().getDetailFreeReturnMsg())
        );
        builder.setNeutralButton(context.getString(R.string.title_open_dispute),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        LocalCacheHandler cache = new LocalCacheHandler(context,
                                ConstantOnBoarding.CACHE_FREE_RETURN);
                        if (cache.getBoolean(ConstantOnBoarding.HAS_SEEN_FREE_RETURN_ONBOARDING)) {
                            viewListener.navigateToActivityRequest(
                                    InboxRouter.getCreateResCenterActivityIntent(
                                            context, orderData.getOrderDetail().getDetailOrderId()
                                    ), TransactionPurchaseRouter.CREATE_RESCENTER_REQUEST_CODE
                            );
                        } else {
                            viewListener.navigateToActivityRequest(
                                    InboxRouter.getFreeReturnOnBoardingActivityIntent(
                                            context, orderData.getOrderDetail().getDetailOrderId()
                                    ), TransactionPurchaseRouter.CREATE_RESCENTER_REQUEST_CODE
                            );
                        }
                    }
                });
        builder.setPositiveButton(context.getString(R.string.title_done),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        confirmPurchase(context, dialog, orderData);
                    }
                });
        builder.setNegativeButton(R.string.title_cancel,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }

    private void confirmPurchase(final Context context, final DialogInterface dialog,
                                 OrderData orderData) {
        viewListener.showProgressLoading();
        if (orderData.getOrderDetail().getDetailOrderStatus()
                .equals(context.getString(R.string.ORDER_DELIVERED))
                || orderData.getOrderDetail()
                .getDetailOrderStatus()
                .equals(context.getString(R.string.ORDER_DELIVERY_FAILURE))) {
            Map<String, String> params = new HashMap<>();
            params.put("order_id", orderData.getOrderDetail().getDetailOrderId());
            netInteractor.confirmDeliver(context, params,
                    new TxOrderNetInteractor.OnConfirmFinishDeliver() {

                        @Override
                        public void onSuccess(String message, JSONObject lucky) {
                            TxListUIReceiver.sendBroadcastForceRefreshListData(context);
                            viewListener.hideProgressLoading();
                            TrackingUtils.eventLoca(context.getString(R.string.confirm_received));
                            processReview(context, message);
                            dialog.dismiss();
                        }

                        @Override
                        public void onError(String message) {
                            viewListener.hideProgressLoading();
                            viewListener.showToastMessage(message);
                            dialog.dismiss();
                        }
                    });
        } else {
            Map<String, String> params = new HashMap<>();
            params.put("order_id", orderData.getOrderDetail().getDetailOrderId());
            netInteractor.confirmFinishDeliver(context, params,
                    new TxOrderNetInteractor.OnConfirmFinishDeliver() {

                        @Override
                        public void onSuccess(String message, JSONObject lucky) {
                            viewListener.hideProgressLoading();
                            TxListUIReceiver.sendBroadcastForceRefreshListData(context);
                            processReview(context, message);
                            dialog.dismiss();
                        }

                        @Override
                        public void onError(String message) {
                            viewListener.hideProgressLoading();
                            viewListener.showToastMessage(message);
                            dialog.dismiss();
                        }
                    });
        }
    }


    @Override
    public void onActivityResult(Context context, int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TransactionPurchaseRouter.CREATE_RESCENTER_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    processResolution(context, null);
                }
                break;
            default:
                break;
        }
    }
}
