package com.tokopedia.transaction.purchase.presenter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.tokopedia.core2.R;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.transaction.purchase.activity.ConfirmPaymentActivity;
import com.tokopedia.transaction.purchase.activity.TxConfirmationDetailActivity;
import com.tokopedia.transaction.purchase.fragment.TxConfirmationFragment;
import com.tokopedia.transaction.purchase.interactor.TxOrderNetInteractor;
import com.tokopedia.transaction.purchase.interactor.TxOrderNetInteractorImpl;
import com.tokopedia.transaction.purchase.listener.TxConfViewListener;
import com.tokopedia.transaction.purchase.model.response.cancelform.CancelFormData;
import com.tokopedia.transaction.purchase.model.response.txconfirmation.TxConfData;
import com.tokopedia.transaction.purchase.model.response.txconfirmation.TxConfListData;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Angga.Prasetiyo on 13/05/2016.
 */
public class TxConfirmationPresenterImpl implements TxConfirmationPresenter {
    private final TxConfViewListener viewListener;
    private final TxOrderNetInteractorImpl netInteractor;

    public TxConfirmationPresenterImpl(TxConfViewListener viewListener) {
        this.viewListener = viewListener;
        this.netInteractor = new TxOrderNetInteractorImpl();
    }

    @Override
    public void getPaymentConfirmationData(Context context, int page, final int typeRequest) {
        Map<String, String> params = new HashMap<>();
        params.put("page", page + "");
        params.put("per_page", "10");
        netInteractor.getPaymentConfirmationList(context, params,
                new TxOrderNetInteractor.OnGetPaymentConfirmationList() {
                    @Override
                    public void onSuccess(TxConfListData dataObj) {
                        viewListener.renderDataList(dataObj.getTxConfDataList(),
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
                    public void onEmptyData() {
                        viewListener.showEmptyData(typeRequest);
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
                });
    }

    @Override
    public void processToTxConfirmationDetail(Context context, TxConfData data) {
        viewListener.navigateToActivityRequest(TxConfirmationDetailActivity.newInstance(context, data),
                TxConfirmationFragment.REQUEST_CONFIRMATION_DETAIL);
    }

    @Override
    public void processMultiConfirmPayment(Context context, Set<TxConfData> dataSelected) {
        StringBuilder stringBuilder = new StringBuilder();
        for (TxConfData data : dataSelected) {
            stringBuilder.append(data.getConfirmation().getConfirmationId()).append("~");
        }
        String params = stringBuilder.toString();
        if (params.substring(params.length() - 1).equals("~")) {
            params = params.substring(0, params.length() - 1);
        }
        viewListener.navigateToActivityRequest(ConfirmPaymentActivity.instanceConfirm(context,
                params), TxConfirmationFragment.REQUEST_CONFIRMATION_DETAIL);
    }


    private boolean isAnyVoucherAmount(Set<TxConfData> txConfDataSelected) {
        for (TxConfData data : txConfDataSelected) {
            if (!data.getConfirmation().getVoucherAmount().equals("0")) return true;
        }
        return false;
    }


    @Override
    public void processMultipleCancelPayment(Context context, Set<TxConfData> datas) {
        if (isAnyVoucherAmount(datas)) {
            cancelPaymentWithCheckVoucher(context, datas);
        } else {
            cancelPaymentNoCheckVoucher(context, datas);
        }
    }

    @Override
    public void onDestroyView() {
        netInteractor.unSubscribeObservable();
    }

    private void cancelPaymentNoCheckVoucher(Context context, Set<TxConfData> datas) {
        Dialog dialog = createDialogCancelPayment(context, datas);
        viewListener.showDialog(dialog);
    }

    private Dialog createDialogCancelPayment(final Context context, Set<TxConfData> datas) {
        final AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(context);
        myAlertDialog.setMessage(context.getString(R.string.message_confirm_delete));

        final Set<TxConfData> datasCancel = new HashSet<>();
        for (TxConfData data : datas) {
            datasCancel.add(data);
        }

        myAlertDialog.setPositiveButton(context.getString(R.string.title_yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        cancelPayment(context, datasCancel);
                    }
                });

        myAlertDialog.setNegativeButton(context.getString(R.string.title_no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
        Dialog dialog = myAlertDialog.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    private void cancelPayment(final Context context, Set<TxConfData> datas) {
        Map<String, String> params = new HashMap<>();
        params.put("confirmation_id", generateMultipleIds(datas));
        netInteractor.cancelPayment(context, params,
                new TxOrderNetInteractor.OnCancelPayment() {
                    @Override
                    public void onSuccess() {
//                        viewListener.closeActionMode();
//                        viewListener.resetTxConfDataSelected();
                        viewListener.resetData();
                    }

                    @Override
                    public void onError(String message) {
                        viewListener.showToastMessage(message);
//                        viewListener.resetTxConfDataSelected();
                    }
                });
    }

    private String generateMultipleIds(Set<TxConfData> datas) {
        StringBuilder stringBuilder = new StringBuilder();
        for (TxConfData data : datas) {
            stringBuilder.append(data.getConfirmation().getConfirmationId()).append("~");
        }
        String params = stringBuilder.toString();
        if (params.length() > 0) {
            if (params.substring(params.length() - 1).equals("~")) {
                params = params.substring(0, params.length() - 1);
            }
        }
        return params;
    }

    private void cancelPaymentWithCheckVoucher(final Context context, final Set<TxConfData> datas) {
        Map<String, String> params = new HashMap<>();
        params.put("confirmation_id", generateMultipleIds(datas));
        netInteractor.getCancelPaymentForm(context, params,
                new TxOrderNetInteractor.OnCancelPaymentForm() {
                    @Override
                    public void onSuccess(CancelFormData data) {
                        Dialog dialog = createDialogCancelPaymentVoucher(context, datas, data);
                        viewListener.showDialog(dialog);
                    }

                    @Override
                    public void onError(String message) {
                        viewListener.showToastMessage(message);
                    }
                });
    }

    private Dialog createDialogCancelPaymentVoucher(final Context context,
                                                    final Set<TxConfData> datas,
                                                    CancelFormData data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.dialog_cancel_payment,
                null);
        TextView tvVoucher = (TextView) view.findViewById(R.id.voucher_number);
        ListView lvVoucher = (ListView) view.findViewById(R.id.voucher_list);

        tvVoucher.setText(MessageFormat.format("Jumlah Kupon yang diaktifkan : {0}",
                data.getForm().getVoucherUsed()));
        ArrayAdapter<String> adapterTemp = new ArrayAdapter<>(context,
                android.R.layout.simple_list_item_1, android.R.id.text1,
                data.getForm().getVouchers());
        lvVoucher.setAdapter(adapterTemp);


        builder.setView(view).setPositiveButton(R.string.title_yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                cancelPayment(context, datas);
            }
        }).setNegativeButton(R.string.title_no, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        return builder.create();
    }
}
