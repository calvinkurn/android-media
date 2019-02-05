package com.tokopedia.transaction.purchase.presenter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tkpd.library.utils.data.DataManager;
import com.tkpd.library.utils.data.DataManagerImpl;
import com.tkpd.library.utils.data.DataReceiver;
import com.tokopedia.core2.R;
import com.tokopedia.core.database.manager.DbManagerImpl;
import com.tokopedia.core.database.model.Bank;
import com.tokopedia.core.database.model.CategoryDB;
import com.tokopedia.core.database.model.City;
import com.tokopedia.core.database.model.District;
import com.tokopedia.core.database.model.Province;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.transaction.purchase.activity.ConfirmPaymentActivity;
import com.tokopedia.transaction.purchase.adapter.TxConfBankListAdapter;
import com.tokopedia.transaction.purchase.interactor.TxOrderNetInteractor;
import com.tokopedia.transaction.purchase.interactor.TxOrderNetInteractorImpl;
import com.tokopedia.transaction.purchase.listener.ConfirmPaymentViewListener;
import com.tokopedia.transaction.purchase.model.ConfirmPaymentData;
import com.tokopedia.transaction.purchase.model.response.formconfirmpayment.Form;
import com.tokopedia.transaction.purchase.model.response.formconfirmpayment.FormConfPaymentData;
import com.tokopedia.transaction.purchase.model.response.formconfirmpayment.FormEdit;
import com.tokopedia.transaction.purchase.model.response.formconfirmpayment.FormEditPaymentData;
import com.tokopedia.transaction.purchase.model.response.formconfirmpayment.Method;
import com.tokopedia.transaction.purchase.services.TxActionIntentService;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.subscriptions.CompositeSubscription;

/**
 * @author Angga.Prasetiyo on 20/06/2016.
 */
public class ConfirmPaymentPresenterImpl implements ConfirmPaymentPresenter {
    private final ConfirmPaymentViewListener viewListener;
    private final TxOrderNetInteractorImpl netInteractor;

    public ConfirmPaymentPresenterImpl(ConfirmPaymentViewListener viewListener) {
        this.viewListener = viewListener;
        this.netInteractor = new TxOrderNetInteractorImpl();
    }

    @Override
    public void processChooseBank(final Context context,
                                  final ConfirmPaymentActivity.OnNewAccountBankSelected listener) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        @SuppressLint("InflateParams")
        final View view = LayoutInflater.from(context).inflate(R.layout.choose_bank_dialog, null);
        alertDialogBuilder.setView(view);
        final ListView lvBank = (ListView) view.findViewById(R.id.lv_bank);
        final SearchView search = (SearchView) view.findViewById(R.id.search);
        search.setIconified(false);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                TxConfBankListAdapter adapter = new TxConfBankListAdapter(context,
                        android.R.layout.simple_list_item_1, getBankListFromDB(query));
                lvBank.setAdapter(adapter);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                TxConfBankListAdapter adapter = new TxConfBankListAdapter(context,
                        android.R.layout.simple_list_item_1, getBankListFromDB(newText));
                lvBank.setAdapter(adapter);
                return true;
            }
        });
        TxConfBankListAdapter adapter = new TxConfBankListAdapter(context,
                android.R.layout.simple_list_item_1, getBankListFromDB(""));
        lvBank.setAdapter(adapter);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        viewListener.showDialog(alertDialog);
        lvBank.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bank bank = (Bank) parent.getAdapter().getItem(position);
                listener.onBankSelected(bank);
                if (alertDialog.isShowing()) alertDialog.dismiss();
            }
        });
    }

    @Override
    public void processGetEditPaymentForm(final Context context, final String confirmationId) {
        LocalCacheHandler bankCache = new LocalCacheHandler(context, HomeRouter.TAG_FETCH_BANK);
        if (bankCache.isExpired() || getBankListFromDB("").size() == 0) {
            requestBankList(context, new OnFinishBankList() {
                @Override
                public void actionOnSuccess() {
                    requestEditPaymentForm(context, confirmationId);
                }
            });
        } else {
            requestEditPaymentForm(context, confirmationId);
        }
    }

    @Override
    public void processGetConfirmPaymentForm(final Context context, final String confirmationId) {
        LocalCacheHandler bankCache = new LocalCacheHandler(context, HomeRouter.TAG_FETCH_BANK);
        if (bankCache.isExpired() || getBankListFromDB("").size() == 0) {
            requestBankList(context, new OnFinishBankList() {
                @Override
                public void actionOnSuccess() {
                    requestConfirmPaymentForm(context, confirmationId);
                }
            });
        } else {
            requestConfirmPaymentForm(context, confirmationId);
        }
    }

    @Override
    public void processSubmitConfirmation(Context context, ConfirmPaymentData data, Form formData,
                                          FormEdit formEditData) {
        if (validConfirmationData(context, data, formData, formEditData)) {
            if (data.isConfirmation()) {
                actionConfirmationPayment(context, data);
            } else {
                actionVerificationPayment(context, data);
            }
        }
    }

    @Override
    public void onDestroyView() {
        netInteractor.unSubscribeObservable();
    }

    private String getTotalTransactionCategory(Context context, int totalAmt) {
        if (totalAmt < 100000) {
            return context.getString(R.string.less_than_100k);
        } else if (totalAmt >= 100000 && totalAmt < 200000) {
            return context.getString(R.string.between_100k_to_200k);
        } else if (totalAmt >= 200000 && totalAmt < 400000) {
            return context.getString(R.string.between_200k_to_400k);
        } else if (totalAmt >= 400000 && totalAmt < 600000) {
            return context.getString(R.string.between_400k_to_600k);
        } else if (totalAmt >= 600000 && totalAmt < 800000) {
            return context.getString(R.string.between_600k_to_800k);
        } else if (totalAmt >= 800000 && totalAmt < 1000000) {
            return context.getString(R.string.between_800k_to_1000k);
        } else if (totalAmt >= 1000000) {
            return context.getString(R.string.more_than_1000k);
        } else {
            return null;
        }
    }

    private void actionConfirmationPayment(Context context, ConfirmPaymentData data) {
        Intent service = new Intent(context, TxActionIntentService.class);
        service.putExtra(TxActionIntentService.EXTRA_ACTION,
                TxActionIntentService.ACTION_CONFIRM_PAYMENT);
        service.putExtra(TxActionIntentService.EXTRA_DATA_CONFIRM, data);
        context.startService(service);
    }

    private void actionVerificationPayment(Context context, ConfirmPaymentData data) {
        Intent service = new Intent(context, TxActionIntentService.class);
        service.putExtra(TxActionIntentService.EXTRA_ACTION,
                TxActionIntentService.ACTION_EDIT_PAYMENT);
        service.putExtra(TxActionIntentService.EXTRA_DATA_CONFIRM, data);
        context.startService(service);
    }

    private boolean validConfirmationData(Context context, ConfirmPaymentData data, Form formData,
                                          FormEdit formEditData) {
        if (!data.isConfirmation()) {
            if (Integer.parseInt(data.getPaymentDay())
                    > formEditData.getDatetime().getDayInt()) {
                if (Integer.parseInt(data.getPaymentMonth())
                        >= formEditData.getDatetime().getMonthInt()) {
                    //noinspection WrongConstant
                    if (Integer.parseInt(data.getPaymentYear())
                            == Calendar.getInstance().get(Calendar.YEAR)) {
                        viewListener.renderErrorDate(context.getString(R.string.error_payment_date_not_valid));
                        return false;
                    }
                }
            } else if (Integer.parseInt(data.getPaymentDay())
                    > formEditData.getDatetime().getMonthInt()) {
                viewListener.renderErrorDate(context.getString(R.string.error_payment_date_not_valid));
                return false;
            }
        }
        if (data.getPaymentMethod() == null || data.getPaymentMethod().equals("0")) {
            viewListener.renderErrorPaymentMethod(
                    context.getString(R.string.error_payment_not_selected));
            return false;
        }
        if ((!data.getPaymentMethod().equals("5") && !data.getPaymentMethod().equals("6"))
                && (data.getBankAccountId() == null
                || data.getBankAccountId().isEmpty() || data.getBankAccountId().equals("0"))) {
            viewListener.renderErrorAccountBank("Bank harus dipilih");
            return false;
        }
        if (!data.getPaymentMethod().equals(Method.BALANCE_TOKOPEDIA)) {
            if (data.getSysBankId() == null || data.getSysBankId().equals("0")) {
                viewListener.renderErrorSysBank("Bank tujuan harus diisi.");
                return false;
            }
            if (!data.getPaymentMethod().equals(Method.CASH_DEPOSIT)) {
                if (data.isNewAccountBank()) {
                    if (data.getBankAccountName() == null || data.getBankAccountName().equals("")) {
                        viewListener.renderErrorAccountName(
                                context.getString(R.string.error_field_required));
                        return false;
                    }
                    if (data.getBankAccountNumber() == null
                            || data.getBankAccountNumber().equals("")) {
                        viewListener.renderErrorAccountNumber(
                                context.getString(R.string.error_field_required));
                        return false;
                    }
                    if (data.getBankAccountId() == null || data.getBankId().equals("")
                            || data.getBankId().equals("0")
                            || (data.getBankId().equals("ADD_NEW"))) {
                        viewListener.renderErrorChooseBank(
                                context.getString(R.string.error_field_required));
                        return false;
                    } else {
                        if (!data.getBankId().equals("71")) {
                            if (data.getBankAccountBranch() == null
                                    || data.getBankAccountBranch().equals("")) {
                                viewListener.renderErrorAccountBranch(
                                        context.getString(R.string.error_field_required));
                                return false;
                            }
                        }
                    }
                } else {
                    if (data.getBankId() == null || data.getBankId().equals("0")) {
                        viewListener.renderErrorAccountBank("Bank harus dipilih");
                        return false;
                    }
                }
            }
        }
        if (data.getPaymentMethod() != null
                && data.getPaymentMethod().equals(Method.CASH_DEPOSIT)) {
            if (data.getDepositor() == null || data.getDepositor().isEmpty()) {
                viewListener.renderErrotDepositorName("Nama Penyetor harus diisi.");
                return false;
            }
        }
        if (data.getPaymentMethod() != null
                && data.getPaymentMethod().equals(Method.BALANCE_TOKOPEDIA)) {
            if (data.getPasswordDeposit() == null || data.getPasswordDeposit().isEmpty()) {
                viewListener.renderErrorDepositorPassword(context.getString(R.string.error_empty_password));
                return false;
            }
        }
        if (!data.getPaymentMethod().equals("5")
                && ((data.getPaymentAmount() == null || data.getPaymentAmount().isEmpty()))) {
            viewListener.renderErrorPaymentAmount("Jumlah Pembayaran harus diisi");
            return false;
        } else {
            try {
                if (!data.getPaymentMethod().equals("5")) {
                    if (data.isConfirmation()) {
                        if (Double.parseDouble(CurrencyFormatHelper
                                .RemoveNonNumeric(data.getPaymentAmount()))
                                < Double.parseDouble(formData.getOrder().getOrderLeftAmount())) {
                            viewListener.renderErrorPaymentAmount(
                                    context.getString(R.string.error_payment_not_enough)
                                            + " " + formData.getOrder().getOrderLeftAmountIdr());
                            return false;
                        }
                    } else {
                        if (Double.parseDouble(CurrencyFormatHelper
                                .RemoveNonNumeric(data.getPaymentAmount()))
                                < Double.parseDouble(formEditData.getPayment().getOrderLeftAmount())) {
                            viewListener.renderErrorPaymentAmount(
                                    context.getString(R.string.error_payment_not_enough)
                                            + " " + formEditData.getPayment().getOrderLeftAmountIdr());
                            return false;
                        }
                    }
                    if (Double.parseDouble(CurrencyFormatHelper
                            .RemoveNonNumeric(data.getPaymentAmount())) > 50000000) {
                        viewListener.renderErrorPaymentAmount(
                                context.getString(R.string.error_price_lower25));
                        return false;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                viewListener.renderErrorPaymentAmount("Salah format penulisan");
                return false;
            }
        }
        return true;
    }

    private void requestEditPaymentForm(Context context, String confirmationId) {
        Map<String, String> params = new HashMap<>();
        params.put("payment_id", confirmationId);
        netInteractor.getEditPaymentForm(context, params,
                new TxOrderNetInteractor.EditPaymentFormListener() {

                    @Override
                    public void onSuccess(FormEditPaymentData data) {
                        viewListener.renderFormEdit(data);
//                        viewListener.renderErrorFormEdit("Test error coy");
                    }

                    @Override
                    public void onError(String message) {
                        viewListener.renderErrorFormEdit(message);
                    }

                    @Override
                    public void onTimeout(String message) {
                        viewListener.renderErrorFormEdit(message);
                    }

                    @Override
                    public void onNoConnection(String message) {
                        viewListener.renderNoConnectionFormEdit(message);
                    }
                });
    }

    private void requestConfirmPaymentForm(Context context, String confirmationId) {
        Map<String, String> params = new HashMap<>();
        params.put("confirmation_id", confirmationId);
        netInteractor.getConfirmPaymentForm(context, params,
                new TxOrderNetInteractor.ConfirmPaymentFormListener() {

                    @Override
                    public void onSuccess(FormConfPaymentData data) {
                        viewListener.renderFormConfirmation(data);
//                        viewListener.renderErrorFormConfirmation("Test error get form coy");
                    }

                    @Override
                    public void onError(String message) {
                        viewListener.renderErrorFormConfirmation(message);
                    }

                    @Override
                    public void onTimeout(String message) {
                        viewListener.renderErrorFormConfirmation(message);
                    }

                    @Override
                    public void onNoConnection(String message) {
                        viewListener.renderNoConnectionFormConfirmation(message);
                    }
                });
    }

    private void requestBankList(final Context context, final OnFinishBankList action) {
        DataManager dataManager = DataManagerImpl.getDataManager();
        dataManager.getListBank(context, new DataReceiver() {
            @Override
            public CompositeSubscription getSubscription() {
                return new CompositeSubscription();
            }

            @Override
            public void setDistricts(List<District> districts) {

            }

            @Override
            public void setCities(List<City> cities) {

            }

            @Override
            public void setProvinces(List<Province> provinces) {

            }

            @Override
            public void setBank(List<Bank> banks) {
                LocalCacheHandler cache = new LocalCacheHandler(context, HomeRouter.TAG_FETCH_BANK);
                cache.setExpire(86400);
                cache.applyEditor();
                action.actionOnSuccess();
            }

            @Override
            public void setShippingCity(List<District> districts) {

            }

            @Override
            public void onNetworkError(String message) {

            }

            @Override
            public void onMessageError(String message) {

            }

            @Override
            public void onUnknownError(String message) {

            }

            @Override
            public void onTimeout() {

            }

            @Override
            public void onFailAuth() {

            }
        });
    }

    private List<Bank> getBankListFromDB(String query) {
        return DbManagerImpl.getInstance().getBankBasedOnText(query);
    }

    interface OnFinishBankList {
        void actionOnSuccess();
    }
}
