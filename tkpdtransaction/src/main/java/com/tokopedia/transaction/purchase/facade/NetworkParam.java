package com.tokopedia.transaction.purchase.facade;

import com.tokopedia.transaction.purchase.model.TxGetPaymentItem;
import com.tokopedia.transaction.purchase.model.TxPaymentItem;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by herdimac on 4/8/16.
 */
public class NetworkParam {

    private static final String TAG = NetworkParam.class.getSimpleName();

    /**
     * Param for Payment Action
     * Created by : Hafizh Herdi
     */
    private static final String PARAM_BANK_ACC_BRANCH = "bank_account_branch";
    private static final String PARAM_BANK_ACC_ID = "bank_account_id";
    private static final String PARAM_BANK_ACC_NAME = "bank_account_name";
    private static final String PARAM_BANK_ACC_NUMBER = "bank_account_number";
    private static final String PARAM_BANK_ID = "bank_id";
    private static final String PARAM_BANK_NAME = "bank_name";
    private static final String PARAM_COMMENTS = "comments";
    private static final String PARAM_CONFIRMATION_ID = "confirmation_id";
    private static final String PARAM_DEPOSITOR = "depositor";
    private static final String PARAM_FILE_NAME = "file_name";
    private static final String PARAM_FILE_PATH = "file_path";
    private static final String PARAM_METHOD_ID = "method_id";
    private static final String PARAM_PASSWORD = "password";
    private static final String PARAM_PASSWORD_DEPOSIT = "password_deposit";
    private static final String PARAM_PAYMENT_AMOUNT = "payment_amount";
    private static final String PARAM_PAYMENT_DAY = "payment_day";
    private static final String PARAM_PAYMENT_MONTH = "payment_month";
    private static final String PARAM_PAYMENT_YEAR = "payment_year";
    private static final String PARAM_PAYMENT_ID = "payment_id";
    private static final String PARAM_SERVER_ID = "server_id";
    private static final String PARAM_SYSBANK_ID = "sysbank_id";
    private static final String PARAM_TOKEN = "token";
    private static final String PARAM_USER_ID = "user_id";

    /**
     * Param for Get Payment Data
     * Created by : Hafizh Herdi
     */
    private static final String PARAM_PAGE = "page";
    private static final String PARAM_PER_PAGE = "per_page";


    private static Map<String, String> getParamPaymentConfirmation(TxPaymentItem item, Map<String, String> existingParam) {

        existingParam.put(PARAM_COMMENTS, item.getComments());
        existingParam.put(PARAM_DEPOSITOR, item.getDepositor());
        existingParam.put(PARAM_PAYMENT_AMOUNT, item.getPaymentAmount());
        existingParam.put(PARAM_PAYMENT_DAY, item.getPaymentDay());
        existingParam.put(PARAM_METHOD_ID, item.getPaymentMethod());
        existingParam.put(PARAM_PAYMENT_MONTH, item.getPaymentMonth());
        existingParam.put(PARAM_PAYMENT_YEAR, item.getPaymentYear());
        existingParam.put(PARAM_BANK_ACC_ID, item.getAccId());
        existingParam.put(PARAM_BANK_ACC_NAME, item.getAccName());
        existingParam.put(PARAM_BANK_ACC_NUMBER, item.getAccNo());
        existingParam.put(PARAM_BANK_ID, item.getBankId());
        existingParam.put(PARAM_BANK_NAME, item.getBankName());
        existingParam.put(PARAM_BANK_ACC_BRANCH, item.getBankBranch());
        existingParam.put(PARAM_PASSWORD_DEPOSIT, item.getDepositPwd());
        existingParam.put(PARAM_SYSBANK_ID, item.getSysBank());

        return existingParam;
    }

    public static Map<String, String> getParamPaymentEdit(TxPaymentItem item) {

        Map<String, String> params = new HashMap<>();
        params.put(PARAM_PAYMENT_ID, item.getIdPayment());

        return getParamPaymentConfirmation(item, params);
    }

    public static Map<String, String> getParamPaymentConfirm(TxPaymentItem item) {

        Map<String, String> params = new HashMap<>();
        params.put(PARAM_CONFIRMATION_ID, item.getIdPayment());
        params.put(PARAM_TOKEN, item.getToken());

        return getParamPaymentConfirmation(item, params);
    }

    public static Map<String, String> getParamPaymentList(TxGetPaymentItem item) {

        Map<String, String> params = new HashMap<>();
        params.put(PARAM_PAGE, item.getPage());
        params.put(PARAM_PER_PAGE, item.getPerPage());
        params.put(PARAM_USER_ID, item.getUserID());

        return params;
    }

}
