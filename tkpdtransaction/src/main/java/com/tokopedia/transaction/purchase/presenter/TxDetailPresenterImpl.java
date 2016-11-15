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
import android.view.Window;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.facade.FacadeLuckyNotification;
import com.tokopedia.core.inboxmessage.activity.SendMessageActivity;
import com.tokopedia.core.inboxmessage.fragment.SendMessageFragment;
import com.tokopedia.core.inboxreputation.activity.InboxReputationActivity;
import com.tokopedia.core.loyaltysystem.model.LoyaltyNotification;
import com.tokopedia.core.rescenter.create.activity.CreateResCenterActivity;
import com.tokopedia.core.rescenter.detail.activity.ResCenterActivity;
import com.tokopedia.core.rescenter.detail.model.passdata.ActivityParamenterPassData;
import com.tokopedia.core.rescenter.inbox.activity.InboxResCenterActivity;
import com.tokopedia.core.rescenter.onboarding.FreeReturnOnboardingActivity;
import com.tokopedia.core.router.TransactionRouter;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.tracking.activity.TrackingActivity;
import com.tokopedia.core.util.AppUtils;
import com.tokopedia.core.util.UploadImageHandler;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Angga.Prasetiyo on 28/04/2016.
 */
public class TxDetailPresenterImpl implements TxDetailPresenter {
    private static final String TAG = TxDetailPresenterImpl.class.getSimpleName();
    private final TxDetailViewListener viewListener;
    private final TxOrderNetInteractor netInteractor;

    public TxDetailPresenterImpl(TxDetailViewListener viewListener) {
        this.viewListener = viewListener;
        this.netInteractor = new TxOrderNetInteractorImpl();
    }

    @Override
    public void processInvoice(TxDetailActivity txDetailActivity, OrderData data) {
        AppUtils.InvoiceDialog(txDetailActivity, data.getOrderDetail().getDetailPdfUri(),
                data.getOrderDetail().getDetailPdf(), data.getOrderDetail().getDetailInvoice());
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
        ActivityParamenterPassData activityParamenterPassData = new ActivityParamenterPassData();
        activityParamenterPassData.setResCenterId(res_id);
        Intent intent = ResCenterActivity.newInstance(context, activityParamenterPassData);
        viewListener.navigateToActivity(intent);
    }

    @Override
    public void processOpenDispute(final Context context, final OrderData orderData, int state) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(
                Html.fromHtml(
                        context.getString(R.string.dialog_package_not_rcv).replace("XXX", orderData.getOrderShop().getShopName())
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
                        viewListener.navigateToActivityRequest(CreateResCenterActivity.newInstancePackageNotReceived(context,
                                orderData.getOrderDetail().getDetailOrderId(), 5, 6), TransactionRouter.CREATE_RESCENTER_REQUEST_CODE);
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
                        viewListener.navigateToActivityRequest(CreateResCenterActivity.newInstancePackageNotReceived(context,
                                orderData.getOrderDetail().getDetailOrderId(), 5, 1), TransactionRouter.CREATE_RESCENTER_REQUEST_CODE);
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
    public void processUploadProof(Context context, OrderData orderData) {
        UploadImageHandler uploadImage = new UploadImageHandler((Activity) context,
                "image-upload-tcpdn.pl", "image");
        uploadImage.AddEntity("upload_proof", "1");
        uploadImage.Commit(new UploadImageHandler.UploadImageInterface() {

            @Override
            public void onUploadStart() {
                viewListener.showProgressLoading();
            }

            @Override
            public void onCancel() {

            }
        });
    }

    @Override
    public void processSeeAllHistories(Context context, OrderData orderData) {
        viewListener.navigateToActivity(TxHistoryActivity.createInstance(context,
                orderData.getOrderHistory()));

    }

    @Override
    public void processAskSeller(Context context, OrderData orderData) {
        Intent intent = new Intent(context, SendMessageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(SendMessageFragment.PARAM_SHOP_ID,
                orderData.getOrderShop().getShopId());
        bundle.putString(SendMessageFragment.PARAM_OWNER_FULLNAME,
                orderData.getOrderShop().getShopName());
        bundle.putString(SendMessageFragment.PARAM_CUSTOM_SUBJECT,
                orderData.getOrderDetail().getDetailInvoice());
        bundle.putString(SendMessageFragment.PARAM_CUSTOM_MESSAGE,
                Html.fromHtml(context.getString(R.string.custom_content_message_ask_seller)
                        .replace("XXX", orderData.getOrderDetail().getDetailPdfUri())).toString());
        viewListener.navigateToActivity(intent.putExtras(bundle));
    }

    @Override
    public void onDestroyView() {
        netInteractor.unSubscribeObservable();
    }

    private void checkClover(final Context context, JSONObject lucky, final String message)
            throws JSONException {
        viewListener.showProgressLoading();
        FacadeLuckyNotification facade = new FacadeLuckyNotification(context);
        facade.getLoyaltyNotification(lucky, new FacadeLuckyNotification.OnGetNotification() {
            @Override
            public void onSuccess(LoyaltyNotification notification) {
                viewListener.hideProgressLoading();
                processReviewWithNotif(context, notification, message);
            }

            @Override
            public void onEmpty() {
                viewListener.hideProgressLoading();
                processReview(context, message);
            }
        });
    }

    private void processReviewWithNotif(final Context context,
                                        final LoyaltyNotification notification, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(context, InboxReputationActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("unread", true);
                bundle.putBoolean("lucky", true);
                bundle.putParcelable("lucky_notif", notification);
                intent.putExtras(bundle);
                dialog.dismiss();
                viewListener.navigateToActivity(intent);
                viewListener.closeWithResult(TkpdState.TxActivityCode.BuyerItemReceived, null);
            }
        });
        Dialog alertDialog = builder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        viewListener.showDialog(builder.create());
    }

    private void processReview(final Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (message == null || message.isEmpty())
            message = context.getString(R.string.dialog_rating_review);
        builder.setMessage(message).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(context, InboxReputationActivity.class);
                intent.putExtra("unread", true);
                dialog.dismiss();
                viewListener.navigateToActivity(intent);
                viewListener.closeWithResult(TkpdState.TxActivityCode.BuyerItemReceived, null);
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
        builder.setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        viewListener.navigateToActivity(InboxResCenterActivity.createIntent(context));
                        viewListener.closeWithResult(TkpdState.TxActivityCode.BuyerCreateResolution, null);
                    }
                });
        Dialog alertDialog = builder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        viewListener.showDialog(builder.create());
    }

    private Dialog generateDialogConfirm(final Context context, final OrderData orderData) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(Html.fromHtml(context.getString(R.string.dialog_package_received)));
        builder.setNegativeButton(context.getString(R.string.title_no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.setPositiveButton(context.getString(R.string.title_yes),
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
                    viewListener.navigateToActivityRequest(CreateResCenterActivity.newInstance(context,
                            orderData.getOrderDetail().getDetailOrderId()), TransactionRouter.CREATE_RESCENTER_REQUEST_CODE);
                }

            });
        }

        return builder.create();
    }


    private Dialog generateDialogFreeReturn(final Context context, final OrderData orderData) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(Html.fromHtml(orderData.getOrderDetail().getDetailFreeReturnMsg()));
        builder.setNegativeButton(context.getString(R.string.title_open_dispute),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        LocalCacheHandler cache = new LocalCacheHandler(context,
                                FreeReturnOnboardingActivity.CACHE_FREE_RETURN);
                        if (cache.getBoolean(FreeReturnOnboardingActivity.HAS_SEEN_ONBOARDING))
                            viewListener.navigateToActivityRequest(CreateResCenterActivity.newInstance(context,
                                    orderData.getOrderDetail().getDetailOrderId()), TransactionRouter.CREATE_RESCENTER_REQUEST_CODE);
                        else
                            viewListener.navigateToActivityRequest(FreeReturnOnboardingActivity.newInstance(context,
                                    orderData.getOrderDetail().getDetailOrderId()), TransactionRouter.CREATE_RESCENTER_REQUEST_CODE);
                    }
                });
        builder.setPositiveButton(context.getString(R.string.title_ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        confirmPurchase(context, dialog, orderData);
                    }
                });
        builder.setNeutralButton(R.string.title_cancel,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }

    private void confirmPurchase(final Context context, final DialogInterface dialog, OrderData orderData) {
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
                                            if (lucky != null) {
                                                try {
                                                    checkClover(context, lucky, message);
                                                    dialog.dismiss();
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    processReview(context, message);
                                                    dialog.dismiss();
                                                }
                                            } else {
                                                processReview(context, message);
                                                dialog.dismiss();
                                            }
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
                            if (lucky != null) {
                                try {
                                    checkClover(context, lucky, message);
                                    dialog.dismiss();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    processReview(context, message);
                                    dialog.dismiss();
                                }
                            } else {
                                processReview(context, message);
                                dialog.dismiss();
                            }
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
            case TransactionRouter.CREATE_RESCENTER_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    processResolution(context, null);
                }
                break;
            default:
                break;
        }
    }
}
