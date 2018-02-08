package com.tokopedia.tokocash.network.api;

/**
 * Created by nabillasabbaha on 1/17/18.
 */

public interface WalletUrl {

    class BaseUrl {
        public static String ACCOUNTS_DOMAIN = "https://accounts.tokopedia.com/";
        public static String WALLET_DOMAIN = "https://www.tokocash.com/";
    }

    class KeyHmac {
        public static String HMAC_PENDING_CASHBACK = "CPAnAGpC3NIg7ZSj";
    }


    class Account {
        public static final String PATH_CASH_BACK_DOMAIN = "api/v1/me/cashback/balance";
        public static final String PATH_WALLET = "api/v1/wallet/balance";
        public static final String PATH_REQUEST_OTP_WALLET = "api/v1/wallet/otp/request";
        public static final String PATH_LINK_WALLET_TO_TOKOCASH = "api/v1/wallet/link";
        public static final String GET_TOKEN_WALLET = "api/v1/wallet/token";
    }

    class Wallet {
        public static final String GET_HISTORY = "api/v1/me/history";
        public static final String POST_COMPLAINT = "api/v1/cs/complaint";
        public static final String GET_OAUTH_INFO_ACCOUNT = "api/v1/me/profile";
        public static final String REVOKE_ACCESS_TOKOCASH = "api/v1/me/client/revoke";
        public static final String GET_QR_INFO = "api/v1/qr/{identifier}";
        public static final String POST_QR_PAYMENT = "api/v1/paymentqr";
        public static final String GET_BALANCE = "api/v1/wallet/balance";
    }

}
