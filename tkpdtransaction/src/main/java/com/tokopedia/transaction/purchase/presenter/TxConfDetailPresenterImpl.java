package com.tokopedia.transaction.purchase.presenter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.tokopedia.core2.R;
import com.tokopedia.transaction.purchase.activity.ConfirmPaymentActivity;
import com.tokopedia.transaction.purchase.activity.TxConfirmationDetailActivity;
import com.tokopedia.transaction.purchase.interactor.TxOrderNetInteractor;
import com.tokopedia.transaction.purchase.interactor.TxOrderNetInteractorImpl;
import com.tokopedia.transaction.purchase.listener.TxConfDetailViewListener;
import com.tokopedia.transaction.purchase.model.response.cancelform.CancelFormData;
import com.tokopedia.transaction.purchase.model.response.txconfirmation.TxConfData;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Angga.Prasetiyo on 15/06/2016.
 */
public class TxConfDetailPresenterImpl implements TxConfDetailPresenter {
    private static final String TAG = TxConfDetailPresenterImpl.class.getSimpleName();
    private final TxConfDetailViewListener viewListener;
    private final TxOrderNetInteractor netInteractor;

    public TxConfDetailPresenterImpl(TxConfDetailViewListener viewListener) {
        this.viewListener = viewListener;
        this.netInteractor = new TxOrderNetInteractorImpl();
    }


    @Override
    public void processCancelTransaction(final Context context, final TxConfData txConfData) {
        if (!txConfData.getConfirmation().getVoucherAmount().equals("0")) {
            cancelPaymentWithCheckVoucher(context, txConfData);
        } else {
            cancelPaymentNoCheckVoucher(context, txConfData);
        }
    }

    @Override
    public void processConfirmTransaction(Context context, TxConfData txConfData) {
        viewListener.navigateToActivityRequest(ConfirmPaymentActivity.instanceConfirm(context,
                txConfData.getConfirmation().getConfirmationId()),
                TxConfirmationDetailActivity.REQUEST_CONFIRMATION);
    }

    @Override
    public void onDestroyView() {
        netInteractor.unSubscribeObservable();
    }

    private void cancelPaymentNoCheckVoucher(Context context, TxConfData txConfData) {
        Dialog dialog = createDialogCancelPayment(context, txConfData);
        viewListener.showDialog(dialog);
    }

    private Dialog createDialogCancelPayment(final Context context, final TxConfData txConfData) {
        final AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(context);
        myAlertDialog.setMessage(context.getString(R.string.message_confirm_delete));

        myAlertDialog.setPositiveButton(context.getString(R.string.title_yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        cancelPayment(context, txConfData);
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

    private void cancelPaymentWithCheckVoucher(final Context context, final TxConfData txConfData) {
        Map<String, String> params = new HashMap<>();
        params.put("confirmation_id", txConfData.getConfirmation().getConfirmationId());
        netInteractor.getCancelPaymentForm(context, params,
                new TxOrderNetInteractor.OnCancelPaymentForm() {
                    @Override
                    public void onSuccess(CancelFormData data) {
                        Dialog dialog = createDialogCancelPaymentVoucher(context, txConfData, data);
                        viewListener.showDialog(dialog);
                    }

                    @Override
                    public void onError(String message) {
                        viewListener.showToastMessage(message);
                    }
                });
    }

    private Dialog createDialogCancelPaymentVoucher(final Context context,
                                                    final TxConfData txConfData,
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


        builder.setView(view).setPositiveButton(R.string.title_yes,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancelPayment(context, txConfData);
                    }
                }).setNegativeButton(R.string.title_no,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder.create();
    }

    private void cancelPayment(final Context context, TxConfData txConfData) {
        viewListener.showProgressLoading();
        Map<String, String> params = new HashMap<>();
        params.put("confirmation_id", txConfData.getConfirmation().getConfirmationId());
        netInteractor.cancelPayment(context, params, new TxOrderNetInteractor.OnCancelPayment() {
            @Override
            public void onSuccess() {
                viewListener.showProgressLoading();
                Bundle bundle = new Bundle();
                bundle.putInt("tab", 0);
                viewListener.setResultActivity(Activity.RESULT_OK, bundle);
                viewListener.closeView();
            }

            @Override
            public void onError(String message) {
                viewListener.showProgressLoading();
                viewListener.showToastMessage(message);
            }
        });
    }
}
