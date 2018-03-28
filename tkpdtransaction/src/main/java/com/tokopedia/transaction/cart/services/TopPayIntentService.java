package com.tokopedia.transaction.cart.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.util.Base64;

import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.container.GTMContainer;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.cart.interactor.CartDataInteractor;
import com.tokopedia.transaction.cart.interactor.ICartDataInteractor;
import com.tokopedia.transaction.cart.model.paramcheckout.CheckoutData;
import com.tokopedia.transaction.cart.model.paramcheckout.CheckoutDropShipperData;
import com.tokopedia.transaction.cart.model.thankstoppaydata.ThanksTopPayData;
import com.tokopedia.transaction.cart.model.toppaydata.TopPayParameterData;
import com.tokopedia.transaction.cart.receivers.TopPayBroadcastReceiver;
import com.tokopedia.transaction.exception.HttpErrorException;
import com.tokopedia.transaction.exception.ResponseErrorException;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * @author anggaprasetiyo on 11/28/16.
 */
public class TopPayIntentService extends IntentService {
    public static final String EXTRA_ACTION = "EXTRA_ACTION";
    public static final String EXTRA_CHECKOUT_DATA = "EXTRA_CHECKOUT_DATA";
    public static final String EXTRA_PAYMENT_ID = "EXTRA_PAYMENT_ID";
    private static final String IS_THANKYOU_NATIVE = "is_thankyou_native";
    private static final String IS_THANKYOU_NATIVE_NEW = "is_thankyou_native_new";

    public static final int SERVICE_ACTION_NO_DEFINED = 0;
    public static final int SERVICE_ACTION_GET_PARAMETER_DATA = 1;
    public static final int SERVICE_ACTION_GET_THANKS_TOP_PAY = 2;
    public static final String FINGERPRINT_PUBLICKEY = "fingerprint_publickey";
    public static final String FINGERPRINT_SUPPORT = "fingerprint_support";
    public static final String FINGERPRINT_SUPPORT1 = "fingerprint_support";
    public static final String ANDROID_KEY_STORE = "AndroidKeyStore";
    public static final String FINGERPRINT = "fingerprint";

    private ICartDataInteractor cartDataInteractor;

    public TopPayIntentService() {
        super(TopPayIntentService.class.getCanonicalName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int extraAction = intent.getIntExtra(EXTRA_ACTION, SERVICE_ACTION_NO_DEFINED);
        switch (extraAction) {
            case SERVICE_ACTION_GET_PARAMETER_DATA:
                CheckoutData checkoutData = intent.getParcelableExtra(EXTRA_CHECKOUT_DATA);
                getParameterDataTopPay(checkoutData);
                break;
            case SERVICE_ACTION_GET_THANKS_TOP_PAY:
                String paymentId = intent.getStringExtra(EXTRA_PAYMENT_ID);
                getThanksTopPay(paymentId);
                break;
        }
    }

    private void getThanksTopPay(final String paymentId) {
        final TKPDMapParam<String, String> params = new TKPDMapParam<>();
        params.put("id", paymentId);
        Intent intent = new Intent(TopPayBroadcastReceiver.ACTION_TOP_PAY);
        intent.putExtra(TopPayBroadcastReceiver.EXTRA_RESULT_CODE_TOP_PAY_ACTION,
                TopPayBroadcastReceiver.RESULT_CODE_TOP_PAY_VERIFICATION_PROCESS_ONGOING);
        intent.putExtra(TopPayBroadcastReceiver.EXTRA_MESSAGE_TOP_PAY_ACTION,
                "Validasi pembayaran");
        sendBroadcast(intent);
        if (cartDataInteractor == null) cartDataInteractor = new CartDataInteractor();
        cartDataInteractor.getThanksTopPay(AuthUtil.generateParamsNetwork(this, params),
                Schedulers.immediate(),
                new Subscriber<ThanksTopPayData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Intent intent = new Intent(
                                TopPayBroadcastReceiver.ACTION_TOP_PAY
                        );
                        intent.putExtra(TopPayBroadcastReceiver.EXTRA_PAYMENT_ID, paymentId);
                        String messageError;
                        if (e instanceof SocketTimeoutException) {
                            intent.putExtra(TopPayBroadcastReceiver.EXTRA_RESULT_CODE_TOP_PAY_ACTION,
                                    TopPayBroadcastReceiver.RESULT_CODE_TOP_PAY_VERIFICATION_ERROR);
                            messageError = ErrorNetMessage.MESSAGE_ERROR_TIMEOUT;
                        } else if (e instanceof UnknownHostException) {
                            intent.putExtra(TopPayBroadcastReceiver.EXTRA_RESULT_CODE_TOP_PAY_ACTION,
                                    TopPayBroadcastReceiver.RESULT_CODE_TOP_PAY_VERIFICATION_NO_CONNECTION);
                            messageError = ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION;
                        } else if (e.getCause() instanceof ResponseErrorException) {
                            intent.putExtra(TopPayBroadcastReceiver.EXTRA_RESULT_CODE_TOP_PAY_ACTION,
                                    TopPayBroadcastReceiver.RESULT_CODE_TOP_PAY_VERIFICATION_ERROR);
                            messageError = e.getCause().getMessage();
                        } else if (e.getCause() instanceof HttpErrorException) {
                            intent.putExtra(TopPayBroadcastReceiver.EXTRA_RESULT_CODE_TOP_PAY_ACTION,
                                    TopPayBroadcastReceiver.RESULT_CODE_TOP_PAY_VERIFICATION_ERROR);
                            messageError = e.getCause().getMessage();
                        } else {
                            intent.putExtra(TopPayBroadcastReceiver.EXTRA_RESULT_CODE_TOP_PAY_ACTION,
                                    TopPayBroadcastReceiver.RESULT_CODE_TOP_PAY_VERIFICATION_ERROR);
                            messageError = ErrorNetMessage.MESSAGE_ERROR_DEFAULT;
                        }
                        intent.putExtra(
                                TopPayBroadcastReceiver.EXTRA_MESSAGE_TOP_PAY_ACTION, messageError
                        );
                        sendBroadcast(intent);
                    }

                    @Override
                    public void onNext(ThanksTopPayData thanksTopPayData) {
                        if (thanksTopPayData.getIsSuccess() == 1) {
                            Intent intent = new Intent(
                                    TopPayBroadcastReceiver.ACTION_TOP_PAY
                            );
                            intent.putExtra(
                                    TopPayBroadcastReceiver.EXTRA_RESULT_CODE_TOP_PAY_ACTION,
                                    TopPayBroadcastReceiver.RESULT_CODE_TOP_PAY_VERIFICATION_SUCCESS
                            );
                            intent.putExtra(
                                    TopPayBroadcastReceiver.EXTRA_TOP_PAY_THANKS_TOP_PAY_ACTION,
                                    thanksTopPayData
                            );
                            intent.putExtra(
                                    TopPayBroadcastReceiver.EXTRA_MESSAGE_TOP_PAY_ACTION,
                                    "Pembayaran berhasil"
                            );
                            intent.putExtra(TopPayBroadcastReceiver.EXTRA_PAYMENT_ID, paymentId);
                            sendBroadcast(intent);
                        } else {
                            Intent intent = new Intent(
                                    TopPayBroadcastReceiver.ACTION_TOP_PAY
                            );
                            intent.putExtra(TopPayBroadcastReceiver.EXTRA_PAYMENT_ID, paymentId);
                            intent.putExtra(
                                    TopPayBroadcastReceiver.EXTRA_RESULT_CODE_TOP_PAY_ACTION,
                                    TopPayBroadcastReceiver.RESULT_CODE_TOP_PAY_VERIFICATION_PAYMENT_NOT_VERIFIED
                            );
                            intent.putExtra(
                                    TopPayBroadcastReceiver.EXTRA_MESSAGE_TOP_PAY_ACTION,
                                    "Proses pembayaran tidak berhasil atau dibatalkan"
                            );
                            sendBroadcast(intent);
                        }
                    }
                });
    }

    private void getParameterDataTopPay(CheckoutData checkoutData) {
        TKPDMapParam<String, Object> params = new TKPDMapParam<>();
        params.put("donation_amt", checkoutData.getDonationValue());
        params.put("gateway", checkoutData.getGateway());
        params.put("token_cart", checkoutData.getToken());
        params.put("chosen", checkoutData.getPartialString());
        params.put("use_deposit", checkoutData.getUsedDeposit());
        params.put("deposit_amt", checkoutData.getDepositAmount());
        params.put("partial_str", checkoutData.getPartialString());
        params.put("dropship_str", checkoutData.getDropShipString());
        for (int i = 0; i < checkoutData.getKeroKeyParams().size(); i++) {
            params.put(checkoutData.getKeroKeyParams().get(i),
                    checkoutData.getKeroValueParams().get(i));
        }
        params.put(GTMContainer.CLIENT_ID, TrackingUtils.getClientID());

        if (checkoutData.getVoucherCode() != null) {
            params.put("voucher_code", checkoutData.getVoucherCode());
        }
        for (CheckoutDropShipperData data : checkoutData.getDropShipperDataList()) {
            params.put(data.getKey(), data.getValue());
        }
        params.put(IS_THANKYOU_NATIVE, "1");
        params.put(IS_THANKYOU_NATIVE_NEW, "1");
        params = createParamFingerprint(params);

        if (cartDataInteractor == null) cartDataInteractor = new CartDataInteractor();
        Intent intent = new Intent(TopPayBroadcastReceiver.ACTION_TOP_PAY);
        intent.putExtra(TopPayBroadcastReceiver.EXTRA_RESULT_CODE_TOP_PAY_ACTION,
                TopPayBroadcastReceiver.RESULT_CODE_TOP_PAY_GET_PARAMETER_PROCESS_ONGOING);
        intent.putExtra(TopPayBroadcastReceiver.EXTRA_MESSAGE_TOP_PAY_ACTION,
                "Melakukan proses checkout");
        sendBroadcast(intent);

        cartDataInteractor.getParameterTopPay(
                AuthUtil.generateParamsNetwork2(this, params), Schedulers.immediate(),
                new Subscriber<TopPayParameterData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Intent intent = new Intent(
                                TopPayBroadcastReceiver.ACTION_TOP_PAY
                        );
                        String messageError;
                        if (e instanceof SocketTimeoutException) {
                            intent.putExtra(TopPayBroadcastReceiver.EXTRA_RESULT_CODE_TOP_PAY_ACTION,
                                    TopPayBroadcastReceiver.RESULT_CODE_TOP_PAY_GET_PARAMETER_ERROR);
                            messageError = ErrorNetMessage.MESSAGE_ERROR_TIMEOUT;
                        } else if (e instanceof UnknownHostException) {
                            intent.putExtra(TopPayBroadcastReceiver.EXTRA_RESULT_CODE_TOP_PAY_ACTION,
                                    TopPayBroadcastReceiver.RESULT_CODE_TOP_PAY_GET_PARAMETER_NO_CONNECTION);
                            messageError = ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION;
                        } else if (e.getCause() instanceof ResponseErrorException) {
                            intent.putExtra(TopPayBroadcastReceiver.EXTRA_RESULT_CODE_TOP_PAY_ACTION,
                                    TopPayBroadcastReceiver.RESULT_CODE_TOP_PAY_GET_PARAMETER_ERROR);
                            messageError = e.getCause().getMessage();
                        } else if (e.getCause() instanceof HttpErrorException) {
                            intent.putExtra(TopPayBroadcastReceiver.EXTRA_RESULT_CODE_TOP_PAY_ACTION,
                                    TopPayBroadcastReceiver.RESULT_CODE_TOP_PAY_GET_PARAMETER_ERROR);
                            messageError = e.getCause().getMessage();
                        } else {
                            intent.putExtra(TopPayBroadcastReceiver.EXTRA_RESULT_CODE_TOP_PAY_ACTION,
                                    TopPayBroadcastReceiver.RESULT_CODE_TOP_PAY_GET_PARAMETER_ERROR);
                            messageError = ErrorNetMessage.MESSAGE_ERROR_DEFAULT;
                        }
                        intent.putExtra(
                                TopPayBroadcastReceiver.EXTRA_MESSAGE_TOP_PAY_ACTION, messageError
                        );
                        sendBroadcast(intent);
                    }

                    @Override
                    public void onNext(TopPayParameterData topPayParameterData) {
                        Intent intent = new Intent(
                                TopPayBroadcastReceiver.ACTION_TOP_PAY
                        );
                        intent.putExtra(TopPayBroadcastReceiver.EXTRA_RESULT_CODE_TOP_PAY_ACTION,
                                TopPayBroadcastReceiver.RESULT_CODE_TOP_PAY_GET_PARAMETER_SUCCESS);
                        intent.putExtra(
                                TopPayBroadcastReceiver.EXTRA_TOP_PAY_PARAMETER_DATA_TOP_PAY_ACTION,
                                topPayParameterData
                        );
                        sendBroadcast(intent);
                    }
                }
        );
    }

    private TKPDMapParam<String, Object> createParamFingerprint(TKPDMapParam<String, Object> params) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            FingerprintManagerCompat fingerprintManagerCompat = FingerprintManagerCompat.from(this);
            if (fingerprintManagerCompat.isHardwareDetected() && fingerprintManagerCompat.hasEnrolledFingerprints()) {
                String publicKey = "";
                try {
                    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, ANDROID_KEY_STORE);
                    KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(FINGERPRINT,
                            KeyProperties.PURPOSE_ENCRYPT |
                                    KeyProperties.PURPOSE_DECRYPT)
                            .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_OAEP)
                            .setBlockModes(KeyProperties.BLOCK_MODE_ECB);
                    keyPairGenerator.initialize(builder.build());
                    byte[] publicKeyBytes = Base64.encode(keyPairGenerator.generateKeyPair().getPublic().getEncoded(), 0);
                    publicKey = new String(publicKeyBytes);
                } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                } catch (NoSuchProviderException e) {
                    e.printStackTrace();
                }
                params.put(FINGERPRINT_PUBLICKEY, publicKey);
                params.put(FINGERPRINT_SUPPORT, true);
            }else{
                params.put(FINGERPRINT_SUPPORT1, false);
            }
        }
        return params;

    }
}
