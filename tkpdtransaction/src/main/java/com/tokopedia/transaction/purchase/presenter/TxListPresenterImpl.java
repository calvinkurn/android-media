package com.tokopedia.transaction.purchase.presenter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.core2.R;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.onboarding.ConstantOnBoarding;
import com.tokopedia.core.router.InboxRouter;
import com.tokopedia.core.util.AppUtils;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.transaction.common.TransactionRouter;
import com.tokopedia.transaction.opportunity.domain.interactor.CancelReplacementUseCase;
import com.tokopedia.transaction.opportunity.view.subsriber.CancelReplacementSubscriber;
import com.tokopedia.transaction.purchase.detail.activity.OrderDetailActivity;
import com.tokopedia.transaction.purchase.interactor.TxOrderNetInteractor;
import com.tokopedia.transaction.purchase.interactor.TxOrderNetInteractorImpl;
import com.tokopedia.transaction.purchase.listener.TxListViewListener;
import com.tokopedia.transaction.purchase.model.AllTxFilter;
import com.tokopedia.transaction.purchase.model.response.txlist.OrderData;
import com.tokopedia.transaction.purchase.model.response.txlist.OrderListData;
import com.tokopedia.transaction.purchase.receiver.TxListUIReceiver;

import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Angga.Prasetiyo on 21/04/2016.
 */
public class TxListPresenterImpl implements TxListPresenter {
    private static final int CREATE_RESCENTER_REQUEST_CODE = 789;
    private final TxListViewListener viewListener;
    private final TxOrderNetInteractor netInteractor;
    private final CancelReplacementUseCase cancelReplacementUseCase;
    private final SessionHandler sessionHandler;
    private static final int FREE_RETURN = 1;

    public TxListPresenterImpl(TxListViewListener viewListener,
                               CancelReplacementUseCase cancelReplacementUseCase,
                               SessionHandler sessionHandler) {
        this.viewListener = viewListener;
        this.cancelReplacementUseCase = cancelReplacementUseCase;
        this.netInteractor = new TxOrderNetInteractorImpl();
        this.sessionHandler = sessionHandler;
    }

    @Override
    public void getStatusOrderData(Context context, final int page, final int typeRequest) {
        Map<String, String> params = new HashMap<>();
        params.put("page", page + "");
        params.put("per_page", "10");
        viewListener.showProcessGetData(typeRequest);
        netInteractor.getOrderStatusList(context, params,
                new TxOrderNetInteractor.OnGetOrderStatusList() {
                    @Override
                    public void onSuccess(JSONObject data, OrderListData dataObj) {
                        viewListener.renderDataList(dataObj.getOrderDataList(),
                                PagingHandler.CheckHasNext(dataObj.getPaging()), typeRequest);
                    }

                    @Override
                    public void onError(String message) {
                        switch (typeRequest) {
                            case TxOrderNetInteractor.TypeRequest.INITIAL:
                                viewListener.showFailedResetData(message);
                                break;
                            case TxOrderNetInteractor.TypeRequest.PULL_REFRESH:
                                viewListener.showFailedPullRefresh(message);
                                break;
                            case TxOrderNetInteractor.TypeRequest.LOAD_MORE:
                                viewListener.showFailedLoadMoreData(message);
                                break;
                        }
                    }

                    @Override
                    public void onNoConnection(String message) {
                        switch (typeRequest) {
                            case TxOrderNetInteractor.TypeRequest.INITIAL:
                                viewListener.showNoConnectionResetData(message);
                                break;
                            case TxOrderNetInteractor.TypeRequest.PULL_REFRESH:
                                viewListener.showNoConnectionPullRefresh(message);
                                break;
                            case TxOrderNetInteractor.TypeRequest.LOAD_MORE:
                                viewListener.showNoConnectionLoadMoreData(message);
                                break;
                        }
                    }

                    @Override
                    public void onEmptyData() {
                        viewListener.showEmptyData(typeRequest);
                    }
                });
    }

    @Override
    public void getDeliverOrderData(Context context, final int page, final int typeRequest) {
        Map<String, String> params = new HashMap<>();
        params.put("page", page + "");
        params.put("per_page", "10");
        viewListener.showProcessGetData(typeRequest);
        netInteractor.getOrderReceiveList(context, params,
                new TxOrderNetInteractor.OnGetOrderReceiveList() {
                    @Override
                    public void onSuccess(JSONObject data, OrderListData dataObj) {
                        viewListener.renderDataList(dataObj.getOrderDataList(),
                                PagingHandler.CheckHasNext(dataObj.getPaging()), typeRequest);
                    }

                    @Override
                    public void onError(String message) {
                        switch (typeRequest) {
                            case TxOrderNetInteractor.TypeRequest.INITIAL:
                                viewListener.showFailedResetData(message);
                                break;
                            case TxOrderNetInteractor.TypeRequest.PULL_REFRESH:
                                viewListener.showFailedPullRefresh(message);
                                break;
                            case TxOrderNetInteractor.TypeRequest.LOAD_MORE:
                                viewListener.showFailedLoadMoreData(message);
                                break;
                        }
                    }

                    @Override
                    public void onNoConnection(String message) {
                        switch (typeRequest) {
                            case TxOrderNetInteractor.TypeRequest.INITIAL:
                                viewListener.showNoConnectionResetData(message);
                                break;
                            case TxOrderNetInteractor.TypeRequest.PULL_REFRESH:
                                viewListener.showNoConnectionPullRefresh(message);
                                break;
                            case TxOrderNetInteractor.TypeRequest.LOAD_MORE:
                                viewListener.showNoConnectionLoadMoreData(message);
                                break;
                        }
                    }

                    @Override
                    public void onEmptyData() {
                        viewListener.showEmptyData(typeRequest);
                    }
                });
    }

    @Override
    public void getAllOrderData(Context context, final int page, AllTxFilter filter,
                                final int typeRequest) {
        Map<String, String> params = new HashMap<>();
        params.put("page", page + "");
        params.put("per_page", "10");
        params.put("status", filter.getFilter());
        params.put("start", filter.getDateStart());
        params.put("end", filter.getDateEnd());
        params.put("invoice", filter.getQuery());
        viewListener.showProcessGetData(typeRequest);
        netInteractor.getOrderTxList(context, params,
                new TxOrderNetInteractor.OnGetOrderTxList() {
                    @Override
                    public void onSuccess(JSONObject data, OrderListData dataObj) {
                        viewListener.renderDataList(dataObj.getOrderDataList(),
                                PagingHandler.CheckHasNext(dataObj.getPaging()), typeRequest);
                    }

                    @Override
                    public void onError(String message) {
                        switch (typeRequest) {
                            case TxOrderNetInteractor.TypeRequest.INITIAL:
                                viewListener.showFailedResetData(message);
                                break;
                            case TxOrderNetInteractor.TypeRequest.PULL_REFRESH:
                                viewListener.showFailedPullRefresh(message);
                                break;
                            case TxOrderNetInteractor.TypeRequest.LOAD_MORE:
                                viewListener.showFailedLoadMoreData(message);
                                break;
                        }
                    }

                    @Override
                    public void onNoConnection(String message) {
                        switch (typeRequest) {
                            case TxOrderNetInteractor.TypeRequest.INITIAL:
                                viewListener.showNoConnectionResetData(message);
                                break;
                            case TxOrderNetInteractor.TypeRequest.PULL_REFRESH:
                                viewListener.showNoConnectionPullRefresh(message);
                                break;
                            case TxOrderNetInteractor.TypeRequest.LOAD_MORE:
                                viewListener.showNoConnectionLoadMoreData(message);
                                break;
                        }
                    }

                    @Override
                    public void onEmptyData() {
                        viewListener.showEmptyData(typeRequest);
                    }
                });
    }

    @Override
    public void processToDetailOrder(Context context, OrderData data, int typeInstance) {
        viewListener.navigateToActivityRequest(OrderDetailActivity.createInstance(context,
                data.getOrderDetail().getDetailOrderId()), OrderDetailActivity.REQUEST_CODE_ORDER_DETAIL);
    }

    @Override
    public void processToInvoice(Context context, OrderData data) {
        AppUtils.InvoiceDialog(
                context, data.getOrderDetail().getDetailPdfUri(),
                data.getOrderDetail().getDetailInvoice()
        );
    }

    @Override
    public void processRejectOrder(final Context context, final OrderData data) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        LayoutInflater li = LayoutInflater.from(context);
        @SuppressLint("InflateParams") final View promptsView = li.inflate(R.layout.dialog_package_not_rcv,
                null);
        alertDialogBuilder.setView(promptsView);
        TextView dShopName = (TextView) promptsView
                .findViewById(R.id.shop_name);
        TextView dInvoice = (TextView) promptsView.findViewById(R.id.invoice);
        dShopName.setText(MessageFormat.format("{0}: {1}",
                context.getString(R.string.title_product_received_fr),
                data.getOrderShop().getShopName()));
        dInvoice.setText(MessageFormat.format("{0} {1}",
                context.getString(R.string.title_invoice_number),
                data.getOrderDetail().getDetailInvoice()));
        final EditText dMessage = (EditText) promptsView
                .findViewById(R.id.remark);
        alertDialogBuilder.setPositiveButton(
                context.getString(R.string.title_yes),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        if (dMessage.toString().trim().length() > 0) {
                            viewListener.showProgressLoading();
                            Map<String, String> param = new HashMap<>();
                            param.put("order_id", data.getOrderDetail().getDetailOrderId());
                            param.put("comments", dMessage.getText().toString().trim());
                            netInteractor.deliverReject(context, param,
                                    new TxOrderNetInteractor.OnDeliverReject() {
                                        @Override
                                        public void onSuccess(String message) {
                                            viewListener.resetData();
                                            viewListener.hideProgressLoading();
                                        }

                                        @Override
                                        public void onFailed(String message) {
                                            viewListener.showToastMessage(message);
                                            viewListener.hideProgressLoading();
                                        }

                                        @Override
                                        public void onError(String message) {
                                            viewListener.hideProgressLoading();
                                            viewListener.showToastMessage(message);
                                        }
                                    });
                        } else {
                            dMessage.setError(context.getString(R.string.error_field_required));
                        }
                    }
                });
        alertDialogBuilder.setNegativeButton(
                context.getString(R.string.title_no),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        Dialog alertDialog = alertDialogBuilder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        viewListener.showDialog(alertDialog);
    }

    @Override
    public void processConfirmDeliver(final Context context, final OrderData orderData,
                                      final int typeInstance) {
        Dialog dialogConfirm;
        if (orderData.getOrderDetail().getDetailFreeReturn() == 1) {
            dialogConfirm = generateDialogFreeReturn(context, orderData);
        } else {
            dialogConfirm = generateDialogConfirm(context, orderData);
        }
        viewListener.showDialog(dialogConfirm);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void processOpenDispute(final Context context, final OrderData data, int state) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(
                MethodChecker.fromHtml(
                        context.getString(R.string.dialog_package_not_rcv)
                                .replace("XXX", data.getOrderShop().getShopName())
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
                                        context, data.getOrderDetail().getDetailOrderId(), 5, InboxRouter.SOLUTION_CHECK_COURIER
                                ), CREATE_RESCENTER_REQUEST_CODE
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
                                        context, data.getOrderDetail().getDetailOrderId(), 5, InboxRouter.SOLUTION_REFUND
                                ), CREATE_RESCENTER_REQUEST_CODE
                        );
                    }
                });
        Dialog alertDialog = builder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        viewListener.showDialog(alertDialog);
    }

    @Override
    public void processTrackOrder(Context context, OrderData data) {
        String routingAppLink;
        routingAppLink = ApplinkConst.ORDER_TRACKING;
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.appendQueryParameter(ApplinkConst.Query.ORDER_TRACKING_ORDER_ID,
                data.getOrderDetail().getDetailOrderId())
                .appendQueryParameter(
                        ApplinkConst.Query.ORDER_TRACKING_URL_LIVE_TRACKING,
                        processLiveTrackingUrl(data));
        routingAppLink += uriBuilder.toString();
        RouteManager.route(context, routingAppLink);
    }

    private String processLiveTrackingUrl(OrderData orderData) {
        if (orderData.getDriverInfo() != null) {
            return orderData.getDriverInfo().getTrackingUrl();
        } else return "";
    }

    @Override
    public void processShowComplain(Context context, OrderData data) {
        Uri uri = Uri.parse(data.getOrderButton().getButtonResCenterUrl());
        String res_id = uri.getQueryParameter("id");

        if (MainApplication.getAppContext() instanceof TransactionRouter) {
            Intent intent = ((TransactionRouter) MainApplication.getAppContext())
                    .getDetailResChatIntentBuyer(context, res_id, data.getOrderShop().getShopName());
            viewListener.navigateToActivity(intent);
        }
    }

    @Override
    public void onDestroyView() {
        netInteractor.unSubscribeObservable();
    }


    @Override
    public void cancelReplacement(Context context, final OrderData orderData) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(com.tokopedia.transaction.R.string.dialog_cancel_order)
                .setPositiveButton(context.getString(R.string.title_ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                viewListener.showProgressLoading();
                                doCancelReplacement(orderData);
                            }
                        })
                .setNegativeButton(context.getString(R.string.title_cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });

        Dialog alertDialog = builder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        viewListener.showDialog(builder.create());
    }

    @Override
    public void processComplain(final Context context, final OrderData orderData) {
        showComplainDialog(context, orderData);
    }

    @Override
    public void processComplainConfirmDeliver(Context context, OrderData orderData) {
        showFinishDialog(context, orderData);
    }

    private void showFinishDialog(final Context context, final OrderData orderData) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_finish);
        TextView tvFinishTitle = (TextView) dialog.findViewById(R.id.tvFinishTitle);
        TextView tvFinishBody = (TextView) dialog.findViewById(R.id.tvFinishBody);
        Button btnFinish = (Button) dialog.findViewById(R.id.btnFinish);
        Button btnComplain = (Button) dialog.findViewById(R.id.btnComplain);
        tvFinishTitle.setText(Html.fromHtml(orderData.getOrderDetail().getDetailFinishPopupTitle()));
        tvFinishBody.setText(Html.fromHtml(orderData.getOrderDetail().getDetailFinishPopupMsg()));
        btnComplain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmPurchaseOrder(context, dialog, orderData);
            }
        });

        dialog.show();
    }

    private void showComplainDialog(final Context context, final OrderData orderData) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(com.tokopedia.transaction.R.layout.dialog_complaint);
        Button btnBack = (Button) dialog.findViewById(com.tokopedia.transaction.R.id.cancel_button);
        Button btnNotReceive = (Button) dialog.findViewById(com.tokopedia.transaction.R.id.not_receive_btn);
        Button btnReceive = (Button) dialog.findViewById(com.tokopedia.transaction.R.id.receive_btn);
        LinearLayout llFreeReturn = (LinearLayout) dialog.findViewById(com.tokopedia.transaction.R.id.layout_free_return);
        TextView tvFreeReturn = (TextView) dialog.findViewById(com.tokopedia.transaction.R.id.tv_free_return);
        TextView tvComplainTitle = (TextView) dialog.findViewById(com.tokopedia.transaction.R.id.complaint_title);
        TextView tvComplainBody = (TextView) dialog.findViewById(com.tokopedia.transaction.R.id.complaint_body);

        tvComplainTitle.setText(Html.fromHtml(orderData.getOrderDetail().getDetailComplaintPopupTitle()));
        tvComplainBody.setText(orderData.getOrderDetail().getDetailComplaintPopupMsgV2() != null ?
                Html.fromHtml(orderData.getOrderDetail().getDetailComplaintPopupMsgV2()) :
                "");

        llFreeReturn.setVisibility(View.GONE);
        btnBack.setVisibility(View.VISIBLE);
        btnNotReceive.setVisibility(View.GONE);

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
        tvNotReceivedTitle.setText(Html.fromHtml(orderData.getOrderDetail().getDetailComplaintNotReceivedTitle()));
        tvNotReceivedBody.setText(Html.fromHtml(orderData.getOrderDetail().getDetailComplaintNotReceivedMsg()));
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

    private void doCancelReplacement(OrderData orderData) {
        cancelReplacementUseCase.execute(
                CancelReplacementUseCase.getParameters(
                        Integer.parseInt(orderData.getOrderDetail().getDetailOrderId()),
                        sessionHandler.getLoginID(),
                        SessionHandler.getAccessToken()
                ),
                new CancelReplacementSubscriber(viewListener));
    }

    private void processReview(final Context context, String message) {
        viewListener.hideProgressLoading();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (message == null || message.isEmpty())
            message = context.getString(R.string.dialog_rating_review);
        builder.setMessage(message).setPositiveButton(context.getString(R.string.title_ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (MainApplication.getAppContext() instanceof TransactionRouter) {
                            Intent intent = ((TransactionRouter) MainApplication.getAppContext())
                                    .getInboxReputationIntent(MainApplication.getAppContext());
                            intent.putExtra("unread", true);
                            dialog.dismiss();
                            viewListener.navigateToActivity(intent);
                        }
                    }
                });
        Dialog alertDialog = builder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        viewListener.showDialog(builder.create());
    }

    private void processResolution(final Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (message == null || message.isEmpty())
            message = context.getString(R.string.success_create_rescenter);
        builder.setMessage(message).setPositiveButton(context.getString(R.string.title_ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (MainApplication.getAppContext() instanceof TransactionRouter) {
                            viewListener.navigateToActivity(((TransactionRouter) MainApplication.getAppContext())
                                    .getResolutionCenterIntent(context)
                            );

                        }
                    }
                });
        Dialog alertDialog = builder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        viewListener.showDialog(builder.create());
    }

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
                        confirmPurchaseOrder(context, dialog, orderData);
                    }
                });

        if (orderData.getOrderButton().getButtonComplaintReceived() != null
                && orderData.getOrderButton().getButtonComplaintReceived().equals("1")) {
            builder.setNeutralButton(R.string.title_open_dispute,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            viewListener.navigateToActivityRequest(
                                    InboxRouter.getCreateResCenterActivityIntent(
                                            context, orderData.getOrderDetail().getDetailOrderId()
                                    ), CREATE_RESCENTER_REQUEST_CODE
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
                                    ), CREATE_RESCENTER_REQUEST_CODE
                            );
                        } else {
                            viewListener.navigateToActivityRequest(
                                    InboxRouter.getFreeReturnOnBoardingActivityIntent(
                                            context, orderData.getOrderDetail().getDetailOrderId()
                                    ), CREATE_RESCENTER_REQUEST_CODE
                            );
                        }
                    }
                });
        builder.setPositiveButton(context.getString(R.string.title_done),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        confirmPurchaseOrder(context, dialog, orderData);
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

    private void confirmPurchaseOrder(final Context context, final DialogInterface dialog,
                                      OrderData orderData) {
        if (orderData.getOrderDetail().getDetailOrderStatus()
                .equals(context.getString(R.string.ORDER_DELIVERED))
                || orderData.getOrderDetail().getDetailOrderStatus()
                .equals(context.getString(R.string.ORDER_DELIVERY_FAILURE))) {
            Map<String, String> params = new HashMap<>();
            params.put("order_id", orderData.getOrderDetail().getDetailOrderId());
            viewListener.showProgressLoading();
            netInteractor.confirmDeliver(context, params,
                    new TxOrderNetInteractor.OnConfirmFinishDeliver() {

                        @Override
                        public void onSuccess(String message, JSONObject lucky) {
                            TxListUIReceiver.sendBroadcastForceRefreshListData(context);
                            viewListener.hideProgressLoading();
                            viewListener.showToastSuccessMessage(
                                    context.getString(com.tokopedia.transaction.R.string.success_finish_order_message));
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
                            TxListUIReceiver.sendBroadcastForceRefreshListData(context);
                            viewListener.showToastSuccessMessage(
                                    context.getString(com.tokopedia.transaction.R.string.success_finish_order_message));
                            dialog.dismiss();
                        }

                        @Override
                        public void onError(String message) {
                            viewListener.showToastMessage(message);
                            dialog.dismiss();
                        }
                    });
        }
    }


    @Override
    public void onActivityResult(Context context, int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CREATE_RESCENTER_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    processResolution(context, null);
                }
                break;
            case OrderDetailActivity.REQUEST_CODE_ORDER_DETAIL:
                if (resultCode == Activity.RESULT_OK) {
                    viewListener.showToastSuccessMessage(
                            context.getString(com.tokopedia.transaction.R.string.success_cancel_replacement));
                } else {
                    viewListener.resetData();
                }
            default:
                break;
        }
    }
}
